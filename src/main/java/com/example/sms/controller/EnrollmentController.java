package com.example.sms.controller;

import com.example.sms.dto.EnrollmentDto;
import com.example.sms.entity.Enrollment;
import com.example.sms.entity.EnrollmentStatus;
import com.example.sms.service.CourseService;
import com.example.sms.service.EnrollmentService;
import com.example.sms.service.StudentService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/enrollments")
public class EnrollmentController {

    private final EnrollmentService enrollmentService;
    private final StudentService studentService;
    private final CourseService courseService;

    public EnrollmentController(EnrollmentService enrollmentService,
                                StudentService studentService,
                                CourseService courseService) {
        this.enrollmentService = enrollmentService;
        this.studentService = studentService;
        this.courseService = courseService;
    }

    @GetMapping
    public String listEnrollments(@RequestParam(value = "search", required = false) String search,
                                  @RequestParam(value = "courseId", required = false) Long courseId,
                                  @RequestParam(value = "semester", required = false) Integer semester,
                                  @RequestParam(value = "academicYear", required = false) String academicYear,
                                  @RequestParam(value = "status", required = false) EnrollmentStatus status,
                                  @RequestParam(value = "page", defaultValue = "0") int page,
                                  @RequestParam(value = "size", defaultValue = "10") int size,
                                  Model model) {

        Page<Enrollment> enrollmentPage = enrollmentService.searchEnrollments(search, courseId, semester, academicYear, status, PageRequest.of(page, size, Sort.by("id").descending()));

        model.addAttribute("enrollments", enrollmentPage.map(enrollmentService::convertToDto));
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", enrollmentPage.getTotalPages());
        model.addAttribute("totalElements", enrollmentPage.getTotalElements());
        model.addAttribute("search", search);
        model.addAttribute("selectedCourse", courseId);
        model.addAttribute("selectedSemester", semester);
        model.addAttribute("selectedYear", academicYear);
        model.addAttribute("selectedStatus", status);
        model.addAttribute("courses", courseService.getAllCourses());
        model.addAttribute("statuses", EnrollmentStatus.values());
        model.addAttribute("activeNav", "07");

        return "enrollments/list";
    }

    @GetMapping("/new")
    public String showEnrollForm(Model model) {
        model.addAttribute("enrollmentDto", new EnrollmentDto());
        model.addAttribute("students", studentService.getAllStudents());
        model.addAttribute("courses", courseService.getAllCourses());
        model.addAttribute("statuses", EnrollmentStatus.values());
        model.addAttribute("activeNav", "07");
        return "enrollments/form";
    }

    @PostMapping("/save")
    public String saveEnrollment(@Valid @ModelAttribute("enrollmentDto") EnrollmentDto enrollmentDto,
                                 BindingResult bindingResult,
                                 Model model,
                                 RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("students", studentService.getAllStudents());
            model.addAttribute("courses", courseService.getAllCourses());
            model.addAttribute("statuses", EnrollmentStatus.values());
            model.addAttribute("activeNav", "07");
            return "enrollments/form";
        }

        try {
            if (enrollmentDto.getId() == null) {
                enrollmentService.enrollStudent(enrollmentDto);
                redirectAttributes.addFlashAttribute("successMessage", "STUDENT SUCCESSFULLY ENROLLED");
            } else {
                enrollmentService.updateEnrollment(enrollmentDto.getId(), enrollmentDto);
                redirectAttributes.addFlashAttribute("successMessage", "ENROLLMENT RECORD UPDATED");
            }
        } catch (Exception ex) {
            redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());
            return "redirect:/enrollments/new";
        }

        return "redirect:/enrollments";
    }

    @PostMapping("/delete/{id}")
    public String deleteEnrollment(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        try {
            enrollmentService.deleteEnrollment(id);
            redirectAttributes.addFlashAttribute("successMessage", "ENROLLMENT CANCELLED");
        } catch (Exception ex) {
            redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());
        }
        return "redirect:/enrollments";
    }
}
