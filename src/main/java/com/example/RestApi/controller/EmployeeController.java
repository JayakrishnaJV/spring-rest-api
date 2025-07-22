package com.example.RestApi.controller;

import com.example.RestApi.model.Employee;
import com.example.RestApi.model.Department;
import com.example.RestApi.service.EmployeeService;

import io.swagger.v3.oas.annotations.Operation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.example.RestApi.service.DepartmentService;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api")
public class EmployeeController {

    private static final Logger logger = LoggerFactory.getLogger(EmployeeController.class);

	
    @Autowired 
    private EmployeeService employeeService;

    @Autowired
    private DepartmentService departmentService;

    // ─── EMPLOYEE CRUD ─────────────────────────────────────

    @PostMapping("/employees")
    @Operation(
            summary = "Post the employee",
            description = "Save the employee into DB"
        )
    public ResponseEntity<Employee> createEmployee(@Valid @RequestBody Employee emp) {
    	logger.info("Creating new employee: {}", emp);
        Employee saved = employeeService.create(emp);
        URI location = URI.create("/api/employees/" + saved.getId());
        return ResponseEntity.created(location).body(saved); // 201 Created
    }

    @GetMapping("/employees")
    @Operation(
            summary = "Get all employees",
            description = "Returns list of employee"
        )
    public ResponseEntity<List<Employee>> getAllEmployees() {
    	logger.info("Fetching all employees");
        List<Employee> employees = employeeService.getAll();
        return ResponseEntity.ok(employees); // 200 OK
    }

    @GetMapping("/employees/{id}")
    @Operation(
            summary = "Get employee",
            description = "Returns the employee of mentioned ID"
        )
    public ResponseEntity<Employee> getEmployeeById(@PathVariable Long id) {
    	    logger.info("Fetching employee with ID {}:", id);
            Employee emp = employeeService.getById(id);
            return ResponseEntity.ok(emp); // 200 OK
    }

    @PutMapping("/employees/{id}")
    @Operation(
            summary = "Updates employee",
            description = "Updates the employee changes into DB"
        )
    public ResponseEntity<?> updateEmployee(@PathVariable Long id, @Valid @RequestBody Employee emp) {
    		logger.info("Updating employee {}:", emp);
            Employee updated = employeeService.update(id, emp);
            return ResponseEntity.ok(updated); // 200 OK
    }

    @DeleteMapping("/employees/{id}")
    @Operation(
            summary = "Deletes the employee",
            description = "Deletes the employee record in DB"
        )
    public ResponseEntity<?> deleteEmployee(@PathVariable Long id) {
    		logger.info("Deleting employee of ID {}:", id);
            employeeService.delete(id);
            return ResponseEntity.noContent().build(); // 204 No Content       
    }

    // ─── DEPARTMENT CRUD ───────────────────────────────────

    @PostMapping("/departments")
    @Operation(
            summary = "Post the department",
            description = "Saves the department into DB"
        )
    public ResponseEntity<Department> createDepartment(@Valid @RequestBody Department dept) {
    	logger.info("Creating new Department {}:", dept);
        Department saved = departmentService.create(dept);
        URI location = URI.create("/api/departments/" + saved.getId());
        return ResponseEntity.created(location).body(saved); // 201 Created
    }

    @GetMapping("/departments")
    @Operation(
            summary = "Get all departments",
            description = "Returns list of departments"
        )
    public ResponseEntity<List<Department>> getAllDepartments() {
    	logger.info("Fetching all departments");
        return ResponseEntity.ok(departmentService.getAll()); // 200 OK
    }

    @GetMapping("/departments/{id}")
    @Operation(
            summary = "Gives department",
            description = "Returns department of mentioned ID"
        )
    public ResponseEntity<?> getDepartmentById(@PathVariable Long id) {
    		logger.info("Fetching department with ID {}:", id);
            Department dept = departmentService.getById(id);
            return ResponseEntity.ok(dept);
    }

    @PutMapping("/departments/{id}")
    @Operation(
            summary = "updates the department",
            description = "updates the department in the DB"
        )
    public ResponseEntity<?> updateDepartment(@PathVariable Long id, @Valid @RequestBody Department dept) {
    		logger.info("Updating department {}:", dept);
            Department updated = departmentService.update(id, dept);
            return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/departments/{id}")
    @Operation(
            summary = "Deletes the department ",
            description = "deletes the department from DB"
        )
    public ResponseEntity<?> deleteDepartment(@PathVariable Long id) {
    		logger.info("Deleting department of ID {}:", id);
            departmentService.delete(id);
            return ResponseEntity.noContent().build(); // 204 No Content
       
    }
}
