package com.estsoft.astronautbe.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.estsoft.astronautbe.domain.Keyword;

@Repository
public interface KeywordRepository extends JpaRepository<Keyword,Long> {
	Optional<Keyword> findByKeywordName(String keywordName);

	@Query("SELECT k FROM Keyword k WHERE DATE(k.createdAt) = :today ")
	List<Keyword> findByCreatedAtToday(@Param("today") LocalDateTime createdAt);

	List<Keyword> findAllByOrderByRankingAsc();

	List<Keyword> findAllByOrderByInterestAsc();

}
