package com.estsoft.astronautbe.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.estsoft.astronautbe.domain.Users;

public interface UsersRepository extends JpaRepository<Users, Long> {
}
