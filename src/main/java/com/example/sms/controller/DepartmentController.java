package com.example.sms.controller;

import com.example.sms.dto.DepartmentDto;
import com.example.sms.entity.Department;
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
@RequestMapping("/departments")
public class DepartmentController {

    private final DepartmentService departmentService;

    public DepartmentController(DepartmentService departmentService) {
        this.departmentService = departmentService;
    }

    @GetMapping
    public String listDepartments(@RequestParam(value = "search", required = false) String search,
                                  @RequestParam(value = "page", defaultValue = "0") int page,
                                  @RequestParam(value = "size", defaultValue = "10") int size,
                                  Model model) {

        Page<Department> departmentPage = departmentService.searchDepartments(search, PageRequest.of(page, size, Sort.by("id").ascending()));

        model.addAttribute("departments", departmentPage.map(departmentService::convertToDto));
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", departmentPage.getTotalPages());
        model.addAttribute("totalElements", departmentPage.getTotalElements());
        model.addAttribute("search", search);
        model.addAttribute("activeNav", "04");

        return "departments/list";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("departmentDto", new DepartmentDto());
        model.addAttribute("isEdit", false);
        model.addAttribute("activeNav", "04");
        return "departments/form";
    }

    @PostMapping("/save")
    public String saveDepartment(@Valid @ModelAttribute("departmentDto") DepartmentDto departmentDto,
                                 BindingResult bindingResult,
                                 Model model,
                                 RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("isEdit", departmentDto.getId() != null);
            model.addAttribute("activeNav", "04");
            return "departments/form";
        }

        try {
            if (departmentDto.getId() == null) {
                departmentService.createDepartment(departmentDto);
                redirectAttributes.addFlashAttribute("successMessage", "DEPARTMENT CREATED: " + departmentDto.getCode());
            } else {
                departmentService.updateDepartment(departmentDto.getId(), departmentDto);
                redirectAttributes.addFlashAttribute("successMessage", "DEPARTMENT UPDATED: " + departmentDto.getCode());
            }
        } catch (Exception ex) {
            redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());
            if (departmentDto.getId() != null) {
                return "redirect:/departments/edit/" + departmentDto.getId();
            }
            return "redirect:/departments/new";
        }

        return "redirect:/departments";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable("id") Long id, Model model) {
        Department department = departmentService.getDepartmentById(id);
        model.addAttribute("departmentDto", departmentService.convertToDto(department));
        model.addAttribute("isEdit", true);
        model.addAttribute("activeNav", "04");
        return "departments/form";
    }

    @PostMapping("/delete/{id}")
    public String deleteDepartment(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        try {
            departmentService.deleteDepartment(id);
            redirectAttributes.addFlashAttribute("successMessage", "DEPARTMENT DELETED");
        } catch (Exception ex) {
            redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());
        }
        return "redirect:/departments";
    }
}
