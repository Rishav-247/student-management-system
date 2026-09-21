package com.example.sms.service;

import com.example.sms.dto.TeacherDto;
import com.example.sms.entity.Teacher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface TeacherService {

    Teacher createTeacher(TeacherDto dto);

    Teacher updateTeacher(Long id, TeacherDto dto);

    Teacher getTeacherById(Long id);

    Teacher getTeacherByTeacherId(String teacherId);

    Teacher getTeacherByEmail(String email);

    Teacher getTeacherByUserId(Long userId);

    List<Teacher> getAllTeachers();

    List<Teacher> getTeachersByDepartment(Long departmentId);

    Page<Teacher> searchTeachers(String search, Long departmentId, Pageable pageable);

    void deleteTeacher(Long id);

    long countTeachers();

    TeacherDto convertToDto(Teacher teacher);
}
