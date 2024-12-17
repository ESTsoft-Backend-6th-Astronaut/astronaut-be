package com.estsoft.astronautbe.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "users")
@Getter
@NoArgsConstructor
public class Users {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "users_id", nullable = false)
    private Integer usersId;

    @Column(name = "kakao_id", columnDefinition = "VARCHAR(255)", nullable = false)
    private String kakaoId;

    @Column(name = "email", columnDefinition = "VARCHAR(255)", nullable = false)
    private String email;

    @Column(name = "name", columnDefinition = "VARCHAR(255)", nullable = false)
    private String name;

    @Column(name = "provider", columnDefinition = "VARCHAR(255)", nullable = false)
    private String provider;

    public Users(String kakaoId, String email, String name, String provider) {
        this.kakaoId = kakaoId;
        this.email = email;
        this.name = name;
        this.provider = provider;
    }
}