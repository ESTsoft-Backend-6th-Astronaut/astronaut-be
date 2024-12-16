package com.estsoft.astronautbe.controller.keyword;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;


import com.estsoft.astronautbe.domain.dto.RecommendKeywordStockDTO;
import com.estsoft.astronautbe.domain.dto.SearchVolumeWithStockDTO;
import com.estsoft.astronautbe.repository.RecommendKeywordStockRepository;
import com.estsoft.astronautbe.service.KeywordService;

@RestController
public class KeywordController {

	private final KeywordService keywordService;
	private final RecommendKeywordStockRepository recommendKeywordStockRepository;

	public KeywordController(KeywordService keywordService,
			RecommendKeywordStockRepository recommendKeywordStockRepository) {
		this.keywordService = keywordService;
		this.recommendKeywordStockRepository = recommendKeywordStockRepository;
	}

	// GET -  키워드를 받아 DB의 추천 종목 5개 추출 API
	@GetMapping("/api/keywords/{keyword_id}/recommend")
	public List<RecommendKeywordStockDTO> getRecommendKeywordStock(
			@PathVariable("keyword_id") Long keywordId) {
		return keywordService.getRecommendStocks(keywordId);
	}

	// POST - 종목 리스트를 받아 DB에 저장한 네이버 검색량 조회 API
	@GetMapping("/api/keywords/{keyword_id}")
	public List<SearchVolumeWithStockDTO> getTrends(@PathVariable("keyword_id") Long keywordId) {

		return keywordService.getRecommendStockWithSearchVolumes(keywordId);
	}

	// 전체 프로세스 실행 (1일 1회 사용하도록 스케줄러로 설정 필요)
	@PostMapping("/api/keywords/allProcess")
	public ResponseEntity<String> processRecommendStockAndSearch() {
		keywordService.processKeywordRecommendation();
		return ResponseEntity.ok("전체 프로세스 실행 완료");
	}

	// 오늘의 키워드 ID 반환(리액트 연결용)
	@GetMapping("/api/keywords/today")
	public ResponseEntity<List<Long>> getTodayKeywordsIds() {
		LocalDateTime today = LocalDateTime.now();
		List<Long> todayKeywordIds = keywordService.getTodayKeywordIds(today);
		return ResponseEntity.ok(todayKeywordIds);
	}

}
