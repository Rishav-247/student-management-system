package com.example.sms.controller;

import com.example.sms.dto.StudentDto;
import com.example.sms.dto.StudentProfileDto;
import com.example.sms.entity.Student;
import com.example.sms.entity.StudentStatus;
import com.example.sms.service.StudentService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/students")
public class StudentApiController {

    private final StudentService studentService;

    public StudentApiController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping
    public ResponseEntity<List<StudentDto>> getAllStudents(
            @RequestParam(value = "search", required = false) String search,
            @RequestParam(value = "departmentId", required = false) Long departmentId,
            @RequestParam(value = "courseId", required = false) Long courseId,
            @RequestParam(value = "semester", required = false) Integer semester,
            @RequestParam(value = "status", required = false) StudentStatus status,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "50") int size) {

        Page<Student> students = studentService.getFilteredStudents(search, departmentId, courseId, semester, status, PageRequest.of(page, size, Sort.by("id").descending()));
        return ResponseEntity.ok(students.map(studentService::convertToDto).getContent());
    }

    @GetMapping("/{id}")
    public ResponseEntity<StudentDto> getStudentById(@PathVariable Long id) {
        Student student = studentService.getStudentById(id);
        return ResponseEntity.ok(studentService.convertToDto(student));
    }

    @GetMapping("/{id}/profile")
    public ResponseEntity<StudentProfileDto> getStudentProfile(@PathVariable Long id) {
        StudentProfileDto profile = studentService.getStudentProfile(id);
        return ResponseEntity.ok(profile);
    }

    @PostMapping
    public ResponseEntity<StudentDto> createStudent(@Valid @RequestBody StudentDto studentDto) {
        Student created = studentService.createStudent(studentDto);
        return new ResponseEntity<>(studentService.convertToDto(created), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<StudentDto> updateStudent(@PathVariable Long id, @Valid @RequestBody StudentDto studentDto) {
        Student updated = studentService.updateStudent(id, studentDto);
        return ResponseEntity.ok(studentService.convertToDto(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStudent(@PathVariable Long id) {
        studentService.deleteStudent(id);
        return ResponseEntity.noContent().build();
    }
}
