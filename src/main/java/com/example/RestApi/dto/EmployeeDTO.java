package com.example.RestApi.dto;

import lombok.*;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeDTO {

    private Long id;

    @NotBlank(message = "Name is mandatory")
    @Size(min = 2, max = 50, message = "Name must be 2 to 50 characters")
    private String name;

    @NotBlank(message = "Address is mandatory")
    @Size(max = 100, message = "Address must not exceed 100 characters")
    private String address;

    // Include only necessary department info to avoid recursion
    private DepartmentSummaryDTO department;
}
