package com.example.RestApi.service;

import com.example.RestApi.model.Employee;
import java.util.List;

public interface EmployeeService {
    Employee create(Employee emp);
    List<Employee> getAll();
    Employee getById(Long id);
    Employee update(Long id, Employee emp);
    void delete(Long id);
}
