package com.estsoft.astronautbe.domain;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "recommend_keyword_stock")
public class RecommendKeywordStock {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "recommend_stock_id", columnDefinition = "BIGINT", nullable = false)
	private Long recommendStockId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "keyword_id", referencedColumnName = "keyword_id", nullable = false)
	private Keyword keyword;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "stock_code", referencedColumnName = "stock_code", nullable = false)
	private Stock stock;

	@Column(name = "reason", columnDefinition = "TEXT", nullable = false)
	private String reason;

	@CreatedDate
	@Column(name = "created_at", columnDefinition = "TIMESTAMP", nullable = false, updatable = false)
	private LocalDateTime createdAt;

	public void setKeywordId(Long keywordId) {
		this.keyword = new Keyword();
		this.keyword.setKeywordId(keywordId);
	}

	public Long getKeywordId() {
		return this.keyword != null ? this.keyword.getKeywordId() : null;
	}

	public void setStockCode(String stockCode) {
		this.stock = new Stock();
		this.stock.setStockCode(stockCode);
	}

	public String getStockCode() {
		return this.stock != null ? this.stock.getStockCode() : null;
	}
}
