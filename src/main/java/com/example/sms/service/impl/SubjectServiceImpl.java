package com.example.sms.service.impl;

import com.example.sms.dto.SubjectDto;
import com.example.sms.entity.Course;
import com.example.sms.entity.Department;
import com.example.sms.entity.Subject;
import com.example.sms.exception.DuplicateResourceException;
import com.example.sms.exception.ResourceNotFoundException;
import com.example.sms.repository.CourseRepository;
import com.example.sms.repository.DepartmentRepository;
import com.example.sms.repository.SubjectRepository;
import com.example.sms.service.SubjectService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class SubjectServiceImpl implements SubjectService {

    private final SubjectRepository subjectRepository;
    private final CourseRepository courseRepository;
    private final DepartmentRepository departmentRepository;

    public SubjectServiceImpl(SubjectRepository subjectRepository,
                              CourseRepository courseRepository,
                              DepartmentRepository departmentRepository) {
        this.subjectRepository = subjectRepository;
        this.courseRepository = courseRepository;
        this.departmentRepository = departmentRepository;
    }

    @Override
    public Subject createSubject(SubjectDto dto) {
        String code = dto.getCode().trim().toUpperCase();
        if (subjectRepository.existsByCode(code)) {
            throw new DuplicateResourceException("Subject", "code", code);
        }

        Course course = courseRepository.findById(dto.getCourseId())
                .orElseThrow(() -> new ResourceNotFoundException("Course", "id", dto.getCourseId()));

        Department department;
        if (dto.getDepartmentId() != null) {
            department = departmentRepository.findById(dto.getDepartmentId())
                    .orElseThrow(() -> new ResourceNotFoundException("Department", "id", dto.getDepartmentId()));
        } else {
            department = course.getDepartment();
        }

        Subject subject = new Subject();
        subject.setCode(code);
        subject.setName(dto.getName().trim());
        subject.setCourse(course);
        subject.setDepartment(department);
        subject.setSemester(dto.getSemester());
        subject.setCredits(dto.getCredits());
        subject.setType(dto.getType());

        return subjectRepository.save(subject);
    }

    @Override
    public Subject updateSubject(Long id, SubjectDto dto) {
        Subject subject = getSubjectById(id);
        String code = dto.getCode().trim().toUpperCase();

        if (!subject.getCode().equalsIgnoreCase(code) && subjectRepository.existsByCode(code)) {
            throw new DuplicateResourceException("Subject", "code", code);
        }

        Course course = courseRepository.findById(dto.getCourseId())
                .orElseThrow(() -> new ResourceNotFoundException("Course", "id", dto.getCourseId()));

        Department department;
        if (dto.getDepartmentId() != null) {
            department = departmentRepository.findById(dto.getDepartmentId())
                    .orElseThrow(() -> new ResourceNotFoundException("Department", "id", dto.getDepartmentId()));
        } else {
            department = course.getDepartment();
        }

        subject.setCode(code);
        subject.setName(dto.getName().trim());
        subject.setCourse(course);
        subject.setDepartment(department);
        subject.setSemester(dto.getSemester());
        subject.setCredits(dto.getCredits());
        subject.setType(dto.getType());

        return subjectRepository.save(subject);
    }

    @Override
    @Transactional(readOnly = true)
    public Subject getSubjectById(Long id) {
        return subjectRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Subject", "id", id));
    }

    @Override
    @Transactional(readOnly = true)
    public Subject getSubjectByCode(String code) {
        return subjectRepository.findByCode(code.toUpperCase())
                .orElseThrow(() -> new ResourceNotFoundException("Subject", "code", code));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Subject> getAllSubjects() {
        return subjectRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Subject> getSubjectsByCourse(Long courseId) {
        return subjectRepository.findByCourseId(courseId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Subject> getSubjectsByCourseAndSemester(Long courseId, Integer semester) {
        return subjectRepository.findByCourseIdAndSemester(courseId, semester);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Subject> searchSubjects(String search, Long courseId, Integer semester, Pageable pageable) {
        return subjectRepository.searchSubjects(search, courseId, semester, pageable);
    }

    @Override
    public void deleteSubject(Long id) {
        Subject subject = getSubjectById(id);
        subjectRepository.delete(subject);
    }

    @Override
    @Transactional(readOnly = true)
    public long countSubjects() {
        return subjectRepository.count();
    }

    @Override
    @Transactional(readOnly = true)
    public SubjectDto convertToDto(Subject subject) {
        SubjectDto dto = new SubjectDto();
        dto.setId(subject.getId());
        dto.setCode(subject.getCode());
        dto.setName(subject.getName());
        dto.setCourseId(subject.getCourse().getId());
        dto.setCourseName(subject.getCourse().getName());
        dto.setDepartmentId(subject.getDepartment().getId());
        dto.setDepartmentName(subject.getDepartment().getName());
        dto.setSemester(subject.getSemester());
        dto.setCredits(subject.getCredits());
        dto.setType(subject.getType());
        return dto;
    }
}
