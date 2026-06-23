package com.adnan.rrs.repository;

import com.adnan.rrs.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long>{

    boolean existsByPhoneNumber(String phoneNumber);
    boolean existsByEmail(String email);

    Optional<User> findByEmail(String email);
}
