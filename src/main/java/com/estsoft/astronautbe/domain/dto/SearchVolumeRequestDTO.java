package com.estsoft.astronautbe.domain.dto;

import java.time.LocalDateTime;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SearchVolumeRequestDTO {

	private String startDate;
	private String endDate;
	private String timeUnit;
	private List<KeywordGroup> keywordGroups;

	@Getter
	@Setter
	public static class KeywordGroup{
		private String groupName;
		private List<String> keywords;
	}

	public SearchVolumeRequestDTO(List<String> keywords) {
		this.startDate = DateTimeFormatter.ofPattern("yyyy-MM-dd").format(LocalDateTime.now().minus(Period.ofDays(1)));
		// this.startDate = DateTimeFormatter.ofPattern("yyyy-MM-dd").format(LocalDateTime.now());
		this.endDate = this.startDate;
		this.timeUnit = "date";

		this.keywordGroups = new ArrayList<>();
		for (String keyword : keywords) {
			KeywordGroup keywordGroup = new KeywordGroup();
			keywordGroup.setGroupName(keyword);
			keywordGroup.setKeywords(List.of(keyword));
			this.keywordGroups.add(keywordGroup);
		}
	}
}


