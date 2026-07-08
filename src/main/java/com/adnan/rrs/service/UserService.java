package com.adnan.rrs.service;

import com.adnan.rrs.dto.LoginRequest;
import com.adnan.rrs.dto.RegisterRequest;
import com.adnan.rrs.entity.AccountType;
import com.adnan.rrs.repository.UserRepository;
import com.adnan.rrs.entity.User;


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

        user.setAccountType(request.getAccountType());

        if (request.getAccountType() == AccountType.ADMIN) {
            throw new RuntimeException("Account type not allowed");
        }

        switch (request.getAccountType()){
            case ADMIN       -> user.setEnabled(true);
            case RESTAURANT  -> user.setEnabled(false);
            case CUSTOMER    -> user.setEnabled(true);

            default          -> throw new IllegalArgumentException("Invalid account type");
        }

        return userRepository.save(user);
    }

    public User loginUser(LoginRequest request){

        User user = userRepository.findByEmail(request.getEmail()).orElseThrow(() -> new RuntimeException("Email not found"));
        if(!user.getPassword().equals(request.getPassword())){
            throw new RuntimeException("Invalid password");
        }

        if (!user.isEnabled()) {
            throw new RuntimeException("Your restaurant account is not active.");
        }

        return user;
    }
}
