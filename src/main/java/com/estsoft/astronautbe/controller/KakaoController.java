package com.estsoft.astronautbe.controller;

import com.estsoft.astronautbe.domain.Users;
import com.estsoft.astronautbe.service.KakaoService;
import com.estsoft.astronautbe.service.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class KakaoController {

    @Autowired
    private KakaoService kakaoService;

    @Autowired
    private JwtService jwtService;

    @GetMapping("/auth/kakao/callback")
    public ResponseEntity<String> kakaoLogin(@RequestParam String code) {
        Users user = kakaoService.getKakaoUser(code);
        String jwtToken = jwtService.createToken(user.getUsersId());
        return ResponseEntity.ok(jwtToken);
    }
}