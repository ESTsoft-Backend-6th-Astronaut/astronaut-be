package com.estsoft.astronautbe.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.web.embedded.NettyWebServerFactoryCustomizer;
import org.springframework.boot.http.client.ClientHttpRequestFactorySettings;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;

import com.estsoft.astronautbe.config.ApiConstants;
import com.estsoft.astronautbe.domain.Keyword;
import com.estsoft.astronautbe.domain.RecommendKeywordStock;
import com.estsoft.astronautbe.domain.SearchVolume;
import com.estsoft.astronautbe.domain.Stock;
import com.estsoft.astronautbe.domain.dto.RecommendKeywordStockDTO;
import com.estsoft.astronautbe.domain.dto.RecommendStockAnswer;
import com.estsoft.astronautbe.domain.dto.SearchVolumeRequestDTO;
import com.estsoft.astronautbe.domain.dto.SearchVolumeResponseDTO;
import com.estsoft.astronautbe.domain.dto.SearchVolumeWithStockDTO;
import com.estsoft.astronautbe.repository.KeywordRepository;
import com.estsoft.astronautbe.repository.RecommendKeywordStockRepository;
import com.estsoft.astronautbe.repository.SearchVolumeRepository;
import com.estsoft.astronautbe.repository.StockRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

@Service
public class KeywordService {

	private final KeywordRepository keywordRepository;
	private final SearchVolumeRepository searchVolumeRepository;
	private final RecommendKeywordStockRepository recommendKeywordStockRepository;
	private final StockRepository stockRepository;

	@Value("${naver.client.id}")
	private String naverClientId;

	@Value("${naver.client.secret}")
	private String naverClientSecret;

	@Value("${allen.client.id}")
	private String alanClientId;

	private final WebClient webClient;
	private final ObjectMapper objectMapper;

	public KeywordService(WebClient.Builder webClientBuilder, KeywordRepository keywordRepository,
		SearchVolumeRepository searchVolumeRepository, RecommendKeywordStockRepository recommendKeywordStockRepository,
		ObjectMapper objectMapper, StockRepository stockRepository,
		ClientHttpRequestFactorySettings clientHttpRequestFactorySettings,
		NettyWebServerFactoryCustomizer nettyWebServerFactoryCustomizer) {
		this.webClient = webClientBuilder.build();
		this.keywordRepository = keywordRepository;
		this.searchVolumeRepository = searchVolumeRepository;
		this.recommendKeywordStockRepository = recommendKeywordStockRepository;
		this.objectMapper = objectMapper;
		objectMapper.registerModule(new JavaTimeModule());
		this.stockRepository = stockRepository;
	}

	// 오늘 날짜의 키워드 ID 조회
	public List<Long> getTodayKeywordIds(LocalDateTime dateTime) {
		LocalDateTime startOfDay = LocalDateTime.now().toLocalDate().atStartOfDay();
		LocalDateTime endOfDay = startOfDay.plusDays(1).minusNanos(1);
		List<Keyword> keywords = keywordRepository.findByCreatedAtToday(startOfDay, endOfDay);
		return keywords.stream().map(Keyword::getKeywordId).collect(Collectors.toList());
	}

	// 추천 키워드 ID로 추천 키워드 이름 반환
	public String getKeywordNameById(Long keywordId) {
		Optional<Keyword> keyword = keywordRepository.findById(keywordId);
		return keyword.map(Keyword::getKeywordName)
			.orElseThrow(() -> new IllegalArgumentException("키워드 ID가 없습니다. : " + keywordId));
	}

	// alan api로 추천 종목 뽑아오기
	public List<RecommendKeywordStock> getRecommendKeywordStock(String keyword, Long keywordId) {
		try {
			String url = ApiConstants.ALLEN_API_URL;

			String prompt = keyword + " 키워드에 관련된 국내 주식 추천 종목을 5개 알려줘. " +
				"종목 코드, 추천 사유를 내가 제시하는 json 형태에 넣어줘. " +
				"답변은 순수한 json 배열 형태로만 보내줘. 다음은 내가 제시하는 형식이야.\n" +
				"{" +
				"  \"message\": \"키워드 추천 종목 조회 성공\"," +
				"  \"data\": [" +
				"    {" +
				"      \"stockCode\": \"String\"," +
				"      \"reason\": \"String\"" +
				"    }" +
				"  ]" +
				"}";

			String fullUrl = UriComponentsBuilder.fromHttpUrl(url)
				.queryParam("content", prompt)
				.queryParam("client_id", alanClientId)
				.toUriString();

			// 응답 받기
			String stringResponse = webClient.get()
				.uri(fullUrl)
				.accept(MediaType.APPLICATION_JSON)
				.retrieve()
				.bodyToMono(String.class)
				.block();

			return parseRecommendKeywordStock(stringResponse, keywordId);

		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("Alan API 호출 중 오류 발생");
		}
	}

	// 응답 가공
	private List<RecommendKeywordStock> parseRecommendKeywordStock(String stringResponse, Long keywordId) throws
		Exception {
		stringResponse = stringResponse.replace("\\n", "")     // 줄바꿈 제거
			.replace("```json", "") // ```json 제거
			.replace("```", "");    // 닫는 백틱 제거

		// json으로 파싱
		JsonNode rootNode = objectMapper.readTree(stringResponse);
		String content = rootNode.path("content").asText();
		RecommendStockAnswer answer = objectMapper.readValue(content, RecommendStockAnswer.class);

		// 엔티티에 매핑
		List<RecommendKeywordStock> stocks = answer.getData().stream()
			.map(recommendStockData -> {
				RecommendKeywordStock stock = new RecommendKeywordStock();
				stock.setKeywordId(keywordId);
				stock.setStockCode(recommendStockData.getStockCode());
				stock.setReason(recommendStockData.getReason());
				return stock;
			})
			.collect(Collectors.toList());

		recommendKeywordStockRepository.saveAll(stocks);

		return stocks;
	}

	// 추천 종목 리스트(이름X) 받아 종목 이름 리스트 반환
	public List<String> getStockNamesByRecommendKeywordStocks(List<RecommendKeywordStock> recommendKeywordStocks) {
		try {
			List<String> stockNames = new ArrayList<>();
			for (RecommendKeywordStock myRecommendStock : recommendKeywordStocks) {
				List<Stock> stocks = stockRepository.findByStockCode(myRecommendStock.getStockCode());
				if (!stocks.isEmpty()) {
					stockNames.add(stocks.get(0).getStockName());
				}
			}
			return stockNames;
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("주식 정보가 존재하지 않습니다.");
		}
	}

	// 단어 리스트로 검색량 조회
	public List<SearchVolume> getSearchAmount(List<String> stockNames, Long keywordId,
		List<RecommendKeywordStock> recommendKeywordStocks) {
		if (stockNames == null || stockNames.isEmpty()) {
			throw new IllegalArgumentException("주식 이름 리스트가 비어 있습니다.");
		}

		String url = ApiConstants.NAVER_TREND_API_URL;

		SearchVolumeRequestDTO request = new SearchVolumeRequestDTO(stockNames);

		SearchVolumeResponseDTO response = webClient.post()
			.uri(url)
			.header("X-Naver-Client-Id", naverClientId)
			.header("X-Naver-Client-Secret", naverClientSecret)
			.contentType(MediaType.APPLICATION_JSON)
			.bodyValue(request)
			.retrieve()
			.bodyToMono(SearchVolumeResponseDTO.class)
			.block();

		List<SearchVolume> searchVolumes = convertToSearchVolumes(response, request, keywordId, recommendKeywordStocks);
		searchVolumeRepository.saveAll(searchVolumes);

		return searchVolumes;
	}

	// 답변 가공
	private List<SearchVolume> convertToSearchVolumes(SearchVolumeResponseDTO apiResponse,
		SearchVolumeRequestDTO request, Long keywordId, List<RecommendKeywordStock> recommendKeywordStocks) {
		List<SearchVolume> searchVolumes = new ArrayList<>();

		for (SearchVolumeResponseDTO.ResultItem resultItem : apiResponse.getResults()) {
			RecommendKeywordStock matchingStock = recommendKeywordStocks.stream()
				.filter(stock -> {
					List<Stock> stocks = stockRepository.findByStockCode(stock.getStockCode());
					return !stocks.isEmpty() && stocks.get(0).getStockName().equals(resultItem.getTitle());
				})
				.findFirst()
				.orElseThrow(() -> new RuntimeException(("해당하는 주식이 존재하지 않습니다.")));

			SearchVolume volume = new SearchVolume();
			volume.setKeywordId(keywordId);
			volume.setRecommendStockId(matchingStock.getRecommendStockId());
			volume.setSearchVolume(Double.parseDouble(resultItem.getData().get(0).getRatio()));
			volume.setSearchDate(
				LocalDateTime.parse(resultItem.getData().get(0).getPeriod() + "T00:00:00",
					DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")));
			volume.setCreatedAt(LocalDateTime.now());
			searchVolumes.add(volume);
		}

		return searchVolumes;
	}

	// 전체 프로세스 실행 메서드

	public void processKeywordRecommendation() {

		// 오늘의 키워드+ID 조회
		LocalDateTime today = LocalDateTime.now();
		List<Long> todayKeywordIds = getTodayKeywordIds(today);

		// 각 키워드에 대해 반복
		for (Long keywordId : todayKeywordIds) {
			// 키워드 이름 조회
			String keywordName = getKeywordNameById(keywordId);
			// 추천 키워드에 관련된 종목 조회
			List<RecommendKeywordStock> recommendKeywordStocks = getRecommendKeywordStock(keywordName, keywordId);
			// 키워드 추천 종목 이름 조회
			List<String> recommendStockNames = getStockNamesByRecommendKeywordStocks(recommendKeywordStocks);
			// 키워드 추천 종목 검색량 조회
			List<SearchVolume> searchVolumes = getSearchAmount(recommendStockNames, keywordId, recommendKeywordStocks);
			// getSearchAmount(recommendStockNames, keywordId, recommendKeywordStocks);
		}
	}

	// 프론트엔드의 recommendstock에서 필요한대로 반환해주는 메소드
	public List<RecommendKeywordStockDTO> getRecommendStocks(Long keywordId) {
		List<RecommendKeywordStock> recommendKeywordStocks = recommendKeywordStockRepository.findByKeyword_KeywordId(
			keywordId);

		List<RecommendKeywordStockDTO> dtos = recommendKeywordStocks.stream()
			.map(recommendKeywordStock -> {
				Stock stock = recommendKeywordStock.getStock();
				return new RecommendKeywordStockDTO(recommendKeywordStock.getRecommendStockId(), stock.getStockName(),
					recommendKeywordStock.getReason(), stock.getStockPrice());
			}).collect(Collectors.toList());

		return dtos;
	}

	public List<SearchVolumeWithStockDTO> getRecommendStockWithSearchVolumes(Long keywordId) {
		// 추천 종목 조회
		List<RecommendKeywordStock> recommendKeywordStocks = recommendKeywordStockRepository.findByKeyword_KeywordId(
			keywordId);

		// 결과 리스트 준비
		List<SearchVolumeWithStockDTO> result = new ArrayList<>();

		for (RecommendKeywordStock stock : recommendKeywordStocks) {
			// 검색량 조회: keywordId와 recommendStockId 기준
			List<SearchVolume> searchVolumes = searchVolumeRepository.findByKeyword_KeywordIdAndRecommendKeywordStock_RecommendStockId(
				keywordId, stock.getRecommendStockId());

			// 검색량 데이터를 DTO로 변환
			for (SearchVolume volume : searchVolumes) {
				List<Stock> stocks = stockRepository.findByStockCode(stock.getStockCode());
				if (!stocks.isEmpty()) {
					String stockName = stocks.get(0).getStockName();

					SearchVolumeWithStockDTO detail = new SearchVolumeWithStockDTO(
						getKeywordNameById(keywordId),
						stockName,
						stock.getStockCode(),
						volume.getSearchDate().toLocalDate(),
						volume.getSearchVolume()
					);

					result.add(detail);
				}
			}
		}

		return result;
	}
}
