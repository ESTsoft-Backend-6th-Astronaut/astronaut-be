package com.estsoft.astronautbe.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Entity
@Table(name = "users")
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Users {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "users_id", nullable = false)
    private Integer usersId;

    @Column(name = "socialId", columnDefinition = "VARCHAR(255)", nullable = false)
    private String socialId;

    @Column(name = "email", columnDefinition = "VARCHAR(255)", nullable = false)
    private String email;

    @Column(name = "name", columnDefinition = "VARCHAR(255)", nullable = false)
    private String name;

    @Column(name = "provider", columnDefinition = "VARCHAR(255)", nullable = false)
    private String provider;

    public Users(String socialId, String email, String name, String provider) {
        this.socialId = socialId;
        this.email = email;
        this.name = name;
        this.provider = provider;
    }
}