package com.example.sms.config;

import com.example.sms.entity.*;
import com.example.sms.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Configuration
public class DataInitializer {

    @Bean
    public CommandLineRunner initData(UserRepository userRepository,
                                      DepartmentRepository departmentRepository,
                                      CourseRepository courseRepository,
                                      TeacherRepository teacherRepository,
                                      StudentRepository studentRepository,
                                      SubjectRepository subjectRepository,
                                      EnrollmentRepository enrollmentRepository,
                                      AttendanceRepository attendanceRepository,
                                      MarksRepository marksRepository,
                                      PasswordEncoder passwordEncoder) {
        return args -> {
            // Check if database is already initialized
            if (userRepository.count() > 0) {
                return;
            }

            // 1. Seed Users
            User adminUser = new User("admin", "admin@university.edu", passwordEncoder.encode("admin123"), Role.ROLE_ADMIN, true);
            User teacherUser1 = new User("turing", "alan.turing@university.edu", passwordEncoder.encode("teacher123"), Role.ROLE_TEACHER, true);
            User teacherUser2 = new User("hopper", "grace.hopper@university.edu", passwordEncoder.encode("teacher123"), Role.ROLE_TEACHER, true);
            User teacherUser3 = new User("lovelace", "ada.lovelace@university.edu", passwordEncoder.encode("teacher123"), Role.ROLE_TEACHER, true);
            User studentUser1 = new User("student", "rishav.thakur@university.edu", passwordEncoder.encode("student123"), Role.ROLE_STUDENT, true);
            User studentUser2 = new User("rahul", "rahul.sharma@university.edu", passwordEncoder.encode("student123"), Role.ROLE_STUDENT, true);

            userRepository.saveAll(List.of(adminUser, teacherUser1, teacherUser2, teacherUser3, studentUser1, studentUser2));

            // 2. Seed Departments
            Department cse = new Department("CSE", "Computer Science & Engineering",
                    "Department of Computer Science and Engineering focusing on Systems, AI, and Software Architecture.",
                    "Dr. Alan Turing", "cse.admin@university.edu", "+1-555-0101");

            Department ece = new Department("ECE", "Electronics & Communication Engineering",
                    "Department of Electronics, Signal Processing, Microelectronics and Embedded Systems.",
                    "Dr. Grace Hopper", "ece.admin@university.edu", "+1-555-0102");

            Department ai = new Department("AI-DS", "Artificial Intelligence & Data Science",
                    "Advanced cognitive computing, deep learning neural models and high-performance data processing.",
                    "Dr. Ada Lovelace", "aids.admin@university.edu", "+1-555-0103");

            Department cyber = new Department("CYS", "Cyber Security & Information Assurance",
                    "Cryptographic engineering, network defense, threat intelligence, and digital forensics.",
                    "Prof. Claude Shannon", "cys.admin@university.edu", "+1-555-0104");

            departmentRepository.saveAll(List.of(cse, ece, ai, cyber));

            // 3. Seed Courses
            Course btechCse = new Course("BTECH-CS", "Bachelor of Technology in Computer Science", cse, 4, 8, "UNDERGRADUATE", "Four-year comprehensive undergraduate engineering program.");
            Course btechAi = new Course("BTECH-AI", "Bachelor of Technology in Artificial Intelligence", ai, 4, 8, "UNDERGRADUATE", "Undergraduate program with specialization in Deep Learning and Large Scale Machine Learning.");
            Course btechCyber = new Course("BTECH-CYS", "Bachelor of Technology in Cyber Security", cyber, 4, 8, "UNDERGRADUATE", "Undergraduate degree in Cryptography, Network Protocols, and Security Operations.");
            Course mca = new Course("MCA", "Master of Computer Applications", cse, 2, 4, "POSTGRADUATE", "Advanced graduate program for enterprise software systems development.");
            Course mtechCs = new Course("MTECH-CS", "Master of Technology in Distributed Systems", cse, 2, 4, "POSTGRADUATE", "Research-oriented post-graduate program in Distributed Computing.");

            courseRepository.saveAll(List.of(btechCse, btechAi, btechCyber, mca, mtechCs));

            // 4. Seed Teachers
            Teacher teacher1 = new Teacher("TCH2026001", "Alan", "Turing", "alan.turing@university.edu", "+1-555-0201", cse, "Professor & Chair", "Ph.D. in Mathematical Logic", LocalDate.of(2018, 8, 1), "Theory of Computation");
            teacher1.setUser(teacherUser1);

            Teacher teacher2 = new Teacher("TCH2026002", "Grace", "Hopper", "grace.hopper@university.edu", "+1-555-0202", ece, "Professor", "Ph.D. in Mathematics", LocalDate.of(2019, 1, 15), "Compilers and System Architecture");
            teacher2.setUser(teacherUser2);

            Teacher teacher3 = new Teacher("TCH2026003", "Ada", "Lovelace", "ada.lovelace@university.edu", "+1-555-0203", ai, "Associate Professor", "Ph.D. in Computational Sciences", LocalDate.of(2020, 7, 10), "Algorithmic Analysis");
            teacher3.setUser(teacherUser3);

            Teacher teacher4 = new Teacher("TCH2026004", "Claude", "Shannon", "c.shannon@university.edu", "+1-555-0204", cyber, "Professor", "Ph.D. in Electrical Engineering", LocalDate.of(2017, 3, 20), "Information Theory & Cryptography");

            teacherRepository.saveAll(List.of(teacher1, teacher2, teacher3, teacher4));

            // 5. Seed Subjects
            Subject sub1 = new Subject("CS701", "Distributed Operating Systems", btechCse, cse, 7, 4, SubjectType.THEORY);
            Subject sub2 = new Subject("CS702", "Network Security & Cryptography", btechCyber, cyber, 7, 4, SubjectType.THEORY);
            Subject sub3 = new Subject("CS703", "Cloud Computing & DevOps", btechCse, cse, 7, 3, SubjectType.THEORY);
            Subject sub4 = new Subject("CS704", "Advanced Machine Learning", btechAi, ai, 7, 4, SubjectType.THEORY);
            Subject sub5 = new Subject("CS705L", "Security Audit & Pen-Testing Lab", btechCyber, cyber, 7, 2, SubjectType.LAB);
            Subject sub6 = new Subject("CS706L", "Distributed Systems Practical Lab", btechCse, cse, 7, 2, SubjectType.PRACTICAL);

            Subject sub7 = new Subject("CS501", "Database Management Systems", btechCse, cse, 5, 4, SubjectType.THEORY);
            Subject sub8 = new Subject("CS502", "Computer Networks Protocol Suite", btechCse, cse, 5, 4, SubjectType.THEORY);
            Subject sub9 = new Subject("CS503", "Design & Analysis of Algorithms", btechCse, cse, 5, 4, SubjectType.THEORY);

            subjectRepository.saveAll(List.of(sub1, sub2, sub3, sub4, sub5, sub6, sub7, sub8, sub9));

            // 6. Seed Students
            Student s1 = new Student("CS20260041", "Rishav", "Thakur", "rishav.thakur@university.edu", "+91-9876543210",
                    LocalDate.of(2003, 5, 14), Gender.MALE, "Suite 401, Academic Block North, Campus", cyber, btechCyber, 7, LocalDate.of(2022, 8, 1), StudentStatus.ACTIVE);
            s1.setUser(studentUser1);

            Student s2 = new Student("CS20260042", "Rahul", "Sharma", "rahul.sharma@university.edu", "+91-9876543211",
                    LocalDate.of(2003, 8, 22), Gender.MALE, "Sector 14, University Residency", cse, btechCse, 7, LocalDate.of(2022, 8, 1), StudentStatus.ACTIVE);
            s2.setUser(studentUser2);

            Student s3 = new Student("CS20260043", "Aman", "Singh", "aman.singh@university.edu", "+91-9876543212",
                    LocalDate.of(2003, 11, 3), Gender.MALE, "Hostel 7, Room 302, Campus", ai, btechAi, 7, LocalDate.of(2022, 8, 1), StudentStatus.ACTIVE);

            Student s4 = new Student("CS20260044", "Priya", "Nair", "priya.nair@university.edu", "+91-9876543213",
                    LocalDate.of(2004, 2, 18), Gender.FEMALE, "Girls Hostel 2, Room 104, Campus", cse, btechCse, 5, LocalDate.of(2023, 8, 1), StudentStatus.ACTIVE);

            Student s5 = new Student("CS20260045", "Kavya", "Iyer", "kavya.iyer@university.edu", "+91-9876543214",
                    LocalDate.of(2003, 9, 30), Gender.FEMALE, "Park Avenue Residency, Sector 8", cyber, btechCyber, 7, LocalDate.of(2022, 8, 1), StudentStatus.ACTIVE);

            Student s6 = new Student("CS20260046", "Rohan", "Verma", "rohan.verma@university.edu", "+91-9876543215",
                    LocalDate.of(2004, 1, 10), Gender.MALE, "Block C-12, Sector 21", cse, btechCse, 5, LocalDate.of(2023, 8, 1), StudentStatus.ACTIVE);

            Student s7 = new Student("CS20260047", "Ananya", "Das", "ananya.das@university.edu", "+91-9876543216",
                    LocalDate.of(2002, 12, 15), Gender.FEMALE, "Academic Staff Quarters 4", ai, btechAi, 7, LocalDate.of(2022, 8, 1), StudentStatus.ACTIVE);

            Student s8 = new Student("CS20260048", "Vikram", "Malhotra", "vikram.m@university.edu", "+91-9876543217",
                    LocalDate.of(2003, 4, 25), Gender.MALE, "Hostel 5, Room 201, Campus", cse, btechCse, 7, LocalDate.of(2022, 8, 1), StudentStatus.ACTIVE);

            studentRepository.saveAll(List.of(s1, s2, s3, s4, s5, s6, s7, s8));

            // 7. Seed Enrollments
            Enrollment e1 = new Enrollment(s1, btechCyber, "2025-2026", 7, LocalDate.of(2025, 8, 1), EnrollmentStatus.ACTIVE);
            Enrollment e2 = new Enrollment(s2, btechCse, "2025-2026", 7, LocalDate.of(2025, 8, 1), EnrollmentStatus.ACTIVE);
            Enrollment e3 = new Enrollment(s3, btechAi, "2025-2026", 7, LocalDate.of(2025, 8, 1), EnrollmentStatus.ACTIVE);
            Enrollment e4 = new Enrollment(s4, btechCse, "2025-2026", 5, LocalDate.of(2025, 8, 1), EnrollmentStatus.ACTIVE);
            Enrollment e5 = new Enrollment(s5, btechCyber, "2025-2026", 7, LocalDate.of(2025, 8, 1), EnrollmentStatus.ACTIVE);
            Enrollment e6 = new Enrollment(s6, btechCse, "2025-2026", 5, LocalDate.of(2025, 8, 1), EnrollmentStatus.ACTIVE);
            Enrollment e7 = new Enrollment(s7, btechAi, "2025-2026", 7, LocalDate.of(2025, 8, 1), EnrollmentStatus.ACTIVE);
            Enrollment e8 = new Enrollment(s8, btechCse, "2025-2026", 7, LocalDate.of(2025, 8, 1), EnrollmentStatus.ACTIVE);

            enrollmentRepository.saveAll(List.of(e1, e2, e3, e4, e5, e6, e7, e8));

            // 8. Seed Attendance (Recent dates)
            LocalDate d1 = LocalDate.now().minusDays(4);
            LocalDate d2 = LocalDate.now().minusDays(3);
            LocalDate d3 = LocalDate.now().minusDays(2);
            LocalDate d4 = LocalDate.now().minusDays(1);
            LocalDate d5 = LocalDate.now();

            attendanceRepository.saveAll(List.of(
                    new Attendance(s1, sub2, d1, AttendanceStatus.PRESENT, "Attended lecture"),
                    new Attendance(s1, sub2, d2, AttendanceStatus.PRESENT, "On time"),
                    new Attendance(s1, sub2, d3, AttendanceStatus.PRESENT, "Active participation"),
                    new Attendance(s1, sub2, d4, AttendanceStatus.ABSENT, "Medical leave"),
                    new Attendance(s1, sub2, d5, AttendanceStatus.PRESENT, "Lab session"),

                    new Attendance(s2, sub1, d1, AttendanceStatus.PRESENT, "Regular"),
                    new Attendance(s2, sub1, d2, AttendanceStatus.PRESENT, "Regular"),
                    new Attendance(s2, sub1, d3, AttendanceStatus.ABSENT, "Unexcused"),
                    new Attendance(s2, sub1, d4, AttendanceStatus.PRESENT, "Regular"),
                    new Attendance(s2, sub1, d5, AttendanceStatus.PRESENT, "Regular"),

                    new Attendance(s3, sub4, d1, AttendanceStatus.PRESENT, "Regular"),
                    new Attendance(s3, sub4, d2, AttendanceStatus.PRESENT, "Regular"),
                    new Attendance(s3, sub4, d3, AttendanceStatus.PRESENT, "Regular"),
                    new Attendance(s3, sub4, d4, AttendanceStatus.PRESENT, "Regular"),
                    new Attendance(s3, sub4, d5, AttendanceStatus.PRESENT, "Regular")
            ));

            // 9. Seed Marks & Results
            Marks m1 = new Marks(s1, sub2, 7, "2025-2026",
                    new BigDecimal("18.50"), new BigDecimal("19.00"), new BigDecimal("18.00"), new BigDecimal("36.50"));

            Marks m2 = new Marks(s1, sub5, 7, "2025-2026",
                    new BigDecimal("19.00"), new BigDecimal("19.50"), new BigDecimal("20.00"), new BigDecimal("38.00"));

            Marks m3 = new Marks(s2, sub1, 7, "2025-2026",
                    new BigDecimal("16.00"), new BigDecimal("17.50"), new BigDecimal("17.00"), new BigDecimal("32.00"));

            Marks m4 = new Marks(s2, sub3, 7, "2025-2026",
                    new BigDecimal("15.50"), new BigDecimal("16.00"), new BigDecimal("16.00"), new BigDecimal("30.50"));

            Marks m5 = new Marks(s3, sub4, 7, "2025-2026",
                    new BigDecimal("20.00"), new BigDecimal("20.00"), new BigDecimal("19.50"), new BigDecimal("39.00"));

            Marks m6 = new Marks(s5, sub2, 7, "2025-2026",
                    new BigDecimal("17.00"), new BigDecimal("18.00"), new BigDecimal("18.50"), new BigDecimal("35.00"));

            marksRepository.saveAll(List.of(m1, m2, m3, m4, m5, m6));

            System.out.println("====================================================================");
            System.out.println(">>> SWISS BRUTALIST STUDENT MANAGEMENT SYSTEM DATA SEEDED <<<");
            System.out.println(">>> ADMIN:   username: admin    / password: admin123");
            System.out.println(">>> TEACHER: username: turing   / password: teacher123");
            System.out.println(">>> STUDENT: username: student  / password: student123");
            System.out.println("====================================================================");
        };
    }
}
