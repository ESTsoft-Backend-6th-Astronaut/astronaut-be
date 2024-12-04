package com.estsoft.astronautbe.domain;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "users")
@Getter
@NoArgsConstructor
public class Users { // user entity
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "users_id", nullable = false)
    private Integer usersId;

    @Column(name = "email", columnDefinition = "VARCHAR(255)", nullable = false)
    private String email;

    @Column(name = "name", columnDefinition = "VARCHAR(255)", nullable = false)
    private String name;

    @Column(name = "provider", columnDefinition = "VARCHAR(255)", nullable = false)
    private String provider;
}
