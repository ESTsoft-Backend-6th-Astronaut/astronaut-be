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
public class SearchVolumeResponseDTO {

	private String startDate;
	private String endDate;
	private String timeUnit;
	private List<ResultItem> results;

	@Getter
	@Setter
	public static class ResultItem{
		private String title;
		private List<String> keywords;
		private List<DataItem> data;

		@Getter
		@Setter
		public static class DataItem{
			private String period;
			private String ratio;
		}
	}
}
