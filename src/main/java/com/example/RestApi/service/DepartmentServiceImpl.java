package com.example.RestApi.service;

import com.example.RestApi.dto.DepartmentDTO;
import com.example.RestApi.dto.DepartmentSummaryDTO;
import com.example.RestApi.exception.ResourceNotFoundException;
import com.example.RestApi.model.Department;
import com.example.RestApi.repository.DepartmentRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class DepartmentServiceImpl implements DepartmentService {

    @Autowired
    private DepartmentRepository departmentRepository;

    @Override
    public DepartmentSummaryDTO create(DepartmentDTO deptDto) {
        Department dept = new Department();
        dept.setName(deptDto.getName());
        Department saved = departmentRepository.save(dept);
        return mapToSummaryDTO(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DepartmentSummaryDTO> getAll() {
        return departmentRepository.findAll()
                .stream()
                .map(this::mapToSummaryDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public DepartmentDTO getById(Long id) {
        Optional<Department> optionalDept = departmentRepository.findById(id);
        return optionalDept
                .map(this::mapToDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Department not found with ID: " + id));
    }

    @Override
    public DepartmentDTO update(Long id, DepartmentDTO deptDto) {
        Optional<Department> optionalDept = departmentRepository.findById(id);

        Department dept = optionalDept
                .orElseThrow(() -> new ResourceNotFoundException("Department not found with ID: " + id));

        dept.setName(deptDto.getName());
        Department updated = departmentRepository.save(dept);

        return mapToDTO(updated);
    }

    @Override
    public void delete(Long id) {
        if (!departmentRepository.existsById(id)) {
            throw new ResourceNotFoundException("Department not found with ID: " + id);
        }
        departmentRepository.deleteById(id);
    }

    // ─── Mapping Helpers ──────────────────────────────

    private DepartmentDTO mapToDTO(Department dept) {
        DepartmentDTO dto = new DepartmentDTO();
        dto.setId(dept.getId());
        dto.setName(dept.getName());
        return dto;
    }

    private DepartmentSummaryDTO mapToSummaryDTO(Department dept) {
        DepartmentSummaryDTO dto = new DepartmentSummaryDTO();
        dto.setId(dept.getId());
        dto.setName(dept.getName());
        return dto;
    }
}
