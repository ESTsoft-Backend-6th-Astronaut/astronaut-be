package com.estsoft.astronautbe.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class RecommendKeywordStockDTO {
	private Long recommendStockId;
	private String stockName;
	private String reason;
	private String stockPrice;

}
