package com.example.sms.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "courses", indexes = {
    @Index(name = "idx_course_code", columnList = "code", unique = true)
})
public class Course extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Course code is required")
    @Size(max = 20, message = "Code cannot exceed 20 characters")
    @Column(nullable = false, unique = true, length = 20)
    private String code;

    @NotBlank(message = "Course name is required")
    @Size(max = 150, message = "Name cannot exceed 150 characters")
    @Column(nullable = false, length = 150)
    private String name;

    @NotNull(message = "Department is required")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id", nullable = false)
    private Department department;

    @NotNull(message = "Duration in years is required")
    @Min(value = 1, message = "Duration must be at least 1 year")
    @Column(name = "duration_years", nullable = false)
    private Integer durationYears;

    @NotNull(message = "Total semesters is required")
    @Min(value = 1, message = "Total semesters must be at least 1")
    @Column(name = "total_semesters", nullable = false)
    private Integer totalSemesters;

    @NotBlank(message = "Degree type is required")
    @Column(name = "degree_type", nullable = false, length = 50)
    private String degreeType;

    @Column(columnDefinition = "TEXT")
    private String description;

    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL)
    private List<Subject> subjects = new ArrayList<>();

    @OneToMany(mappedBy = "course")
    private List<Student> students = new ArrayList<>();

    public Course() {
    }

    public Course(String code, String name, Department department, Integer durationYears, Integer totalSemesters, String degreeType, String description) {
        this.code = code;
        this.name = name;
        this.department = department;
        this.durationYears = durationYears;
        this.totalSemesters = totalSemesters;
        this.degreeType = degreeType;
        this.description = description;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code != null ? code.toUpperCase().trim() : null;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    public Integer getDurationYears() {
        return durationYears;
    }

    public void setDurationYears(Integer durationYears) {
        this.durationYears = durationYears;
    }

    public Integer getTotalSemesters() {
        return totalSemesters;
    }

    public void setTotalSemesters(Integer totalSemesters) {
        this.totalSemesters = totalSemesters;
    }

    public String getDegreeType() {
        return degreeType;
    }

    public void setDegreeType(String degreeType) {
        this.degreeType = degreeType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Subject> getSubjects() {
        return subjects;
    }

    public void setSubjects(List<Subject> subjects) {
        this.subjects = subjects;
    }

    public List<Student> getStudents() {
        return students;
    }

    public void setStudents(List<Student> students) {
        this.students = students;
    }
}
