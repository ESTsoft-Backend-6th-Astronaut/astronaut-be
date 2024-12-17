package com.estsoft.astronautbe.repository;

import com.estsoft.astronautbe.domain.Portfolio;
import com.estsoft.astronautbe.domain.Stock;
import com.estsoft.astronautbe.domain.Users;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PortfolioRepository extends JpaRepository<Portfolio, Long> {
	List<Portfolio> findAllByUser_UsersId(Long usersId);
	Optional<Portfolio> findByUserAndStock(Users user, Stock stock);
}
