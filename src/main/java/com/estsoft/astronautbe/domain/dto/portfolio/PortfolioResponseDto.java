package com.estsoft.astronautbe.domain.dto.portfolio;

import com.estsoft.astronautbe.domain.Portfolio;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PortfolioResponseDto {
	private Long portfolioId;
	private String stockName;
	private String stockCode;
	private Integer averagePrice;
	private Double stockCount;
	private Double totalPrice; // averagePrice * stockCount

	public static PortfolioResponseDto fromEntity(Portfolio portfolio) {
		return PortfolioResponseDto.builder()
			.portfolioId(portfolio.getPortfolioId())
			.stockName(portfolio.getStock().getStockName())
			.stockCode(portfolio.getStock().getStockCode())
			.averagePrice(portfolio.getAveragePrice())
			.stockCount(portfolio.getStockCount())
			.totalPrice(portfolio.getAveragePrice() * portfolio.getStockCount())
			.build();
	}
}