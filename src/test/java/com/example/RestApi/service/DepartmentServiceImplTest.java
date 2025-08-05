package com.example.RestApi.service;

import com.example.RestApi.dto.DepartmentDTO;
import com.example.RestApi.dto.DepartmentSummaryDTO;
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
    private DepartmentDTO departmentDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        department = new Department();
        department.setId(1L);
        department.setName("IT");

        departmentDTO = new DepartmentDTO();
        departmentDTO.setName("IT");
    }

    @Test
    void testCreateDepartment() {
        when(departmentRepo.save(any(Department.class))).thenReturn(department);

        DepartmentSummaryDTO result = departmentService.create(departmentDTO);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("IT", result.getName());
        verify(departmentRepo, times(1)).save(any(Department.class));
    }

    @Test
    void testGetAllDepartments() {
        when(departmentRepo.findAll()).thenReturn(List.of(department));

        List<DepartmentSummaryDTO> result = departmentService.getAll();

        assertEquals(1, result.size());
        assertEquals("IT", result.get(0).getName());
        verify(departmentRepo, times(1)).findAll();
    }

    @Test
    void testGetDepartmentById_Found() {
        when(departmentRepo.findById(1L)).thenReturn(Optional.of(department));

        DepartmentDTO result = departmentService.getById(1L);

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
        DepartmentDTO updatedDTO = new DepartmentDTO();
        updatedDTO.setName("HR");

        Department updatedDepartment = new Department();
        updatedDepartment.setId(1L);
        updatedDepartment.setName("HR");

        when(departmentRepo.findById(1L)).thenReturn(Optional.of(department));
        when(departmentRepo.save(any(Department.class))).thenReturn(updatedDepartment);

        DepartmentDTO result = departmentService.update(1L, updatedDTO);

        assertEquals("HR", result.getName());
        verify(departmentRepo).save(any(Department.class));
    }

    @Test
    void testUpdateDepartment_NotFound() {
        when(departmentRepo.findById(1L)).thenReturn(Optional.empty());

        DepartmentDTO updatedDTO = new DepartmentDTO();
        updatedDTO.setName("HR");

        assertThrows(ResourceNotFoundException.class, () -> departmentService.update(1L, updatedDTO));
    }

    @Test
    void testDeleteDepartment() {
        when(departmentRepo.existsById(1L)).thenReturn(true);
        doNothing().when(departmentRepo).deleteById(1L);

        departmentService.delete(1L);

        verify(departmentRepo).deleteById(1L);
    }

    @Test
    void testDeleteDepartment_NotFound() {
        when(departmentRepo.existsById(99L)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> departmentService.delete(99L));
    }
}
