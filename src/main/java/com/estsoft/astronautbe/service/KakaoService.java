package com.estsoft.astronautbe.service;

import com.estsoft.astronautbe.domain.Users;
import com.estsoft.astronautbe.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class KakaoService {

    @Value("${KAKAO_API_ID}")
    private String clientId;


    @Autowired
    private UsersRepository userRepository;


    private final RestTemplate restTemplate = new RestTemplate();

    public Users getKakaoUser(String code, String redirectUri) {
        String accessToken = getAccessToken(code, redirectUri);
        return getUserInfo(accessToken);
    }

    private String getAccessToken(String code, String redirectUri) {
        String url = "https://kauth.kakao.com/oauth/token";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);


        MultiValueMap<String, String>body = new LinkedMultiValueMap<String, String>(){};
        body.add("grant_type", "authorization_code");
        body.add("client_id", "81bc0ad2d93a08a2bd006f342b98f29f");
        body.add("redirect_uri", redirectUri);
        body.add("code", code);


        HttpEntity<MultiValueMap<String,String>> entity = new HttpEntity<>(body, headers);
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(response.getBody());
            System.out.println(jsonNode.toString());
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
        System.out.println(response.getBody());
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(response.getBody());
            String kakaoId = jsonNode.get("id").asText();

            String email = jsonNode.get("id").asText()+"@kakao.user";
            String name = jsonNode.get("properties").get("nickname").asText();

            Users user = userRepository.findBySocialIdIsAndProviderIs(kakaoId,"KAKAO").orElse(
                    Users.builder().provider("KAKAO").socialId(kakaoId).email(email).name(name).build()
            );
            return userRepository.save(user);

        } catch (Exception e) {
            throw new RuntimeException("Failed to get user info", e);
        }
    }
}