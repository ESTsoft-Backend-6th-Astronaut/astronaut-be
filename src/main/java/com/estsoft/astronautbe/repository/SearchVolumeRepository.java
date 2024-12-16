package com.estsoft.astronautbe.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.estsoft.astronautbe.domain.SearchVolume;

public interface SearchVolumeRepository extends JpaRepository<SearchVolume,Long> {

	List<SearchVolume> findByKeyword_KeywordId(Long keywordId);

	@Query(value = "SELECT k.keyword_name, rks.stock_code, sv.search_date, sv.search_volume " +
			"FROM search_volume sv " +
			"LEFT JOIN recommend_keyword_stock rks ON sv.recommend_keyword_stock_id = rks.recommend_stock_id " +
			"LEFT JOIN keyword k ON rks.keyword_id = k.keyword_id " +
			"WHERE k.keyword_id = :keywordId " +
			"AND sv.search_date BETWEEN :startDate AND :endDate", nativeQuery = true)
	List<Object[]> findSearchVolumesWithStockCodeNative(
			@Param("keywordId") Long keywordId,
			@Param("startDate") LocalDateTime startDate,
			@Param("endDate") LocalDateTime endDate);

	List<SearchVolume> findByKeyword_KeywordIdAndRecommendKeywordStock_RecommendStockId(Long keywordId, Long recommendStockId);
}
