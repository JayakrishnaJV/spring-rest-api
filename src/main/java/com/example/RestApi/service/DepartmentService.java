package com.example.RestApi.service;


import com.example.RestApi.model.Department;
import java.util.List;

public interface DepartmentService {
    Department create(Department department);
    List<Department> getAll();
    Department getById(Long id);
    Department update(Long id, Department department);
    void delete(Long id);
}
