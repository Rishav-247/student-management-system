package com.example.sms.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class CourseDto {

    private Long id;

    @NotBlank(message = "Course code is required")
    @Size(max = 20, message = "Code cannot exceed 20 characters")
    private String code;

    @NotBlank(message = "Course name is required")
    @Size(max = 150, message = "Name cannot exceed 150 characters")
    private String name;

    @NotNull(message = "Department is required")
    private Long departmentId;

    private String departmentName;

    @NotNull(message = "Duration in years is required")
    @Min(value = 1, message = "Duration must be at least 1 year")
    private Integer durationYears;

    @NotNull(message = "Total semesters is required")
    @Min(value = 1, message = "Total semesters must be at least 1")
    private Integer totalSemesters;

    @NotBlank(message = "Degree type is required")
    private String degreeType;

    private String description;

    private int studentCount;
    private int subjectCount;

    public CourseDto() {
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
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Long departmentId) {
        this.departmentId = departmentId;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
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

    public int getStudentCount() {
        return studentCount;
    }

    public void setStudentCount(int studentCount) {
        this.studentCount = studentCount;
    }

    public int getSubjectCount() {
        return subjectCount;
    }

    public void setSubjectCount(int subjectCount) {
        this.subjectCount = subjectCount;
    }
}
