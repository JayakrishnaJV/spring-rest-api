package com.example.RestApi.service;

import com.example.RestApi.dto.EmployeeDTO;
import com.example.RestApi.dto.EmployeeSummaryDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;



public interface EmployeeService {
    EmployeeSummaryDTO create(EmployeeDTO empDto);
    List<EmployeeSummaryDTO> getAll();
    EmployeeDTO getById(Long id);
    EmployeeDTO update(Long id, EmployeeDTO empDto);
    void delete(Long id);
//    List<EmployeeSummaryDTO> search(String name, String address, Long departmentId);
    Page<EmployeeSummaryDTO> search(String name, String address, Long departmentId, Pageable pageable);
}
