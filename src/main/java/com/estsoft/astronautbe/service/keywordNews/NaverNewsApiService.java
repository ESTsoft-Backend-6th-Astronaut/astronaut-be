package com.estsoft.astronautbe.service.keywordNews;

import java.io.*;
import java.net.*;
import java.sql.Timestamp;
import java.text.*;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.text.StringEscapeUtils;
import org.jsoup.Jsoup;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.estsoft.astronautbe.config.ApiConstants;
import com.estsoft.astronautbe.domain.Keyword;
import com.estsoft.astronautbe.domain.KeywordNews;
import com.estsoft.astronautbe.repository.KeywordNewsRepository;
import com.estsoft.astronautbe.repository.KeywordRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class NaverNewsApiService {
	private final KeywordRepository keywordRepository;
	@Value("${naver.client.id}")
	private String clientId;

	@Value("${naver.client.secret}")
	private String clientSecret;

	private final KeywordNewsRepository repository;

	public NaverNewsApiService(KeywordNewsRepository repository, KeywordRepository keywordRepository) {
		this.repository = repository;
		this.keywordRepository = keywordRepository;
	}

	public String getNews(String query) {
		String text = query;

		try {
			text = URLEncoder.encode(text, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException("검색어 인코딩 실패", e);
		}

		String apiURL = ApiConstants.NAVER_NEWS_API_URL + text;

		Map<String, String> requestHeaders = new HashMap<>();
		requestHeaders.put("X-Naver-Client-Id", clientId);
		requestHeaders.put("X-Naver-Client-Secret", clientSecret);

		return get(apiURL, requestHeaders);
	}

	public void saveNewsToDatabase(String responseBody, Long keywordId) {
		try {
			ObjectMapper objectMapper = new ObjectMapper();
			JsonNode rootNode = objectMapper.readTree(responseBody);
			JsonNode itemsNode = rootNode.get("items");

			if (itemsNode != null && itemsNode.isArray()) {
				DateFormat dateFormat = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss Z", Locale.ENGLISH);

				for (JsonNode itemNode : itemsNode) {
					String title = StringEscapeUtils.unescapeHtml4(Jsoup.parse(itemNode.get("title").asText()).text());
					String link = itemNode.get("originallink").asText();
					String pubDateString = itemNode.get("pubDate").asText();

					Timestamp pubDate;
					try {
						pubDate = new Timestamp(dateFormat.parse(pubDateString).getTime());
					} catch (ParseException e) {
						throw new RuntimeException("날짜 형식 변환 실패: " + pubDateString, e);
					}

					Keyword keyword = keywordRepository.findById(keywordId).orElse(null);
					KeywordNews news = new KeywordNews(keyword, title, null, link, pubDate, null, null);
					repository.save(news);
				}
			}
		} catch (Exception e) {
			throw new RuntimeException("뉴스 데이터를 저장하는 동안 오류 발생", e);
		}
	}

	private String get(String apiUrl, Map<String, String> requestHeaders) {
		HttpURLConnection con = connect(apiUrl);
		try {
			con.setRequestMethod("GET");
			for (Map.Entry<String, String> header : requestHeaders.entrySet()) {
				con.setRequestProperty(header.getKey(), header.getValue());
			}

			int responseCode = con.getResponseCode();
			if (responseCode == HttpURLConnection.HTTP_OK) {
				return readBody(con.getInputStream());
			} else {
				return readBody(con.getErrorStream());
			}
		} catch (IOException e) {
			throw new RuntimeException("API 요청과 응답 실패", e);
		} finally {
			con.disconnect();
		}
	}

	private HttpURLConnection connect(String apiUrl) {
		try {
			URL url = new URL(apiUrl);
			return (HttpURLConnection)url.openConnection();
		} catch (MalformedURLException e) {
			throw new RuntimeException("API URL이 잘못되었습니다. : " + apiUrl, e);
		} catch (IOException e) {
			throw new RuntimeException("연결이 실패했습니다. : " + apiUrl, e);
		}
	}

	private String readBody(InputStream body) {
		InputStreamReader streamReader = new InputStreamReader(body);
		try (BufferedReader lineReader = new BufferedReader(streamReader)) {
			StringBuilder responseBody = new StringBuilder();
			String line;
			while ((line = lineReader.readLine()) != null) {
				responseBody.append(line);
			}
			return responseBody.toString();
		} catch (IOException e) {
			throw new RuntimeException("API 응답을 읽는 데 실패했습니다.", e);
		}
	}
}
