package com.estsoft.astronautbe.repository;

import com.estsoft.astronautbe.domain.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface KakaoLoginRepository extends JpaRepository<Users, Integer> {
    Optional<Users> findByKakaoId(String kakaoId);
}