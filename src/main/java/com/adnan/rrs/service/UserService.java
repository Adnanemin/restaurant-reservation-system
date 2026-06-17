package com.adnan.rrs.service;

import com.adnan.rrs.dto.RegisterRequest;
import com.adnan.rrs.repository.UserRepository;
import com.adnan.rrs.entity.User;
import com.adnan.rrs.entity.UserRole;

import java.time.LocalDateTime;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User registerUser(RegisterRequest request){
        if(userRepository.existsByEmail(request.getEmail())){
            throw new RuntimeException("Email already exists");
        }

        if(userRepository.existsByPhoneNumber(request.getPhoneNumber())){
            throw new RuntimeException("Phone number already exists");
        }

        User user = new User();

        user.setName(request.getName());
        user.setSurname(request.getSurname());
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword());
        user.setPhoneNumber(request.getPhoneNumber());

        user.setRole(UserRole.CUSTOMER);
        user.setCreatedAt(LocalDateTime.now());

        return userRepository.save(user);
    }
}
