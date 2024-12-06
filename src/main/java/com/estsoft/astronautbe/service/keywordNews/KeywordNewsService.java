package com.estsoft.astronautbe.service.keywordNews;

import java.util.List;

import org.springframework.stereotype.Service;

import com.estsoft.astronautbe.domain.Keyword;
import com.estsoft.astronautbe.domain.KeywordNews;
import com.estsoft.astronautbe.domain.dto.keywordNews.KeywordNewsResponseDTO;
import com.estsoft.astronautbe.repository.KeywordNewsRepository;
import com.estsoft.astronautbe.repository.KeywordRepository;

@Service
public class KeywordNewsService {
	private final KeywordRepository keywordRepository;
	private final KeywordNewsRepository keywordNewsRepository;

	public KeywordNewsService(KeywordRepository keywordRepository, KeywordNewsRepository keywordNewsRepository) {
		this.keywordRepository = keywordRepository;
		this.keywordNewsRepository = keywordNewsRepository;
	}

	public List<KeywordNewsResponseDTO> getKeywordNewsByKeywordId(Long keywordId) {
		Keyword keyword = keywordRepository.findById(keywordId)
			.orElseThrow(() -> new IllegalArgumentException(keywordId + "에 해당하는 키워드가 없습니다."));
		List<KeywordNews> newsList = keywordNewsRepository.findTop10ByKeywordOrderByCreatedAtDesc(keyword);
		return newsList.stream()
			.map(KeywordNewsResponseDTO::fromEntity)
			.toList();
	}
}
