package com.estsoft.astronautbe.domain;

import java.time.LocalDateTime;

import org.hibernate.annotations.UpdateTimestamp;
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
@Table(name = "search_volume")
public class SearchVolume {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "search_id", columnDefinition = "BIGINT", nullable = false)
	private Long searchId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "recommend_stock_id", referencedColumnName = "recommend_stock_id", nullable = false)
	private RecommendKeywordStock recommendKeywordStock;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "keyword_id", referencedColumnName = "keyword_id", nullable = false)
	private Keyword keyword;

	@Column(name = "search_volume", columnDefinition = "DOUBLE", nullable = false)
	private Double searchVolume;

	@UpdateTimestamp
	@Column(name = "search_date", columnDefinition = "TIMESTAMP", nullable = false)
	private LocalDateTime searchDate;

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

	public void setRecommendStockId(Long recommendStockId) {
		this.recommendKeywordStock = new RecommendKeywordStock();
		this.recommendKeywordStock.setRecommendStockId(recommendStockId);
	}

	public Long getRecommendStockId() {
		return this.recommendKeywordStock != null ? this.recommendKeywordStock.getRecommendStockId() : null;
	}

}
