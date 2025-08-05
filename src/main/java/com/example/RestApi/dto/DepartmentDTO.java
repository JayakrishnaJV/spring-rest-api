package com.example.RestApi.dto;

import lombok.*;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DepartmentDTO {

    private Long id;

    @NotBlank(message = "Department name is mandatory")
    @Size(min = 2, max = 50, message = "Department name must be 2 to 50 characters")
    private String name;

    // Include only basic employee info
    private List<EmployeeSummaryDTO> employees;
}
