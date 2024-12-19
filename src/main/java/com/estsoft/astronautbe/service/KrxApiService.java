package com.estsoft.astronautbe.service;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import com.estsoft.astronautbe.config.ApiConstants;
import com.estsoft.astronautbe.domain.Stock;
import com.estsoft.astronautbe.repository.StockRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

// @PropertySource("classpath:env.properties")
@Service
public class KrxApiService {
	@Value("${krx.client.id}")
	private String authKey;

	private final StockRepository repository;
	private final RestTemplate restTemplate = new RestTemplate();

	public KrxApiService(StockRepository repository) {
		this.repository = repository;
	}

	public String getKrxKospiDataForYesterday() {
		Timestamp date_now = Timestamp.valueOf(LocalDateTime.now().minusDays(1));
		SimpleDateFormat today = new SimpleDateFormat("yyyyMMdd");

		return getKrxKospiData(today.format(date_now));
	}

	public String getKrxKosdaqDataForYesterday() {
		Timestamp date_now = Timestamp.valueOf(LocalDateTime.now().minusDays(1));
		SimpleDateFormat today = new SimpleDateFormat("yyyyMMdd");

		return getKrxKosdaqData(today.format(date_now));
	}

	public String getKrxKospiData(String date) {
		HttpHeaders headers = new HttpHeaders();
		headers.set("AUTH_KEY", authKey);

		HttpEntity<String> entity = new HttpEntity<>(headers);

		try {
			String krxApiUrl = ApiConstants.KRX_KOSPI_API_URL;
			ResponseEntity<String> responseEntity = restTemplate.exchange(
				krxApiUrl + date,
				HttpMethod.GET,
				entity,
				String.class
			);

			return responseEntity.getBody();
		} catch (Exception e) {
			throw new RuntimeException("KRX KOSPI API 요청 실패", e);
		}
	}

	public String getKrxKosdaqData(String date) {
		HttpHeaders headers = new HttpHeaders();
		headers.set("AUTH_KEY", authKey);

		HttpEntity<String> entity = new HttpEntity<>(headers);

		try {
			String krxApiUrl = ApiConstants.KRX_KOSDAQ_API_URL;
			ResponseEntity<String> responseEntity = restTemplate.exchange(
				krxApiUrl + date,
				HttpMethod.GET,
				entity,
				String.class
			);

			return responseEntity.getBody();
		} catch (Exception e) {
			throw new RuntimeException("KRX KOSDAQ API 요청 실패", e);
		}
	}

	@Transactional
	public void saveKrxData(String responseBody) {
		try {
			ObjectMapper objectMapper = new ObjectMapper();
			JsonNode rootNode = objectMapper.readTree(responseBody);
			JsonNode outBlockNode = rootNode.get("OutBlock_1");

			if (outBlockNode != null && outBlockNode.isArray()) {
				for (JsonNode itemNode : outBlockNode) {
					String stockCode = itemNode.get("ISU_CD").asText();

					Stock stock = repository.findById(stockCode).orElse(null);

					if (stock != null) {
						stock.updateDetails(
							itemNode.get("ISU_NM").asText(),
							itemNode.get("MKT_NM").asText(),
							itemNode.get("TDD_CLSPRC").asText(),
							itemNode.get("CMPPREVDD_PRC").asText(),
							itemNode.get("FLUC_RT").asText(),
							itemNode.get("TDD_OPNPRC").asText(),
							itemNode.get("TDD_HGPRC").asText(),
							itemNode.get("TDD_LWPRC").asText(),
							itemNode.get("ACC_TRDVOL").asText(),
							itemNode.get("ACC_TRDVAL").asText(),
							itemNode.get("MKTCAP").asText(),
							itemNode.get("LIST_SHRS").asText()
						);
					} else {
						stock = Stock.builder()
							.stockCode(stockCode)
							.stockName(itemNode.get("ISU_NM").asText())
							.marketName(itemNode.get("MKT_NM").asText())
							.stockPrice(itemNode.get("TDD_CLSPRC").asText())
							.contrast(itemNode.get("CMPPREVDD_PRC").asText())
							.fluctuationRate(itemNode.get("FLUC_RT").asText())
							.marketPrice(itemNode.get("TDD_OPNPRC").asText())
							.highPrice(itemNode.get("TDD_HGPRC").asText())
							.lowPrice(itemNode.get("TDD_LWPRC").asText())
							.volume(itemNode.get("ACC_TRDVOL").asText())
							.dollarVolume(itemNode.get("ACC_TRDVAL").asText())
							.marketCapitalization(itemNode.get("MKTCAP").asText())
							.totalSharesOutstanding(itemNode.get("LIST_SHRS").asText())
							.build();
					}

					repository.save(stock);
				}
			} else {
				throw new IllegalStateException("OutBlock_1 노드를 찾을 수 없거나 배열이 아닙니다.");
			}
		} catch (Exception e) {
			throw new RuntimeException("KRX 데이터를 저장하는 동안 오류 발생", e);
		}
	}
}
