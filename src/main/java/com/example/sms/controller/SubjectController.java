package com.example.sms.controller;

import com.example.sms.dto.SubjectDto;
import com.example.sms.entity.Subject;
import com.example.sms.entity.SubjectType;
import com.example.sms.service.CourseService;
import com.example.sms.service.DepartmentService;
import com.example.sms.service.SubjectService;
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
@RequestMapping("/subjects")
public class SubjectController {

    private final SubjectService subjectService;
    private final CourseService courseService;
    private final DepartmentService departmentService;

    public SubjectController(SubjectService subjectService,
                             CourseService courseService,
                             DepartmentService departmentService) {
        this.subjectService = subjectService;
        this.courseService = courseService;
        this.departmentService = departmentService;
    }

    @GetMapping
    public String listSubjects(@RequestParam(value = "search", required = false) String search,
                               @RequestParam(value = "courseId", required = false) Long courseId,
                               @RequestParam(value = "semester", required = false) Integer semester,
                               @RequestParam(value = "page", defaultValue = "0") int page,
                               @RequestParam(value = "size", defaultValue = "10") int size,
                               Model model) {

        Page<Subject> subjectPage = subjectService.searchSubjects(search, courseId, semester, PageRequest.of(page, size, Sort.by("id").descending()));

        model.addAttribute("subjects", subjectPage.map(subjectService::convertToDto));
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", subjectPage.getTotalPages());
        model.addAttribute("totalElements", subjectPage.getTotalElements());
        model.addAttribute("search", search);
        model.addAttribute("selectedCourse", courseId);
        model.addAttribute("selectedSemester", semester);
        model.addAttribute("courses", courseService.getAllCourses());
        model.addAttribute("activeNav", "06");

        return "subjects/list";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("subjectDto", new SubjectDto());
        model.addAttribute("courses", courseService.getAllCourses());
        model.addAttribute("departments", departmentService.getAllDepartments());
        model.addAttribute("types", SubjectType.values());
        model.addAttribute("isEdit", false);
        model.addAttribute("activeNav", "06");
        return "subjects/form";
    }

    @PostMapping("/save")
    public String saveSubject(@Valid @ModelAttribute("subjectDto") SubjectDto subjectDto,
                              BindingResult bindingResult,
                              Model model,
                              RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("courses", courseService.getAllCourses());
            model.addAttribute("departments", departmentService.getAllDepartments());
            model.addAttribute("types", SubjectType.values());
            model.addAttribute("isEdit", subjectDto.getId() != null);
            model.addAttribute("activeNav", "06");
            return "subjects/form";
        }

        try {
            if (subjectDto.getId() == null) {
                subjectService.createSubject(subjectDto);
                redirectAttributes.addFlashAttribute("successMessage", "SUBJECT CREATED: " + subjectDto.getCode());
            } else {
                subjectService.updateSubject(subjectDto.getId(), subjectDto);
                redirectAttributes.addFlashAttribute("successMessage", "SUBJECT UPDATED: " + subjectDto.getCode());
            }
        } catch (Exception ex) {
            redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());
            if (subjectDto.getId() != null) {
                return "redirect:/subjects/edit/" + subjectDto.getId();
            }
            return "redirect:/subjects/new";
        }

        return "redirect:/subjects";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable("id") Long id, Model model) {
        Subject subject = subjectService.getSubjectById(id);
        model.addAttribute("subjectDto", subjectService.convertToDto(subject));
        model.addAttribute("courses", courseService.getAllCourses());
        model.addAttribute("departments", departmentService.getAllDepartments());
        model.addAttribute("types", SubjectType.values());
        model.addAttribute("isEdit", true);
        model.addAttribute("activeNav", "06");
        return "subjects/form";
    }

    @PostMapping("/delete/{id}")
    public String deleteSubject(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        try {
            subjectService.deleteSubject(id);
            redirectAttributes.addFlashAttribute("successMessage", "SUBJECT DELETED");
        } catch (Exception ex) {
            redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());
        }
        return "redirect:/subjects";
    }
}
