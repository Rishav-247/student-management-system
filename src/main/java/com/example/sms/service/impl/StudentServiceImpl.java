package com.example.sms.service.impl;

import com.example.sms.dto.*;
import com.example.sms.entity.*;
import com.example.sms.exception.DuplicateResourceException;
import com.example.sms.exception.ResourceNotFoundException;
import com.example.sms.repository.*;
import com.example.sms.service.StudentService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class StudentServiceImpl implements StudentService {

    private final StudentRepository studentRepository;
    private final DepartmentRepository departmentRepository;
    private final CourseRepository courseRepository;
    private final UserRepository userRepository;
    private final AttendanceRepository attendanceRepository;
    private final MarksRepository marksRepository;
    private final EnrollmentRepository enrollmentRepository;

    public StudentServiceImpl(StudentRepository studentRepository,
                              DepartmentRepository departmentRepository,
                              CourseRepository courseRepository,
                              UserRepository userRepository,
                              AttendanceRepository attendanceRepository,
                              MarksRepository marksRepository,
                              EnrollmentRepository enrollmentRepository) {
        this.studentRepository = studentRepository;
        this.departmentRepository = departmentRepository;
        this.courseRepository = courseRepository;
        this.userRepository = userRepository;
        this.attendanceRepository = attendanceRepository;
        this.marksRepository = marksRepository;
        this.enrollmentRepository = enrollmentRepository;
    }

    @Override
    public Student createStudent(StudentDto dto) {
        String studentId = dto.getStudentId().trim().toUpperCase();
        if (studentRepository.existsByStudentId(studentId)) {
            throw new DuplicateResourceException("Student", "studentId", studentId);
        }
        if (studentRepository.existsByEmail(dto.getEmail().trim())) {
            throw new DuplicateResourceException("Student", "email", dto.getEmail().trim());
        }

        Department department = departmentRepository.findById(dto.getDepartmentId())
                .orElseThrow(() -> new ResourceNotFoundException("Department", "id", dto.getDepartmentId()));

        Course course = courseRepository.findById(dto.getCourseId())
                .orElseThrow(() -> new ResourceNotFoundException("Course", "id", dto.getCourseId()));

        Student student = new Student();
        student.setStudentId(studentId);
        student.setFirstName(dto.getFirstName().trim());
        student.setLastName(dto.getLastName().trim());
        student.setEmail(dto.getEmail().trim().toLowerCase());
        student.setPhone(dto.getPhone());
        student.setDateOfBirth(dto.getDateOfBirth());
        student.setGender(dto.getGender());
        student.setAddress(dto.getAddress());
        student.setDepartment(department);
        student.setCourse(course);
        student.setSemester(dto.getSemester() != null ? dto.getSemester() : 1);
        student.setAdmissionDate(dto.getAdmissionDate());
        student.setProfileImageUrl(dto.getProfileImageUrl());
        student.setStatus(dto.getStatus() != null ? dto.getStatus() : StudentStatus.ACTIVE);

        if (dto.getUserId() != null) {
            User user = userRepository.findById(dto.getUserId())
                    .orElseThrow(() -> new ResourceNotFoundException("User", "id", dto.getUserId()));
            student.setUser(user);
        }

        return studentRepository.save(student);
    }

    @Override
    public Student updateStudent(Long id, StudentDto dto) {
        Student student = getStudentById(id);
        String studentId = dto.getStudentId().trim().toUpperCase();

        if (!student.getStudentId().equalsIgnoreCase(studentId) && studentRepository.existsByStudentId(studentId)) {
            throw new DuplicateResourceException("Student", "studentId", studentId);
        }
        if (!student.getEmail().equalsIgnoreCase(dto.getEmail().trim()) && studentRepository.existsByEmail(dto.getEmail().trim())) {
            throw new DuplicateResourceException("Student", "email", dto.getEmail().trim());
        }

        Department department = departmentRepository.findById(dto.getDepartmentId())
                .orElseThrow(() -> new ResourceNotFoundException("Department", "id", dto.getDepartmentId()));

        Course course = courseRepository.findById(dto.getCourseId())
                .orElseThrow(() -> new ResourceNotFoundException("Course", "id", dto.getCourseId()));

        student.setStudentId(studentId);
        student.setFirstName(dto.getFirstName().trim());
        student.setLastName(dto.getLastName().trim());
        student.setEmail(dto.getEmail().trim().toLowerCase());
        student.setPhone(dto.getPhone());
        student.setDateOfBirth(dto.getDateOfBirth());
        student.setGender(dto.getGender());
        student.setAddress(dto.getAddress());
        student.setDepartment(department);
        student.setCourse(course);
        student.setSemester(dto.getSemester() != null ? dto.getSemester() : 1);
        student.setAdmissionDate(dto.getAdmissionDate());
        if (dto.getProfileImageUrl() != null) {
            student.setProfileImageUrl(dto.getProfileImageUrl());
        }
        if (dto.getStatus() != null) {
            student.setStatus(dto.getStatus());
        }

        return studentRepository.save(student);
    }

    @Override
    @Transactional(readOnly = true)
    public Student getStudentById(Long id) {
        return studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student", "id", id));
    }

    @Override
    @Transactional(readOnly = true)
    public Student getStudentByStudentId(String studentId) {
        return studentRepository.findByStudentId(studentId.toUpperCase())
                .orElseThrow(() -> new ResourceNotFoundException("Student", "studentId", studentId));
    }

    @Override
    @Transactional(readOnly = true)
    public Student getStudentByEmail(String email) {
        return studentRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Student", "email", email));
    }

    @Override
    @Transactional(readOnly = true)
    public Student getStudentByUserId(Long userId) {
        return studentRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Student", "userId", userId));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Student> getFilteredStudents(String search, Long departmentId, Long courseId, Integer semester, StudentStatus status, Pageable pageable) {
        return studentRepository.findWithFilters(search, departmentId, courseId, semester, status, pageable);
    }

    @Override
    public void deleteStudent(Long id) {
        Student student = getStudentById(id);
        studentRepository.delete(student);
    }

    @Override
    @Transactional(readOnly = true)
    public long countStudents() {
        return studentRepository.count();
    }

    @Override
    @Transactional(readOnly = true)
    public long countActiveStudents() {
        return studentRepository.countByStatus(StudentStatus.ACTIVE);
    }

    @Override
    @Transactional(readOnly = true)
    public StudentProfileDto getStudentProfile(Long id) {
        Student student = getStudentById(id);
        StudentProfileDto profile = new StudentProfileDto();
        profile.setStudent(convertToDto(student));

        long totalClasses = attendanceRepository.countByStudentId(id);
        long presentCount = attendanceRepository.countByStudentIdAndStatus(id, AttendanceStatus.PRESENT);
        long absentCount = attendanceRepository.countByStudentIdAndStatus(id, AttendanceStatus.ABSENT);

        profile.setTotalClasses(totalClasses);
        profile.setAttendedClasses(presentCount);
        profile.setAbsentClasses(absentCount);
        profile.setOverallAttendanceRate(totalClasses > 0 ? (presentCount * 100.0) / totalClasses : 0.0);

        List<AttendanceDto> recentAttendance = attendanceRepository.findByStudentId(id).stream()
                .limit(10)
                .map(a -> {
                    AttendanceDto dto = new AttendanceDto();
                    dto.setId(a.getId());
                    dto.setAttendanceDate(a.getAttendanceDate());
                    dto.setStatus(a.getStatus());
                    dto.setSubjectName(a.getSubject().getName());
                    dto.setSubjectCode(a.getSubject().getCode());
                    dto.setRemarks(a.getRemarks());
                    return dto;
                }).collect(Collectors.toList());
        profile.setRecentAttendance(recentAttendance);

        List<MarksDto> marksList = marksRepository.findByStudentId(id).stream()
                .map(m -> {
                    MarksDto dto = new MarksDto();
                    dto.setId(m.getId());
                    dto.setSubjectName(m.getSubject().getName());
                    dto.setSubjectCode(m.getSubject().getCode());
                    dto.setSemester(m.getSemester());
                    dto.setAcademicYear(m.getAcademicYear());
                    dto.setInternalMarks(m.getInternalMarks());
                    dto.setAssignmentMarks(m.getAssignmentMarks());
                    dto.setPracticalMarks(m.getPracticalMarks());
                    dto.setFinalExamMarks(m.getFinalExamMarks());
                    dto.setTotalMarks(m.getTotalMarks());
                    dto.setPercentage(m.getPercentage());
                    dto.setGrade(m.getGrade());
                    dto.setStatus(m.getStatus());
                    return dto;
                }).collect(Collectors.toList());
        profile.setMarks(marksList);

        List<EnrollmentDto> enrollmentList = enrollmentRepository.findByStudentId(id).stream()
                .map(e -> {
                    EnrollmentDto dto = new EnrollmentDto();
                    dto.setId(e.getId());
                    dto.setCourseName(e.getCourse().getName());
                    dto.setCourseCode(e.getCourse().getCode());
                    dto.setAcademicYear(e.getAcademicYear());
                    dto.setSemester(e.getSemester());
                    dto.setEnrollmentDate(e.getEnrollmentDate());
                    dto.setStatus(e.getStatus());
                    return dto;
                }).collect(Collectors.toList());
        profile.setEnrollments(enrollmentList);

        return profile;
    }

    @Override
    @Transactional(readOnly = true)
    public StudentDto convertToDto(Student student) {
        StudentDto dto = new StudentDto();
        dto.setId(student.getId());
        dto.setStudentId(student.getStudentId());
        dto.setFirstName(student.getFirstName());
        dto.setLastName(student.getLastName());
        dto.setEmail(student.getEmail());
        dto.setPhone(student.getPhone());
        dto.setDateOfBirth(student.getDateOfBirth());
        dto.setGender(student.getGender());
        dto.setAddress(student.getAddress());
        dto.setDepartmentId(student.getDepartment().getId());
        dto.setDepartmentName(student.getDepartment().getName());
        dto.setCourseId(student.getCourse().getId());
        dto.setCourseName(student.getCourse().getName());
        dto.setSemester(student.getSemester());
        dto.setAdmissionDate(student.getAdmissionDate());
        dto.setProfileImageUrl(student.getProfileImageUrl());
        dto.setStatus(student.getStatus());

        if (student.getUser() != null) {
            dto.setUserId(student.getUser().getId());
            dto.setUsername(student.getUser().getUsername());
        }

        long total = attendanceRepository.countByStudentId(student.getId());
        long present = attendanceRepository.countByStudentIdAndStatus(student.getId(), AttendanceStatus.PRESENT);
        dto.setAttendanceRate(total > 0 ? (present * 100.0) / total : 100.0);

        List<Marks> marks = marksRepository.findByStudentId(student.getId());
        if (!marks.isEmpty()) {
            double avgScore = marks.stream().mapToDouble(m -> m.getTotalMarks().doubleValue()).average().orElse(0.0);
            dto.setCurrentGpa(Math.round((avgScore / 10.0) * 100.0) / 100.0);
        }

        return dto;
    }
}
