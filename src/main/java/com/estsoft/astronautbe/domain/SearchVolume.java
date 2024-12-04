package com.estsoft.astronautbe.domain;

import java.time.LocalDateTime;

import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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

	@Column(name = "recommend_stock_id", columnDefinition = "BIGINT", nullable = false)
	private Long recommendStockId;

	@Column(name = "keyword_id", columnDefinition = "BIGINT", nullable = false)
	private Long keywordId;

	@Column(name = "search_volume", columnDefinition = "DOUBLE", nullable = false)
	private Double searchVolume;

	@UpdateTimestamp
	@Column(name = "search_date", columnDefinition = "TIMESTAMP", nullable = false)
	private LocalDateTime searchDate;

	@CreatedDate
	@Column(name = "created_at", columnDefinition = "TIMESTAMP", nullable = false)
	private LocalDateTime createdAt;
}
