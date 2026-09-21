package com.example.sms.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "subjects", indexes = {
    @Index(name = "idx_subject_code", columnList = "code", unique = true),
    @Index(name = "idx_subject_course", columnList = "course_id")
})
public class Subject extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Subject code is required")
    @Size(max = 20, message = "Subject code cannot exceed 20 characters")
    @Column(nullable = false, unique = true, length = 20)
    private String code;

    @NotBlank(message = "Subject name is required")
    @Size(max = 150, message = "Subject name cannot exceed 150 characters")
    @Column(nullable = false, length = 150)
    private String name;

    @NotNull(message = "Course is required")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    @NotNull(message = "Department is required")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id", nullable = false)
    private Department department;

    @NotNull(message = "Semester is required")
    @Min(value = 1, message = "Semester must be at least 1")
    @Column(nullable = false)
    private Integer semester;

    @NotNull(message = "Credits are required")
    @Min(value = 1, message = "Credits must be at least 1")
    @Column(nullable = false)
    private Integer credits = 3;

    @NotNull(message = "Subject type is required")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private SubjectType type = SubjectType.THEORY;

    public Subject() {
    }

    public Subject(String code, String name, Course course, Department department, Integer semester, Integer credits, SubjectType type) {
        this.code = code;
        this.name = name;
        this.course = course;
        this.department = department;
        this.semester = semester;
        this.credits = credits;
        this.type = type;
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

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    public Integer getSemester() {
        return semester;
    }

    public void setSemester(Integer semester) {
        this.semester = semester;
    }

    public Integer getCredits() {
        return credits;
    }

    public void setCredits(Integer credits) {
        this.credits = credits;
    }

    public SubjectType getType() {
        return type;
    }

    public void setType(SubjectType type) {
        this.type = type;
    }
}
