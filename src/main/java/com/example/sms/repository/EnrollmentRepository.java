package com.example.sms.repository;

import com.example.sms.entity.Enrollment;
import com.example.sms.entity.EnrollmentStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {

    @org.springframework.data.jpa.repository.EntityGraph(attributePaths = {"student", "course"})
    List<Enrollment> findByStudentId(Long studentId);

    @org.springframework.data.jpa.repository.EntityGraph(attributePaths = {"student", "course"})
    List<Enrollment> findByCourseId(Long courseId);

    @org.springframework.data.jpa.repository.EntityGraph(attributePaths = {"student", "course"})
    List<Enrollment> findByCourseIdAndAcademicYearAndSemester(Long courseId, String academicYear, Integer semester);

    Optional<Enrollment> findByStudentIdAndCourseIdAndAcademicYearAndSemester(Long studentId, Long courseId, String academicYear, Integer semester);

    boolean existsByStudentIdAndCourseIdAndAcademicYearAndSemester(Long studentId, Long courseId, String academicYear, Integer semester);

    long countByStatus(EnrollmentStatus status);

    @org.springframework.data.jpa.repository.EntityGraph(attributePaths = {"student", "course"})
    @Query("SELECT e FROM Enrollment e WHERE " +
           "(:courseId IS NULL OR e.course.id = :courseId) AND " +
           "(:semester IS NULL OR e.semester = :semester) AND " +
           "(:academicYear IS NULL OR :academicYear = '' OR e.academicYear = :academicYear) AND " +
           "(:status IS NULL OR e.status = :status) AND " +
           "(:search IS NULL OR :search = '' OR " +
           "LOWER(e.student.firstName) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(e.student.lastName) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(e.student.studentId) LIKE LOWER(CONCAT('%', :search, '%')))")
    Page<Enrollment> searchEnrollments(@Param("search") String search,
                                       @Param("courseId") Long courseId,
                                       @Param("semester") Integer semester,
                                       @Param("academicYear") String academicYear,
                                       @Param("status") EnrollmentStatus status,
                                       Pageable pageable);
}
