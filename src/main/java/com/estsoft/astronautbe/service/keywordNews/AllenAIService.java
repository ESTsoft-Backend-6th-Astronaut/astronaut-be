package com.estsoft.astronautbe.service.keywordNews;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.estsoft.astronautbe.config.ApiConstants;
import com.estsoft.astronautbe.domain.KeywordNews;
import com.estsoft.astronautbe.repository.KeywordNewsRepository;
import com.estsoft.astronautbe.util.JsonParserUtil;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

@Service
public class AllenAIService {
	@Value("${allen.client.id}")
	private String clientId;

	private final KeywordNewsRepository repository;
	private final RestTemplate restTemplate = new RestTemplate();

	public AllenAIService(KeywordNewsRepository repository) {
		this.repository = repository;
	}

	public String getAnswerFromAllenAI() {
		String baseUrl = ApiConstants.ALLEN_API_URL;
		String content = createPrompt();

		try {
			String encodedContent = URLEncoder.encode(content, "UTF-8");
			String encodedClientId = URLEncoder.encode(clientId, "UTF-8");

			String requestUrl = String.format("%s?content=%s&client_id=%s", baseUrl, encodedContent, encodedClientId);
			URI uri = new URI(requestUrl);

			HttpHeaders headers = new HttpHeaders();
			headers.set("Accept", "application/json");

			HttpEntity<String> entity = new HttpEntity<>(headers);

			ResponseEntity<String> responseEntity = restTemplate.exchange(
				uri,
				HttpMethod.GET,
				entity,
				String.class
			);

			return JsonParserUtil.extractJsonFromContent(responseEntity.getBody());
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException("인코딩 실패: " + e.getMessage(), e);
		} catch (URISyntaxException e) {
			throw new RuntimeException("잘못된 URI: " + e.getMessage(), e);
		} catch (Exception e) {
			throw new RuntimeException("앨런 AI API 요청 실패: " + e.getMessage(), e);
		}
	}

	public String createPrompt() {
		ObjectMapper objectMapper = new ObjectMapper();
		ArrayNode newsArray = objectMapper.createArrayNode();

		for (long i = 41L; i <= 50L; i++) {
			ObjectNode newsObject = objectMapper.createObjectNode();
			newsObject.put("id", i);
			newsObject.put("url", getNewsById(i).getOriginalLink());
			newsArray.add(newsObject);
		}

		return newsArray
			+ "위의 10개의 뉴스 기사는 통신에 대한 기사들이야 각각의 기사는 id 값이 있어 "
			+ "응답은 id, emotion, newspaper, summary를 키 값으로 갖는 json 형식으로 알려줘 요구한 모든 데이터는 10개의 json 형식으로 빠짐없이 알려줘야해 "
			+ "각각의 뉴스가 통신이라는 주제의 주식 종목들에 대해 긍정적인지 부정적인지 감정 분석 해서 emotion의 value로 긍정적이면 0, 부정적이면 1으로 알려줘 "
			+ "각각의 뉴스의 언론사를 newspaper의 value로 알려줘 "
			+ "각각의 뉴스의 기사를 두문장으로 요약해서 summary의 value로 알려줘";
	}

	public KeywordNews getNewsById(Long newsId) {
		return repository.findById(newsId).orElseThrow(() -> new IllegalArgumentException(newsId + ": 뉴스를 찾을 수 없습니다."));
	}

	public void saveNewsToDatabase(String responseBody) {
		try {
			ObjectMapper objectMapper = new ObjectMapper();
			JsonNode itemsNode = objectMapper.readTree(responseBody);

			if (itemsNode != null && itemsNode.isArray()) {
				for (JsonNode itemNode : itemsNode) {
					Long newsId = itemNode.get("id").asLong();
					KeywordNews news = repository.findById(newsId)
						.orElseThrow(() -> new IllegalArgumentException(newsId + ": 뉴스를 찾을 수 없습니다."));
					if (news != null) {
						Boolean emotion = itemNode.get("emotion").asBoolean();
						String newspaper = itemNode.get("newspaper").asText();
						String summary = itemNode.get("summary").asText();
						news.updateAIInfo(newspaper, emotion, summary);
						repository.save(news);
					}
				}
			}
		} catch (Exception e) {
			throw new RuntimeException("뉴스 데이터를 저장하는 동안 오류 발생", e);
		}
	}
}
