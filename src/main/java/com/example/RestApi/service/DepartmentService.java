package com.example.RestApi.service;

import com.example.RestApi.dto.DepartmentDTO;
import com.example.RestApi.dto.DepartmentSummaryDTO;

import jakarta.validation.Valid;

import java.util.List;

public interface DepartmentService {

    DepartmentSummaryDTO create(@Valid DepartmentDTO deptDto);

    List<DepartmentSummaryDTO> getAll();

    DepartmentDTO getById(Long id);

    DepartmentDTO update(Long id, @Valid DepartmentDTO deptDto);

    void delete(Long id);
}
