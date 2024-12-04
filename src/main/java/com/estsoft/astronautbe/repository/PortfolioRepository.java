package com.estsoft.astronautbe.repository;

import com.estsoft.astronautbe.domain.Portfolio;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PortfolioRepository extends JpaRepository<Portfolio, Long> {
	List<Portfolio> findAllByUser_UsersId(Long usersId);
}
