package com.example.sms.controller;

import com.example.sms.dto.StudentDto;
import com.example.sms.dto.StudentProfileDto;
import com.example.sms.entity.Gender;
import com.example.sms.entity.Student;
import com.example.sms.entity.StudentStatus;
import com.example.sms.service.CourseService;
import com.example.sms.service.DepartmentService;
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
@RequestMapping("/students")
public class StudentController {

    private final StudentService studentService;
    private final DepartmentService departmentService;
    private final CourseService courseService;

    public StudentController(StudentService studentService,
                             DepartmentService departmentService,
                             CourseService courseService) {
        this.studentService = studentService;
        this.departmentService = departmentService;
        this.courseService = courseService;
    }

    @GetMapping
    public String listStudents(@RequestParam(value = "search", required = false) String search,
                               @RequestParam(value = "departmentId", required = false) Long departmentId,
                               @RequestParam(value = "courseId", required = false) Long courseId,
                               @RequestParam(value = "semester", required = false) Integer semester,
                               @RequestParam(value = "status", required = false) StudentStatus status,
                               @RequestParam(value = "page", defaultValue = "0") int page,
                               @RequestParam(value = "size", defaultValue = "10") int size,
                               @RequestParam(value = "sort", defaultValue = "id") String sort,
                               @RequestParam(value = "direction", defaultValue = "DESC") String direction,
                               Model model) {

        Sort sortObj = direction.equalsIgnoreCase("ASC") ? Sort.by(sort).ascending() : Sort.by(sort).descending();
        Page<Student> studentPage = studentService.getFilteredStudents(search, departmentId, courseId, semester, status, PageRequest.of(page, size, sortObj));

        model.addAttribute("students", studentPage.map(studentService::convertToDto));
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", studentPage.getTotalPages());
        model.addAttribute("totalElements", studentPage.getTotalElements());
        model.addAttribute("search", search);
        model.addAttribute("selectedDept", departmentId);
        model.addAttribute("selectedCourse", courseId);
        model.addAttribute("selectedSemester", semester);
        model.addAttribute("selectedStatus", status);
        model.addAttribute("sort", sort);
        model.addAttribute("direction", direction);
        model.addAttribute("departments", departmentService.getAllDepartments());
        model.addAttribute("courses", courseService.getAllCourses());
        model.addAttribute("statuses", StudentStatus.values());
        model.addAttribute("activeNav", "02");

        return "students/list";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("studentDto", new StudentDto());
        model.addAttribute("departments", departmentService.getAllDepartments());
        model.addAttribute("courses", courseService.getAllCourses());
        model.addAttribute("genders", Gender.values());
        model.addAttribute("statuses", StudentStatus.values());
        model.addAttribute("isEdit", false);
        model.addAttribute("activeNav", "02");
        return "students/form";
    }

    @PostMapping("/save")
    public String saveStudent(@Valid @ModelAttribute("studentDto") StudentDto studentDto,
                              BindingResult bindingResult,
                              Model model,
                              RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("departments", departmentService.getAllDepartments());
            model.addAttribute("courses", courseService.getAllCourses());
            model.addAttribute("genders", Gender.values());
            model.addAttribute("statuses", StudentStatus.values());
            model.addAttribute("isEdit", studentDto.getId() != null);
            model.addAttribute("activeNav", "02");
            return "students/form";
        }

        try {
            if (studentDto.getId() == null) {
                studentService.createStudent(studentDto);
                redirectAttributes.addFlashAttribute("successMessage", "STUDENT RECORD CREATED: " + studentDto.getStudentId());
            } else {
                studentService.updateStudent(studentDto.getId(), studentDto);
                redirectAttributes.addFlashAttribute("successMessage", "STUDENT RECORD UPDATED: " + studentDto.getStudentId());
            }
        } catch (Exception ex) {
            redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());
            if (studentDto.getId() != null) {
                return "redirect:/students/edit/" + studentDto.getId();
            }
            return "redirect:/students/new";
        }

        return "redirect:/students";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable("id") Long id, Model model) {
        Student student = studentService.getStudentById(id);
        model.addAttribute("studentDto", studentService.convertToDto(student));
        model.addAttribute("departments", departmentService.getAllDepartments());
        model.addAttribute("courses", courseService.getAllCourses());
        model.addAttribute("genders", Gender.values());
        model.addAttribute("statuses", StudentStatus.values());
        model.addAttribute("isEdit", true);
        model.addAttribute("activeNav", "02");
        return "students/form";
    }

    @GetMapping("/profile/{id}")
    public String viewProfile(@PathVariable("id") Long id, Model model) {
        StudentProfileDto profile = studentService.getStudentProfile(id);
        model.addAttribute("profile", profile);
        model.addAttribute("activeNav", "02");
        return "students/profile";
    }

    @PostMapping("/delete/{id}")
    public String deleteStudent(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        try {
            studentService.deleteStudent(id);
            redirectAttributes.addFlashAttribute("successMessage", "STUDENT RECORD DELETED");
        } catch (Exception ex) {
            redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());
        }
        return "redirect:/students";
    }
}
