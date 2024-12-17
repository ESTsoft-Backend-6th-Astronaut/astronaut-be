package com.estsoft.astronautbe.service.keywordNews;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.estsoft.astronautbe.domain.Keyword;
import com.estsoft.astronautbe.domain.KeywordNews;
import com.estsoft.astronautbe.domain.dto.keywordNews.KeywordNewsResponseDTO;
import com.estsoft.astronautbe.repository.KeywordNewsRepository;
import com.estsoft.astronautbe.repository.KeywordRepository;
import com.estsoft.astronautbe.service.KeywordService;

@Service
public class KeywordNewsService {
	private final KeywordRepository keywordRepository;
	private final KeywordNewsRepository keywordNewsRepository;
	private final KeywordService keywordService;
	private final NaverNewsApiService naverNewsApiService;
	private final AllenAIService allenAIService;

	public KeywordNewsService(KeywordRepository keywordRepository, KeywordNewsRepository keywordNewsRepository,
		KeywordService keywordService, NaverNewsApiService naverNewsApiService, AllenAIService allenAIService) {
		this.keywordRepository = keywordRepository;
		this.keywordNewsRepository = keywordNewsRepository;
		this.keywordService = keywordService;
		this.naverNewsApiService = naverNewsApiService;
		this.allenAIService = allenAIService;
	}

	public List<KeywordNewsResponseDTO> getKeywordNewsByKeywordId(Long keywordId) {
		Keyword keyword = keywordRepository.findById(keywordId)
			.orElseThrow(() -> new IllegalArgumentException(keywordId + "에 해당하는 키워드가 없습니다."));
		List<KeywordNews> newsList = keywordNewsRepository.findTop10ByKeywordOrderByCreatedAtDesc(keyword);
		return newsList.stream()
			.map(KeywordNewsResponseDTO::fromEntity)
			.toList();
	}

	public void processKeywordNews() {

		LocalDateTime today = LocalDateTime.now();
		List<Long> todayKeywordIds = keywordService.getTodayKeywordIds(today);

		for (Long keywordId : todayKeywordIds) {
			String keywordName = keywordService.getKeywordNameById(keywordId);

			String response = naverNewsApiService.getNews(keywordName);
			naverNewsApiService.saveNewsToDatabase(response, keywordId);

			response = allenAIService.getAnswerFromAllenAI(keywordId);
			allenAIService.saveNewsToDatabase(response);
		}
	}
}
