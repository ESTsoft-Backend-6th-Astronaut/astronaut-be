package com.estsoft.astronautbe.domain.dto;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class SearchVolumeWithStockDTO {
	private String keywordName;
	private String stockName;
	private String stockCode;
	private LocalDate searchDate;
	private Double searchVolume;


}
