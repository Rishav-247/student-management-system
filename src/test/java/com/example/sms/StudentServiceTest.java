package com.example.sms;

import com.example.sms.dto.DepartmentDto;
import com.example.sms.dto.CourseDto;
import com.example.sms.dto.StudentDto;
import com.example.sms.entity.Course;
import com.example.sms.entity.Department;
import com.example.sms.entity.Gender;
import com.example.sms.entity.Student;
import com.example.sms.entity.StudentStatus;
import com.example.sms.service.CourseService;
import com.example.sms.service.DepartmentService;
import com.example.sms.service.StudentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class StudentServiceTest {

    @Autowired
    private DepartmentService departmentService;

    @Autowired
    private CourseService courseService;

    @Autowired
    private StudentService studentService;

    private Department department;
    private Course course;

    @BeforeEach
    void setUp() {
        DepartmentDto deptDto = new DepartmentDto();
        deptDto.setCode("TEST-CSE");
        deptDto.setName("Test Computer Science Department");
        deptDto.setHeadOfDepartment("Dr. Test Lead");
        department = departmentService.createDepartment(deptDto);

        CourseDto courseDto = new CourseDto();
        courseDto.setCode("TEST-BTECH");
        courseDto.setName("Test B.Tech Course");
        courseDto.setDepartmentId(department.getId());
        courseDto.setDurationYears(4);
        courseDto.setTotalSemesters(8);
        courseDto.setDegreeType("UNDERGRADUATE");
        course = courseService.createCourse(courseDto);
    }

    @Test
    void testCreateAndRetrieveStudent() {
        StudentDto studentDto = new StudentDto();
        studentDto.setStudentId("TEST-STU-999");
        studentDto.setFirstName("Rishav");
        studentDto.setLastName("Thakur");
        studentDto.setEmail("rishav.unique.test@university.edu");
        studentDto.setPhone("+1234567890");
        studentDto.setGender(Gender.MALE);
        studentDto.setDepartmentId(department.getId());
        studentDto.setCourseId(course.getId());
        studentDto.setSemester(7);
        studentDto.setStatus(StudentStatus.ACTIVE);
        studentDto.setAdmissionDate(LocalDate.now());

        Student created = studentService.createStudent(studentDto);

        assertNotNull(created.getId());
        assertEquals("TEST-STU-999", created.getStudentId());
        assertEquals("Rishav Thakur", created.getFullName());

        Student fetched = studentService.getStudentByStudentId("TEST-STU-999");
        assertNotNull(fetched);
        assertEquals("rishav.unique.test@university.edu", fetched.getEmail());
    }
}
