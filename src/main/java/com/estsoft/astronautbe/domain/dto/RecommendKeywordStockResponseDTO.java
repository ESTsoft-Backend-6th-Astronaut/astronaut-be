package com.estsoft.astronautbe.domain.dto;

import java.time.LocalDateTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Data
public class RecommendKeywordStockResponseDTO {
	private String content; // RecommendStockAnswer로 파싱되어 들어감

// 	private List<RecommendStockAnswer> content;
//
// }
// @Getter
// @Setter
// @Data
// class RecommendStockAnswer{
// 	private String message;
// 	private List<StockData> data;
// }
//
//
// @Getter
// @Setter
// @Data
// class StockData {
// 	private String StockCode;
// 	private String reason;
// 	private LocalDateTime createdAt;
}
