package com.example.sms.dto;

import java.math.BigDecimal;
import java.util.List;

public class StudentReportCardDto {

    private StudentDto student;
    private Integer semester;
    private String academicYear;
    private List<MarksDto> subjectMarks;

    private BigDecimal totalMarksObtained = BigDecimal.ZERO;
    private BigDecimal maxTotalMarks = BigDecimal.ZERO;
    private BigDecimal percentage = BigDecimal.ZERO;
    private String overallGrade;
    private String overallResult; // PASS / FAIL

    public StudentReportCardDto() {
    }

    public StudentDto getStudent() {
        return student;
    }

    public void setStudent(StudentDto student) {
        this.student = student;
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

    public List<MarksDto> getSubjectMarks() {
        return subjectMarks;
    }

    public void setSubjectMarks(List<MarksDto> subjectMarks) {
        this.subjectMarks = subjectMarks;
    }

    public BigDecimal getTotalMarksObtained() {
        return totalMarksObtained;
    }

    public void setTotalMarksObtained(BigDecimal totalMarksObtained) {
        this.totalMarksObtained = totalMarksObtained;
    }

    public BigDecimal getMaxTotalMarks() {
        return maxTotalMarks;
    }

    public void setMaxTotalMarks(BigDecimal maxTotalMarks) {
        this.maxTotalMarks = maxTotalMarks;
    }

    public BigDecimal getPercentage() {
        return percentage;
    }

    public void setPercentage(BigDecimal percentage) {
        this.percentage = percentage;
    }

    public String getOverallGrade() {
        return overallGrade;
    }

    public void setOverallGrade(String overallGrade) {
        this.overallGrade = overallGrade;
    }

    public String getOverallResult() {
        return overallResult;
    }

    public void setOverallResult(String overallResult) {
        this.overallResult = overallResult;
    }
}
