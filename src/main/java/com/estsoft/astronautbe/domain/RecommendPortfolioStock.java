package com.estsoft.astronautbe.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "recommend_portfolio_stock")
@Getter
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class RecommendPortfolioStock {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "recommend_portfolio_id", nullable = false)
	private Long recommendPortfolioId;

	// 임시
	@Column(name = "users_id", nullable = false)
	private Long userId;

	// 임시
	@Column(name = "stock_code", nullable = false)
	private String stockCode;

	@Column(name = "reason", columnDefinition = "TEXT", nullable = false)
	private String reason;

	@Column(name = "created_at", nullable = false, updatable = false)
	@CreatedDate
	private LocalDateTime createdAt;
}