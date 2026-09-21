package com.example.sms.service.impl;

import com.example.sms.dto.CourseDto;
import com.example.sms.entity.Course;
import com.example.sms.entity.Department;
import com.example.sms.exception.BadRequestException;
import com.example.sms.exception.DuplicateResourceException;
import com.example.sms.exception.ResourceNotFoundException;
import com.example.sms.repository.CourseRepository;
import com.example.sms.repository.DepartmentRepository;
import com.example.sms.repository.StudentRepository;
import com.example.sms.repository.SubjectRepository;
import com.example.sms.service.CourseService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class CourseServiceImpl implements CourseService {

    private final CourseRepository courseRepository;
    private final DepartmentRepository departmentRepository;
    private final StudentRepository studentRepository;
    private final SubjectRepository subjectRepository;

    public CourseServiceImpl(CourseRepository courseRepository,
                             DepartmentRepository departmentRepository,
                             StudentRepository studentRepository,
                             SubjectRepository subjectRepository) {
        this.courseRepository = courseRepository;
        this.departmentRepository = departmentRepository;
        this.studentRepository = studentRepository;
        this.subjectRepository = subjectRepository;
    }

    @Override
    public Course createCourse(CourseDto dto) {
        String code = dto.getCode().trim().toUpperCase();
        if (courseRepository.existsByCode(code)) {
            throw new DuplicateResourceException("Course", "code", code);
        }

        Department department = departmentRepository.findById(dto.getDepartmentId())
                .orElseThrow(() -> new ResourceNotFoundException("Department", "id", dto.getDepartmentId()));

        Course course = new Course();
        course.setCode(code);
        course.setName(dto.getName().trim());
        course.setDepartment(department);
        course.setDurationYears(dto.getDurationYears());
        course.setTotalSemesters(dto.getTotalSemesters());
        course.setDegreeType(dto.getDegreeType().trim());
        course.setDescription(dto.getDescription());

        return courseRepository.save(course);
    }

    @Override
    public Course updateCourse(Long id, CourseDto dto) {
        Course course = getCourseById(id);
        String code = dto.getCode().trim().toUpperCase();

        if (!course.getCode().equalsIgnoreCase(code) && courseRepository.existsByCode(code)) {
            throw new DuplicateResourceException("Course", "code", code);
        }

        Department department = departmentRepository.findById(dto.getDepartmentId())
                .orElseThrow(() -> new ResourceNotFoundException("Department", "id", dto.getDepartmentId()));

        course.setCode(code);
        course.setName(dto.getName().trim());
        course.setDepartment(department);
        course.setDurationYears(dto.getDurationYears());
        course.setTotalSemesters(dto.getTotalSemesters());
        course.setDegreeType(dto.getDegreeType().trim());
        course.setDescription(dto.getDescription());

        return courseRepository.save(course);
    }

    @Override
    @Transactional(readOnly = true)
    public Course getCourseById(Long id) {
        return courseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Course", "id", id));
    }

    @Override
    @Transactional(readOnly = true)
    public Course getCourseByCode(String code) {
        return courseRepository.findByCode(code.toUpperCase())
                .orElseThrow(() -> new ResourceNotFoundException("Course", "code", code));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Course> getAllCourses() {
        return courseRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Course> getCoursesByDepartment(Long departmentId) {
        return courseRepository.findByDepartmentId(departmentId);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Course> searchCourses(String search, Long departmentId, Pageable pageable) {
        return courseRepository.searchCourses(search, departmentId, pageable);
    }

    @Override
    public void deleteCourse(Long id) {
        Course course = getCourseById(id);
        long studentCount = studentRepository.countByCourseId(id);
        if (studentCount > 0) {
            throw new BadRequestException("Cannot delete course with active students enrolled (" + studentCount + " students)");
        }
        courseRepository.delete(course);
    }

    @Override
    @Transactional(readOnly = true)
    public long countCourses() {
        return courseRepository.count();
    }

    @Override
    @Transactional(readOnly = true)
    public CourseDto convertToDto(Course course) {
        CourseDto dto = new CourseDto();
        dto.setId(course.getId());
        dto.setCode(course.getCode());
        dto.setName(course.getName());
        dto.setDepartmentId(course.getDepartment().getId());
        dto.setDepartmentName(course.getDepartment().getName());
        dto.setDurationYears(course.getDurationYears());
        dto.setTotalSemesters(course.getTotalSemesters());
        dto.setDegreeType(course.getDegreeType());
        dto.setDescription(course.getDescription());
        dto.setStudentCount((int) studentRepository.countByCourseId(course.getId()));
        dto.setSubjectCount(subjectRepository.findByCourseId(course.getId()).size());
        return dto;
    }
}
