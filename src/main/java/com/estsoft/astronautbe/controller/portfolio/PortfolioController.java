package com.estsoft.astronautbe.controller.portfolio;

import com.estsoft.astronautbe.domain.dto.portfolio.PortfolioRequestDto;
import com.estsoft.astronautbe.domain.dto.portfolio.PortfolioResponseDto;
import com.estsoft.astronautbe.service.PortfolioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/portfolios")
@RequiredArgsConstructor
public class PortfolioController {

	private final PortfolioService portfolioService;

	// 전쳊 조회
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
		@RequestBody PortfolioRequestDto requestDto) {

		Long userId = 1L; // 임시 userId

		PortfolioResponseDto responseDto = portfolioService.createPortfolio(userId, requestDto);

		return ResponseEntity.ok()
			.body(Map.of(
				"message", "포트폴리오 등록 성공",
				"data", responseDto
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
}