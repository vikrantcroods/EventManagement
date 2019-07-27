package com.croods.eventmanagement.model;

public class LoginRequest
{
    private String username,password;

    public LoginRequest(String username, String password) {
        this.username = username;
        this.password = password;
    }
}
