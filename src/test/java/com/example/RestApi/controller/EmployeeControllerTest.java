package com.example.RestApi.controller;

import com.example.RestApi.dto.EmployeeDTO;
import com.example.RestApi.dto.EmployeeSummaryDTO;
import com.example.RestApi.dto.DepartmentSummaryDTO;
import com.example.RestApi.service.EmployeeService;
import com.example.RestApi.service.DepartmentService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(EmployeeController.class)
@AutoConfigureMockMvc(addFilters = false) // Disable security filters for testing
class EmployeeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EmployeeService employeeService;

    @MockBean
    private DepartmentService departmentService;

    private EmployeeDTO employeeDTO;
    private EmployeeSummaryDTO employeeSummary;
    private DepartmentSummaryDTO departmentSummary;

    @BeforeEach
    void setup() {
        departmentSummary = new DepartmentSummaryDTO(1L, "IT");
        employeeDTO = new EmployeeDTO(1L, "John Doe", "Hyderabad", departmentSummary);
        employeeSummary = new EmployeeSummaryDTO(1L, "John Doe");
    }

    @Test
    void testCreateEmployee() throws Exception {
        when(employeeService.create(any(EmployeeDTO.class))).thenReturn(employeeSummary);

        mockMvc.perform(post("/api/employees")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"John Doe\",\"address\":\"Hyderabad\",\"department\":{\"id\":1,\"name\":\"IT\"}}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("John Doe"));
    }

    @Test
    void testGetAllEmployees() throws Exception {
        when(employeeService.getAll()).thenReturn(List.of(employeeSummary));

        mockMvc.perform(get("/api/employees/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("John Doe"));
    }

    @Test
    void testGetEmployeeById() throws Exception {
        when(employeeService.getById(1L)).thenReturn(employeeDTO);

        mockMvc.perform(get("/api/employees")
                .param("id", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("John Doe"));
    }

    @Test
    void testUpdateEmployee() throws Exception {
        when(employeeService.update(eq(1L), any(EmployeeDTO.class))).thenReturn(employeeDTO);

        mockMvc.perform(put("/api/employees")
                .param("id", "1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"John Doe\",\"address\":\"Hyderabad\",\"department\":{\"id\":1,\"name\":\"IT\"}}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("John Doe"));
    }

    @Test
    void testDeleteEmployee() throws Exception {
        mockMvc.perform(delete("/api/employees")
                .param("id", "1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void testSearchEmployees() throws Exception {
        Page<EmployeeSummaryDTO> employeePage = new PageImpl<>(List.of(employeeSummary));

        when(employeeService.search(eq("John"), eq(null), eq(null), any(Pageable.class)))
                .thenReturn(employeePage);

        mockMvc.perform(get("/api/employees/search")
                .param("name", "John")
                .param("page", "0")
                .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(1))
                .andExpect(jsonPath("$.content[0].name").value("John Doe"));
    }


}
