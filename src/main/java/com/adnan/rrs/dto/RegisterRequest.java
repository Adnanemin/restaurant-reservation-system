package com.adnan.rrs.dto;

import com.adnan.rrs.entity.AccountType;
import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
public class RegisterRequest {

    private String name;

    private String surname;

    private String email;

    private String password;

    private String phoneNumber;

    private AccountType accountType;
}
