package com.estsoft.astronautbe.domain;

import java.sql.Timestamp;

import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "keyword_news")
@Getter
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class KeywordNews {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "news_id", nullable = false)
	private Long newsId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "keyword_id", referencedColumnName = "keyword_id", nullable = false)
	private Keyword keyword;

	@Column(name = "title", columnDefinition = "VARCHAR(255)", nullable = false)
	private String title;

	@Column(name = "newspaper", columnDefinition = "VARCHAR(255)", nullable = false)
	private String newspaper;

	@Setter
	@Column(name = "original_link", columnDefinition = "VARCHAR(255)", nullable = false)
	private String originalLink;

	@Column(name = "pub_date", columnDefinition = "TIMESTAMP", nullable = false)
	private Timestamp pubDate;

	@Column(name = "emotion", columnDefinition = "TINYINT(1)", nullable = false)
	private Boolean emotion;

	@Column(name = "summary", columnDefinition = "TEXT", nullable = false)
	private String summary;

	@Column(name = "created_at", columnDefinition = "TIMESTAMP", nullable = false)
	@CreatedDate
	private Timestamp createdAt;

	@Column(name = "updated_at", columnDefinition = "TIMESTAMP", nullable = false)
	@UpdateTimestamp
	private Timestamp updatedAt;

	public KeywordNews(Keyword keyword, String title, String newspaper, String originalLink, Timestamp pubDate,
		Boolean emotion,
		String summary) {
		this.keyword = keyword;
		this.title = title;
		this.newspaper = newspaper != null ? newspaper : "";
		this.originalLink = originalLink;
		this.pubDate = pubDate;
		this.emotion = emotion != null ? emotion : false;
		this.summary = summary != null ? summary : "";
	}

	public void updateAIInfo(String newspaper, Boolean emotion, String summary) {
		this.newspaper = newspaper;
		this.emotion = emotion;
		this.summary = summary;
	}
}



