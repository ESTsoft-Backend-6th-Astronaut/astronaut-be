package com.estsoft.astronautbe.controller.keywordNews;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.estsoft.astronautbe.domain.dto.keywordNews.KeywordNewsResponseDTO;
import com.estsoft.astronautbe.service.keywordNews.KeywordNewsService;

@RestController
public class KeywordNewsController {

	private final KeywordNewsService keywordNewsService;

	public KeywordNewsController(KeywordNewsService keywordNewsService) {
		this.keywordNewsService = keywordNewsService;
	}

	@GetMapping("/api/keywords/{keyword_id}/news")
	public ResponseEntity<?> getNews(@PathVariable("keyword_id") Long keywordId) {
		List<KeywordNewsResponseDTO> newsList = keywordNewsService.getKeywordNewsByKeywordId(keywordId);
		return ResponseEntity.ok()
			.body(Map.of(
				"message", "키워드 뉴스 전체 조회 성공",
				"data", newsList
			));
	}

	@PostMapping("/api/keywords/news/allProcess")
	public ResponseEntity<?> allProcess() {
		keywordNewsService.processKeywordNews();
		return ResponseEntity.ok("전체 프로세스 실행 성공");
	}
}
