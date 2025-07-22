package com.example.RestApi.service;


import com.example.RestApi.exception.ResourceNotFoundException;
import com.example.RestApi.model.Employee;
import com.example.RestApi.repository.EmployeeRepository;

import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class EmployeeServiceImpl implements EmployeeService {
	
	 private static final Logger logger = LoggerFactory.getLogger(EmployeeServiceImpl.class);

    @Autowired
    private EmployeeRepository repo;

    @Override
    public Employee create(Employee emp) {
    	logger.debug("Saving employee to DB {}: ", emp);
        return repo.save(emp);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Employee> getAll() {
    	logger.debug("Getting all employees from DB");
        return repo.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Employee getById(Long id) {
    	logger.debug("Getting employee from DB with ID {}: ", id);
    	return repo.findById(id)
    			.orElseThrow(() -> new ResourceNotFoundException("Employee not found with ID: " + id));
    }

    @Override
    public Employee update(Long id, Employee updatedEmp) {
    	logger.debug("Updating employee in DB of ID {}: ", id);
        Employee emp = repo.findById(id).orElse(null);
        if (emp != null) {
            emp.setName(updatedEmp.getName());
            emp.setDepartment(updatedEmp.getDepartment());
            return repo.save(emp);
        }
        return null;
    }

    @Override
    public void delete(Long id) {
    	logger.debug("Deleting employee from DB with ID {}: ", id);
        repo.deleteById(id);
    }
}
