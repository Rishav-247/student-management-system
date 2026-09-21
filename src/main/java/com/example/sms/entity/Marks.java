package com.example.sms.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Entity
@Table(name = "marks", uniqueConstraints = {
    @UniqueConstraint(name = "uk_student_marks", columnNames = {"student_id", "subject_id", "semester", "academic_year"})
})
public class Marks extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Student is required")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    @NotNull(message = "Subject is required")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subject_id", nullable = false)
    private Subject subject;

    @NotNull(message = "Semester is required")
    @Column(nullable = false)
    private Integer semester;

    @NotBlank(message = "Academic year is required")
    @Column(name = "academic_year", nullable = false, length = 20)
    private String academicYear;

    @DecimalMin("0.00")
    @DecimalMax("20.00")
    @Column(name = "internal_marks", precision = 5, scale = 2)
    private BigDecimal internalMarks = BigDecimal.ZERO;

    @DecimalMin("0.00")
    @DecimalMax("20.00")
    @Column(name = "assignment_marks", precision = 5, scale = 2)
    private BigDecimal assignmentMarks = BigDecimal.ZERO;

    @DecimalMin("0.00")
    @DecimalMax("20.00")
    @Column(name = "practical_marks", precision = 5, scale = 2)
    private BigDecimal practicalMarks = BigDecimal.ZERO;

    @DecimalMin("0.00")
    @DecimalMax("40.00")
    @Column(name = "final_exam_marks", precision = 5, scale = 2)
    private BigDecimal finalExamMarks = BigDecimal.ZERO;

    @Column(name = "total_marks", precision = 5, scale = 2)
    private BigDecimal totalMarks = BigDecimal.ZERO;

    @Column(precision = 5, scale = 2)
    private BigDecimal percentage = BigDecimal.ZERO;

    @Column(length = 5)
    private String grade;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private ResultStatus status = ResultStatus.PASS;

    public Marks() {
    }

    public Marks(Student student, Subject subject, Integer semester, String academicYear,
                 BigDecimal internalMarks, BigDecimal assignmentMarks, BigDecimal practicalMarks, BigDecimal finalExamMarks) {
        this.student = student;
        this.subject = subject;
        this.semester = semester;
        this.academicYear = academicYear;
        this.internalMarks = internalMarks != null ? internalMarks : BigDecimal.ZERO;
        this.assignmentMarks = assignmentMarks != null ? assignmentMarks : BigDecimal.ZERO;
        this.practicalMarks = practicalMarks != null ? practicalMarks : BigDecimal.ZERO;
        this.finalExamMarks = finalExamMarks != null ? finalExamMarks : BigDecimal.ZERO;
        calculateTotalAndGrade();
    }

    @PrePersist
    @PreUpdate
    public void calculateTotalAndGrade() {
        BigDecimal internal = internalMarks != null ? internalMarks : BigDecimal.ZERO;
        BigDecimal assignment = assignmentMarks != null ? assignmentMarks : BigDecimal.ZERO;
        BigDecimal practical = practicalMarks != null ? practicalMarks : BigDecimal.ZERO;
        BigDecimal finalExam = finalExamMarks != null ? finalExamMarks : BigDecimal.ZERO;

        this.totalMarks = internal.add(assignment).add(practical).add(finalExam);
        this.percentage = this.totalMarks; // Max total is 100 (20+20+20+40)

        double score = this.totalMarks.doubleValue();
        if (score >= 90.0) {
            this.grade = "A+";
            this.status = ResultStatus.PASS;
        } else if (score >= 80.0) {
            this.grade = "A";
            this.status = ResultStatus.PASS;
        } else if (score >= 70.0) {
            this.grade = "B";
            this.status = ResultStatus.PASS;
        } else if (score >= 60.0) {
            this.grade = "C";
            this.status = ResultStatus.PASS;
        } else if (score >= 50.0) {
            this.grade = "D";
            this.status = ResultStatus.PASS;
        } else if (score >= 40.0) {
            this.grade = "E";
            this.status = ResultStatus.PASS;
        } else {
            this.grade = "F";
            this.status = ResultStatus.FAIL;
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public Subject getSubject() {
        return subject;
    }

    public void setSubject(Subject subject) {
        this.subject = subject;
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
        calculateTotalAndGrade();
    }

    public BigDecimal getAssignmentMarks() {
        return assignmentMarks;
    }

    public void setAssignmentMarks(BigDecimal assignmentMarks) {
        this.assignmentMarks = assignmentMarks;
        calculateTotalAndGrade();
    }

    public BigDecimal getPracticalMarks() {
        return practicalMarks;
    }

    public void setPracticalMarks(BigDecimal practicalMarks) {
        this.practicalMarks = practicalMarks;
        calculateTotalAndGrade();
    }

    public BigDecimal getFinalExamMarks() {
        return finalExamMarks;
    }

    public void setFinalExamMarks(BigDecimal finalExamMarks) {
        this.finalExamMarks = finalExamMarks;
        calculateTotalAndGrade();
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
