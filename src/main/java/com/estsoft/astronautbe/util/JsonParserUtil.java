package com.estsoft.astronautbe.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonParserUtil {
	private JsonParserUtil() {
	}

	public static String extractJsonFromContent(String response) {
		try {
			ObjectMapper objectMapper = new ObjectMapper();
			JsonNode rootNode = objectMapper.readTree(response);

			String content = rootNode.get("content").asText();

			if (content.startsWith("```json")) {
				content = content.replaceFirst("```json\\n", "");
			}
			if (content.endsWith("```")) {
				content = content.substring(0, content.length() - 3);
			}

			return content.trim();
		} catch (Exception e) {
			throw new RuntimeException("JSON 파싱 실패: " + e.getMessage(), e);
		}
	}
}
