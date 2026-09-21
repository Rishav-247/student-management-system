package com.example.sms.dto;

import com.example.sms.entity.ResultStatus;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public class MarksDto {

    private Long id;

    @NotNull(message = "Student is required")
    private Long studentId;

    private String studentName;
    private String studentCode;

    @NotNull(message = "Subject is required")
    private Long subjectId;

    private String subjectName;
    private String subjectCode;

    @NotNull(message = "Semester is required")
    @Min(value = 1, message = "Semester must be at least 1")
    private Integer semester;

    @NotBlank(message = "Academic year is required")
    private String academicYear;

    @DecimalMin(value = "0.00", message = "Internal marks cannot be negative")
    @DecimalMax(value = "20.00", message = "Internal marks cannot exceed 20")
    private BigDecimal internalMarks = BigDecimal.ZERO;

    @DecimalMin(value = "0.00", message = "Assignment marks cannot be negative")
    @DecimalMax(value = "20.00", message = "Assignment marks cannot exceed 20")
    private BigDecimal assignmentMarks = BigDecimal.ZERO;

    @DecimalMin(value = "0.00", message = "Practical marks cannot be negative")
    @DecimalMax(value = "20.00", message = "Practical marks cannot exceed 20")
    private BigDecimal practicalMarks = BigDecimal.ZERO;

    @DecimalMin(value = "0.00", message = "Final exam marks cannot be negative")
    @DecimalMax(value = "40.00", message = "Final exam marks cannot exceed 40")
    private BigDecimal finalExamMarks = BigDecimal.ZERO;

    private BigDecimal totalMarks = BigDecimal.ZERO;
    private BigDecimal percentage = BigDecimal.ZERO;
    private String grade;
    private ResultStatus status = ResultStatus.PASS;

    public MarksDto() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getStudentId() {
        return studentId;
    }

    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getStudentCode() {
        return studentCode;
    }

    public void setStudentCode(String studentCode) {
        this.studentCode = studentCode;
    }

    public Long getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(Long subjectId) {
        this.subjectId = subjectId;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public String getSubjectCode() {
        return subjectCode;
    }

    public void setSubjectCode(String subjectCode) {
        this.subjectCode = subjectCode;
    }

    public Integer getSemester() {
        return semester;
    }

    public void setSemester(Integer semester) {
        this.semester = semester;
    }

    public String getAcademicYear() {
        return academicYear;
    }

    public void setAcademicYear(String academicYear) {
        this.academicYear = academicYear;
    }

    public BigDecimal getInternalMarks() {
        return internalMarks;
    }

    public void setInternalMarks(BigDecimal internalMarks) {
        this.internalMarks = internalMarks;
    }

    public BigDecimal getAssignmentMarks() {
        return assignmentMarks;
    }

    public void setAssignmentMarks(BigDecimal assignmentMarks) {
        this.assignmentMarks = assignmentMarks;
    }

    public BigDecimal getPracticalMarks() {
        return practicalMarks;
    }

    public void setPracticalMarks(BigDecimal practicalMarks) {
        this.practicalMarks = practicalMarks;
    }

    public BigDecimal getFinalExamMarks() {
        return finalExamMarks;
    }

    public void setFinalExamMarks(BigDecimal finalExamMarks) {
        this.finalExamMarks = finalExamMarks;
    }

    public BigDecimal getTotalMarks() {
        return totalMarks;
    }

    public void setTotalMarks(BigDecimal totalMarks) {
        this.totalMarks = totalMarks;
    }

    public BigDecimal getPercentage() {
        return percentage;
    }

    public void setPercentage(BigDecimal percentage) {
        this.percentage = percentage;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public ResultStatus getStatus() {
        return status;
    }

    public void setStatus(ResultStatus status) {
        this.status = status;
    }
}
