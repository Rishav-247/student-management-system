package com.example.sms.service.impl;

import com.example.sms.dto.DepartmentDto;
import com.example.sms.entity.Department;
import com.example.sms.exception.BadRequestException;
import com.example.sms.exception.DuplicateResourceException;
import com.example.sms.exception.ResourceNotFoundException;
import com.example.sms.repository.CourseRepository;
import com.example.sms.repository.DepartmentRepository;
import com.example.sms.repository.StudentRepository;
import com.example.sms.repository.TeacherRepository;
import com.example.sms.service.DepartmentService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class DepartmentServiceImpl implements DepartmentService {

    private final DepartmentRepository departmentRepository;
    private final CourseRepository courseRepository;
    private final TeacherRepository teacherRepository;
    private final StudentRepository studentRepository;

    public DepartmentServiceImpl(DepartmentRepository departmentRepository,
                                 CourseRepository courseRepository,
                                 TeacherRepository teacherRepository,
                                 StudentRepository studentRepository) {
        this.departmentRepository = departmentRepository;
        this.courseRepository = courseRepository;
        this.teacherRepository = teacherRepository;
        this.studentRepository = studentRepository;
    }

    @Override
    public Department createDepartment(DepartmentDto dto) {
        String code = dto.getCode().trim().toUpperCase();
        if (departmentRepository.existsByCode(code)) {
            throw new DuplicateResourceException("Department", "code", code);
        }
        if (departmentRepository.existsByName(dto.getName().trim())) {
            throw new DuplicateResourceException("Department", "name", dto.getName().trim());
        }

        Department department = new Department();
        department.setCode(code);
        department.setName(dto.getName().trim());
        department.setDescription(dto.getDescription());
        department.setHeadOfDepartment(dto.getHeadOfDepartment());
        department.setContactEmail(dto.getContactEmail());
        department.setContactPhone(dto.getContactPhone());

        return departmentRepository.save(department);
    }

    @Override
    public Department updateDepartment(Long id, DepartmentDto dto) {
        Department department = getDepartmentById(id);
        String code = dto.getCode().trim().toUpperCase();

        if (!department.getCode().equalsIgnoreCase(code) && departmentRepository.existsByCode(code)) {
            throw new DuplicateResourceException("Department", "code", code);
        }
        if (!department.getName().equalsIgnoreCase(dto.getName().trim()) && departmentRepository.existsByName(dto.getName().trim())) {
            throw new DuplicateResourceException("Department", "name", dto.getName().trim());
        }

        department.setCode(code);
        department.setName(dto.getName().trim());
        department.setDescription(dto.getDescription());
        department.setHeadOfDepartment(dto.getHeadOfDepartment());
        department.setContactEmail(dto.getContactEmail());
        department.setContactPhone(dto.getContactPhone());

        return departmentRepository.save(department);
    }

    @Override
    @Transactional(readOnly = true)
    public Department getDepartmentById(Long id) {
        return departmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Department", "id", id));
    }

    @Override
    @Transactional(readOnly = true)
    public Department getDepartmentByCode(String code) {
        return departmentRepository.findByCode(code.toUpperCase())
                .orElseThrow(() -> new ResourceNotFoundException("Department", "code", code));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Department> getAllDepartments() {
        return departmentRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Department> searchDepartments(String search, Pageable pageable) {
        if (search == null || search.trim().isEmpty()) {
            return departmentRepository.findAll(pageable);
        }
        return departmentRepository.searchDepartments(search.trim(), pageable);
    }

    @Override
    public void deleteDepartment(Long id) {
        Department department = getDepartmentById(id);
        long studentCount = studentRepository.countByDepartmentId(id);
        if (studentCount > 0) {
            throw new BadRequestException("Cannot delete department with enrolled students (" + studentCount + " students present)");
        }
        departmentRepository.delete(department);
    }

    @Override
    @Transactional(readOnly = true)
    public long countDepartments() {
        return departmentRepository.count();
    }

    @Override
    @Transactional(readOnly = true)
    public DepartmentDto convertToDto(Department department) {
        DepartmentDto dto = new DepartmentDto();
        dto.setId(department.getId());
        dto.setCode(department.getCode());
        dto.setName(department.getName());
        dto.setDescription(department.getDescription());
        dto.setHeadOfDepartment(department.getHeadOfDepartment());
        dto.setContactEmail(department.getContactEmail());
        dto.setContactPhone(department.getContactPhone());
        dto.setCourseCount((int) courseRepository.findByDepartmentId(department.getId()).size());
        dto.setTeacherCount((int) teacherRepository.findByDepartmentId(department.getId()).size());
        dto.setStudentCount((int) studentRepository.countByDepartmentId(department.getId()));
        return dto;
    }
}
