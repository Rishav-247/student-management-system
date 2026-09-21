package com.example.sms.repository;

import com.example.sms.entity.Teacher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TeacherRepository extends JpaRepository<Teacher, Long> {

    @org.springframework.data.jpa.repository.EntityGraph(attributePaths = {"department"})
    Optional<Teacher> findByTeacherId(String teacherId);

    @org.springframework.data.jpa.repository.EntityGraph(attributePaths = {"department"})
    Optional<Teacher> findByEmail(String email);

    @org.springframework.data.jpa.repository.EntityGraph(attributePaths = {"department"})
    Optional<Teacher> findByUserId(Long userId);

    boolean existsByTeacherId(String teacherId);

    boolean existsByEmail(String email);

    @org.springframework.data.jpa.repository.EntityGraph(attributePaths = {"department"})
    List<Teacher> findByDepartmentId(Long departmentId);

    @org.springframework.data.jpa.repository.EntityGraph(attributePaths = {"department"})
    @Query("SELECT t FROM Teacher t WHERE " +
           "(:departmentId IS NULL OR t.department.id = :departmentId) AND " +
           "(:search IS NULL OR :search = '' OR " +
           "LOWER(t.firstName) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(t.lastName) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(t.teacherId) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(t.email) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(t.designation) LIKE LOWER(CONCAT('%', :search, '%')))")
    Page<Teacher> searchTeachers(@Param("search") String search,
                                 @Param("departmentId") Long departmentId,
                                 Pageable pageable);
}
