# 🏛️ ACADEMIA / SWISS BRUTALIST STUDENT MANAGEMENT SYSTEM

> **A Production-Grade, Enterprise-Ready Academic Administration Platform Built with Java 17+, Spring Boot 3.3.5, Spring Security, Spring Data JPA, Thymeleaf, and an Authentic Swiss Brutalist UI/UX.**

---

## 📌 1. OVERVIEW & DESIGN PHILOSOPHY

**Student Management System** is a complete, real-world educational management application designed for universities, colleges, and academic institutions. 

Unlike generic CRUD templates with rounded corners and gradient cards, this system is engineered with an uncompromising **Swiss Brutalist Design System**:
- **Palette**: Paper Canvas (`#F5F3EE`), Jet Black (`#111111`), Pure White (`#FFFFFF`), High-Contrast Alert Red (`#FF3B30`), and Electric Lime (`#D9FF00`).
- **Typography**: Space Grotesk / Inter for sharp headers and dense body text, JetBrains Mono for system codes, dates, and IDs.
- **Form & Structure**: Sharp 0px border-radius, solid 2px black grid borders, bold offset shadows (`3px 3px 0 #111111`), and numbered section navigation (`01 / DASHBOARD`, `02 / STUDENTS`, etc.).
- **Information Density**: Clean, high-throughput tabular layouts, real-time statistics, and publication-ready print/export views.

---

## 🛠️ 2. TECH STACK

- **Backend**: Java 17+, Spring Boot 3.3.5, Spring MVC, Spring Data JPA, Spring Security 6, Hibernate 6, Jakarta Bean Validation.
- **Frontend**: HTML5, CSS3 (Custom Swiss Brutalist Framework), JavaScript (Vanilla ES6), Thymeleaf 3 Template Engine.
- **Database**: MySQL 8.0+ (Production) / H2 In-Memory (Test/Demo mode).
- **Build Tool**: Apache Maven 3.9.9 + Maven Wrapper (`mvnw`).

---

## 🔑 3. DEFAULT CREDENTIALS (PRE-SEEDED)

The system automatically initializes test accounts upon first startup via `DataInitializer.java`:

| Role | Username | Password | Access Capabilities |
| :--- | :--- | :--- | :--- |
| **Administrator** | `admin` | `admin123` | Full access to all modules, users, departments, courses, subjects, reports, settings, and logs. |
| **Teacher** | `turing` | `teacher123` | Access to assigned students, subject enrollments, attendance marking, and grade entries. |
| **Teacher** | `lovelace` | `teacher123` | Secondary faculty account for testing multi-department assignments. |
| **Student** | `student` | `student123` | Personal academic profile, enrolled subjects, real-time attendance ledger, and semester report card. |

---

## 🚀 4. QUICK START & RUNNING THE APPLICATION

### Prerequisites
- **Java Development Kit (JDK) 17 or higher** installed and available on `PATH`.
- **MySQL Server** (Optional if running with default H2/MySQL profiles).

### Option A: Run with Embedded H2 Database (Instant Test Mode)
If you want to run immediately without configuring a local MySQL instance:
```bash
# In the project root:
.\mvnw spring-boot:run -Dspring-boot.run.profiles=test
```
Access the application at: **`http://localhost:8080`**

---

### Option B: Run with MySQL Database (Production Mode)
1. Start your local MySQL server.
2. Create the database:
   ```sql
   CREATE DATABASE sms_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
   ```
3. Verify or adjust database credentials in `src/main/resources/application.yml`:
   ```yaml
   spring:
     datasource:
       url: jdbc:mysql://localhost:3306/sms_db?createDatabaseIfNotExist=true&useSSL=false&allowPublicKeyRetrieval=true
       username: root
       password: password
   ```
4. Start the application:
   ```bash
   .\mvnw spring-boot:run
   ```
5. Open your browser and navigate to: **`http://localhost:8080`**

---

## 📐 5. CORE SYSTEM MODULES

### `01 / DASHBOARD`
- Live institutional metrics: Total Students, Active Faculty, Department count, Average Attendance %, and Course enrollment distribution.
- Recent registration timeline and fast-action command grid.

### `02 / STUDENTS`
- Paginated & searchable student directory with department and status filtering.
- Comprehensive student profile view: personal info, academic metadata, enrolled subjects, calculated GPA/CGPA, and attendance stats.
- Complete Add / Edit / Status Toggle / Delete workflows.

### `03 / TEACHERS`
- Faculty registry with employee IDs, designations, assigned departments, specializations, and contact records.

### `04 / ACADEMIC (DEPARTMENTS, COURSES, SUBJECTS)`
- **Departments**: Code, name, HOD assignment, established year, and capacity tracking.
- **Courses**: Degree programs (B.Tech, B.Sc, BCA, MCA), duration in years, and semester configurations.
- **Subjects**: Course-linked syllabus management, subject codes, credit hours, subject type (Theory/Practical/Elective), and semester mapping.

### `05 / ENROLLMENTS`
- Semester enrollment engine with duplicate enrollment prevention and active status tracking.

### `06 / ATTENDANCE`
- Daily/lecture attendance recording interface (`PRESENT`, `ABSENT`, `LATE`, `EXCUSED`).
- Summary aggregation with color-coded threshold alerts (< 75% attendance warning badges).

### `07 / MARKS & EXAMINATIONS`
- Grade entry module supporting Internal Marks, Mid-term Marks, Final Exams, and Assignment Scores.
- Automatic grade point calculation, percentage resolution, and result status determination (`PASS`, `FAIL`, `WITHHELD`).
- High-fidelity **Semester Report Card Generator** with clean print stylesheet support.

### `08 / ANALYTICS & REPORTS`
- Department-wise enrollment reports.
- Attendance shortfall compliance audit reports.
- Academic performance distribution reports.

### `09 / SYSTEM USERS & SETTINGS`
- Role assignment (`ROLE_ADMIN`, `ROLE_TEACHER`, `ROLE_STUDENT`), account status toggling, password resets, and system configuration preferences.

---

## 🛡️ 6. REST API ENDPOINTS

The application includes a documented REST API layer for programmatic access:

- `GET /api/v1/students` - Paginated list of students with query filters (`keyword`, `departmentId`, `status`, `page`, `size`).
- `GET /api/v1/students/{id}` - Complete student profile with academic history.
- `POST /api/v1/students` - Register a new student (JSON payload with validation).
- `PUT /api/v1/students/{id}` - Update student record.
- `DELETE /api/v1/students/{id}` - Remove student record.

---

## 🧪 7. RUNNING TESTS

Execute all unit and integration test suites:
```bash
.\mvnw clean test
```

---

## 📂 8. DIRECTORY STRUCTURE

```
student-management-system/
├── pom.xml
├── mvnw / mvnw.cmd
├── src/
│   ├── main/
│   │   ├── java/com/sms/
│   │   │   ├── config/          # Security, JPA Auditing, PasswordEncoder
│   │   │   ├── controller/      # Spring MVC & REST API Controllers
│   │   │   ├── dto/             # Data Transfer Objects & View Models
│   │   │   ├── entity/          # JPA Entities (Student, Teacher, Marks, etc.)
│   │   │   ├── enums/           # Domain Enums (Role, Status, Result, etc.)
│   │   │   ├── exception/       # Custom Exception Hierarchy & Global Handler
│   │   │   ├── repository/      # Spring Data JPA Repositories & Specifications
│   │   │   ├── security/        # CustomUserDetails & Authentication Handlers
│   │   │   ├── service/         # Business Logic Layer & Implementations
│   │   │   ├── util/            # Seeders, Formatters, & Helper Utilities
│   │   │   └── StudentManagementSystemApplication.java
│   │   └── resources/
│   │       ├── application.yml
│   │       ├── application-test.yml
│   │       ├── schema-mysql.sql
│   │       ├── static/
│   │       │   ├── css/brutalist.css   # Authentic Swiss Brutalist UI System
│   │       │   └── js/main.js          # Interactive UX Scripts & Charts
│   │       └── templates/              # Thymeleaf HTML Templates
│   └── test/
│       ├── java/com/sms/
│       └── resources/
└── README.md
```

---
*Built with precision and purpose. Academia Student Management System.*
