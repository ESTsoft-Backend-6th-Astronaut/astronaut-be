package com.estsoft.astronautbe.domain.dto.keywordNews;

import java.sql.Timestamp;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;

import com.estsoft.astronautbe.domain.KeywordNews;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class KeywordNewsResponseDTO {
	private Long newsId;
	private String title;
	private String newspaper;
	private String originalLink;
	private String summary;
	private String timeAgo;
	private String emotion;

	public static KeywordNewsResponseDTO fromEntity(KeywordNews keywordNews) {
		return KeywordNewsResponseDTO.builder()
			.newsId(keywordNews.getNewsId())
			.title(keywordNews.getTitle())
			.newspaper(keywordNews.getNewspaper())
			.originalLink(keywordNews.getOriginalLink())
			.summary(keywordNews.getSummary())
			.timeAgo(calculateTimeAgo(keywordNews.getPubDate()))
			.emotion(keywordNews.getEmotion() ? "부정적" : "긍정적")
			.build();
	}

	private static String calculateTimeAgo(Timestamp pubDate) {
		LocalDateTime pubDateTime = pubDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
		Duration duration = Duration.between(pubDateTime, LocalDateTime.now());
		long minutes = duration.toMinutes();
		long hours = duration.toHours();

		if (hours < 1) {
			return minutes + "분 전";
		} else {
			return hours + "시간 전";
		}
	}
}
