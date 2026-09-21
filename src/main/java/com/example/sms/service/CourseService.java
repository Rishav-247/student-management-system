package com.example.sms.service;

import com.example.sms.dto.CourseDto;
import com.example.sms.entity.Course;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CourseService {

    Course createCourse(CourseDto dto);

    Course updateCourse(Long id, CourseDto dto);

    Course getCourseById(Long id);

    Course getCourseByCode(String code);

    List<Course> getAllCourses();

    List<Course> getCoursesByDepartment(Long departmentId);

    Page<Course> searchCourses(String search, Long departmentId, Pageable pageable);

    void deleteCourse(Long id);

    long countCourses();

    CourseDto convertToDto(Course course);
}
