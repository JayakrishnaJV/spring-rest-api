package com.example.RestApi.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.RestApi.model.Department;


public interface DepartmentRepository extends JpaRepository<Department, Long>{

}
