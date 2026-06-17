package com.adnan.rrs.repository;

import com.adnan.rrs.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
public interface UserRepository extends JpaRepository<User, Long>{

}
