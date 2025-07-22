package com.example.RestApi.service;

import com.example.RestApi.exception.ResourceNotFoundException;
import com.example.RestApi.model.Department;
import com.example.RestApi.model.Employee;
import com.example.RestApi.repository.EmployeeRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmployeeServiceImplTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @InjectMocks
    private EmployeeServiceImpl employeeService;

    private Employee emp;
    private Department dept;

    @BeforeEach
    void setup() {
        dept = new Department();
        dept.setId(1L);
        dept.setName("IT");

        emp = new Employee();
        emp.setId(1L);
        emp.setName("John Doe");
        emp.setDepartment(dept);
    }

    @Test
    void testCreateEmployee() {
        when(employeeRepository.save(emp)).thenReturn(emp);

        Employee savedEmp = employeeService.create(emp);
        assertNotNull(savedEmp);
        assertEquals("John Doe", savedEmp.getName());
        assertEquals("IT", savedEmp.getDepartment().getName());
    }

    @Test
    void testGetAllEmployees() {
        List<Employee> employees = List.of(emp);
        when(employeeRepository.findAll()).thenReturn(employees);

        List<Employee> result = employeeService.getAll();
        assertEquals(1, result.size());
    }

    @Test
    void testGetByIdSuccess() {
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(emp));

        Employee result = employeeService.getById(1L);
        assertEquals("John Doe", result.getName());
    }

    @Test
    void testGetByIdNotFound() {
        when(employeeRepository.findById(2L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> employeeService.getById(2L));
    }

    @Test
    void testUpdateEmployeeSuccess() {
        Employee updated = new Employee();
        updated.setName("Jane Doe");
        updated.setDepartment(dept);

        when(employeeRepository.findById(1L)).thenReturn(Optional.of(emp));
        when(employeeRepository.save(any(Employee.class))).thenReturn(updated);

        Employee result = employeeService.update(1L, updated);
        assertNotNull(result);
        assertEquals("Jane Doe", result.getName());
    }

    @Test
    void testUpdateEmployeeNotFound() {
        when(employeeRepository.findById(2L)).thenReturn(Optional.empty());

        Employee updated = new Employee();
        updated.setName("Someone");
        updated.setDepartment(dept);

        Employee result = employeeService.update(2L, updated);
        assertNull(result);
    }

    @Test
    void testDeleteEmployee() {
        doNothing().when(employeeRepository).deleteById(1L);
        employeeService.delete(1L);
        verify(employeeRepository, times(1)).deleteById(1L);
    }
}
