package com.estsoft.astronautbe.domain.dto.stock;

import com.estsoft.astronautbe.domain.Stock;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class StockDetailResponseDTO {
	private String stockCode;
	private String stockName;
	private String marketName;
	private String stockPrice;
	private String contrast;
	private String fluctuationRate;
	private String marketPrice;
	private String highPrice;
	private String lowPrice;
	private String volume;
	private String dollarVolume;
	private String marketCapitalization;
	private String totalSharesOutstanding;

	public static StockDetailResponseDTO fromEntity(Stock stock) {
		return StockDetailResponseDTO.builder()
			.stockCode(stock.getStockCode())
			.stockName(stock.getStockName())
			.marketName(stock.getMarketName())
			.stockPrice(stock.getStockPrice())
			.contrast(stock.getContrast())
			.fluctuationRate(stock.getFluctuationRate())
			.marketPrice(stock.getMarketPrice())
			.highPrice(stock.getHighPrice())
			.lowPrice(stock.getLowPrice())
			.volume(stock.getVolume())
			.dollarVolume(stock.getDollarVolume())
			.marketCapitalization(stock.getMarketCapitalization())
			.totalSharesOutstanding(stock.getTotalSharesOutstanding())
			.build();
	}
}
