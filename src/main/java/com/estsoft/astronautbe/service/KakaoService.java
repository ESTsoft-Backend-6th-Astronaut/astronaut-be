package com.estsoft.astronautbe.service;

import com.estsoft.astronautbe.domain.Users;
import com.estsoft.astronautbe.repository.KakaoLoginRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class KakaoService {

    @Value("${kakao.client.id}")
    private String clientId;

    @Value("${kakao.client.secret}")
    private String clientSecret;

    @Value("${kakao.redirect.uri}")
    private String redirectUri;

    @Autowired
    private KakaoLoginRepository kakaoLoginRepository;

    private final RestTemplate restTemplate = new RestTemplate();

    public Users getKakaoUser(String code) {
        String accessToken = getAccessToken(code);
        return getUserInfo(accessToken);
    }

    private String getAccessToken(String code) {
        String url = "https://kauth.kakao.com/oauth/token";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        String body = "grant_type=authorization_code"
                + "&client_id=" + clientId
                + "&redirect_uri=" + redirectUri
                + "&code=" + code
                + "&client_secret=" + clientSecret;

        HttpEntity<String> entity = new HttpEntity<>(body, headers);
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(response.getBody());
            return jsonNode.get("access_token").asText();
        } catch (Exception e) {
            throw new RuntimeException("Failed to get access token", e);
        }
    }

    private Users getUserInfo(String accessToken) {
        String url = "https://kapi.kakao.com/v2/user/me";
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);

        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(response.getBody());
            String kakaoId = jsonNode.get("id").asText();
            String email = jsonNode.get("kakao_account").get("email").asText();
            String name = jsonNode.get("properties").get("nickname").asText();

            Users user = kakaoLoginRepository.findByKakaoId(kakaoId)
                    .orElse(new Users(kakaoId, email, name, "kakao"));
            return kakaoLoginRepository.save(user);
        } catch (Exception e) {
            throw new RuntimeException("Failed to get user info", e);
        }
    }
}