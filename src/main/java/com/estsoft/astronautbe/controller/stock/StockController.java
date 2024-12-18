package com.estsoft.astronautbe.controller.stock;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.estsoft.astronautbe.domain.dto.stock.StockDetailResponseDTO;
import com.estsoft.astronautbe.domain.dto.stock.StockResponseDTO;
import com.estsoft.astronautbe.service.StockService;

@RequestMapping("/api/stock")
@RestController
public class StockController {

	private final StockService stockService;

	public StockController(StockService stockService) {
		this.stockService = stockService;
	}

	@GetMapping
	public ResponseEntity<?> searchStock(@RequestParam(name = "query", required = false) String query) {
		List<StockResponseDTO> stocks = stockService.findStockByQuery(query);
		return ResponseEntity.ok()
			.body(Map.of(
				"message", "주식 종목 검색 성공",
				"data", stocks
			));
	}

	@GetMapping("/{stock_code}")
	public ResponseEntity<?> getStockDetail(@PathVariable(name = "stock_code") String stockCode) {
		StockDetailResponseDTO stock = stockService.findStockByCode(stockCode);
		return ResponseEntity.ok()
			.body(Map.of(
				"message", "종목 상세 조회 성공",
				"data", stock
			));
	}

	// stock -> recommend_stock_id parameter로 받아서 stock_code 조회해서 리턴
	@GetMapping("/stock_detail")
	public ResponseEntity<?> getStock(@RequestParam(name = "stockId") Long stockId,
		@RequestParam(name = "dataType") String stockType) {
		StockDetailResponseDTO stock = stockService.findStockByRecommendStockId(stockType, stockId);
		return ResponseEntity.ok()
			.body(Map.of(
				"message", "종목 상세 조회 성공",
				"data", stock
			));
	}
}
