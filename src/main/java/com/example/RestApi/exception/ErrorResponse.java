package com.example.RestApi.exception;

import java.time.LocalDateTime;
import java.util.List;

import com.example.RestApi.model.Employee;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponse {
    private LocalDateTime timestamp;
    private String message;
    private int status;
    private String path;
    private String error;

}