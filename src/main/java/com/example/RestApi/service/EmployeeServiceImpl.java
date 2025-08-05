package com.example.RestApi.service;

import com.example.RestApi.dto.DepartmentSummaryDTO;
import com.example.RestApi.dto.EmployeeDTO;
import com.example.RestApi.dto.EmployeeSummaryDTO;
import com.example.RestApi.exception.ResourceNotFoundException;
import com.example.RestApi.model.Employee;
import com.example.RestApi.model.Department;
import com.example.RestApi.repository.EmployeeRepository;
import com.example.RestApi.specification.EmployeeSpecification;
import com.example.RestApi.repository.DepartmentRepository;

import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class EmployeeServiceImpl implements EmployeeService {

    private static final Logger logger = LoggerFactory.getLogger(EmployeeServiceImpl.class);

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private DepartmentRepository departmentRepository;

    @Override
    public EmployeeSummaryDTO create(EmployeeDTO empDto) {
        logger.debug("Saving employee to DB: {}", empDto);

        Department dept = departmentRepository.findById(empDto.getDepartment().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Department not found"));

        Employee emp = new Employee();
        emp.setName(empDto.getName());
        emp.setAddress(empDto.getAddress());
        emp.setDepartment(dept);

        Employee saved = employeeRepository.save(emp);
        return mapToSummaryDTO(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public List<EmployeeSummaryDTO> getAll() {
        logger.debug("Getting all employees from DB");
        return employeeRepository.findAll()
                .stream()
                .map(this::mapToSummaryDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public EmployeeDTO getById(Long id) {
        logger.debug("Getting employee from DB with ID: {}", id);
        Optional<Employee> optionalEmp = employeeRepository.findById(id);
        return optionalEmp
                .map(this::mapToDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with ID: " + id));
    }

    @Override
    public EmployeeDTO update(Long id, EmployeeDTO dto) {
        logger.debug("Updating employee in DB with ID: {}", id);

        Optional<Employee> optionalEmp = employeeRepository.findById(id);
        Optional<Department> optionalDept = departmentRepository.findById(dto.getDepartment().getId());

        if (optionalEmp.isEmpty()) {
            throw new ResourceNotFoundException("Employee not found with ID: " + id);
        }

        if (optionalDept.isEmpty()) {
            throw new ResourceNotFoundException("Department not found");
        }

        Employee emp = optionalEmp.get();
        Department dept = optionalDept.get();

        emp.setName(dto.getName());
        emp.setAddress(dto.getAddress());
        emp.setDepartment(dept);

        return mapToDTO(employeeRepository.save(emp));
    }

    @Override
    public void delete(Long id) {
        logger.debug("Deleting employee from DB with ID: {}", id);
        Optional<Employee> empOpt = employeeRepository.findById(id);
        empOpt.ifPresentOrElse(
            emp -> employeeRepository.deleteById(id),
            () -> { throw new ResourceNotFoundException("Employee not found with ID: " + id); }
        );
    }
    
//    @Override
//    @Transactional(readOnly = true)
//    public List<EmployeeSummaryDTO> search(String name, String address, Long departmentId) {
//        Specification<Employee> spec = Specification
//                .where(EmployeeSpecification.hasName(name))
//                .and(EmployeeSpecification.hasAddress(address))
//                .and(EmployeeSpecification.hasDepartment(departmentId));
//
//        return employeeRepository.findAll(spec)
//                .stream()
//                .map(this::mapToSummaryDTO)
//                .collect(Collectors.toList());
//    }

    @Override
    @Transactional(readOnly = true)
    public Page<EmployeeSummaryDTO> search(String name, String address, Long departmentId, Pageable pageable) {
        Specification<Employee> spec = Specification
                .where(EmployeeSpecification.hasName(name))
                .and(EmployeeSpecification.hasAddress(address))
                .and(EmployeeSpecification.hasDepartment(departmentId));

        return employeeRepository.findAll(spec, pageable)
                .map(this::mapToSummaryDTO);
    }
    
    // ─── Mapping Helpers ──────────────────────────────

    private EmployeeDTO mapToDTO(Employee emp) {
        EmployeeDTO dto = new EmployeeDTO();
        dto.setId(emp.getId());
        dto.setName(emp.getName());
        dto.setAddress(emp.getAddress());

        DepartmentSummaryDTO deptDto = new DepartmentSummaryDTO();
        deptDto.setId(emp.getDepartment().getId());
        deptDto.setName(emp.getDepartment().getName());

        dto.setDepartment(deptDto);
        return dto;
    }

    private EmployeeSummaryDTO mapToSummaryDTO(Employee emp) {
        EmployeeSummaryDTO dto = new EmployeeSummaryDTO();
        dto.setId(emp.getId());
        dto.setName(emp.getName());
        return dto;
    }
}
