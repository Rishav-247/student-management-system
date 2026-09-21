package com.example.sms.service;

import com.example.sms.dto.EnrollmentDto;
import com.example.sms.entity.Enrollment;
import com.example.sms.entity.EnrollmentStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface EnrollmentService {

    Enrollment enrollStudent(EnrollmentDto dto);

    Enrollment updateEnrollment(Long id, EnrollmentDto dto);

    Enrollment getEnrollmentById(Long id);

    List<Enrollment> getEnrollmentsByStudent(Long studentId);

    List<Enrollment> getEnrollmentsByCourse(Long courseId);

    Page<Enrollment> searchEnrollments(String search, Long courseId, Integer semester, String academicYear, EnrollmentStatus status, Pageable pageable);

    void deleteEnrollment(Long id);

    long countActiveEnrollments();

    EnrollmentDto convertToDto(Enrollment enrollment);
}
