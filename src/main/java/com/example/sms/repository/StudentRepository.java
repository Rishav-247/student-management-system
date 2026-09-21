package com.example.sms.repository;

import com.example.sms.entity.Student;
import com.example.sms.entity.StudentStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long>, JpaSpecificationExecutor<Student> {

    @org.springframework.data.jpa.repository.EntityGraph(attributePaths = {"department", "course"})
    Optional<Student> findByStudentId(String studentId);

    @org.springframework.data.jpa.repository.EntityGraph(attributePaths = {"department", "course"})
    Optional<Student> findByEmail(String email);

    @org.springframework.data.jpa.repository.EntityGraph(attributePaths = {"department", "course"})
    Optional<Student> findByUserId(Long userId);

    boolean existsByStudentId(String studentId);

    boolean existsByEmail(String email);

    long countByStatus(StudentStatus status);

    long countByDepartmentId(Long departmentId);

    long countByCourseId(Long courseId);

    @org.springframework.data.jpa.repository.EntityGraph(attributePaths = {"department", "course"})
    List<Student> findByDepartmentId(Long departmentId);

    @org.springframework.data.jpa.repository.EntityGraph(attributePaths = {"department", "course"})
    List<Student> findByCourseId(Long courseId);

    @org.springframework.data.jpa.repository.EntityGraph(attributePaths = {"department", "course"})
    List<Student> findByCourseIdAndSemester(Long courseId, Integer semester);

    @org.springframework.data.jpa.repository.EntityGraph(attributePaths = {"department", "course"})
    @Query("SELECT s FROM Student s WHERE " +
           "(:departmentId IS NULL OR s.department.id = :departmentId) AND " +
           "(:courseId IS NULL OR s.course.id = :courseId) AND " +
           "(:semester IS NULL OR s.semester = :semester) AND " +
           "(:status IS NULL OR s.status = :status) AND " +
           "(:search IS NULL OR :search = '' OR " +
           "LOWER(s.firstName) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(s.lastName) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(s.studentId) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(s.email) LIKE LOWER(CONCAT('%', :search, '%')))")
    Page<Student> findWithFilters(@Param("search") String search,
                                   @Param("departmentId") Long departmentId,
                                   @Param("courseId") Long courseId,
                                   @Param("semester") Integer semester,
                                   @Param("status") StudentStatus status,
                                   Pageable pageable);
}
