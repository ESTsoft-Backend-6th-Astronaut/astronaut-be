package com.estsoft.astronautbe.controller.portfolio;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.estsoft.astronautbe.domain.dto.portfolio.PortfolioPriceResponseDTO;
import com.estsoft.astronautbe.domain.dto.portfolio.PortfolioRequestDto;
import com.estsoft.astronautbe.domain.dto.portfolio.PortfolioResponseDto;
import com.estsoft.astronautbe.domain.dto.portfolio.PortfolioStockResponseDTO;
import com.estsoft.astronautbe.service.PortfolioService;
import com.estsoft.astronautbe.service.StockService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/portfolios")
@RequiredArgsConstructor
public class PortfolioController {

	private final PortfolioService portfolioService;
	private final StockService stockService;

	// 전체 조회
	@GetMapping
	public ResponseEntity<?> getAllPortfolios() {
		// 임시
		Long hardCodedUserId = 1L;

		List<PortfolioResponseDto> portfolios = portfolioService.getAllPortfolios(hardCodedUserId);

		return ResponseEntity.ok()
			.body(Map.of(
				"message", "포트폴리오 전체 조회 성공",
				"data", portfolios
			));
	}

	// 등록
	@PostMapping
	public ResponseEntity<?> createPortfolio(
		@RequestBody List<PortfolioRequestDto> requestDTOs) {

		Long userId = 1L; // 임시 userId

		List<PortfolioResponseDto> responseDTOs = requestDTOs.stream()
			.map(dto -> portfolioService.createPortfolio(userId, dto))
			.toList();

		return ResponseEntity.ok()
			.body(Map.of(
				"message", "포트폴리오 등록 성공",
				"data", responseDTOs
			));
	}

	// 수정
	@PatchMapping("/{portfolioId}")
	public ResponseEntity<?> updatePortfolio(
		@PathVariable Long portfolioId,
		@RequestBody PortfolioRequestDto requestDto) {
		PortfolioResponseDto updatedPortfolio = portfolioService.updatePortfolio(portfolioId, requestDto);
		return ResponseEntity.ok(Map.of(
			"message", "포트폴리오 수정 성공",
			"data", updatedPortfolio
		));
	}

	// 삭제
	@DeleteMapping("/{portfolioId}")
	public ResponseEntity<?> deletePortfolio(@PathVariable Long portfolioId) {
		portfolioService.deletePortfolio(portfolioId);
		return ResponseEntity.ok(Map.of(
			"message", "포트폴리오 삭제 성공"
		));
	}

	// 포트폴리오 전체 현재가 조회
	@GetMapping("/current_price")
	public ResponseEntity<?> getCurrentPrice() {
		Long userId = 1L;
		List<PortfolioPriceResponseDTO> responseList = portfolioService.getCurrentPortfolioPrices(userId);
		return ResponseEntity.ok(responseList);
	}

	// 포트폴리오 종목 추천
	@GetMapping(value = "/portfolio_recommend")
	public ResponseEntity<?> recommendStocks() {
		if (!portfolioService.isTodayRecommendStock()) {
			String response = portfolioService.getRecommendStockFromAllenAI();
			portfolioService.saveRecommendStock(response);
		}
		List<PortfolioStockResponseDTO> allRecommendStock = portfolioService.getAllRecommendStock();
		return ResponseEntity.ok(allRecommendStock);
	}

	// 추천받은 종목 조회
	@GetMapping("/recommend")
	public ResponseEntity<?> getRecommendStock() {
		List<PortfolioStockResponseDTO> allRecommendStock = portfolioService.getAllRecommendStock();

		return ResponseEntity.ok(allRecommendStock);
	}

	// 오늘 포트폴리오 종목 추천을 받았는지 조회
	@GetMapping(value = "/is_recommend")
	public ResponseEntity<?> isRecommendStock() {
		return ResponseEntity.ok(Map.of(
			"message", "오늘 종목 추천을 받았는지 조회 성공",
			"data", portfolioService.isTodayRecommendStock()
		));
	}
}