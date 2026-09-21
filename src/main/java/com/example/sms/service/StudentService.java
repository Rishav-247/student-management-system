package com.example.sms.service;

import com.example.sms.dto.StudentDto;
import com.example.sms.dto.StudentProfileDto;
import com.example.sms.entity.Student;
import com.example.sms.entity.StudentStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface StudentService {

    Student createStudent(StudentDto dto);

    Student updateStudent(Long id, StudentDto dto);

    Student getStudentById(Long id);

    Student getStudentByStudentId(String studentId);

    Student getStudentByEmail(String email);

    Student getStudentByUserId(Long userId);

    List<Student> getAllStudents();

    Page<Student> getFilteredStudents(String search, Long departmentId, Long courseId, Integer semester, StudentStatus status, Pageable pageable);

    void deleteStudent(Long id);

    long countStudents();

    long countActiveStudents();

    StudentProfileDto getStudentProfile(Long id);

    StudentDto convertToDto(Student student);
}
