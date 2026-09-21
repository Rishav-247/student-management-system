package com.example.sms.service;

import com.example.sms.dto.MarksDto;
import com.example.sms.dto.StudentReportCardDto;
import com.example.sms.entity.Marks;
import com.example.sms.entity.ResultStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface MarksService {

    Marks recordMarks(MarksDto dto);

    Marks updateMarks(Long id, MarksDto dto);

    Marks getMarksById(Long id);

    List<Marks> getMarksByStudent(Long studentId);

    List<Marks> getMarksByStudentAndSemester(Long studentId, Integer semester);

    Page<Marks> searchMarks(String search, Long studentId, Long subjectId, Integer semester, String academicYear, ResultStatus status, Pageable pageable);

    void deleteMarks(Long id);

    double calculateOverallPassRate();

    StudentReportCardDto generateReportCard(Long studentId, Integer semester, String academicYear);

    MarksDto convertToDto(Marks marks);
}
