package com.example.sms.repository;

import com.example.sms.entity.Course;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {

    @org.springframework.data.jpa.repository.EntityGraph(attributePaths = {"department"})
    Optional<Course> findByCode(String code);

    boolean existsByCode(String code);

    @org.springframework.data.jpa.repository.EntityGraph(attributePaths = {"department"})
    List<Course> findByDepartmentId(Long departmentId);

    @org.springframework.data.jpa.repository.EntityGraph(attributePaths = {"department"})
    @Query("SELECT c FROM Course c WHERE " +
           "(:departmentId IS NULL OR c.department.id = :departmentId) AND " +
           "(:search IS NULL OR :search = '' OR " +
           "LOWER(c.name) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(c.code) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(c.degreeType) LIKE LOWER(CONCAT('%', :search, '%')))")
    Page<Course> searchCourses(@Param("search") String search,
                               @Param("departmentId") Long departmentId,
                               Pageable pageable);
}
