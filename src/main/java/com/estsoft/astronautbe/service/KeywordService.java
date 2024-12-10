package com.estsoft.astronautbe.service;

import com.estsoft.astronautbe.domain.Keyword;
import com.estsoft.astronautbe.domain.RecommendKeywordStock;
import com.estsoft.astronautbe.domain.SearchVolume;
import com.estsoft.astronautbe.domain.Stock;
import com.estsoft.astronautbe.repository.StockRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.http.client.ClientHttpRequestFactorySettings;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;

import com.estsoft.astronautbe.config.ApiConstants;
import com.estsoft.astronautbe.domain.dto.RecommendKeywordStockRequestDTO;
import com.estsoft.astronautbe.domain.dto.RecommendKeywordStockResponseDTO;
import com.estsoft.astronautbe.domain.dto.RecommendStockAnswer;
import com.estsoft.astronautbe.domain.dto.SearchVolumeRequestDTO;
import com.estsoft.astronautbe.domain.dto.SearchVolumeResponseDTO;
import com.estsoft.astronautbe.repository.KeywordRepository;
import com.estsoft.astronautbe.repository.RecommendKeywordStockRepository;
import com.estsoft.astronautbe.repository.SearchVolumeRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class KeywordService {

	private final KeywordRepository keywordRepository;
	private final SearchVolumeRepository searchVolumeRepository;
	private final RecommendKeywordStockRepository recommendKeywordStockRepository;
	private final StockRepository stockRepository;
	private final ClientHttpRequestFactorySettings clientHttpRequestFactorySettings;

	@Value("${naver.client.id}")
	private String naverClientId;

	@Value("${naver.client.secret}")
	private String naverClientSecret;

	@Value("${alan.client.id}")
	private String alanClientId;

	private final WebClient webClient;
	private final ObjectMapper objectMapper;

	public KeywordService(WebClient.Builder webClientBuilder, KeywordRepository keywordRepository,
			SearchVolumeRepository searchVolumeRepository, RecommendKeywordStockRepository recommendKeywordStockRepository,
			ObjectMapper objectMapper, StockRepository stockRepository,
			ClientHttpRequestFactorySettings clientHttpRequestFactorySettings) {
		this.webClient = webClientBuilder.build();
		this.keywordRepository = keywordRepository;
		this.searchVolumeRepository = searchVolumeRepository;
		this.recommendKeywordStockRepository = recommendKeywordStockRepository;
		this.objectMapper = objectMapper;
		objectMapper.registerModule(new JavaTimeModule());
		this.stockRepository = stockRepository;
		this.clientHttpRequestFactorySettings = clientHttpRequestFactorySettings;
	}

	// alan api로 추천 종목 뽑아오기
	public List<RecommendKeywordStockResponseDTO> getRecommendKeywordStock(RecommendKeywordStockRequestDTO request) {
		try {
			String url = ApiConstants.ALLEN_API_URL;

			String prompt = request.getContent() + " 키워드에 관련된 국내 주식 추천 종목을 5개 알려줘. " +
					"종목 코드, 추천 사유, 답변 생성 시간을 내가 제시하는 json 형태에 넣어줘. " +
					"답변은 순수한 json 형태로만 보내줘. 다음은 내가 제시하는 형식이야.\n" +
					"{" +
					"  \"message\": \"키워드 추천 종목 조회 성공\"," +
					"  \"data\": {" +
					"    \"stockCode\": \"String\"," +
					"    \"reason\": \"String\"," +
					"    \"createdAt\": \"timestamp\"" +
					"  }" +
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

			parseRecommendKeywordStock(stringResponse);

			RecommendKeywordStockResponseDTO responseDTO = objectMapper.readValue(stringResponse,
					RecommendKeywordStockResponseDTO.class);

			return List.of(responseDTO);

		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("Alan API 호출 중 오류 발생");
		}
	}

	// 응답 가공
	private List<RecommendKeywordStock> parseRecommendKeywordStock(String stringResponse) throws Exception {
		stringResponse = stringResponse.replace("\\n", "");

		// json으로 파싱
		JsonNode rootNode = objectMapper.readTree(stringResponse);
		String content = rootNode.path("content").asText();
		RecommendStockAnswer answer = objectMapper.readValue(content, RecommendStockAnswer.class);

		// 엔티티에 매핑
		List<RecommendKeywordStock> stocks = answer.getData().stream()
				.map(recommendStockData -> {
					RecommendKeywordStock stock = new RecommendKeywordStock();
					// 임시 키워드 아이디
					stock.setKeywordId(1L);
					stock.setStockCode(recommendStockData.getStockCode());
					stock.setReason(recommendStockData.getReason());
					stock.setCreatedAt(recommendStockData.getCreatedAt());
					return stock;
				})
				.collect(Collectors.toList());

		recommendKeywordStockRepository.saveAll(stocks);

		return stocks;
	}


	// 단어 리스트로 검색량 조회
	public List<SearchVolumeResponseDTO> getSearchAmount(SearchVolumeRequestDTO request) {
		String url = ApiConstants.NAVER_TREND_API_URL;

		SearchVolumeResponseDTO response = webClient.post()
				.uri(url)
				.header("X-Naver-Client-Id", naverClientId)
				.header("X-Naver-Client-Secret", naverClientSecret)
				.contentType(MediaType.APPLICATION_JSON)
				.bodyValue(request)
				.retrieve()
				.bodyToMono(SearchVolumeResponseDTO.class)
				.block();

		List<SearchVolume> searchVolumes = convertToSearchVolumes(response, request);
		searchVolumeRepository.saveAll(searchVolumes);

		return List.of(response);
	}

	private List<SearchVolume> convertToSearchVolumes(SearchVolumeResponseDTO apiResponse,
			SearchVolumeRequestDTO request) {
		List<SearchVolume> searchVolumes = new ArrayList<>();

		//title->추천종목에서 찾아서 id로 recommend_stock_id .. 키워드도 동일
		RecommendKeywordStock recommendKeywordStock = recommendKeywordStockRepository.findByRecommendStockId(1L);

		for (SearchVolumeResponseDTO.ResultItem result : apiResponse.getResults()) {
			SearchVolume volume = new SearchVolume();
			// 임시 아이디
			volume.setKeywordId(1L);
			//임시 추천종목 아이디
			volume.setRecommendStockId(1L);
			volume.setSearchVolume(Double.parseDouble(result.getData().get(0).getRatio()));
			volume.setSearchDate( LocalDateTime.parse( result.getData().get(0).getPeriod() + "T00:00:00",
					DateTimeFormatter.ISO_LOCAL_DATE_TIME ) );
			volume.setCreatedAt(LocalDateTime.now());

			searchVolumes.add(volume);
		}

		return searchVolumes;
	}
}
