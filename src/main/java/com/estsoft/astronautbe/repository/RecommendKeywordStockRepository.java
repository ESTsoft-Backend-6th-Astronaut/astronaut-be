package com.estsoft.astronautbe.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.estsoft.astronautbe.domain.RecommendKeywordStock;

public interface RecommendKeywordStockRepository extends JpaRepository<RecommendKeywordStock,Long> {
	Optional<RecommendKeywordStock> findByRecommendStockId(Long recommendStockId);
	Optional<RecommendKeywordStock> findByKeywordId(Long keywordId);
}
