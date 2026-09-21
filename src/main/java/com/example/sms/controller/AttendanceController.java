package com.example.sms.controller;

import com.example.sms.dto.AttendanceDto;
import com.example.sms.entity.*;
import com.example.sms.service.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/attendance")
public class AttendanceController {

    private final AttendanceService attendanceService;
    private final StudentService studentService;
    private final SubjectService subjectService;
    private final CourseService courseService;
    private final DepartmentService departmentService;

    public AttendanceController(AttendanceService attendanceService,
                                StudentService studentService,
                                SubjectService subjectService,
                                CourseService courseService,
                                DepartmentService departmentService) {
        this.attendanceService = attendanceService;
        this.studentService = studentService;
        this.subjectService = subjectService;
        this.courseService = courseService;
        this.departmentService = departmentService;
    }

    @GetMapping
    public String listAttendance(@RequestParam(value = "studentId", required = false) Long studentId,
                                 @RequestParam(value = "subjectId", required = false) Long subjectId,
                                 @RequestParam(value = "date", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
                                 @RequestParam(value = "status", required = false) AttendanceStatus status,
                                 @RequestParam(value = "page", defaultValue = "0") int page,
                                 @RequestParam(value = "size", defaultValue = "15") int size,
                                 Model model) {

        Page<Attendance> attendancePage = attendanceService.filterAttendance(studentId, subjectId, date, status, PageRequest.of(page, size, Sort.by("attendanceDate").descending().and(Sort.by("id").descending())));

        model.addAttribute("attendances", attendancePage.map(attendanceService::convertToDto));
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", attendancePage.getTotalPages());
        model.addAttribute("totalElements", attendancePage.getTotalElements());
        model.addAttribute("selectedStudent", studentId);
        model.addAttribute("selectedSubject", subjectId);
        model.addAttribute("selectedDate", date);
        model.addAttribute("selectedStatus", status);
        model.addAttribute("subjects", subjectService.getAllSubjects());
        model.addAttribute("statuses", AttendanceStatus.values());
        model.addAttribute("overallRate", attendanceService.calculateOverallAttendanceRate());
        model.addAttribute("activeNav", "08");

        return "attendance/list";
    }

    @GetMapping("/mark")
    public String showMarkAttendanceForm(@RequestParam(value = "courseId", required = false) Long courseId,
                                         @RequestParam(value = "subjectId", required = false) Long subjectId,
                                         @RequestParam(value = "date", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
                                         Model model) {

        LocalDate targetDate = date != null ? date : LocalDate.now();
        model.addAttribute("selectedCourseId", courseId);
        model.addAttribute("selectedSubjectId", subjectId);
        model.addAttribute("selectedDate", targetDate);
        model.addAttribute("courses", courseService.getAllCourses());
        model.addAttribute("activeNav", "08");

        if (courseId != null) {
            model.addAttribute("subjects", subjectService.getSubjectsByCourse(courseId));
        }

        if (courseId != null && subjectId != null) {
            Subject subject = subjectService.getSubjectById(subjectId);
            List<Student> enrolledStudents = studentService.getAllStudents().stream()
                    .filter(s -> s.getCourse().getId().equals(courseId) && s.getStatus() == StudentStatus.ACTIVE)
                    .toList();

            List<AttendanceDto> studentAttendanceList = new ArrayList<>();
            for (Student s : enrolledStudents) {
                AttendanceDto dto = new AttendanceDto();
                dto.setStudentId(s.getId());
                dto.setStudentName(s.getFullName());
                dto.setStudentCode(s.getStudentId());
                dto.setSubjectId(subject.getId());
                dto.setSubjectName(subject.getName());
                dto.setSubjectCode(subject.getCode());
                dto.setAttendanceDate(targetDate);
                dto.setStatus(AttendanceStatus.PRESENT); // Default to PRESENT
                studentAttendanceList.add(dto);
            }
            model.addAttribute("studentAttendanceList", studentAttendanceList);
        }

        return "attendance/mark";
    }

    @PostMapping("/save")
    public String saveBulkAttendance(HttpServletRequest request, RedirectAttributes redirectAttributes) {
        try {
            String subjectIdStr = request.getParameter("subjectId");
            String dateStr = request.getParameter("attendanceDate");
            String[] studentIds = request.getParameterValues("studentIds");

            if (subjectIdStr == null || dateStr == null || studentIds == null) {
                redirectAttributes.addFlashAttribute("errorMessage", "MISSING ATTENDANCE FORM DATA");
                return "redirect:/attendance/mark";
            }

            Long subjectId = Long.parseLong(subjectIdStr);
            LocalDate date = LocalDate.parse(dateStr);

            List<AttendanceDto> dtos = new ArrayList<>();
            for (String studentIdStr : studentIds) {
                Long sId = Long.parseLong(studentIdStr);
                String statusStr = request.getParameter("status_" + sId);
                String remarks = request.getParameter("remarks_" + sId);

                AttendanceDto dto = new AttendanceDto();
                dto.setStudentId(sId);
                dto.setSubjectId(subjectId);
                dto.setAttendanceDate(date);
                dto.setStatus(statusStr != null ? AttendanceStatus.valueOf(statusStr) : AttendanceStatus.PRESENT);
                dto.setRemarks(remarks);
                dtos.add(dto);
            }

            attendanceService.markBulkAttendance(dtos);
            redirectAttributes.addFlashAttribute("successMessage", "ATTENDANCE RECORDED FOR " + dtos.size() + " STUDENTS");
        } catch (Exception ex) {
            redirectAttributes.addFlashAttribute("errorMessage", "FAILED TO RECORD ATTENDANCE: " + ex.getMessage());
            return "redirect:/attendance/mark";
        }

        return "redirect:/attendance";
    }
}
