package com.estsoft.astronautbe.service;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.estsoft.astronautbe.config.ApiConstants;
import com.estsoft.astronautbe.domain.Portfolio;
import com.estsoft.astronautbe.domain.RecommendPortfolioStock;
import com.estsoft.astronautbe.domain.Stock;
import com.estsoft.astronautbe.domain.Users;
import com.estsoft.astronautbe.domain.dto.portfolio.PortfolioPriceResponseDTO;
import com.estsoft.astronautbe.domain.dto.portfolio.PortfolioRequestDto;
import com.estsoft.astronautbe.domain.dto.portfolio.PortfolioResponseDto;
import com.estsoft.astronautbe.domain.dto.portfolio.PortfolioStockResponseDTO;
import com.estsoft.astronautbe.repository.PortfolioRepository;
import com.estsoft.astronautbe.repository.RecommendPortfolioStockRepository;
import com.estsoft.astronautbe.repository.StockRepository;
import com.estsoft.astronautbe.repository.UsersRepository;
import com.estsoft.astronautbe.util.JsonParserUtil;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.transaction.Transactional;

@Service
public class PortfolioService {

	private final PortfolioRepository portfolioRepository;
	private final StockRepository stockRepository;
	private final UsersRepository usersRepository;
	private final RecommendPortfolioStockRepository recommendPortfolioStockRepository;
	private final StockService stockService;

	public PortfolioService(PortfolioRepository portfolioRepository,
		StockRepository stockRepository, UsersRepository usersRepository,
		RecommendPortfolioStockRepository recommendPortfolioStockRepository, StockService stockService) {
		this.portfolioRepository = portfolioRepository;
		this.stockRepository = stockRepository;
		this.usersRepository = usersRepository;
		this.recommendPortfolioStockRepository = recommendPortfolioStockRepository;
		this.stockService = stockService;
	}

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

		Optional<Portfolio> existingPortfolio = portfolioRepository.findByUserAndStock(user, stock);
		if (existingPortfolio.isPresent()) {
			throw new IllegalArgumentException("Portfolio already exists for user and stock.");
		}

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

	@Value("${allen.client.id}")
	private String clientId;

	private final RestTemplate restTemplate = new RestTemplate();

	public String getRecommendStockFromAllenAI() {
		String baseUrl = ApiConstants.ALLEN_API_URL;

		Long userId = 1L;
		String content = createPrompt(userId);

		try {
			String encodedContent = URLEncoder.encode(content, "UTF-8");
			String encodedClientId = URLEncoder.encode(clientId, "UTF-8");

			String requestUrl = String.format("%s?content=%s&client_id=%s", baseUrl, encodedContent, encodedClientId);
			URI uri = new URI(requestUrl);

			HttpHeaders headers = new HttpHeaders();
			headers.set("Accept", "application/json");

			HttpEntity<String> entity = new HttpEntity<>(headers);

			ResponseEntity<String> responseEntity = restTemplate.exchange(
				uri,
				HttpMethod.GET,
				entity,
				String.class
			);

			return JsonParserUtil.extractJsonFromContent(responseEntity.getBody());
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException("인코딩 실패: " + e.getMessage(), e);
		} catch (URISyntaxException e) {
			throw new RuntimeException("잘못된 URI: " + e.getMessage(), e);
		} catch (Exception e) {
			throw new RuntimeException("앨런 AI API 요청 실패: " + e.getMessage(), e);
		}
	}

	public String createPrompt(Long userId) {
		List<PortfolioResponseDto> portfolios = getAllPortfolios(userId);

		return portfolios
			+ "위의 데이터들은 보유하고 있는 주식에 대한 포트폴리오 정보야. "
			+ "stockName은 종목명, stockCode는 종목코드, averagePrice는 구매한 평균 가격, stockCount는 보유하고 있는 주식 개수, totalPrice는 구매한 전체 가격이야. "
			+ "이 주식 포트폴리오를 기반으로 리스크가 적게 안전하게 분산투자 할 수 있는 국내 주식 종목 5개를 추천해줘. "
			+ "종목 코드, 3문장의 보유 포트폴리오 기반으로 추천하는 사유를 내가 제시하는 json 형태에 넣어줘. " +
			"답변은 순수한 json 형태로만 보내줘. 다음은 내가 제시하는 형식이야.\n" +
			"{" +
			"  \"message\": \"키워드 추천 종목 조회 성공\"," +
			"  \"data\": [" +
			"    {" +
			"      \"stockCode\": \"String\"," +
			"      \"reason\": \"String\"" +
			"    }" +
			"  ]" +
			"}";
	}

	public void saveRecommendStock(String responseBody) {
		try {
			Long userId = 1L;
			ObjectMapper objectMapper = new ObjectMapper();
			JsonNode rootNode = objectMapper.readTree(responseBody);
			JsonNode itemsNode = rootNode.get("data");

			if (itemsNode != null && itemsNode.isArray()) {
				for (JsonNode itemNode : itemsNode) {
					String stockCode = itemNode.get("stockCode").asText();
					String reason = itemNode.get("reason").asText();

					Users users = usersRepository.findById(userId)
						.orElseThrow(() -> new IllegalArgumentException(userId + "의 유저를 찾을 수 없습니다."));
					Stock stock = stockRepository.findById(stockCode)
						.orElseThrow(() -> new IllegalArgumentException(stockCode + "의 종목을 찾을 수 없습니다."));

					recommendPortfolioStockRepository.save(new RecommendPortfolioStock(users, stock, reason));
				}
			}
		} catch (Exception e) {
			throw new RuntimeException("추천 종목 데이터를 저장하는 동안 오류 발생", e);
		}
	}

	public Boolean isTodayRecommendStock() {
		// 임시
		Long userId = 1L;
		LocalDate today = LocalDate.now();
		Timestamp startOfDay = Timestamp.valueOf(today.atStartOfDay()); // 시작 시간
		Timestamp endOfDay = Timestamp.valueOf(today.atTime(LocalTime.MAX)); // 끝 시간

		List<RecommendPortfolioStock> recommendStocks = recommendPortfolioStockRepository
			.findAllByUser_UsersIdAndCreatedAtBetween(userId, startOfDay, endOfDay);

		return !recommendStocks.isEmpty();
	}

	public List<PortfolioStockResponseDTO> getAllRecommendStock() {
		// 임시
		Long userId = 1L;
		LocalDate today = LocalDate.now();
		Timestamp startOfDay = Timestamp.valueOf(today.atStartOfDay()); // 시작 시간
		Timestamp endOfDay = Timestamp.valueOf(today.atTime(LocalTime.MAX)); // 끝 시간

		List<RecommendPortfolioStock> recommendStocks = recommendPortfolioStockRepository
			.findAllByUser_UsersIdAndCreatedAtBetween(userId, startOfDay, endOfDay);
		return recommendStocks.stream().map(
				PortfolioStockResponseDTO::new
			)
			.toList();
	}

	public List<PortfolioPriceResponseDTO> getCurrentPortfolioPrices(Long userId) {
		List<PortfolioResponseDto> allPortfolios = getAllPortfolios(userId);

		return allPortfolios.stream()
			.map(portfolio -> {
				Stock stock = stockService.findStockById(portfolio.getStockCode());

				double currentStockPrice = Double.parseDouble(stock.getStockPrice());
				double stockCount = portfolio.getStockCount();
				int buyStockPrice = portfolio.getAveragePrice();
				String currentTotalPrice = String.format("%,d", (int)(currentStockPrice * stockCount));

				String profitOrLossPriceStr;
				int profitOrLossPrice = (int)((currentStockPrice - buyStockPrice) * stockCount);
				if (profitOrLossPrice > 0) {
					profitOrLossPriceStr = "+" + String.format("%,d", profitOrLossPrice);
				} else {
					profitOrLossPriceStr = String.format("%,d", profitOrLossPrice);
				}

				String formattedStockCount;
				if (stockCount % 1 == 0) {
					formattedStockCount = String.format("%,d", (int)stockCount);
				} else {
					DecimalFormat decimalFormat = new DecimalFormat("#,###.##");
					formattedStockCount = decimalFormat.format(stockCount);
				}

				return new PortfolioPriceResponseDTO(
					portfolio.getStockCode(),
					stock.getStockName(),
					currentTotalPrice,
					formattedStockCount,
					profitOrLossPriceStr
				);
			})
			.toList();
	}
}