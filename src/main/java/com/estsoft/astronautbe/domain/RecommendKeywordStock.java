package com.estsoft.astronautbe.domain;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;

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
	@Column(name = "recommend_stock_id", columnDefinition = "BIGINT", nullable = false)
	private Long recommendStockId;

	@Column(name = "keyword_id", columnDefinition = "BIGINT", nullable = false)
	private Long keywordId;

	@Column(name = "stock_code", columnDefinition = "VARCHAR(255)", nullable = false)
	private String stockCode;

	@Column(name = "reason", columnDefinition = "TEXT", nullable = false)
	private String reason;

	@CreatedDate
	@Column(name = "created_at", columnDefinition = "TIMESTAMP", nullable = false)
	private LocalDateTime createdAt;

}
