package com.example.RestApi.dto;

import jakarta.persistence.*;
import lombok.*;

@Data
public class AuthRequest {
    private String username;
    private String password;

}