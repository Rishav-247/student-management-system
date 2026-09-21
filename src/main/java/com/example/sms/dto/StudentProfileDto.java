package com.example.sms.dto;

import java.util.List;

public class StudentProfileDto {

    private StudentDto student;
    private double overallAttendanceRate;
    private long totalClasses;
    private long attendedClasses;
    private long absentClasses;

    private List<AttendanceDto> recentAttendance;
    private List<MarksDto> marks;
    private List<EnrollmentDto> enrollments;

    public StudentProfileDto() {
    }

    public StudentDto getStudent() {
        return student;
    }

    public void setStudent(StudentDto student) {
        this.student = student;
    }

    public double getOverallAttendanceRate() {
        return overallAttendanceRate;
    }

    public void setOverallAttendanceRate(double overallAttendanceRate) {
        this.overallAttendanceRate = overallAttendanceRate;
    }

    public long getTotalClasses() {
        return totalClasses;
    }

    public void setTotalClasses(long totalClasses) {
        this.totalClasses = totalClasses;
    }

    public long getAttendedClasses() {
        return attendedClasses;
    }

    public void setAttendedClasses(long attendedClasses) {
        this.attendedClasses = attendedClasses;
    }

    public long getAbsentClasses() {
        return absentClasses;
    }

    public void setAbsentClasses(long absentClasses) {
        this.absentClasses = absentClasses;
    }

    public List<AttendanceDto> getRecentAttendance() {
        return recentAttendance;
    }

    public void setRecentAttendance(List<AttendanceDto> recentAttendance) {
        this.recentAttendance = recentAttendance;
    }

    public List<MarksDto> getMarks() {
        return marks;
    }

    public void setMarks(List<MarksDto> marks) {
        this.marks = marks;
    }

    public List<EnrollmentDto> getEnrollments() {
        return enrollments;
    }

    public void setEnrollments(List<EnrollmentDto> enrollments) {
        this.enrollments = enrollments;
    }
}
