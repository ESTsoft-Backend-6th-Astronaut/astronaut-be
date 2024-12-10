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
	// 키워드 아이디로 받아오게 하기!!!
	// 일단은 키워드를 빼고? 먼저 임의의 단어로 집어넣어서?해보기로
	// 추후 url이나 내용 등 수정하기. 지금은 string으로 받아왔지만 다음번엔 id로 받아서>그걸로 키워드를 찾아서>이 키워드로 검색하도록.
	// 현재는 키워드 string으로 받아오게 했지만 추후 id로 받아오게 해서 매핑하기
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
	// 키워드 아이디로 받아오게 하기!!!
	@PostMapping("/api/keywords")
	public ResponseEntity<List<SearchVolumeResponseDTO>> getTrends(@RequestBody List<String> keywords) {
		SearchVolumeRequestDTO request = new SearchVolumeRequestDTO(keywords);
		List<SearchVolumeResponseDTO> responseList = service.getSearchAmount(request);

		return ResponseEntity.ok(responseList);
	}

}
