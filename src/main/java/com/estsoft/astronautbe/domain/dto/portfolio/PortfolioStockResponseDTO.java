package com.estsoft.astronautbe.domain.dto.portfolio;

import com.estsoft.astronautbe.domain.RecommendPortfolioStock;

import lombok.Getter;

@Getter
public class PortfolioStockResponseDTO {
	private final String reason;
	private final String stockCode;
	private final String stockName;
	private final String stockPrice;

	public PortfolioStockResponseDTO(RecommendPortfolioStock recommendStock) {
		this.stockCode = recommendStock.getStock().getStockCode();
		this.stockName = recommendStock.getStock().getStockName();
		this.reason = recommendStock.getReason();
		this.stockPrice = recommendStock.getStock().getStockPrice();
	}
}
