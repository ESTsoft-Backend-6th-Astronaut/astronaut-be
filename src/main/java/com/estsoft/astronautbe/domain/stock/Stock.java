package com.estsoft.astronautbe.domain.stock;

import java.time.LocalDateTime;

import org.springframework.cglib.core.Local;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
public class Stock {

	@Id
	@Column
	private String stockCode;

	@Column
	private String stockName;

	@Column
	private String marketName;

	@Column
	private String stockPrice;

	@Column
	private String contrast;

	@Column
	private String fluctuationRate;

	@Column
	private String marketPrice;

	@Column
	private String highPrice;

	@Column
	private String lowPrice;

	@Column
	private String volume;

	@Column
	private String dollarVolume;

	@Column
	private String marketCapitalization;

	@Column
	private String totalSharesOutstanding;

	@Column
	private LocalDateTime createdAt;

	@Column
	private LocalDateTime updatedAt;

}
