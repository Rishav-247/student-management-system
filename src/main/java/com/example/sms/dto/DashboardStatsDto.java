package com.example.sms.dto;

import java.util.List;

public class DashboardStatsDto {

    private long totalStudents;
    private long activeStudents;
    private long totalTeachers;
    private long totalCourses;
    private long totalDepartments;
    private long activeEnrollments;
    private double overallAttendanceRate;
    private double overallPassRate;

    private List<StudentDto> recentStudents;
    private List<DepartmentDto> departmentStats;

    public DashboardStatsDto() {
    }

    public long getTotalStudents() {
        return totalStudents;
    }

    public void setTotalStudents(long totalStudents) {
        this.totalStudents = totalStudents;
    }

    public long getActiveStudents() {
        return activeStudents;
    }

    public void setActiveStudents(long activeStudents) {
        this.activeStudents = activeStudents;
    }

    public long getTotalTeachers() {
        return totalTeachers;
    }

    public void setTotalTeachers(long totalTeachers) {
        this.totalTeachers = totalTeachers;
    }

    public long getTotalCourses() {
        return totalCourses;
    }

    public void setTotalCourses(long totalCourses) {
        this.totalCourses = totalCourses;
    }

    public long getTotalDepartments() {
        return totalDepartments;
    }

    public void setTotalDepartments(long totalDepartments) {
        this.totalDepartments = totalDepartments;
    }

    public long getActiveEnrollments() {
        return activeEnrollments;
    }

    public void setActiveEnrollments(long activeEnrollments) {
        this.activeEnrollments = activeEnrollments;
    }

    public double getOverallAttendanceRate() {
        return overallAttendanceRate;
    }

    public void setOverallAttendanceRate(double overallAttendanceRate) {
        this.overallAttendanceRate = overallAttendanceRate;
    }

    public double getOverallPassRate() {
        return overallPassRate;
    }

    public void setOverallPassRate(double overallPassRate) {
        this.overallPassRate = overallPassRate;
    }

    public List<StudentDto> getRecentStudents() {
        return recentStudents;
    }

    public void setRecentStudents(List<StudentDto> recentStudents) {
        this.recentStudents = recentStudents;
    }

    public List<DepartmentDto> getDepartmentStats() {
        return departmentStats;
    }

    public void setDepartmentStats(List<DepartmentDto> departmentStats) {
        this.departmentStats = departmentStats;
    }
}
