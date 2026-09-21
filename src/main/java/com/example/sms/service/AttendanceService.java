package com.example.sms.service;

import com.example.sms.dto.AttendanceDto;
import com.example.sms.dto.AttendanceSummaryDto;
import com.example.sms.entity.Attendance;
import com.example.sms.entity.AttendanceStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;

public interface AttendanceService {

    Attendance markAttendance(AttendanceDto dto);

    List<Attendance> markBulkAttendance(List<AttendanceDto> dtos);

    Attendance updateAttendance(Long id, AttendanceDto dto);

    Attendance getAttendanceById(Long id);

    List<Attendance> getAttendanceByStudent(Long studentId);

    List<Attendance> getAttendanceBySubject(Long subjectId);

    Page<Attendance> filterAttendance(Long studentId, Long subjectId, LocalDate date, AttendanceStatus status, Pageable pageable);

    double calculateStudentAttendanceRate(Long studentId);

    double calculateStudentSubjectAttendanceRate(Long studentId, Long subjectId);

    double calculateOverallAttendanceRate();

    AttendanceDto convertToDto(Attendance attendance);
}
