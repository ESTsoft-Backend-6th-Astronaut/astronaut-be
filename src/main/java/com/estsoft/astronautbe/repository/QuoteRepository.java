package com.estsoft.astronautbe.repository;

import com.estsoft.astronautbe.domain.FamousQuote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuoteRepository extends JpaRepository<FamousQuote, Long> {
}