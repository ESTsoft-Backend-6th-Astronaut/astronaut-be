package com.estsoft.astronautbe.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.estsoft.astronautbe.domain.Stock;
import com.estsoft.astronautbe.domain.dto.stock.StockDetailResponseDTO;
import com.estsoft.astronautbe.domain.dto.stock.StockResponseDTO;
import com.estsoft.astronautbe.repository.StockRepository;

@Service
public class StockService {

	private final StockRepository stockRepository;

	public StockService(StockRepository stockRepository) {
		this.stockRepository = stockRepository;
	}

	public List<StockResponseDTO> findStockByQuery(String query) {
		List<Stock> stockList = stockRepository.findStockByStockNameContaining(query);
		return stockList.stream()
			.map(StockResponseDTO::fromEntity)
			.toList();
	}

	public StockDetailResponseDTO findStockByCode(String StockCode) {
		Stock stock = stockRepository.findById(StockCode)
			.orElseThrow(() -> new IllegalArgumentException(StockCode + "의 종목이 없습니다."));
		return StockDetailResponseDTO.fromEntity(stock);
	}
}
