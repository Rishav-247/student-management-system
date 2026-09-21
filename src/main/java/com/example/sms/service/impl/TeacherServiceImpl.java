package com.example.sms.service.impl;

import com.example.sms.dto.TeacherDto;
import com.example.sms.entity.Department;
import com.example.sms.entity.Teacher;
import com.example.sms.entity.User;
import com.example.sms.exception.DuplicateResourceException;
import com.example.sms.exception.ResourceNotFoundException;
import com.example.sms.repository.DepartmentRepository;
import com.example.sms.repository.TeacherRepository;
import com.example.sms.repository.UserRepository;
import com.example.sms.service.TeacherService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class TeacherServiceImpl implements TeacherService {

    private final TeacherRepository teacherRepository;
    private final DepartmentRepository departmentRepository;
    private final UserRepository userRepository;

    public TeacherServiceImpl(TeacherRepository teacherRepository,
                              DepartmentRepository departmentRepository,
                              UserRepository userRepository) {
        this.teacherRepository = teacherRepository;
        this.departmentRepository = departmentRepository;
        this.userRepository = userRepository;
    }

    @Override
    public Teacher createTeacher(TeacherDto dto) {
        String teacherId = dto.getTeacherId().trim().toUpperCase();
        if (teacherRepository.existsByTeacherId(teacherId)) {
            throw new DuplicateResourceException("Teacher", "teacherId", teacherId);
        }
        if (teacherRepository.existsByEmail(dto.getEmail().trim())) {
            throw new DuplicateResourceException("Teacher", "email", dto.getEmail().trim());
        }

        Teacher teacher = new Teacher();
        teacher.setTeacherId(teacherId);
        teacher.setFirstName(dto.getFirstName().trim());
        teacher.setLastName(dto.getLastName().trim());
        teacher.setEmail(dto.getEmail().trim().toLowerCase());
        teacher.setPhone(dto.getPhone());
        teacher.setDesignation(dto.getDesignation().trim());
        teacher.setQualification(dto.getQualification().trim());
        teacher.setJoiningDate(dto.getJoiningDate());
        teacher.setSpecialization(dto.getSpecialization());

        if (dto.getDepartmentId() != null) {
            Department department = departmentRepository.findById(dto.getDepartmentId())
                    .orElseThrow(() -> new ResourceNotFoundException("Department", "id", dto.getDepartmentId()));
            teacher.setDepartment(department);
        }

        if (dto.getUserId() != null) {
            User user = userRepository.findById(dto.getUserId())
                    .orElseThrow(() -> new ResourceNotFoundException("User", "id", dto.getUserId()));
            teacher.setUser(user);
        }

        return teacherRepository.save(teacher);
    }

    @Override
    public Teacher updateTeacher(Long id, TeacherDto dto) {
        Teacher teacher = getTeacherById(id);
        String teacherId = dto.getTeacherId().trim().toUpperCase();

        if (!teacher.getTeacherId().equalsIgnoreCase(teacherId) && teacherRepository.existsByTeacherId(teacherId)) {
            throw new DuplicateResourceException("Teacher", "teacherId", teacherId);
        }
        if (!teacher.getEmail().equalsIgnoreCase(dto.getEmail().trim()) && teacherRepository.existsByEmail(dto.getEmail().trim())) {
            throw new DuplicateResourceException("Teacher", "email", dto.getEmail().trim());
        }

        teacher.setTeacherId(teacherId);
        teacher.setFirstName(dto.getFirstName().trim());
        teacher.setLastName(dto.getLastName().trim());
        teacher.setEmail(dto.getEmail().trim().toLowerCase());
        teacher.setPhone(dto.getPhone());
        teacher.setDesignation(dto.getDesignation().trim());
        teacher.setQualification(dto.getQualification().trim());
        teacher.setJoiningDate(dto.getJoiningDate());
        teacher.setSpecialization(dto.getSpecialization());

        if (dto.getDepartmentId() != null) {
            Department department = departmentRepository.findById(dto.getDepartmentId())
                    .orElseThrow(() -> new ResourceNotFoundException("Department", "id", dto.getDepartmentId()));
            teacher.setDepartment(department);
        } else {
            teacher.setDepartment(null);
        }

        return teacherRepository.save(teacher);
    }

    @Override
    @Transactional(readOnly = true)
    public Teacher getTeacherById(Long id) {
        return teacherRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Teacher", "id", id));
    }

    @Override
    @Transactional(readOnly = true)
    public Teacher getTeacherByTeacherId(String teacherId) {
        return teacherRepository.findByTeacherId(teacherId.toUpperCase())
                .orElseThrow(() -> new ResourceNotFoundException("Teacher", "teacherId", teacherId));
    }

    @Override
    @Transactional(readOnly = true)
    public Teacher getTeacherByEmail(String email) {
        return teacherRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Teacher", "email", email));
    }

    @Override
    @Transactional(readOnly = true)
    public Teacher getTeacherByUserId(Long userId) {
        return teacherRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Teacher", "userId", userId));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Teacher> getAllTeachers() {
        return teacherRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Teacher> getTeachersByDepartment(Long departmentId) {
        return teacherRepository.findByDepartmentId(departmentId);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Teacher> searchTeachers(String search, Long departmentId, Pageable pageable) {
        return teacherRepository.searchTeachers(search, departmentId, pageable);
    }

    @Override
    public void deleteTeacher(Long id) {
        Teacher teacher = getTeacherById(id);
        teacherRepository.delete(teacher);
    }

    @Override
    @Transactional(readOnly = true)
    public long countTeachers() {
        return teacherRepository.count();
    }

    @Override
    @Transactional(readOnly = true)
    public TeacherDto convertToDto(Teacher teacher) {
        TeacherDto dto = new TeacherDto();
        dto.setId(teacher.getId());
        dto.setTeacherId(teacher.getTeacherId());
        dto.setFirstName(teacher.getFirstName());
        dto.setLastName(teacher.getLastName());
        dto.setEmail(teacher.getEmail());
        dto.setPhone(teacher.getPhone());
        dto.setDesignation(teacher.getDesignation());
        dto.setQualification(teacher.getQualification());
        dto.setJoiningDate(teacher.getJoiningDate());
        dto.setSpecialization(teacher.getSpecialization());

        if (teacher.getDepartment() != null) {
            dto.setDepartmentId(teacher.getDepartment().getId());
            dto.setDepartmentName(teacher.getDepartment().getName());
        }

        if (teacher.getUser() != null) {
            dto.setUserId(teacher.getUser().getId());
            dto.setUsername(teacher.getUser().getUsername());
        }

        return dto;
    }
}
