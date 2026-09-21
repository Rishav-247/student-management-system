package com.example.sms.controller;

import com.example.sms.dto.TeacherDto;
import com.example.sms.entity.Teacher;
import com.example.sms.service.DepartmentService;
import com.example.sms.service.TeacherService;
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
@RequestMapping("/teachers")
public class TeacherController {

    private final TeacherService teacherService;
    private final DepartmentService departmentService;

    public TeacherController(TeacherService teacherService, DepartmentService departmentService) {
        this.teacherService = teacherService;
        this.departmentService = departmentService;
    }

    @GetMapping
    public String listTeachers(@RequestParam(value = "search", required = false) String search,
                               @RequestParam(value = "departmentId", required = false) Long departmentId,
                               @RequestParam(value = "page", defaultValue = "0") int page,
                               @RequestParam(value = "size", defaultValue = "10") int size,
                               Model model) {

        Page<Teacher> teacherPage = teacherService.searchTeachers(search, departmentId, PageRequest.of(page, size, Sort.by("id").descending()));

        model.addAttribute("teachers", teacherPage.map(teacherService::convertToDto));
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", teacherPage.getTotalPages());
        model.addAttribute("totalElements", teacherPage.getTotalElements());
        model.addAttribute("search", search);
        model.addAttribute("selectedDept", departmentId);
        model.addAttribute("departments", departmentService.getAllDepartments());
        model.addAttribute("activeNav", "03");

        return "teachers/list";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("teacherDto", new TeacherDto());
        model.addAttribute("departments", departmentService.getAllDepartments());
        model.addAttribute("isEdit", false);
        model.addAttribute("activeNav", "03");
        return "teachers/form";
    }

    @PostMapping("/save")
    public String saveTeacher(@Valid @ModelAttribute("teacherDto") TeacherDto teacherDto,
                              BindingResult bindingResult,
                              Model model,
                              RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("departments", departmentService.getAllDepartments());
            model.addAttribute("isEdit", teacherDto.getId() != null);
            model.addAttribute("activeNav", "03");
            return "teachers/form";
        }

        try {
            if (teacherDto.getId() == null) {
                teacherService.createTeacher(teacherDto);
                redirectAttributes.addFlashAttribute("successMessage", "TEACHER RECORD CREATED: " + teacherDto.getTeacherId());
            } else {
                teacherService.updateTeacher(teacherDto.getId(), teacherDto);
                redirectAttributes.addFlashAttribute("successMessage", "TEACHER RECORD UPDATED: " + teacherDto.getTeacherId());
            }
        } catch (Exception ex) {
            redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());
            if (teacherDto.getId() != null) {
                return "redirect:/teachers/edit/" + teacherDto.getId();
            }
            return "redirect:/teachers/new";
        }

        return "redirect:/teachers";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable("id") Long id, Model model) {
        Teacher teacher = teacherService.getTeacherById(id);
        model.addAttribute("teacherDto", teacherService.convertToDto(teacher));
        model.addAttribute("departments", departmentService.getAllDepartments());
        model.addAttribute("isEdit", true);
        model.addAttribute("activeNav", "03");
        return "teachers/form";
    }

    @PostMapping("/delete/{id}")
    public String deleteTeacher(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        try {
            teacherService.deleteTeacher(id);
            redirectAttributes.addFlashAttribute("successMessage", "TEACHER RECORD DELETED");
        } catch (Exception ex) {
            redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());
        }
        return "redirect:/teachers";
    }
}
