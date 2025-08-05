package com.example.RestApi.specification;

import com.example.RestApi.model.Employee;
import org.springframework.data.jpa.domain.Specification;

public class EmployeeSpecification {

    public static Specification<Employee> hasName(String name) {
        return (root, query, cb) ->
                name == null ? null : cb.like(cb.lower(root.get("name")), "%" + name.toLowerCase() + "%");
    }

    public static Specification<Employee> hasAddress(String address) {
        return (root, query, cb) ->
                address == null ? null : cb.like(cb.lower(root.get("address")), "%" + address.toLowerCase() + "%");
    }

    public static Specification<Employee> hasDepartment(Long departmentId) {
        return (root, query, cb) ->
                departmentId == null ? null : cb.equal(root.get("department").get("id"), departmentId);
    }
}
