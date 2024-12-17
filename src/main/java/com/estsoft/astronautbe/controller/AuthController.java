package com.estsoft.astronautbe.controller;

import com.estsoft.astronautbe.domain.Users;
import com.estsoft.astronautbe.service.KakaoService;
import com.estsoft.astronautbe.service.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/oauth")
public class AuthController {



    @Autowired
    private KakaoService kakaoService;

    @Autowired
    private JwtService jwtService;

    @GetMapping("/kakao")
    public ResponseEntity<String> kakaoLogin(@RequestParam String code, @RequestParam String redirectUri) {
        Users user = kakaoService.getKakaoUser(code, redirectUri);

        System.out.println(user);
        String jwtToken = jwtService.createToken(user.getUsersId());
        System.out.println(jwtToken);
        return ResponseEntity.ok(jwtToken);
    }
}