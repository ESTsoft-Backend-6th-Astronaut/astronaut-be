package com.estsoft.astronautbe.domain.keyword;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SearchVolume {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column
	private Long searchId;

	@Column
	private Long recommendStockId;

	@Column
	private Long keywordId;

	@Column
	private Double searchVolume;

	@Column
	private LocalDateTime searchDate;

	@Column
	private LocalDateTime createdAt;
}
