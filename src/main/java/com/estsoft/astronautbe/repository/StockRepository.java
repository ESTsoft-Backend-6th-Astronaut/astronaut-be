package com.estsoft.astronautbe.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.estsoft.astronautbe.domain.Stock;

public interface StockRepository extends JpaRepository<Stock, String> {
	List<Stock> findStockByStockNameContaining(String query);

	List<Stock> findByStockCode(String stockCode);

	Optional<Stock> findStockNameByStockCode(String stockCode);
}
