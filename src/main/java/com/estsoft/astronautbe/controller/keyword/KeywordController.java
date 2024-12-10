package com.estsoft.astronautbe.controller.keyword;

import java.util.Arrays;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.estsoft.astronautbe.domain.dto.RecommendKeywordStockRequestDTO;
import com.estsoft.astronautbe.domain.dto.RecommendKeywordStockResponseDTO;
import com.estsoft.astronautbe.domain.dto.SearchVolumeRequestDTO;
import com.estsoft.astronautbe.domain.dto.SearchVolumeResponseDTO;
import com.estsoft.astronautbe.service.KeywordService;

@RestController
public class KeywordController {

	private final KeywordService service;

	public KeywordController(KeywordService service) {
		this.service = service;

	}

	// GET -  키워드를 받아 Alan으로 추천 종목 5개 추출 API
	@GetMapping("/api/keywords/recommend")
	public ResponseEntity<List<RecommendKeywordStockResponseDTO>> getRecommendKeywordStock(@RequestParam(name = "content") String keyword){
		// 추후 추천키워드 받아오도록 수정
		String keywords = "테스트";
		RecommendKeywordStockRequestDTO request = new RecommendKeywordStockRequestDTO(keywords);
		List<RecommendKeywordStockResponseDTO> responseList = service.getRecommendKeywordStock(request);
		System.out.println("컨트롤러단에서 받은 리스폰스:"+responseList);
		return ResponseEntity.ok(responseList);
	}

	// POST - 종목 리스트를 받아 네이버 검색량 조회 API
	@PostMapping("/api/keywords")
	public ResponseEntity<List<SearchVolumeResponseDTO>> getTrends(@RequestBody List<String> keywords) {
		SearchVolumeRequestDTO request = new SearchVolumeRequestDTO(keywords);
		List<SearchVolumeResponseDTO> responseList = service.getSearchAmount(request);

		return ResponseEntity.ok(responseList);
	}

}
