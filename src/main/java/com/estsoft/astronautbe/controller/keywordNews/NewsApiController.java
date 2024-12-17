package com.estsoft.astronautbe.controller.keywordNews;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.estsoft.astronautbe.service.keywordNews.NaverNewsApiService;

@RestController
public class NewsApiController {

	private final NaverNewsApiService service;

	public NewsApiController(NaverNewsApiService service) {
		this.service = service;
	}

	@GetMapping("/news_api")
	public ResponseEntity<String> newsApi(@RequestParam("query") String query) {
		String responseBody = service.getNews(query);
		service.saveNewsToDatabase(responseBody, 1L); // 임시 keywordId
		return ResponseEntity.ok(responseBody);
	}
}
