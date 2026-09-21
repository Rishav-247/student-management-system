package com.example.sms.repository;

import com.example.sms.entity.Attendance;
import com.example.sms.entity.AttendanceStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface AttendanceRepository extends JpaRepository<Attendance, Long> {

    @org.springframework.data.jpa.repository.EntityGraph(attributePaths = {"student", "subject"})
    List<Attendance> findByStudentId(Long studentId);

    @org.springframework.data.jpa.repository.EntityGraph(attributePaths = {"student", "subject"})
    List<Attendance> findBySubjectId(Long subjectId);

    @org.springframework.data.jpa.repository.EntityGraph(attributePaths = {"student", "subject"})
    List<Attendance> findByStudentIdAndSubjectId(Long studentId, Long subjectId);

    Optional<Attendance> findByStudentIdAndSubjectIdAndAttendanceDate(Long studentId, Long subjectId, LocalDate attendanceDate);

    boolean existsByStudentIdAndSubjectIdAndAttendanceDate(Long studentId, Long subjectId, LocalDate attendanceDate);

    long countByStudentId(Long studentId);

    long countByStudentIdAndStatus(Long studentId, AttendanceStatus status);

    long countByStudentIdAndSubjectId(Long studentId, Long subjectId);

    long countByStudentIdAndSubjectIdAndStatus(Long studentId, Long subjectId, AttendanceStatus status);

    @org.springframework.data.jpa.repository.EntityGraph(attributePaths = {"student", "subject"})
    @Query("SELECT a FROM Attendance a WHERE " +
           "(:studentId IS NULL OR a.student.id = :studentId) AND " +
           "(:subjectId IS NULL OR a.subject.id = :subjectId) AND " +
           "(:attendanceDate IS NULL OR a.attendanceDate = :attendanceDate) AND " +
           "(:status IS NULL OR a.status = :status)")
    Page<Attendance> filterAttendance(@Param("studentId") Long studentId,
                                      @Param("subjectId") Long subjectId,
                                      @Param("attendanceDate") LocalDate attendanceDate,
                                      @Param("status") AttendanceStatus status,
                                      Pageable pageable);
}
