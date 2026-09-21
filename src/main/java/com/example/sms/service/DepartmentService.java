package com.example.sms.service;

import com.example.sms.dto.DepartmentDto;
import com.example.sms.entity.Department;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface DepartmentService {

    Department createDepartment(DepartmentDto dto);

    Department updateDepartment(Long id, DepartmentDto dto);

    Department getDepartmentById(Long id);

    Department getDepartmentByCode(String code);

    List<Department> getAllDepartments();

    Page<Department> searchDepartments(String search, Pageable pageable);

    void deleteDepartment(Long id);

    long countDepartments();

    DepartmentDto convertToDto(Department department);
}
