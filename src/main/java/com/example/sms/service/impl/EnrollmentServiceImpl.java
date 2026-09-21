package com.example.sms.service.impl;

import com.example.sms.dto.EnrollmentDto;
import com.example.sms.entity.Course;
import com.example.sms.entity.Enrollment;
import com.example.sms.entity.EnrollmentStatus;
import com.example.sms.entity.Student;
import com.example.sms.exception.DuplicateResourceException;
import com.example.sms.exception.ResourceNotFoundException;
import com.example.sms.repository.CourseRepository;
import com.example.sms.repository.EnrollmentRepository;
import com.example.sms.repository.StudentRepository;
import com.example.sms.service.EnrollmentService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@Transactional
public class EnrollmentServiceImpl implements EnrollmentService {

    private final EnrollmentRepository enrollmentRepository;
    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;

    public EnrollmentServiceImpl(EnrollmentRepository enrollmentRepository,
                                 StudentRepository studentRepository,
                                 CourseRepository courseRepository) {
        this.enrollmentRepository = enrollmentRepository;
        this.studentRepository = studentRepository;
        this.courseRepository = courseRepository;
    }

    @Override
    public Enrollment enrollStudent(EnrollmentDto dto) {
        if (enrollmentRepository.existsByStudentIdAndCourseIdAndAcademicYearAndSemester(
                dto.getStudentId(), dto.getCourseId(), dto.getAcademicYear(), dto.getSemester())) {
            throw new DuplicateResourceException("Student already enrolled in this course for semester " + dto.getSemester() + " (" + dto.getAcademicYear() + ")");
        }

        Student student = studentRepository.findById(dto.getStudentId())
                .orElseThrow(() -> new ResourceNotFoundException("Student", "id", dto.getStudentId()));

        Course course = courseRepository.findById(dto.getCourseId())
                .orElseThrow(() -> new ResourceNotFoundException("Course", "id", dto.getCourseId()));

        Enrollment enrollment = new Enrollment();
        enrollment.setStudent(student);
        enrollment.setCourse(course);
        enrollment.setAcademicYear(dto.getAcademicYear().trim());
        enrollment.setSemester(dto.getSemester());
        enrollment.setEnrollmentDate(dto.getEnrollmentDate() != null ? dto.getEnrollmentDate() : LocalDate.now());
        enrollment.setStatus(dto.getStatus() != null ? dto.getStatus() : EnrollmentStatus.ACTIVE);

        return enrollmentRepository.save(enrollment);
    }

    @Override
    public Enrollment updateEnrollment(Long id, EnrollmentDto dto) {
        Enrollment enrollment = getEnrollmentById(id);

        Course course = courseRepository.findById(dto.getCourseId())
                .orElseThrow(() -> new ResourceNotFoundException("Course", "id", dto.getCourseId()));

        enrollment.setCourse(course);
        enrollment.setAcademicYear(dto.getAcademicYear().trim());
        enrollment.setSemester(dto.getSemester());
        if (dto.getEnrollmentDate() != null) {
            enrollment.setEnrollmentDate(dto.getEnrollmentDate());
        }
        if (dto.getStatus() != null) {
            enrollment.setStatus(dto.getStatus());
        }

        return enrollmentRepository.save(enrollment);
    }

    @Override
    @Transactional(readOnly = true)
    public Enrollment getEnrollmentById(Long id) {
        return enrollmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Enrollment", "id", id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Enrollment> getEnrollmentsByStudent(Long studentId) {
        return enrollmentRepository.findByStudentId(studentId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Enrollment> getEnrollmentsByCourse(Long courseId) {
        return enrollmentRepository.findByCourseId(courseId);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Enrollment> searchEnrollments(String search, Long courseId, Integer semester, String academicYear, EnrollmentStatus status, Pageable pageable) {
        return enrollmentRepository.searchEnrollments(search, courseId, semester, academicYear, status, pageable);
    }

    @Override
    public void deleteEnrollment(Long id) {
        Enrollment enrollment = getEnrollmentById(id);
        enrollmentRepository.delete(enrollment);
    }

    @Override
    @Transactional(readOnly = true)
    public long countActiveEnrollments() {
        return enrollmentRepository.countByStatus(EnrollmentStatus.ACTIVE);
    }

    @Override
    @Transactional(readOnly = true)
    public EnrollmentDto convertToDto(Enrollment enrollment) {
        EnrollmentDto dto = new EnrollmentDto();
        dto.setId(enrollment.getId());
        dto.setStudentId(enrollment.getStudent().getId());
        dto.setStudentName(enrollment.getStudent().getFullName());
        dto.setStudentCode(enrollment.getStudent().getStudentId());
        dto.setCourseId(enrollment.getCourse().getId());
        dto.setCourseName(enrollment.getCourse().getName());
        dto.setCourseCode(enrollment.getCourse().getCode());
        dto.setAcademicYear(enrollment.getAcademicYear());
        dto.setSemester(enrollment.getSemester());
        dto.setEnrollmentDate(enrollment.getEnrollmentDate());
        dto.setStatus(enrollment.getStatus());
        return dto;
    }
}
