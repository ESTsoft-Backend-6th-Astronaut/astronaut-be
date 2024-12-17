package com.estsoft.astronautbe.repository;

import com.estsoft.astronautbe.domain.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;



@Repository
public interface UsersRepository extends JpaRepository<Users, Long> {

    public Optional<Users> findBySocialIdIsAndProviderIs(String socialId, String provider);
}
