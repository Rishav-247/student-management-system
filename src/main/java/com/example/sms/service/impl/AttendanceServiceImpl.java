package com.example.sms.service.impl;

import com.example.sms.dto.AttendanceDto;
import com.example.sms.dto.AttendanceSummaryDto;
import com.example.sms.entity.Attendance;
import com.example.sms.entity.AttendanceStatus;
import com.example.sms.entity.Student;
import com.example.sms.entity.Subject;
import com.example.sms.exception.ResourceNotFoundException;
import com.example.sms.repository.AttendanceRepository;
import com.example.sms.repository.StudentRepository;
import com.example.sms.repository.SubjectRepository;
import com.example.sms.service.AttendanceService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class AttendanceServiceImpl implements AttendanceService {

    private final AttendanceRepository attendanceRepository;
    private final StudentRepository studentRepository;
    private final SubjectRepository subjectRepository;

    public AttendanceServiceImpl(AttendanceRepository attendanceRepository,
                                 StudentRepository studentRepository,
                                 SubjectRepository subjectRepository) {
        this.attendanceRepository = attendanceRepository;
        this.studentRepository = studentRepository;
        this.subjectRepository = subjectRepository;
    }

    @Override
    public Attendance markAttendance(AttendanceDto dto) {
        Student student = studentRepository.findById(dto.getStudentId())
                .orElseThrow(() -> new ResourceNotFoundException("Student", "id", dto.getStudentId()));

        Subject subject = subjectRepository.findById(dto.getSubjectId())
                .orElseThrow(() -> new ResourceNotFoundException("Subject", "id", dto.getSubjectId()));

        LocalDate date = dto.getAttendanceDate() != null ? dto.getAttendanceDate() : LocalDate.now();

        Optional<Attendance> existing = attendanceRepository
                .findByStudentIdAndSubjectIdAndAttendanceDate(student.getId(), subject.getId(), date);

        Attendance attendance;
        if (existing.isPresent()) {
            attendance = existing.get();
            attendance.setStatus(dto.getStatus());
            attendance.setRemarks(dto.getRemarks());
        } else {
            attendance = new Attendance();
            attendance.setStudent(student);
            attendance.setSubject(subject);
            attendance.setAttendanceDate(date);
            attendance.setStatus(dto.getStatus());
            attendance.setRemarks(dto.getRemarks());
        }

        return attendanceRepository.save(attendance);
    }

    @Override
    public List<Attendance> markBulkAttendance(List<AttendanceDto> dtos) {
        List<Attendance> saved = new ArrayList<>();
        for (AttendanceDto dto : dtos) {
            saved.add(markAttendance(dto));
        }
        return saved;
    }

    @Override
    public Attendance updateAttendance(Long id, AttendanceDto dto) {
        Attendance attendance = getAttendanceById(id);
        attendance.setStatus(dto.getStatus());
        attendance.setRemarks(dto.getRemarks());
        if (dto.getAttendanceDate() != null) {
            attendance.setAttendanceDate(dto.getAttendanceDate());
        }
        return attendanceRepository.save(attendance);
    }

    @Override
    @Transactional(readOnly = true)
    public Attendance getAttendanceById(Long id) {
        return attendanceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Attendance", "id", id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Attendance> getAttendanceByStudent(Long studentId) {
        return attendanceRepository.findByStudentId(studentId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Attendance> getAttendanceBySubject(Long subjectId) {
        return attendanceRepository.findBySubjectId(subjectId);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Attendance> filterAttendance(Long studentId, Long subjectId, LocalDate date, AttendanceStatus status, Pageable pageable) {
        return attendanceRepository.filterAttendance(studentId, subjectId, date, status, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public double calculateStudentAttendanceRate(Long studentId) {
        long total = attendanceRepository.countByStudentId(studentId);
        if (total == 0) return 100.0;
        long present = attendanceRepository.countByStudentIdAndStatus(studentId, AttendanceStatus.PRESENT);
        return Math.round(((present * 100.0) / total) * 10.0) / 10.0;
    }

    @Override
    @Transactional(readOnly = true)
    public double calculateStudentSubjectAttendanceRate(Long studentId, Long subjectId) {
        long total = attendanceRepository.countByStudentIdAndSubjectId(studentId, subjectId);
        if (total == 0) return 100.0;
        long present = attendanceRepository.countByStudentIdAndSubjectIdAndStatus(studentId, subjectId, AttendanceStatus.PRESENT);
        return Math.round(((present * 100.0) / total) * 10.0) / 10.0;
    }

    @Override
    @Transactional(readOnly = true)
    public double calculateOverallAttendanceRate() {
        long total = attendanceRepository.count();
        if (total == 0) return 100.0;
        long present = attendanceRepository.findAll().stream()
                .filter(a -> a.getStatus() == AttendanceStatus.PRESENT)
                .count();
        return Math.round(((present * 100.0) / total) * 10.0) / 10.0;
    }

    @Override
    @Transactional(readOnly = true)
    public AttendanceDto convertToDto(Attendance attendance) {
        AttendanceDto dto = new AttendanceDto();
        dto.setId(attendance.getId());
        dto.setStudentId(attendance.getStudent().getId());
        dto.setStudentName(attendance.getStudent().getFullName());
        dto.setStudentCode(attendance.getStudent().getStudentId());
        dto.setSubjectId(attendance.getSubject().getId());
        dto.setSubjectName(attendance.getSubject().getName());
        dto.setSubjectCode(attendance.getSubject().getCode());
        dto.setAttendanceDate(attendance.getAttendanceDate());
        dto.setStatus(attendance.getStatus());
        dto.setRemarks(attendance.getRemarks());
        return dto;
    }
}
