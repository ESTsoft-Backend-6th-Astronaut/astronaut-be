package com.estsoft.astronautbe.controller.stock;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.estsoft.astronautbe.domain.dto.stock.StockResponseDTO;
import com.estsoft.astronautbe.service.StockService;

@RestController
public class StockController {

	private final StockService stockService;

	public StockController(StockService stockService) {
		this.stockService = stockService;
	}

	@GetMapping("/api/stock")
	public ResponseEntity<?> searchStock(@RequestParam(name = "query", required = false) String query) {
		List<StockResponseDTO> stocks = stockService.findStockByQuery(query);
		return ResponseEntity.ok()
			.body(Map.of(
				"message", "주식 종목 검색 성공",
				"data", stocks
			));
	}

}
