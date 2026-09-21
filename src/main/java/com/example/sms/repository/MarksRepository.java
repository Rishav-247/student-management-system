package com.example.sms.repository;

import com.example.sms.entity.Marks;
import com.example.sms.entity.ResultStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MarksRepository extends JpaRepository<Marks, Long> {

    @org.springframework.data.jpa.repository.EntityGraph(attributePaths = {"student", "subject"})
    List<Marks> findByStudentId(Long studentId);

    @org.springframework.data.jpa.repository.EntityGraph(attributePaths = {"student", "subject"})
    List<Marks> findBySubjectId(Long subjectId);

    @org.springframework.data.jpa.repository.EntityGraph(attributePaths = {"student", "subject"})
    List<Marks> findByStudentIdAndSemester(Long studentId, Integer semester);

    @org.springframework.data.jpa.repository.EntityGraph(attributePaths = {"student", "subject"})
    List<Marks> findByStudentIdAndSemesterAndAcademicYear(Long studentId, Integer semester, String academicYear);

    Optional<Marks> findByStudentIdAndSubjectIdAndSemesterAndAcademicYear(Long studentId, Long subjectId, Integer semester, String academicYear);

    boolean existsByStudentIdAndSubjectIdAndSemesterAndAcademicYear(Long studentId, Long subjectId, Integer semester, String academicYear);

    long countByStatus(ResultStatus status);

    @org.springframework.data.jpa.repository.EntityGraph(attributePaths = {"student", "subject"})
    @Query("SELECT m FROM Marks m WHERE " +
           "(:studentId IS NULL OR m.student.id = :studentId) AND " +
           "(:subjectId IS NULL OR m.subject.id = :subjectId) AND " +
           "(:semester IS NULL OR m.semester = :semester) AND " +
           "(:academicYear IS NULL OR :academicYear = '' OR m.academicYear = :academicYear) AND " +
           "(:status IS NULL OR m.status = :status) AND " +
           "(:search IS NULL OR :search = '' OR " +
           "LOWER(m.student.firstName) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(m.student.lastName) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(m.student.studentId) LIKE LOWER(CONCAT('%', :search, '%')))")
    Page<Marks> searchMarks(@Param("search") String search,
                            @Param("studentId") Long studentId,
                            @Param("subjectId") Long subjectId,
                            @Param("semester") Integer semester,
                            @Param("academicYear") String academicYear,
                            @Param("status") ResultStatus status,
                            Pageable pageable);
}
