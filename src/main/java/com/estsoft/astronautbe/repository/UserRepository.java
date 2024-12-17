package com.estsoft.astronautbe.repository;

import com.estsoft.astronautbe.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;



@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    public Optional<User> findBySocialIdIsAndProviderIs(String socialId, String provider);
}
