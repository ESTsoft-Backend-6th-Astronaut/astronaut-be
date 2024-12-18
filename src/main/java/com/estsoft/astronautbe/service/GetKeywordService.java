package com.estsoft.astronautbe.service;

import com.estsoft.astronautbe.domain.Keyword;
import com.estsoft.astronautbe.repository.KeywordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
public class GetKeywordService {

    @Autowired
    private KeywordRepository keywordRepository;

    public List<Keyword> getKeywords() {
        return keywordRepository.findAllByOrderByRankingAsc();
    }

    public List<Keyword> getInterestingKeywords() {
        return keywordRepository.findAllByOrderByInterestAsc();
    }

    public List<Keyword> getTodayKeywords() {
        LocalDateTime startOfDay = LocalDateTime.now().with(LocalTime.MIN);
        LocalDateTime endOfDay = LocalDateTime.now().with(LocalTime.MAX);
        List<Keyword> keywords = keywordRepository.findByCreatedAtToday(startOfDay, endOfDay);
        if (keywords.isEmpty()) {
            return keywordRepository.findTop10ByOrderByCreatedAtDescRankingAsc();
        }
        return keywords;
    }
}