package com.estsoft.astronautbe.controller;

import com.estsoft.astronautbe.domain.Users;
import com.estsoft.astronautbe.service.KakaoService;
import com.estsoft.astronautbe.service.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/oauth")
public class AuthController {



    @Autowired
    private KakaoService kakaoService;

    @Autowired
    private JwtService jwtService;

    @GetMapping("/kakao")
    public ResponseEntity<?> kakaoLogin(@RequestParam String code, @RequestParam String redirectUri) {
        Users user = kakaoService.getKakaoUser(code, redirectUri);
        String jwtToken = jwtService.createToken(user.getUsersId());

        return ResponseEntity.ok(Map.of("token", jwtToken, "user", user));
    }
}