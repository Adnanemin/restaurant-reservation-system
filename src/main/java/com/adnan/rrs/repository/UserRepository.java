package com.adnan.rrs.repository;

import com.adnan.rrs.entity.User;
import com.adnan.rrs.entity.AccountType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.List;

public interface UserRepository extends JpaRepository<User, Long>{

    boolean existsByPhoneNumber(String phoneNumber);
    boolean existsByEmail(String email);

    Optional<User> findByEmail(String email);
    List<User> findByAccountType(AccountType accountType);
}
