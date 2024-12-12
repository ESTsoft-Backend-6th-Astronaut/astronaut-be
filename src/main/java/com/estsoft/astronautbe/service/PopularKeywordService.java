package com.estsoft.astronautbe.service;

import com.estsoft.astronautbe.domain.Keyword;
import com.estsoft.astronautbe.repository.KeywordRepository;
import com.estsoft.astronautbe.util.JsonParserUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.List;

@Service
public class PopularKeywordService {

    @Value("${ALLEN_API_ID}")
    private String allenApiId;

    @Value("${ALLEN_API_URL}")
    private String allenApiUrl;

    private final RestTemplate restTemplate = new RestTemplate();
    private final KeywordRepository keywordRepository;

    public PopularKeywordService(KeywordRepository keywordRepository) {
        this.keywordRepository = keywordRepository;
    }

    public List<Keyword> getYesterdayPopularKeywords() {
        if (allenApiId == null || allenApiUrl == null) {
            throw new IllegalArgumentException("API ID or URL is not set");
        }

        String url = allenApiUrl + "/api/v1/question";
        String content = "전날 주식시장 인기 키워드 10개를 정리하고 뉴스를 제거하고 키워드명, 관심도는 상대적으로 1~100으로, 순위, 이유, 긍정이면 0으로 하고 부정적이면 1로 정리해서 json 형태로만 알려줘 필드의 값은 keywordName, interest, ranking, reason, emotion 순서로 알려줘";
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

        // 실제 응답 바디를 로깅해서 확인
        String rawResponse = responseEntity.getBody();
        System.out.println("Raw Response: " + rawResponse);

        // JSON 파싱 전 본문에서 JSON을 추출
        String responseBody = JsonParserUtil.extractJsonFromContent(rawResponse);
        System.out.println("Extracted JSON: " + responseBody);

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
                System.out.println("data");
                for (JsonNode itemNode : itemsNode) {
                    // 각 필드별 null 체크
                    JsonNode keywordNameNode = itemNode.get("keywordName");
                    JsonNode interestNode = itemNode.get("interest");
                    JsonNode rankingNode = itemNode.get("ranking");
                    JsonNode reasonNode = itemNode.get("reason");
                    JsonNode emotionNode = itemNode.get("emotion");

                    if (keywordNameNode == null || interestNode == null ||
                            rankingNode == null || reasonNode == null || emotionNode == null) {
                        // 필드가 하나라도 없으면 로그 출력 후 continue
                        System.err.println("Some fields are missing in itemNode: " + itemNode.toString());
                        continue;
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
                System.err.println("itemsNode is not an array or is null. Response: " + responseBody);
            }
        } catch (Exception e) {
            throw new RuntimeException("Error saving keywords to database", e);
        }
        return keywords;
    }
}