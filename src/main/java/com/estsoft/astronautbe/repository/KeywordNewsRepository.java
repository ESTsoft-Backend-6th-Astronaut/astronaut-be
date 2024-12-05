package com.estsoft.astronautbe.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.estsoft.astronautbe.domain.Keyword;
import com.estsoft.astronautbe.domain.KeywordNews;

public interface KeywordNewsRepository extends JpaRepository<KeywordNews, Long> {
	List<KeywordNews> findTop10ByKeywordOrderByCreatedAtDesc(Keyword keywordId);
}
