package com.estsoft.astronautbe.service;


import java.util.List;
import java.util.stream.Collectors;

import com.estsoft.astronautbe.domain.Portfolio;
import com.estsoft.astronautbe.domain.Stock;
import com.estsoft.astronautbe.domain.Users;
import com.estsoft.astronautbe.domain.dto.portfolio.PortfolioRequestDto;
import com.estsoft.astronautbe.domain.dto.portfolio.PortfolioResponseDto;
import com.estsoft.astronautbe.repository.PortfolioRepository;
import com.estsoft.astronautbe.repository.StockRepository;
import com.estsoft.astronautbe.repository.UsersRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PortfolioService {

	private final PortfolioRepository portfolioRepository;
	private final StockRepository stockRepository;
	private final UsersRepository usersRepository;

	public List<PortfolioResponseDto> getAllPortfolios(Long userId) {
		return portfolioRepository.findAllByUser_UsersId(userId).stream()
			.map(PortfolioResponseDto::fromEntity)
			.collect(Collectors.toList());
	}

	@Transactional
	public PortfolioResponseDto createPortfolio(Long userId, PortfolioRequestDto requestDto) {
		Stock stock = stockRepository.findById(requestDto.getStockCode())
			.orElseThrow(() -> new IllegalArgumentException("Stock not found: " + requestDto.getStockCode()));

		Users user = usersRepository.findById(userId)
			.orElseThrow(() -> new IllegalArgumentException("User not found: " + userId));

		Portfolio portfolio = new Portfolio();
		portfolio.setStock(stock);
		portfolio.setUser(user);
		portfolio.setAveragePrice(requestDto.getAveragePrice());
		portfolio.setStockCount(requestDto.getStockCount());

		Portfolio savedPortfolio = portfolioRepository.save(portfolio);
		return PortfolioResponseDto.fromEntity(savedPortfolio);
	}


	@Transactional
	public PortfolioResponseDto updatePortfolio(Long portfolioId, PortfolioRequestDto requestDto) {
		Portfolio portfolio = portfolioRepository.findById(portfolioId)
			.orElseThrow(() -> new IllegalArgumentException("Portfolio not found: " + portfolioId));

		Stock stock = stockRepository.findById(requestDto.getStockCode())
			.orElseThrow(() -> new IllegalArgumentException("Stock not found: " + requestDto.getStockCode()));

		portfolio.setStock(stock);
		portfolio.setAveragePrice(requestDto.getAveragePrice());
		portfolio.setStockCount(requestDto.getStockCount());

		Portfolio updatedPortfolio = portfolioRepository.save(portfolio);
		return PortfolioResponseDto.fromEntity(updatedPortfolio);
	}

	// 삭제
	@Transactional
	public void deletePortfolio(Long portfolioId) {
		Portfolio portfolio = portfolioRepository.findById(portfolioId)
			.orElseThrow(() -> new IllegalArgumentException("Portfolio not found: " + portfolioId));

		portfolioRepository.delete(portfolio);
	}
}
