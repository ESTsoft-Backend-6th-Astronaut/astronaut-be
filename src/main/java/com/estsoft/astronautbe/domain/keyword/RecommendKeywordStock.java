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
public class RecommendKeywordStock {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column
	private Long recommendStockId;

	@Column
	private Long keywordId;

	@Column
	private String stockCode;

	@Column
	private String reason;

	@Column
	private LocalDateTime createdAt;

}
