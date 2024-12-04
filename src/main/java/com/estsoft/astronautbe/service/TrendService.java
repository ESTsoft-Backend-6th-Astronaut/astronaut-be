package com.estsoft.astronautbe.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.estsoft.astronautbe.repository.TrendRepository;

@Service
public class TrendService {

	@Value("${naver.client.id}")
	private String clientId;

	@Value("${naver.client.secret}")
	private String clientSecret;

	private final TrendRepository trendRepository;

	private final RestTemplate restTemplate;

	public TrendService(TrendRepository trendRepository, RestTemplate restTemplate) {
		this.trendRepository = trendRepository;
		this.restTemplate = restTemplate;
	}

	public String getSearchAmount(String requestBody) {
		String apiUrl = "https://openapi.naver.com/v1/datalab/search";

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.set("X-Naver-Client-Id", clientId);
		headers.set("X-Naver-Client-Secret", clientSecret);

		HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);

		ResponseEntity<String> response=restTemplate.exchange(apiUrl, HttpMethod.POST,entity, String.class);

		return response.getBody();
	}
}
