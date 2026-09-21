package com.example.sms.controller;

import com.example.sms.dto.CourseDto;
import com.example.sms.entity.Course;
import com.example.sms.service.CourseService;
import com.example.sms.service.DepartmentService;
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
@RequestMapping("/courses")
public class CourseController {

    private final CourseService courseService;
    private final DepartmentService departmentService;

    public CourseController(CourseService courseService, DepartmentService departmentService) {
        this.courseService = courseService;
        this.departmentService = departmentService;
    }

    @GetMapping
    public String listCourses(@RequestParam(value = "search", required = false) String search,
                              @RequestParam(value = "departmentId", required = false) Long departmentId,
                              @RequestParam(value = "page", defaultValue = "0") int page,
                              @RequestParam(value = "size", defaultValue = "10") int size,
                              Model model) {

        Page<Course> coursePage = courseService.searchCourses(search, departmentId, PageRequest.of(page, size, Sort.by("id").descending()));

        model.addAttribute("courses", coursePage.map(courseService::convertToDto));
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", coursePage.getTotalPages());
        model.addAttribute("totalElements", coursePage.getTotalElements());
        model.addAttribute("search", search);
        model.addAttribute("selectedDept", departmentId);
        model.addAttribute("departments", departmentService.getAllDepartments());
        model.addAttribute("activeNav", "05");

        return "courses/list";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("courseDto", new CourseDto());
        model.addAttribute("departments", departmentService.getAllDepartments());
        model.addAttribute("isEdit", false);
        model.addAttribute("activeNav", "05");
        return "courses/form";
    }

    @PostMapping("/save")
    public String saveCourse(@Valid @ModelAttribute("courseDto") CourseDto courseDto,
                             BindingResult bindingResult,
                             Model model,
                             RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("departments", departmentService.getAllDepartments());
            model.addAttribute("isEdit", courseDto.getId() != null);
            model.addAttribute("activeNav", "05");
            return "courses/form";
        }

        try {
            if (courseDto.getId() == null) {
                courseService.createCourse(courseDto);
                redirectAttributes.addFlashAttribute("successMessage", "COURSE CREATED: " + courseDto.getCode());
            } else {
                courseService.updateCourse(courseDto.getId(), courseDto);
                redirectAttributes.addFlashAttribute("successMessage", "COURSE UPDATED: " + courseDto.getCode());
            }
        } catch (Exception ex) {
            redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());
            if (courseDto.getId() != null) {
                return "redirect:/courses/edit/" + courseDto.getId();
            }
            return "redirect:/courses/new";
        }

        return "redirect:/courses";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable("id") Long id, Model model) {
        Course course = courseService.getCourseById(id);
        model.addAttribute("courseDto", courseService.convertToDto(course));
        model.addAttribute("departments", departmentService.getAllDepartments());
        model.addAttribute("isEdit", true);
        model.addAttribute("activeNav", "05");
        return "courses/form";
    }

    @PostMapping("/delete/{id}")
    public String deleteCourse(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        try {
            courseService.deleteCourse(id);
            redirectAttributes.addFlashAttribute("successMessage", "COURSE DELETED");
        } catch (Exception ex) {
            redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());
        }
        return "redirect:/courses";
    }
}
