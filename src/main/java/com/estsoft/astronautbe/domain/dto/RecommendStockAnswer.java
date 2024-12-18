package com.estsoft.astronautbe.domain.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RecommendStockAnswer {
	private String message;
	private List<RecommendStockData> data;

	@Getter
	@Setter
	@NoArgsConstructor
	@AllArgsConstructor
	public static class RecommendStockData {
		private String stockCode;
		private String reason;
	}
}




