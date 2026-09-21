package com.example.sms.controller;

import com.example.sms.dto.MarksDto;
import com.example.sms.dto.StudentReportCardDto;
import com.example.sms.entity.Marks;
import com.example.sms.entity.ResultStatus;
import com.example.sms.service.CourseService;
import com.example.sms.service.MarksService;
import com.example.sms.service.StudentService;
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
@RequestMapping("/marks")
public class MarksController {

    private final MarksService marksService;
    private final StudentService studentService;
    private final SubjectService subjectService;
    private final CourseService courseService;

    public MarksController(MarksService marksService,
                           StudentService studentService,
                           SubjectService subjectService,
                           CourseService courseService) {
        this.marksService = marksService;
        this.studentService = studentService;
        this.subjectService = subjectService;
        this.courseService = courseService;
    }

    @GetMapping
    public String listMarks(@RequestParam(value = "search", required = false) String search,
                            @RequestParam(value = "studentId", required = false) Long studentId,
                            @RequestParam(value = "subjectId", required = false) Long subjectId,
                            @RequestParam(value = "semester", required = false) Integer semester,
                            @RequestParam(value = "academicYear", required = false) String academicYear,
                            @RequestParam(value = "status", required = false) ResultStatus status,
                            @RequestParam(value = "page", defaultValue = "0") int page,
                            @RequestParam(value = "size", defaultValue = "10") int size,
                            Model model) {

        Page<Marks> marksPage = marksService.searchMarks(search, studentId, subjectId, semester, academicYear, status, PageRequest.of(page, size, Sort.by("id").descending()));

        model.addAttribute("marksList", marksPage.map(marksService::convertToDto));
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", marksPage.getTotalPages());
        model.addAttribute("totalElements", marksPage.getTotalElements());
        model.addAttribute("search", search);
        model.addAttribute("selectedStudent", studentId);
        model.addAttribute("selectedSubject", subjectId);
        model.addAttribute("selectedSemester", semester);
        model.addAttribute("selectedYear", academicYear);
        model.addAttribute("selectedStatus", status);
        model.addAttribute("subjects", subjectService.getAllSubjects());
        model.addAttribute("statuses", ResultStatus.values());
        model.addAttribute("overallPassRate", marksService.calculateOverallPassRate());
        model.addAttribute("activeNav", "09");

        return "marks/list";
    }

    @GetMapping("/record")
    public String showRecordMarksForm(Model model) {
        model.addAttribute("marksDto", new MarksDto());
        model.addAttribute("students", studentService.getAllStudents());
        model.addAttribute("subjects", subjectService.getAllSubjects());
        model.addAttribute("isEdit", false);
        model.addAttribute("activeNav", "09");
        return "marks/form";
    }

    @PostMapping("/save")
    public String saveMarks(@Valid @ModelAttribute("marksDto") MarksDto marksDto,
                            BindingResult bindingResult,
                            Model model,
                            RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("students", studentService.getAllStudents());
            model.addAttribute("subjects", subjectService.getAllSubjects());
            model.addAttribute("isEdit", marksDto.getId() != null);
            model.addAttribute("activeNav", "09");
            return "marks/form";
        }

        try {
            if (marksDto.getId() == null) {
                marksService.recordMarks(marksDto);
                redirectAttributes.addFlashAttribute("successMessage", "ACADEMIC MARKS RECORDED SUCCESSFULLY");
            } else {
                marksService.updateMarks(marksDto.getId(), marksDto);
                redirectAttributes.addFlashAttribute("successMessage", "ACADEMIC MARKS UPDATED SUCCESSFULLY");
            }
        } catch (Exception ex) {
            redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());
            return "redirect:/marks/record";
        }

        return "redirect:/marks";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable("id") Long id, Model model) {
        Marks marks = marksService.getMarksById(id);
        model.addAttribute("marksDto", marksService.convertToDto(marks));
        model.addAttribute("students", studentService.getAllStudents());
        model.addAttribute("subjects", subjectService.getAllSubjects());
        model.addAttribute("isEdit", true);
        model.addAttribute("activeNav", "09");
        return "marks/form";
    }

    @GetMapping("/report-card/{studentId}")
    public String viewReportCard(@PathVariable("studentId") Long studentId,
                                 @RequestParam(value = "semester", defaultValue = "1") Integer semester,
                                 @RequestParam(value = "academicYear", defaultValue = "2025-2026") String academicYear,
                                 Model model) {

        StudentReportCardDto reportCard = marksService.generateReportCard(studentId, semester, academicYear);
        model.addAttribute("reportCard", reportCard);
        model.addAttribute("activeNav", "09");
        return "marks/report-card";
    }

    @PostMapping("/delete/{id}")
    public String deleteMarks(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        try {
            marksService.deleteMarks(id);
            redirectAttributes.addFlashAttribute("successMessage", "MARKS RECORD REMOVED");
        } catch (Exception ex) {
            redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());
        }
        return "redirect:/marks";
    }
}
