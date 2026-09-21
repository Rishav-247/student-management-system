package com.example.sms.service.impl;

import com.example.sms.dto.MarksDto;
import com.example.sms.dto.StudentDto;
import com.example.sms.dto.StudentReportCardDto;
import com.example.sms.entity.Marks;
import com.example.sms.entity.ResultStatus;
import com.example.sms.entity.Student;
import com.example.sms.entity.Subject;
import com.example.sms.exception.ResourceNotFoundException;
import com.example.sms.repository.MarksRepository;
import com.example.sms.repository.StudentRepository;
import com.example.sms.repository.SubjectRepository;
import com.example.sms.service.MarksService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class MarksServiceImpl implements MarksService {

    private final MarksRepository marksRepository;
    private final StudentRepository studentRepository;
    private final SubjectRepository subjectRepository;

    public MarksServiceImpl(MarksRepository marksRepository,
                            StudentRepository studentRepository,
                            SubjectRepository subjectRepository) {
        this.marksRepository = marksRepository;
        this.studentRepository = studentRepository;
        this.subjectRepository = subjectRepository;
    }

    @Override
    public Marks recordMarks(MarksDto dto) {
        Student student = studentRepository.findById(dto.getStudentId())
                .orElseThrow(() -> new ResourceNotFoundException("Student", "id", dto.getStudentId()));

        Subject subject = subjectRepository.findById(dto.getSubjectId())
                .orElseThrow(() -> new ResourceNotFoundException("Subject", "id", dto.getSubjectId()));

        Optional<Marks> existing = marksRepository.findByStudentIdAndSubjectIdAndSemesterAndAcademicYear(
                dto.getStudentId(), dto.getSubjectId(), dto.getSemester(), dto.getAcademicYear());

        Marks marks;
        if (existing.isPresent()) {
            marks = existing.get();
        } else {
            marks = new Marks();
            marks.setStudent(student);
            marks.setSubject(subject);
            marks.setSemester(dto.getSemester());
            marks.setAcademicYear(dto.getAcademicYear().trim());
        }

        marks.setInternalMarks(dto.getInternalMarks());
        marks.setAssignmentMarks(dto.getAssignmentMarks());
        marks.setPracticalMarks(dto.getPracticalMarks());
        marks.setFinalExamMarks(dto.getFinalExamMarks());
        marks.calculateTotalAndGrade();

        return marksRepository.save(marks);
    }

    @Override
    public Marks updateMarks(Long id, MarksDto dto) {
        Marks marks = getMarksById(id);

        marks.setInternalMarks(dto.getInternalMarks());
        marks.setAssignmentMarks(dto.getAssignmentMarks());
        marks.setPracticalMarks(dto.getPracticalMarks());
        marks.setFinalExamMarks(dto.getFinalExamMarks());
        marks.calculateTotalAndGrade();

        return marksRepository.save(marks);
    }

    @Override
    @Transactional(readOnly = true)
    public Marks getMarksById(Long id) {
        return marksRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Marks", "id", id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Marks> getMarksByStudent(Long studentId) {
        return marksRepository.findByStudentId(studentId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Marks> getMarksByStudentAndSemester(Long studentId, Integer semester) {
        return marksRepository.findByStudentIdAndSemester(studentId, semester);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Marks> searchMarks(String search, Long studentId, Long subjectId, Integer semester, String academicYear, ResultStatus status, Pageable pageable) {
        return marksRepository.searchMarks(search, studentId, subjectId, semester, academicYear, status, pageable);
    }

    @Override
    public void deleteMarks(Long id) {
        Marks marks = getMarksById(id);
        marksRepository.delete(marks);
    }

    @Override
    @Transactional(readOnly = true)
    public double calculateOverallPassRate() {
        long total = marksRepository.count();
        if (total == 0) return 100.0;
        long passed = marksRepository.countByStatus(ResultStatus.PASS);
        return Math.round(((passed * 100.0) / total) * 10.0) / 10.0;
    }

    @Override
    @Transactional(readOnly = true)
    public StudentReportCardDto generateReportCard(Long studentId, Integer semester, String academicYear) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Student", "id", studentId));

        List<Marks> marksList = marksRepository.findByStudentIdAndSemesterAndAcademicYear(studentId, semester, academicYear);

        StudentReportCardDto report = new StudentReportCardDto();
        StudentDto studentDto = new StudentDto();
        studentDto.setId(student.getId());
        studentDto.setStudentId(student.getStudentId());
        studentDto.setFirstName(student.getFirstName());
        studentDto.setLastName(student.getLastName());
        studentDto.setEmail(student.getEmail());
        studentDto.setDepartmentName(student.getDepartment().getName());
        studentDto.setCourseName(student.getCourse().getName());
        studentDto.setSemester(semester);

        report.setStudent(studentDto);
        report.setSemester(semester);
        report.setAcademicYear(academicYear);

        List<MarksDto> dtoList = marksList.stream().map(this::convertToDto).collect(Collectors.toList());
        report.setSubjectMarks(dtoList);

        if (!marksList.isEmpty()) {
            BigDecimal totalObtained = marksList.stream()
                    .map(Marks::getTotalMarks)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            BigDecimal maxMarks = BigDecimal.valueOf(marksList.size() * 100L);
            BigDecimal percentage = totalObtained.multiply(BigDecimal.valueOf(100)).divide(maxMarks, 2, RoundingMode.HALF_UP);

            report.setTotalMarksObtained(totalObtained);
            report.setMaxTotalMarks(maxMarks);
            report.setPercentage(percentage);

            boolean hasFailedSubject = marksList.stream().anyMatch(m -> m.getStatus() == ResultStatus.FAIL);
            report.setOverallResult(hasFailedSubject ? "FAIL" : "PASS");

            double p = percentage.doubleValue();
            if (p >= 90.0) report.setOverallGrade("A+");
            else if (p >= 80.0) report.setOverallGrade("A");
            else if (p >= 70.0) report.setOverallGrade("B");
            else if (p >= 60.0) report.setOverallGrade("C");
            else if (p >= 50.0) report.setOverallGrade("D");
            else if (p >= 40.0) report.setOverallGrade("E");
            else report.setOverallGrade("F");
        } else {
            report.setOverallResult("PENDING");
            report.setOverallGrade("N/A");
        }

        return report;
    }

    @Override
    @Transactional(readOnly = true)
    public MarksDto convertToDto(Marks marks) {
        MarksDto dto = new MarksDto();
        dto.setId(marks.getId());
        dto.setStudentId(marks.getStudent().getId());
        dto.setStudentName(marks.getStudent().getFullName());
        dto.setStudentCode(marks.getStudent().getStudentId());
        dto.setSubjectId(marks.getSubject().getId());
        dto.setSubjectName(marks.getSubject().getName());
        dto.setSubjectCode(marks.getSubject().getCode());
        dto.setSemester(marks.getSemester());
        dto.setAcademicYear(marks.getAcademicYear());
        dto.setInternalMarks(marks.getInternalMarks());
        dto.setAssignmentMarks(marks.getAssignmentMarks());
        dto.setPracticalMarks(marks.getPracticalMarks());
        dto.setFinalExamMarks(marks.getFinalExamMarks());
        dto.setTotalMarks(marks.getTotalMarks());
        dto.setPercentage(marks.getPercentage());
        dto.setGrade(marks.getGrade());
        dto.setStatus(marks.getStatus());
        return dto;
    }
}
