package com.estsoft.astronautbe.domain;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
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
@Table(name = "keyword")
@EntityListeners(AuditingEntityListener.class)
public class Keyword {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "keyword_id", columnDefinition = "BIGINT", nullable = false)
	private Long keywordId;

	@Column(name = "keyword_name", columnDefinition = "VARCHAR(255)", nullable = false)
	private String keywordName;

	@Column(name = "created_at", columnDefinition = "TIMESTAMP", nullable = false)
	@CreatedDate
	private LocalDateTime createdAt;

	@Column(name = "interest", columnDefinition = "INT", nullable = false)
	private int interest;

	@Column(name = "emotion", columnDefinition = "TINYINT", nullable = false)
	private int emotion;

	@Column(name = "reason", columnDefinition = "TEXT", nullable = false)
	private String reason;

	@Column(name = "ranking", columnDefinition = "INT", nullable = false)
	private int ranking;

}
