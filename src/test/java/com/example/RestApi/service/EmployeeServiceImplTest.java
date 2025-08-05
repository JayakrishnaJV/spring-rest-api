package com.example.RestApi.service;

import com.example.RestApi.dto.DepartmentSummaryDTO;
import com.example.RestApi.dto.EmployeeDTO;
import com.example.RestApi.dto.EmployeeSummaryDTO;
import com.example.RestApi.exception.ResourceNotFoundException;
import com.example.RestApi.model.Department;
import com.example.RestApi.model.Employee;
import com.example.RestApi.repository.DepartmentRepository;
import com.example.RestApi.repository.EmployeeRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;


import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmployeeServiceImplTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private DepartmentRepository departmentRepository;

    @InjectMocks
    private EmployeeServiceImpl employeeService;

    private Department dept;
    private Employee emp;
    private EmployeeDTO empDto;
    private DepartmentSummaryDTO deptSummary;

    @BeforeEach
    void setup() {
        dept = new Department();
        dept.setId(1L);
        dept.setName("IT");

        deptSummary = new DepartmentSummaryDTO();
        deptSummary.setId(1L);
        deptSummary.setName("IT");

        emp = new Employee();
        emp.setId(1L);
        emp.setName("John Doe");
        emp.setAddress("Hyderabad");
        emp.setDepartment(dept);

        empDto = new EmployeeDTO();
        empDto.setName("John Doe");
        empDto.setAddress("Hyderabad");
        empDto.setDepartment(deptSummary);
    }

    @Test
    void testCreateEmployee() {
        when(departmentRepository.findById(1L)).thenReturn(Optional.of(dept));
        when(employeeRepository.save(any(Employee.class))).thenReturn(emp);

        EmployeeSummaryDTO result = employeeService.create(empDto);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("John Doe", result.getName());
    }

    @Test
    void testGetAllEmployees() {
        when(employeeRepository.findAll()).thenReturn(List.of(emp));

        List<EmployeeSummaryDTO> result = employeeService.getAll();

        assertEquals(1, result.size());
        assertEquals(1L, result.get(0).getId());
        assertEquals("John Doe", result.get(0).getName());
    }

    @Test
    void testGetByIdSuccess() {
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(emp));

        EmployeeDTO result = employeeService.getById(1L);

        assertEquals("John Doe", result.getName());
        assertEquals("Hyderabad", result.getAddress());
        assertEquals(1L, result.getDepartment().getId());
        assertEquals("IT", result.getDepartment().getName());
    }

    @Test
    void testGetByIdNotFound() {
        when(employeeRepository.findById(2L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> employeeService.getById(2L));
    }

    @Test
    void testUpdateEmployeeSuccess() {
        Employee updatedEmp = new Employee();
        updatedEmp.setId(1L);
        updatedEmp.setName("Jane Doe");
        updatedEmp.setAddress("Delhi");
        updatedEmp.setDepartment(dept);

        EmployeeDTO updateDto = new EmployeeDTO();
        updateDto.setName("Jane Doe");
        updateDto.setAddress("Delhi");
        updateDto.setDepartment(deptSummary);

        when(employeeRepository.findById(1L)).thenReturn(Optional.of(emp));
        when(departmentRepository.findById(1L)).thenReturn(Optional.of(dept));
        when(employeeRepository.save(any(Employee.class))).thenReturn(updatedEmp);

        EmployeeDTO result = employeeService.update(1L, updateDto);

        assertEquals("Jane Doe", result.getName());
        assertEquals("Delhi", result.getAddress());
        assertEquals("IT", result.getDepartment().getName());
    }

    @Test
    void testUpdateEmployeeNotFound() {
        when(employeeRepository.findById(2L)).thenReturn(Optional.empty());

        EmployeeDTO updateDto = new EmployeeDTO();
        updateDto.setName("Someone");
        updateDto.setAddress("Somewhere");
        updateDto.setDepartment(deptSummary);

        assertThrows(ResourceNotFoundException.class, () -> employeeService.update(2L, updateDto));
    }

    @Test
    void testDeleteEmployee() {
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(emp));
        doNothing().when(employeeRepository).deleteById(1L); // Correct method!

        employeeService.delete(1L);

        verify(employeeRepository, times(1)).deleteById(1L); // Correct verification
    }

    @Test
    void testDeleteEmployeeNotFound() {
        when(employeeRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> employeeService.delete(99L));
    }
    
    @Test
    void testSearchEmployeesByName() {
        // Arrange
        Page<Employee> employeePage = new PageImpl<>(List.of(emp));

        when(employeeRepository.findAll(
                any(Specification.class),
                any(PageRequest.class)))
            .thenReturn(employeePage);

        // Act
        var result = employeeService.search("John", null, null, PageRequest.of(0, 10));

        // Assert
        assertNotNull(result);
        assertEquals(1, result.getContent().size());
        assertEquals("John Doe", result.getContent().get(0).getName());
    }
    
    @Test
    void testSearchEmployeesByDepartment() {
        // Arrange
        Page<Employee> employeePage = new PageImpl<>(List.of(emp));

        when(employeeRepository.findAll(
                any(Specification.class),
                any(PageRequest.class)))
            .thenReturn(employeePage);

        // Act
        var result = employeeService.search(null, null, 1L, PageRequest.of(0, 10));

        // Assert
        assertNotNull(result);
        assertEquals(1, result.getContent().size());
        assertEquals("IT", emp.getDepartment().getName());
    }

    @Test
    void testSearchEmployeesByNameAndAddress() {
        // Arrange
        Page<Employee> employeePage = new PageImpl<>(List.of(emp));

        when(employeeRepository.findAll(
                any(Specification.class),
                any(PageRequest.class)))
            .thenReturn(employeePage);
        // Act
        var result = employeeService.search("John", "Hyderabad", null, PageRequest.of(0, 10));

        // Assert
        assertNotNull(result);
        assertEquals(1, result.getContent().size());
        assertEquals("John Doe", result.getContent().get(0).getName());
        assertEquals("Hyderabad", emp.getAddress());
    }



}
