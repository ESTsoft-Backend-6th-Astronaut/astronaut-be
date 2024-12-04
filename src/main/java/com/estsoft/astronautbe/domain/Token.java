package com.estsoft.astronautbe.domain;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import java.sql.Timestamp;

@Entity
@Table(name = "token")
@Getter
@NoArgsConstructor
public class Token { // token entity
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "token_id", nullable = false)
    private Integer tokenId;

    @Column(name = "users_id", nullable = false)
    private String quoteContent;

    @Column(name = "refresh_token", columnDefinition = "TEXT", nullable = false)
    private String refreshToken;

    @Column(name = "expires_at", columnDefinition = "TIMESTAMP", nullable = false)
    private Timestamp expiresAt;

    @CreatedDate
    @Column(name = "created_at", columnDefinition = "TIMESTAMP", nullable = false)
    private String createdAt;
}
