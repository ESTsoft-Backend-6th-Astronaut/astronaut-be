package com.estsoft.astronautbe.service;

import com.estsoft.astronautbe.domain.Keyword;
import com.estsoft.astronautbe.repository.KeywordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
}