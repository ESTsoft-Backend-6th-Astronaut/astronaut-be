package com.estsoft.astronautbe.controller.keywordNews;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.estsoft.astronautbe.service.keywordNews.AllenAIService;

@RestController
public class AllenAIController {

	private final AllenAIService allenAIService;

	public AllenAIController(AllenAIService allenAIService) {
		this.allenAIService = allenAIService;
	}

	@GetMapping("/allen_api")
	public ResponseEntity<String> allenApi() {
		String response = allenAIService.getAnswerFromAllenAI();
		allenAIService.saveNewsToDatabase(response);

		return ResponseEntity.ok(response);
	}
}
