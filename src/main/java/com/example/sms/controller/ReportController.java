package com.example.sms.controller;

import com.example.sms.dto.DashboardStatsDto;
import com.example.sms.dto.StudentDto;
import com.example.sms.entity.StudentStatus;
import com.example.sms.service.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/reports")
public class ReportController {

    private final DashboardService dashboardService;
    private final StudentService studentService;
    private final AttendanceService attendanceService;
    private final MarksService marksService;
    private final DepartmentService departmentService;

    public ReportController(DashboardService dashboardService,
                            StudentService studentService,
                            AttendanceService attendanceService,
                            MarksService marksService,
                            DepartmentService departmentService) {
        this.dashboardService = dashboardService;
        this.studentService = studentService;
        this.attendanceService = attendanceService;
        this.marksService = marksService;
        this.departmentService = departmentService;
    }

    @GetMapping
    public String index(Model model) {
        DashboardStatsDto stats = dashboardService.getDashboardStats();
        model.addAttribute("stats", stats);
        model.addAttribute("departments", departmentService.getAllDepartments().stream().map(departmentService::convertToDto).toList());
        model.addAttribute("activeNav", "10");
        return "reports/index";
    }

    @GetMapping("/students")
    public String studentReport(Model model) {
        List<StudentDto> students = studentService.getAllStudents().stream().map(studentService::convertToDto).toList();
        model.addAttribute("students", students);
        model.addAttribute("activeNav", "10");
        return "reports/students";
    }

    @GetMapping("/attendance")
    public String attendanceReport(Model model) {
        List<StudentDto> students = studentService.getAllStudents().stream().map(studentService::convertToDto).toList();
        model.addAttribute("students", students);
        model.addAttribute("overallRate", attendanceService.calculateOverallAttendanceRate());
        model.addAttribute("activeNav", "10");
        return "reports/attendance";
    }

    @GetMapping("/performance")
    public String performanceReport(Model model) {
        List<StudentDto> students = studentService.getAllStudents().stream().map(studentService::convertToDto).toList();
        model.addAttribute("students", students);
        model.addAttribute("passRate", marksService.calculateOverallPassRate());
        model.addAttribute("activeNav", "10");
        return "reports/performance";
    }
}
