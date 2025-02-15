package com.estsoft.astronautbe.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.estsoft.astronautbe.config.ApiConstants;
import com.estsoft.astronautbe.domain.Keyword;
import com.estsoft.astronautbe.repository.KeywordRepository;
import com.estsoft.astronautbe.util.JsonParserUtil;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class PopularKeywordService {

	@Value("${ALLEN_API_ID}")
	private String allenApiId;

	private String allenApiUrl = ApiConstants.ALLEN_API_URL;

	private final RestTemplate restTemplate = new RestTemplate();
	private final KeywordRepository keywordRepository;

	public PopularKeywordService(KeywordRepository keywordRepository) {
		this.keywordRepository = keywordRepository;
	}

	public List<Keyword> getYesterdayPopularKeywords() {
		if (allenApiId == null || allenApiUrl == null) {
			throw new IllegalArgumentException("API ID or URL is not set");
		}

		String url = allenApiUrl;
		String content = "전날 주식시장 종목명을 제외한 인기 키워드 10개를 정리하고 뉴스를 제거하고 키워드명, 관심도는 상대적으로 1~100으로, 순위, 이유, 긍정이면 0으로 하고 부정적이면 1로 정리해서 json 배열 형태로만 알려줘. "
			+ "필드의 값은 keywordName, interest, ranking, reason, emotion 순서로 알려줘. "
			+ "reason은 최소 100자 이상으로 작성해줘.";
		String requestUrl = String.format("%s?content=%s&client_id=%s", url, content, allenApiId);

		HttpHeaders headers = new HttpHeaders();
		headers.set("Accept", "application/json");

		HttpEntity<String> entity = new HttpEntity<>(headers);

		ResponseEntity<String> responseEntity = restTemplate.exchange(
			requestUrl,
			HttpMethod.GET,
			entity,
			String.class
		);

		String rawResponse = responseEntity.getBody();

		// JSON 파싱 전 본문에서 JSON을 추출
		String responseBody = JsonParserUtil.extractJsonFromContent(rawResponse);

		return saveKeywordsToDatabase(responseBody);
	}

	private List<Keyword> saveKeywordsToDatabase(String responseBody) {
		if (responseBody == null) {
			throw new IllegalArgumentException("Response body is null");
		}

		List<Keyword> keywords = new ArrayList<>();
		try {
			ObjectMapper objectMapper = new ObjectMapper();
			JsonNode itemsNode = objectMapper.readTree(responseBody);

			if (itemsNode != null && itemsNode.isArray()) {
				// 기존 데이터 삭제
				// keywordRepository.deleteAll();

				for (JsonNode itemNode : itemsNode) {
					// 각 필드별 null 체크
					JsonNode keywordNameNode = itemNode.get("keywordName");
					JsonNode interestNode = itemNode.get("interest");
					JsonNode rankingNode = itemNode.get("ranking");
					JsonNode reasonNode = itemNode.get("reason");
					JsonNode emotionNode = itemNode.get("emotion");

					if (keywordNameNode == null || interestNode == null ||
						rankingNode == null || reasonNode == null || emotionNode == null) {
						throw new IllegalArgumentException(
							"Some fields are missing in itemNode: " + itemNode.toString());
					}
					Keyword keyword = new Keyword();
					keyword.setKeywordName(keywordNameNode.asText());
					keyword.setInterest(interestNode.asInt());
					keyword.setRanking(rankingNode.asInt());
					keyword.setReason(reasonNode.asText());
					keyword.setEmotion(emotionNode.asInt());
					keywords.add(keywordRepository.save(keyword));
				}
			} else {
				throw new IllegalArgumentException("Response body is not an array");
			}
		} catch (Exception e) {
			throw new RuntimeException("Error saving keywords to database", e);
		}
		return keywords;
	}

	// 리액트 반환용 (ALAN에게서 매번 호출하는 방식이 아닌 DB에서 가져오는 방식)
	public List<Keyword> getPopularKeywordsForReact() {
		LocalDateTime startOfDay = LocalDateTime.now().toLocalDate().atStartOfDay();
		LocalDateTime endOfDay = startOfDay.plusDays(1).minusNanos(1);
		return keywordRepository.findByCreatedAtToday(startOfDay, endOfDay);
	}
}