package com.estsoft.astronautbe.domain;

import java.time.LocalDateTime;

import org.hibernate.type.descriptor.jdbc.TinyIntAsSmallIntJdbcType;

import com.fasterxml.jackson.annotation.JsonAnyGetter;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
public class Keyword {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column
	private Long id;

	@Column
	private String keywordName;

	@Column
	private LocalDateTime createdAt;

	@Column
	private int interest;

	@Column
	private int emotion;

	@Column
	private String reason;

	@Column
	private int ranking;
}
