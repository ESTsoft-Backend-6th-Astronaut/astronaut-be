package com.estsoft.astronautbe.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.sql.Timestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Table(name = "portfolio")
@Getter
@Setter
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Portfolio {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "portfolio_id")
	private Long portfolioId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "stock_code", referencedColumnName = "stock_code", nullable = false)
	private Stock stock;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", nullable = false)
	private Users user;

	@Column(name = "average_price", nullable = false)
	private Integer averagePrice;

	@Column(name = "stock_count", nullable = false)
	private Double stockCount;

	@Column(name = "created_at", columnDefinition = "TIMESTAMP", nullable = false)
	@CreatedDate
	private Timestamp created_at;

	@Column(name = "updated_at", columnDefinition = "TIMESTAMP", nullable = false)
	@UpdateTimestamp
	private Timestamp updated_at;
}
