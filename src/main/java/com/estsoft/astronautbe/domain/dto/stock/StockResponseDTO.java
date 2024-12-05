package com.estsoft.astronautbe.domain.dto.stock;

import com.estsoft.astronautbe.domain.Stock;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class StockResponseDTO {
	private String stockCode;
	private String stockName;

	public static StockResponseDTO fromEntity(Stock stock) {
		return new StockResponseDTO(stock.getStockCode(), stock.getStockName());
	}
}
