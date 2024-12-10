package com.estsoft.astronautbe.domain.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
public class RecommendKeywordStockRequestDTO {
	private String content;
	// 키워드 이름으로 검색>id와 매핑해서 저장

	public RecommendKeywordStockRequestDTO(String keyword){
		this.content = keyword;
	}
}


