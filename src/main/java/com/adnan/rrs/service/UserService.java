package com.adnan.rrs.service;

import com.adnan.rrs.dto.LoginRequest;
import com.adnan.rrs.dto.RegisterRequest;
import com.adnan.rrs.entity.AccountType;
import com.adnan.rrs.repository.UserRepository;
import com.adnan.rrs.entity.User;
import com.adnan.rrs.entity.RestaurantStatus;

import java.time.LocalDateTime;
import java.util.Optional;

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

        user.setRole(AccountType.CUSTOMER);
        user.setCreatedAt(LocalDateTime.now());

        return userRepository.save(user);
    }

    public User loginUser(LoginRequest request){

        Optional<User> userOptional = userRepository.findByEmail(request.getEmail());

        if(userOptional.isEmpty()){
            throw new RuntimeException("Email not found");
        }

        User user = userOptional.get();
        if(!user.getPassword().equals(request.getPassword())){
            throw new RuntimeException("Invalid password");
        }

        return user;
    }
}
