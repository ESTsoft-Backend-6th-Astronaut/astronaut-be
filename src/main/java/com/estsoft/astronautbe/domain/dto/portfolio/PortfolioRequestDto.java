package com.estsoft.astronautbe.domain.dto.portfolio;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PortfolioRequestDto {
	private String stockCode;
	private Integer averagePrice;
	private Double stockCount;
}