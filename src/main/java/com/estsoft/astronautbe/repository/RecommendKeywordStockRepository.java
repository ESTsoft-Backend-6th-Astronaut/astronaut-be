package com.estsoft.astronautbe.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.estsoft.astronautbe.domain.RecommendKeywordStock;

public interface RecommendKeywordStockRepository extends JpaRepository<RecommendKeywordStock, Long> {
	RecommendKeywordStock findByRecommendStockId(Long recommendStockId);

	List<RecommendKeywordStock> findByKeyword_KeywordId(Long keywordId);

}
