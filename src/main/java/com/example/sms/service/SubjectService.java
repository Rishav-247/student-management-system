package com.example.sms.service;

import com.example.sms.dto.SubjectDto;
import com.example.sms.entity.Subject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface SubjectService {

    Subject createSubject(SubjectDto dto);

    Subject updateSubject(Long id, SubjectDto dto);

    Subject getSubjectById(Long id);

    Subject getSubjectByCode(String code);

    List<Subject> getAllSubjects();

    List<Subject> getSubjectsByCourse(Long courseId);

    List<Subject> getSubjectsByCourseAndSemester(Long courseId, Integer semester);

    Page<Subject> searchSubjects(String search, Long courseId, Integer semester, Pageable pageable);

    void deleteSubject(Long id);

    long countSubjects();

    SubjectDto convertToDto(Subject subject);
}
