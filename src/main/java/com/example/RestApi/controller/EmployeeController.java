package com.example.RestApi.controller;

import com.example.RestApi.dto.EmployeeDTO;
import com.example.RestApi.dto.EmployeeSummaryDTO;
import com.example.RestApi.dto.DepartmentDTO;
import com.example.RestApi.dto.DepartmentSummaryDTO;
import com.example.RestApi.service.EmployeeService;
import com.example.RestApi.service.DepartmentService;

import io.swagger.v3.oas.annotations.Operation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
    @Operation(summary = "Post the employee", description = "Save the employee into DB")
    public ResponseEntity<EmployeeSummaryDTO> createEmployee(@Valid @RequestBody EmployeeDTO empDto) {
        logger.info("Creating new employee: {}", empDto);
        EmployeeSummaryDTO saved = employeeService.create(empDto);
        URI location = URI.create("/api/employees/" + saved.getId());
        return ResponseEntity.created(location).body(saved);
    }

    @GetMapping("/employees/all")
    @Operation(summary = "Get all employees", description = "Returns list of employee")
    public ResponseEntity<List<EmployeeSummaryDTO>> getAllEmployees() {
        logger.info("Fetching all employees");
        List<EmployeeSummaryDTO> employees = employeeService.getAll();
        return ResponseEntity.ok(employees);
    }

    @GetMapping("/employees")
    @Operation(summary = "Get employee", description = "Returns the employee of mentioned ID")
    public ResponseEntity<EmployeeDTO> getEmployeeById(@RequestParam Long id) {
        logger.info("Fetching employee with ID {}:", id);
        EmployeeDTO emp = employeeService.getById(id);
        return ResponseEntity.ok(emp);
    }
    
    @PutMapping("/employees")
    @Operation(summary = "Updates employee", description = "Updates the employee changes into DB")
    public ResponseEntity<EmployeeDTO> updateEmployee(@RequestParam Long id, @Valid @RequestBody EmployeeDTO empDto) {
        logger.info("Updating employee {}:", empDto);
        EmployeeDTO updated = employeeService.update(id, empDto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/employees")
    @Operation(summary = "Deletes the employee", description = "Deletes the employee record in DB")
    public ResponseEntity<Void> deleteEmployee(@RequestParam Long id) {
        logger.info("Deleting employee of ID {}:", id);
        employeeService.delete(id);
        return ResponseEntity.noContent().build();
    }
    
//    @GetMapping("/employees/search")
//    @Operation(summary = "Search employees", description = "Search employees by name, address, or department")
//    public ResponseEntity<List<EmployeeSummaryDTO>> searchEmployees(
//            @RequestParam(required = false) String name,
//            @RequestParam(required = false) String address,
//            @RequestParam(required = false) Long departmentId) {
//
//        logger.info("Searching employees with filters - name: {}, address: {}, departmentId: {}", name, address, departmentId);
//        List<EmployeeSummaryDTO> employees = employeeService.search(name, address, departmentId);
//        return ResponseEntity.ok(employees);
//    }
    
    @GetMapping("/employees/search")
    @Operation(summary = "Search employees", description = "Search employees by filters with pagination and sorting")
    public ResponseEntity<Page<EmployeeSummaryDTO>> searchEmployees(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String address,
            @RequestParam(required = false) Long departmentId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id,asc") String[] sort) {

        String sortField = sort[0];
        String sortDirection = sort.length > 1 ? sort[1] : "asc";

        Pageable pageable = PageRequest.of(page, size,
                sortDirection.equalsIgnoreCase("desc") ? Sort.by(sortField).descending() : Sort.by(sortField).ascending());

        logger.info("Searching employees with filters - name: {}, address: {}, departmentId: {}, page: {}, size: {}, sort: {} {}",
                name, address, departmentId, page, size, sortField, sortDirection);

        Page<EmployeeSummaryDTO> employees = employeeService.search(name, address, departmentId, pageable);
        return ResponseEntity.ok(employees);
    }



    // ─── DEPARTMENT CRUD ───────────────────────────────────

    @PostMapping("/departments")
    @Operation(summary = "Post the department", description = "Saves the department into DB")
    public ResponseEntity<DepartmentSummaryDTO> createDepartment(@Valid @RequestBody DepartmentDTO deptDto) {
        logger.info("Creating new Department {}:", deptDto);
        DepartmentSummaryDTO saved = departmentService.create(deptDto);
        URI location = URI.create("/api/departments/" + saved.getId());
        return ResponseEntity.created(location).body(saved);
    }

    @GetMapping("/departments")
    @Operation(summary = "Get all departments", description = "Returns list of departments")
    public ResponseEntity<List<DepartmentSummaryDTO>> getAllDepartments() {
        logger.info("Fetching all departments");
        return ResponseEntity.ok(departmentService.getAll());
    }

    @GetMapping("/departments/{id}")
    @Operation(summary = "Gives department", description = "Returns department of mentioned ID")
    public ResponseEntity<DepartmentDTO> getDepartmentById(@PathVariable Long id) {
        logger.info("Fetching department with ID {}:", id);
        DepartmentDTO dept = departmentService.getById(id);
        return ResponseEntity.ok(dept);
    }

    @PutMapping("/departments/{id}")
    @Operation(summary = "updates the department", description = "updates the department in the DB")
    public ResponseEntity<DepartmentDTO> updateDepartment(@PathVariable Long id, @Valid @RequestBody DepartmentDTO deptDto) {
        logger.info("Updating department {}:", deptDto);
        DepartmentDTO updated = departmentService.update(id, deptDto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/departments/{id}")
    @Operation(summary = "Deletes the department ", description = "deletes the department from DB")
    public ResponseEntity<Void> deleteDepartment(@PathVariable Long id) {
        logger.info("Deleting department of ID {}:", id);
        departmentService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
