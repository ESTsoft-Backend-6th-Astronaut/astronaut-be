package com.estsoft.astronautbe.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.estsoft.astronautbe.domain.RecommendKeywordStock;
import com.estsoft.astronautbe.domain.Stock;
import com.estsoft.astronautbe.domain.dto.stock.StockDetailResponseDTO;
import com.estsoft.astronautbe.domain.dto.stock.StockResponseDTO;
import com.estsoft.astronautbe.repository.RecommendKeywordStockRepository;
import com.estsoft.astronautbe.repository.StockRepository;

@Service
public class StockService {

	private final StockRepository stockRepository;
	private final RecommendKeywordStockRepository stockKeywordRepository;

	public StockService(StockRepository stockRepository, RecommendKeywordStockRepository stockKeywordRepository) {
		this.stockRepository = stockRepository;
		this.stockKeywordRepository = stockKeywordRepository;
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

	public Stock findStockById(String stockCode) {
		return stockRepository.findById(stockCode)
			.orElseThrow(() -> new IllegalArgumentException(stockCode + "에 해당하는 종목이 없습니다."));
	}

	public StockDetailResponseDTO findStockByRecommendStockId(String stockType, Long stockId) {
		Stock stock;
		if (stockType.equals("RecommendKeywordStockDTO")) {
			RecommendKeywordStock recommendKeywordStock = stockKeywordRepository.findByRecommendStockId(stockId);
			stock = recommendKeywordStock.getStock();
		} else {
			stock = stockRepository.findById(stockId.toString())
				.orElseThrow(() -> new IllegalArgumentException(stockId + "의 stock 정보가 없습니다."));
		}
		return StockDetailResponseDTO.fromEntity(stock);
	}
}
