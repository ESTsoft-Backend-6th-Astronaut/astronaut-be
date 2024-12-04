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
public class Keyword {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", columnDefinition = "BIGINT", nullable = false)
	private Long id;

	@Column(name = "keyword_name", columnDefinition = "VARCHAR(255)", nullable = false)
	private String keywordName;

	@Column(name = "created_At", columnDefinition = "TIMESTAMP", nullable = false)
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
