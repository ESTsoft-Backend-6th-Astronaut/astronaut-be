package com.estsoft.astronautbe.domain.dto.portfolio;

import lombok.Getter;

@Getter
public class PortfolioPriceResponseDTO {
	private final String stockCode;
	private final String stockName;
	private final String currentTotalPrice;
	private final String stockCount;
	private final String profitOrLossPrice;

	public PortfolioPriceResponseDTO(String stockCode, String stockName, String currentTotalPrice, String stockCount, String profitOrLossPrice) {
		this.stockCode = stockCode;
		this.stockName = stockName;
		this.currentTotalPrice = currentTotalPrice;
		this.stockCount = stockCount;
		this.profitOrLossPrice = profitOrLossPrice;
	}
}
