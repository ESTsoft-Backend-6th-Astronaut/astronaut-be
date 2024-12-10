package com.estsoft.astronautbe.service;

import com.estsoft.astronautbe.domain.RecommendKeywordStock;
import com.estsoft.astronautbe.domain.SearchVolume;
import com.estsoft.astronautbe.repository.StockRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
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
			ObjectMapper objectMapper, StockRepository stockRepository) {
		this.webClient = webClientBuilder.build();
		this.keywordRepository = keywordRepository;
		this.searchVolumeRepository = searchVolumeRepository;
		this.recommendKeywordStockRepository = recommendKeywordStockRepository;
		this.objectMapper = objectMapper;
		objectMapper.registerModule(new JavaTimeModule());
		this.stockRepository = stockRepository;
	}

	// alan api로 추천 종목 뽑아오기
	// 아니 나 단어 10개씩 보내서 한번에 받아오는거 생각했는데 이게 안되는거같다
	// 수정해서 한번에 단어 1개 보내는걸로 하고 그걸 또 5번 반복해야 할듯......

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

			// 응답 가공
			stringResponse = stringResponse.replace("\\n", "");

			// json으로 파싱
			// dto로 변환
			RecommendKeywordStockResponseDTO responseDTO = objectMapper.readValue(stringResponse,
					RecommendKeywordStockResponseDTO.class);

			// 이게될까
			JsonNode roodNode = objectMapper.readTree(stringResponse);
			String content = roodNode.path("content").asText();
			RecommendStockAnswer answer = objectMapper.readValue(content, RecommendStockAnswer.class);

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

			return List.of(responseDTO);

		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("Alan API 호출 중 오류 발생");
		}
	}

	// 단어 리스트로 검색량 조회
	public SearchVolumeResponseDTO getSearchAmount(SearchVolumeRequestDTO request) {
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

		// DB에 저장하게도 해야 함, seachvolumerepository에 save
		// 현재 recommendstockid나 keywordid를 연결하지 않았으니 추후 해당부분 완성 후 연결하고 넣을 것

		// 키워드 id 찾기

		// 주식 찾기

		// ratio를 searchvolume으로 변환

		// searchdate 설정

		// 저장
//
// List<SearchVolume> searchVolumes=conver
// 		// 임시 추천주식 ID
// 		volume.setRecommendStockId(1L);
// 		//임시 키워드 ID
// 		volume.setKeywordId(1L);
// 		// volume.setSearchVolume();
		return response;
	}
}
