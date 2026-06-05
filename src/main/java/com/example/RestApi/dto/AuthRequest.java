package com.example.RestApi.dto;

import lombok.*;

@Data
public class AuthRequest {
    private String username;
    private String password;

}