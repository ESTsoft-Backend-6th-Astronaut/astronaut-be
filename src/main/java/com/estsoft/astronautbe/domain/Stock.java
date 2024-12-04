package com.estsoft.astronautbe.domain;

import java.sql.Timestamp;

import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "stock")
@Getter
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Stock {
	@Id
	@Column(name = "stock_code", columnDefinition = "VARCHAR(255)", nullable = false)
	private String stockCode;

	@Column(name = "stock_name", columnDefinition = "VARCHAR(255)", nullable = false)
	private String stockName;

	@Column(name = "market_name", columnDefinition = "VARCHAR(255)", nullable = false)
	private String marketName;

	@Column(name = "stock_price", columnDefinition = "VARCHAR(255)", nullable = false)
	private String stockPrice;

	@Column(name = "contrast", columnDefinition = "VARCHAR(255)", nullable = false)
	private String contrast;

	@Column(name = "fluctuation_rate", columnDefinition = "VARCHAR(255)", nullable = false)
	private String fluctuationRate;

	@Column(name = "market_price", columnDefinition = "VARCHAR(255)", nullable = false)
	private String marketPrice;

	@Column(name = "high_price", columnDefinition = "VARCHAR(255)", nullable = false)
	private String highPrice;

	@Column(name = "low_price", columnDefinition = "VARCHAR(255)", nullable = false)
	private String lowPrice;

	@Column(name = "volume", columnDefinition = "VARCHAR(255)", nullable = false)
	private String volume;

	@Column(name = "dollar_volume", columnDefinition = "VARCHAR(255)", nullable = false)
	private String dollarVolume;

	@Column(name = "market_capitalization", columnDefinition = "VARCHAR(255)", nullable = false)
	private String marketCapitalization;

	@Column(name = "total_shares_outstanding", columnDefinition = "VARCHAR(255)", nullable = false)
	private String totalSharesOutstanding;

	@Column(name = "created_at", columnDefinition = "TIMESTAMP", nullable = false)
	@CreatedDate
	private Timestamp createdAt;

	@Column(name = "updated_at", columnDefinition = "TIMESTAMP", nullable = false)
	@UpdateTimestamp
	private Timestamp updateAt;
}
