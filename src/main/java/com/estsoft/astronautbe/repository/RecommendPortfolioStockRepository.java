package com.estsoft.astronautbe.repository;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.estsoft.astronautbe.domain.RecommendPortfolioStock;

public interface RecommendPortfolioStockRepository extends JpaRepository<RecommendPortfolioStock, Long> {
	List<RecommendPortfolioStock> findAllByUser_UsersIdAndCreatedAtBetween(Long userId, Timestamp startOfDay, Timestamp endOfDay);
}
