package com.estsoft.astronautbe.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.estsoft.astronautbe.domain.Stock;

public interface StockRepository extends JpaRepository<Stock, String> {
	List<Stock> findStockByStockNameContaining(String query);
}
