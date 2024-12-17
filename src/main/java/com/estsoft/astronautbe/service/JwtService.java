package com.estsoft.astronautbe.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class JwtService {

    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.expiration}")
    private long expirationTime;

    public String createToken(Integer userId) {
        return null;
    }

    public Integer getUserIdFromToken(String token) {
        return null;
    }
}