package com.example.sms.service.impl;

import com.example.sms.dto.DashboardStatsDto;
import com.example.sms.dto.DepartmentDto;
import com.example.sms.dto.StudentDto;
import com.example.sms.entity.StudentStatus;
import com.example.sms.repository.*;
import com.example.sms.service.AttendanceService;
import com.example.sms.service.DashboardService;
import com.example.sms.service.DepartmentService;
import com.example.sms.service.MarksService;
import com.example.sms.service.StudentService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class DashboardServiceImpl implements DashboardService {

    private final StudentRepository studentRepository;
    private final TeacherRepository teacherRepository;
    private final CourseRepository courseRepository;
    private final DepartmentRepository departmentRepository;
    private final EnrollmentRepository enrollmentRepository;
    private final AttendanceService attendanceService;
    private final MarksService marksService;
    private final StudentService studentService;
    private final DepartmentService departmentService;

    public DashboardServiceImpl(StudentRepository studentRepository,
                                TeacherRepository teacherRepository,
                                CourseRepository courseRepository,
                                DepartmentRepository departmentRepository,
                                EnrollmentRepository enrollmentRepository,
                                AttendanceService attendanceService,
                                MarksService marksService,
                                StudentService studentService,
                                DepartmentService departmentService) {
        this.studentRepository = studentRepository;
        this.teacherRepository = teacherRepository;
        this.courseRepository = courseRepository;
        this.departmentRepository = departmentRepository;
        this.enrollmentRepository = enrollmentRepository;
        this.attendanceService = attendanceService;
        this.marksService = marksService;
        this.studentService = studentService;
        this.departmentService = departmentService;
    }

    @Override
    public DashboardStatsDto getDashboardStats() {
        DashboardStatsDto stats = new DashboardStatsDto();

        stats.setTotalStudents(studentRepository.count());
        stats.setActiveStudents(studentRepository.countByStatus(StudentStatus.ACTIVE));
        stats.setTotalTeachers(teacherRepository.count());
        stats.setTotalCourses(courseRepository.count());
        stats.setTotalDepartments(departmentRepository.count());
        stats.setActiveEnrollments(enrollmentRepository.count());
        stats.setOverallAttendanceRate(attendanceService.calculateOverallAttendanceRate());
        stats.setOverallPassRate(marksService.calculateOverallPassRate());

        List<StudentDto> recentStudents = studentRepository
                .findAll(PageRequest.of(0, 5, Sort.by(Sort.Direction.DESC, "createdAt")))
                .getContent()
                .stream()
                .map(studentService::convertToDto)
                .collect(Collectors.toList());
        stats.setRecentStudents(recentStudents);

        List<DepartmentDto> departmentStats = departmentRepository
                .findAll()
                .stream()
                .map(departmentService::convertToDto)
                .collect(Collectors.toList());
        stats.setDepartmentStats(departmentStats);

        return stats;
    }
}
