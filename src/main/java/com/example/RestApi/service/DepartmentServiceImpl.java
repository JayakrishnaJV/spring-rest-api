package com.example.RestApi.service;

import com.example.RestApi.exception.ResourceNotFoundException;
import com.example.RestApi.model.Department;
import com.example.RestApi.repository.DepartmentRepository;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class DepartmentServiceImpl implements DepartmentService {
	
	private static final Logger logger = LoggerFactory.getLogger(EmployeeServiceImpl.class);

    @Autowired
    private DepartmentRepository departmentRepo;

    @Override
    public Department create(Department department) {
    	logger.debug("Saving Department to DB {}: ", department);
        return departmentRepo.save(department);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Department> getAll() {
    	logger.debug("Getting all departments from DB");
        return departmentRepo.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Department getById(Long id) {
    	logger.debug("Getting department from DB with ID {}: ", id);
    	return departmentRepo.findById(id)
    	        .orElseThrow(() -> new ResourceNotFoundException("Department not found with ID: " + id));

    }

    @Override
    public Department update(Long id, Department updatedDept) {
    	logger.debug("Updating department in DB of ID {}: ", id);
        return departmentRepo.findById(id).map(existing -> {
            existing.setName(updatedDept.getName());
            return departmentRepo.save(existing);
        }).orElseThrow(() -> new RuntimeException("Department not found with id: " + id));
    }

    @Override
    public void delete(Long id) {
    	logger.debug("Deleting department from DB with ID {}: ", id);
        departmentRepo.deleteById(id);
    }
}
