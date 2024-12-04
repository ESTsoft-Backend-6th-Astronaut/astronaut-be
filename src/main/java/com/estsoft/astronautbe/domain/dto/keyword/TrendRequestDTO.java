package com.estsoft.astronautbe.domain.dto.keyword;

import jakarta.persistence.Entity;

@Entity
public class TrendRequestDTO {

	private String startDate;
	private String endDate;
	private String timeUnit;
	private String groupName;
	private String keywords;
}
