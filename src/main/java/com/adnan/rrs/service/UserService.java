package com.adnan.rrs.service;

import com.adnan.rrs.dto.LoginRequest;
import com.adnan.rrs.dto.RegisterRequest;
import com.adnan.rrs.dto.LoginResponse;
import com.adnan.rrs.security.JwtService;
import com.adnan.rrs.security.CustomUserDetailsService;
import com.adnan.rrs.entity.AccountType;
import com.adnan.rrs.entity.User;
import com.adnan.rrs.repository.UserRepository;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.security.core.userdetails.UserDetails;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final CustomUserDetailsService userDetailsService;

    public UserService(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            AuthenticationManager authenticationManager,
            JwtService jwtService,
            CustomUserDetailsService userDetailsService
    ) {

        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
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
        user.setPassword(passwordEncoder.encode(request.getPassword()));
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

    public LoginResponse loginUser(LoginRequest request){

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Email not found."));

        if (!user.isEnabled()) {
            throw new RuntimeException("Your restaurant account is not active.");
        }

        UserDetails userDetails = userDetailsService.loadUserByUsername(user.getEmail());
        String token = jwtService.generateToken(userDetails);

        return new LoginResponse(
                token,
                user.getAccountType(),
                user.getName()
        );
    }
}
