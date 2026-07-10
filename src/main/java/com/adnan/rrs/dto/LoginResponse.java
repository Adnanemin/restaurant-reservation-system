package com.adnan.rrs.dto;

import com.adnan.rrs.entity.AccountType;

public class LoginResponse {

    private String token;
    private AccountType accountType;
    private String name;

    public LoginResponse() {

    }

    public LoginResponse(String token, AccountType accountType, String name) {
        this.token = token;
        this.accountType = accountType;
        this.name = name;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public AccountType getAccountType() {
        return accountType;
    }

    public void setAccountType(AccountType accountType) {
        this.accountType = accountType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
