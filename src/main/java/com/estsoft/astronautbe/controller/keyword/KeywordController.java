package com.estsoft.astronautbe.controller.keyword;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.estsoft.astronautbe.domain.RecommendKeywordStock;
import com.estsoft.astronautbe.domain.SearchVolume;
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

	// GET -  키워드를 받아 Alan으로 추천 종목 5개 추출 API
	@GetMapping("/api/keywords/{keyword_id}/recommend")
	public ResponseEntity<List<RecommendKeywordStock>> getRecommendKeywordStock(
			@PathVariable("keyword_id") Long keywordId) {

		String keyword = keywordService.getKeywordNameById(keywordId);

		List<RecommendKeywordStock> recommendKeywordStocks = keywordService.getRecommendKeywordStock(keyword, keywordId);

		return ResponseEntity.ok(recommendKeywordStocks);
	}

	// POST - 종목 리스트를 받아 네이버 검색량 조회 API
	@PostMapping("/api/keywords/{keyword_id}")
	public ResponseEntity<List<SearchVolume>> getTrends(@PathVariable("keyword_id") Long keywordId,
			@RequestBody List<String> stockNames) {

		// 추천 종목 정보
		List<RecommendKeywordStock> recommendKeywordStocks = recommendKeywordStockRepository.findByKeyword_KeywordId(
				keywordId);

		List<SearchVolume> searchVolumes = keywordService.getSearchAmount(stockNames, keywordId, recommendKeywordStocks);

		return ResponseEntity.ok(searchVolumes);
	}

	// 전체 프로세스 실행
	@PostMapping("/api/keywords/allProcess")
	public ResponseEntity<String> processRecommendStockAndSearch() {
		keywordService.processKeywordRecommendation();
		return ResponseEntity.ok("전체 프로세스 실행 완료");
	}

}
