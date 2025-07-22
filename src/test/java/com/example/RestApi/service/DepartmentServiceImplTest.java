package com.example.RestApi.service;

import com.example.RestApi.exception.ResourceNotFoundException;
import com.example.RestApi.model.Department;
import com.example.RestApi.repository.DepartmentRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DepartmentServiceImplTest {

    @Mock
    private DepartmentRepository departmentRepo;

    @InjectMocks
    private DepartmentServiceImpl departmentService;

    private Department department;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        department = new Department();
        department.setId(1L);
        department.setName("IT");
    }

    @Test
    void testCreateDepartment() {
        when(departmentRepo.save(department)).thenReturn(department);

        Department result = departmentService.create(department);

        assertNotNull(result);
        assertEquals("IT", result.getName());
        verify(departmentRepo, times(1)).save(department);
    }

    @Test
    void testGetAllDepartments() {
        List<Department> departments = Arrays.asList(department);
        when(departmentRepo.findAll()).thenReturn(departments);

        List<Department> result = departmentService.getAll();

        assertEquals(1, result.size());
        verify(departmentRepo, times(1)).findAll();
    }

    @Test
    void testGetDepartmentById_Found() {
        when(departmentRepo.findById(1L)).thenReturn(Optional.of(department));

        Department result = departmentService.getById(1L);

        assertNotNull(result);
        assertEquals("IT", result.getName());
        verify(departmentRepo).findById(1L);
    }

    @Test
    void testGetDepartmentById_NotFound() {
        when(departmentRepo.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> departmentService.getById(1L));
    }

    @Test
    void testUpdateDepartment_Found() {
        Department updated = new Department();
        updated.setName("HR");

        when(departmentRepo.findById(1L)).thenReturn(Optional.of(department));
        when(departmentRepo.save(any(Department.class))).thenReturn(department);

        Department result = departmentService.update(1L, updated);

        assertEquals("HR", result.getName());
        verify(departmentRepo).save(department);
    }

    @Test
    void testUpdateDepartment_NotFound() {
        when(departmentRepo.findById(1L)).thenReturn(Optional.empty());

        Department updated = new Department();
        updated.setName("HR");

        assertThrows(RuntimeException.class, () -> departmentService.update(1L, updated));
    }

    @Test
    void testDeleteDepartment() {
        doNothing().when(departmentRepo).deleteById(1L);

        departmentService.delete(1L);

        verify(departmentRepo).deleteById(1L);
    }
}
