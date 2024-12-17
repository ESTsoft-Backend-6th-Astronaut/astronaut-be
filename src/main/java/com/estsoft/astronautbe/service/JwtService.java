package com.estsoft.astronautbe.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class JwtService {

    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.expiration}")
    private long expirationTime;

    public String createToken(Integer userId) {
        Date issuedAt = new Date();
        Date expiresAt = new Date(issuedAt.getTime() + expirationTime);
        // 토큰 생성
        return JWT.create()
                .withIssuer("astronaut") // 발급자 설정
                .withSubject(userId.toString()) // 주제 설정
                .withIssuedAt(issuedAt) // 발급 시간
                .withExpiresAt(expiresAt) // 만료 시간
                .sign(Algorithm.HMAC256(secretKey));
    }

    public Integer getUserIdFromToken(String token) {
        Algorithm algorithm = Algorithm.HMAC256(secretKey);
        JWTVerifier verifier = JWT.require(algorithm)
                .withIssuer("astronaut")
                .build();

        // 토큰 검증 및 디코딩
        DecodedJWT decodedJWT = verifier.verify(token);

        // Subject 추출
        String subject = decodedJWT.getSubject();
        return Integer.valueOf(subject);
    }
}