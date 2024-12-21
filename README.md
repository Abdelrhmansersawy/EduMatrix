# EduMatrix™

![Version](https://img.shields.io/badge/version-1.0.0-blue.svg)
![Java](https://img.shields.io/badge/Java-17-orange.svg)
![Jakarta EE](https://img.shields.io/badge/Jakarta_EE-10-red.svg)
![MySQL](https://img.shields.io/badge/MySQL-8.0-blue.svg)

![Image](/imgs/cover.png)

EduMatrix is an enterprise-grade university management system that transforms academic operations through innovative digital solutions. Our comprehensive platform streamlines administrative workflows, enhances academic oversight, and elevates institutional efficiency.

---

# Table of Contents
- [Core Capabilities](#core-capabilities)
- [Key Features](#key-features)
- [Database Design](#database-design)
- [Entity-Relationship Flowchart](#entity-relationship-flowchart)
- [ER Diagram](#er-diagram)
- [Database Schema](#database-schema)
- [Numeric Constraints](#numeric-constraints)
- [UML Class Diagram](#uml-class-diagram)


# Core Capabilities

- **Seamless Integration**: Unified platform for all administrative and academic processes
- **Intelligent Automation**: Smart workflows that reduce manual intervention
- **Real-time Analytics**: Data-driven insights for informed decision-making
- **Scalable Architecture**: Built to grow with your institution
- **Secure Infrastructure**: Enterprise-level security protocols


# Key Features

🎓 **Academic Management**

- Comprehensive course administration
- Dynamic curriculum planning
- Automated enrollment processing

👥 **User Management**

- Role-based access control
- Integrated faculty administration
- Student lifecycle management

📊 **Department Operations**

- Resource allocation optimization
- Performance analytics
- Departmental collaboration tools

🔄 **Process Automation**

- Streamlined workflows
- Automated reporting
- Intelligent scheduling

---

# Database Design

## Entity-Relationship Flowchart

```mermaid
graph TD
    %% Entities
    User[USER]:::entity
    Teacher[TEACHER]:::entity
    Student[STUDENT]:::entity
    Admin[ADMIN]:::entity
    Course[COURSE]:::entity
    Department[DEPARTMENT]:::entity

    %% Relationships
    IsTeacherRel{is a}:::relationship
    IsStudentRel{is a}:::relationship
    IsAdminRel{is a}:::relationship
    TeachesRel{TEACHES}:::relationship
    EnrollsRel{ENROLLS}:::relationship
    EmplTeachRel{EMPLOYS}:::relationship
    ContainsRel{CONTAINS}:::relationship
    OffersRel{OFFERS}:::relationship

    %% Entity-Relationship Connections
    User --"1"--- IsTeacherRel
    IsTeacherRel --"1"--- Teacher
    User --"1"--- IsStudentRel
    IsStudentRel --"1"--- Student
    User --"1"--- IsAdminRel
    IsAdminRel --"1"--- Admin

    Department --"1"--- EmplTeachRel
    EmplTeachRel --"M"--- Teacher
    Department --"1"--- ContainsRel
    ContainsRel --"M"--- Student
    Department --"1"--- OffersRel
    OffersRel --"M"--- Course

    Teacher --"M"--- TeachesRel
    TeachesRel --"N"--- Course
    Student --"M"--- EnrollsRel
    EnrollsRel --"N"--- Course

    %% User Attributes
    User --- U_USN[["userSerialNumber"]]:::primaryKey
    User --- U_Name{{"name"}}:::composite
    U_Name --- U_FName((firstName)):::attribute
    U_Name --- U_LName((lastName)):::attribute
    User --- U_Phone((phoneNumber)):::attribute
    User --- U_Gmail((gmail)):::attribute
    User --- U_Pass((password)):::attribute
    User --- U_Birth((birthOfDate)):::attribute
    User --- U_Role((role)):::attribute
    User --- U_Created((createdAt)):::attribute

    %% Department Attributes
    Department --- D_Num[["departmentNumber"]]:::primaryKey
    Department --- D_Name((name)):::attribute
    Department --- D_Desc((description)):::attribute
    Department --- D_Loc((location)):::attribute

    %% Teacher Additional Attributes
    Teacher --- T_Dept((departmentNumber)):::attribute

    %% Student Additional Attributes
    Student --- S_Dept((departmentNumber)):::attribute
    Student --- S_Year((schoolYear)):::attribute
    Student --- S_GPA((GPA)):::attribute
    Student --- S_Status((academicStatus)):::attribute
    Student --- S_Scholar((isScholarship)):::attribute

    %% Course Attributes
    Course --- C_Code[["courseCode"]]:::primaryKey
    Course --- C_Name((name)):::attribute
    Course --- C_Desc((description)):::attribute
    Course --- C_Dept((departmentNumber)):::attribute
    Course --- C_Active((isActive)):::attribute
    Course --- C_Cap((maxCapacity)):::attribute
    Course --- C_Sem((semester)):::attribute
    Course --- C_Year((academicYear)):::attribute
    Course --- C_Type((courseType)):::attribute

    %% Style Classes
    classDef entity fill:#f9f,stroke:#333,stroke-width:4px,rx:0;
    classDef relationship fill:#ccf,stroke:#333,stroke-width:2px;
    classDef attribute fill:#fff,stroke:#333,stroke-width:1px;
    classDef primaryKey fill:#fdd,stroke:#333,stroke-width:2px;
    classDef composite fill:#efe,stroke:#333,stroke-width:2px;
```

## ER Diagram

```mermaid
erDiagram
    USER {
        string userSerialNumber PK
        string firstName
        string lastName
        string phoneNumber
        string gmail
        string password
        date birthOfDate
        string role FK
        datetime createdAt
    }

    DEPARTMENT {
        string departmentNumber PK
        string name
        string description
        string location
    }

    TEACHER {
        string userSerialNumber PK, FK
        string departmentNumber FK
    }

    STUDENT {
        string userSerialNumber PK, FK
        string departmentNumber FK
        int schoolYear
        float GPA
        string academicStatus
        boolean isScholarship
    }

    ADMIN {
        string userSerialNumber PK, FK
    }

    COURSE {
        string courseCode PK
        string name
        string description
        string departmentNumber FK
        boolean isActive
        int maxCapacity
        string semester
        int academicYear
        string courseType
    }

    TEACHER_COURSE {
        string userSerialNumber PK, FK
        string courseCode PK, FK
        string semester
        int academicYear
        string classRoom
        datetime scheduleTime
        int maxStudent
    }

    STUDENT_COURSE {
        string userSerialNumber PK, FK
        string courseCode PK, FK
        float grade
        date enrollmentYear
        float attendanceRate
        string semester
        int academicYear
        date withdrawalDate
    }

    USER ||--o| TEACHER : "is a"
    USER ||--o| STUDENT : "is a"
    USER ||--o| ADMIN : "is a"

    DEPARTMENT ||--|{ TEACHER : "employs"
    DEPARTMENT ||--|{ STUDENT : "contains"
    DEPARTMENT ||--|{ COURSE : "offers"

    TEACHER ||--|{ TEACHER_COURSE : "teaches"
    COURSE ||--|{ TEACHER_COURSE : "taught by"

    STUDENT ||--|{ STUDENT_COURSE : "enrolls"
    COURSE ||--|{ STUDENT_COURSE : "enrolled by"
```

## Database Schema

1. USER Table
   
   ```sql
   CREATE TABLE USER (
   USN VARCHAR(50) PRIMARY KEY,
   first_name VARCHAR(50) NOT NULL,
   last_name VARCHAR(50) NOT NULL,
   phone_number VARCHAR(20),
   gmail VARCHAR(100) UNIQUE NOT NULL,
   password VARCHAR(255) NOT NULL,
   birth_of_date DATE,
   role ENUM('ADMIN', 'TEACHER', 'STUDENT') NOT NULL,
   created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
   );
   ```

2. DEPARTMENT Table
   
   ```sql
   CREATE TABLE DEPARTMENT (
   DSN VARCHAR(20) PRIMARY KEY,
   name VARCHAR(100) NOT NULL UNIQUE,
   description TEXT,
   location VARCHAR(100) NOT NULL
   );
   ```

3. TEACHER Table
   
   ```sql
   CREATE TABLE TEACHER (
   USN VARCHAR(50) PRIMARY KEY,
   DSN VARCHAR(20) NOT NULL,
   FOREIGN KEY (USN) REFERENCES USER(USN),
   FOREIGN KEY (DSN) REFERENCES DEPARTMENT(DSN)
   );
   ```

4. STUDENT Table
   
   ```sql
   CREATE TABLE STUDENT (
   USN VARCHAR(50) PRIMARY KEY,
   DSN VARCHAR(20) NOT NULL,
   school_year INT NOT NULL CHECK (school_year BETWEEN 0 AND 4),
   gpa DECIMAL(3,2) CHECK (gpa >= 0.00 AND gpa <= 4.00),
   academic_status ENUM('ACTIVE', 'PROBATION', 'SUSPENDED', 'GRADUATED') DEFAULT 'ACTIVE',
   is_scholarship BOOLEAN DEFAULT FALSE,
   FOREIGN KEY (USN) REFERENCES USER(USN),
   FOREIGN KEY (DSN) REFERENCES DEPARTMENT(DSN)
   );
   ```

5. ADMIN Table
   
   ```sql
   CREATE TABLE ADMIN (
   USN VARCHAR(50) PRIMARY KEY,
   FOREIGN KEY (USN) REFERENCES USER(USN)
   );
   ```

6. COURSE Table
   
   ```sql
   CREATE TABLE COURSE (
   course_code VARCHAR(20) PRIMARY KEY,
   name VARCHAR(100) NOT NULL,
   description TEXT,
   DSN VARCHAR(20) NOT NULL,
   max_capacity INT NOT NULL CHECK (max_capacity > 0),
   semester ENUM('FALL', 'SPRING', 'SUMMER') NOT NULL,
   academic_year INT NOT NULL CHECK (academic_year >= 2000),
   course_type ENUM('MANDATORY', 'ELECTIVE', 'GENERAL') NOT NULL,
   FOREIGN KEY (DSN) REFERENCES DEPARTMENT(DSN)
   );
   ```

7. TEACHER_COURSE Table
   
   ```sql
   CREATE TABLE TEACHER_COURSE (
   USN VARCHAR(50),
   course_code VARCHAR(20),
   semester ENUM('FALL', 'SPRING', 'SUMMER') NOT NULL,
   academic_year INT NOT NULL CHECK (academic_year >= 2000),
   class_room VARCHAR(50) NOT NULL,
   schedule_time TIMESTAMP NOT NULL,
   max_student INT NOT NULL CHECK (max_student > 0),
   PRIMARY KEY (USN, course_code, semester, academic_year),
   FOREIGN KEY (USN) REFERENCES TEACHER(USN),
   FOREIGN KEY (course_code) REFERENCES COURSE(course_code)
   );
   ```

8. STUDENT_COURSE Table
   
   ```sql
   CREATE TABLE STUDENT_COURSE (
   USN VARCHAR(50),
   course_code VARCHAR(20),
   grade DECIMAL(4,2) CHECK (grade >= 0.00 AND grade <= 100.00),
   enrollment_year DATE NOT NULL,
   attendance_rate DECIMAL(5,2) DEFAULT 0.00 CHECK (attendance_rate >= 0.00 AND attendance_rate <= 100.00),
   semester ENUM('FALL', 'SPRING', 'SUMMER') NOT NULL,
   academic_year INT NOT NULL CHECK (academic_year >= 2000),
   withdrawal_date DATE CHECK (withdrawal_date >= enrollment_year),
   PRIMARY KEY (USN, course_code, semester, academic_year),
   FOREIGN KEY (USN) REFERENCES STUDENT(USN),
   FOREIGN KEY (course_code) REFERENCES COURSE(course_code)
   );
   ```
   
   ### Numeric Constraints
   
   - School Year: 0-4
   - GPA: 0.00-4.00
   - Grade: 0.00-100.00
   - Attendance Rate: 0.00%-100.00%
   - Academic Year: ≥ 2000
   - Maximum Capacity: ≥ 0
   - Maximum Students: ≥ 0

---

# UML Class Diagram

## User Management Diagram

```mermaid
classDiagram
    %% User Management Diagram
    User <|-- Teacher : extends
    User <|-- Student : extends
    User <|-- Admin : extends

    class User {
        <>        -String userSerialNumber        -String firstName        -String lastName        -String phoneNumber        -String gmail        -String password        -LocalDate birthOfDate        -String role        -DateTime createdAt        +String getUserSerialNumber()        +String getRole()        +String getFirstName()        +void setFirstName(String)        +String getLastName()        +void setLastName(String)        +String getPhoneNumber()        +void setPhoneNumber(String)        +String getGmail()        +void setGmail(String)        +String getPassword()        +void setPassword(String)        +LocalDate getBirthOfDate()        +void setBirthOfDate(LocalDate)        +DateTime getCreatedAt()    }    class Teacher {        -String departmentNumber        -List~TeacherCourse~ teachingCourses        +String getDepartmentNumber()        +void setDepartmentNumber(String)        +List~TeacherCourse~ getTeachingCourses()        +void addTeachingCourse(TeacherCourse)        +void removeTeachingCourse(TeacherCourse)    }    class Student {        -String departmentNumber        -int schoolYear        -float GPA        -String academicStatus        -boolean isScholarship        -List~StudentCourse~ enrolledCourses        +String getDepartmentNumber()        +void setDepartmentNumber(String)        +int getSchoolYear()        +void setSchoolYear(int)        +float getGPA()        +void setGPA(float)        +String getAcademicStatus()        +void setAcademicStatus(String)        +boolean getIsScholarship()        +void setIsScholarship(boolean)        +List~StudentCourse~ getEnrolledCourses()        +void addEnrolledCourse(StudentCourse)        +void removeEnrolledCourse(StudentCourse)    }    class Admin {        +String getRole()    }
```

## Academic Structure Diagram

```mermaid
classDiagram
    %% Academic Structure Diagram
    Department "1" --> "*" Teacher : employs
    Department "1" --> "*" Student : contains
    Department "1" --> "*" Course : offers

    class Department {
        -String departmentNumber
        -String name
        -String description
        -String location
        +String getDepartmentNumber()
        +String getName()
        +void setName(String)
        +String getDescription()
        +void setDescription(String)
        +String getLocation()
        +void setLocation(String)
    }

    class Course {
        -String courseCode
        -String name
        -String description
        -String departmentNumber
        -boolean isActive
        -int maxCapacity
        -String semester
        -int academicYear
        -String courseType
        +String getCourseCode()
        +String getName()
        +void setName(String)
        +String getDescription()
        +void setDescription(String)
        +String getDepartmentNumber()
        +void setDepartmentNumber(String)
        +boolean getIsActive()
        +void setIsActive(boolean)
        +int getMaxCapacity()
        +void setMaxCapacity(int)
        +String getSemester()
        +void setSemester(String)
        +int getAcademicYear()
        +void setAcademicYear(int)
        +String getCourseType()
        +void setCourseType(String)
    }
```

## Course Relationships Diagram

```mermaid
classDiagram
    %% Course Relationships Diagram
    Teacher "*" --> "*" Course : teaches
    Student "*" --> "*" Course : enrolls
    Teacher "1" -- "*" TeacherCourse
    Course "1" -- "*" TeacherCourse
    Student "1" -- "*" StudentCourse
    Course "1" -- "*" StudentCourse

    class TeacherCourse {
        -String teacherSerialNumber
        -String courseCode
        -String semester
        -int academicYear
        -String classRoom
        -DateTime scheduleTime
        -int maxStudent
        +String getTeacherSerialNumber()
        +String getCourseCode()
        +String getSemester()
        +int getAcademicYear()
        +String getClassRoom()
        +void setClassRoom(String)
        +DateTime getScheduleTime()
        +void setScheduleTime(DateTime)
        +int getMaxStudent()
        +void setMaxStudent(int)
    }

    class StudentCourse {
        -String studentSerialNumber
        -String courseCode
        -float grade
        -Date enrollmentYear
        -float attendanceRate
        -String semester
        -int academicYear
        -Date withdrawalDate
        +String getStudentSerialNumber()
        +String getCourseCode()
        +float getGrade()
        +void setGrade(float)
        +Date getEnrollmentYear()
        +float getAttendanceRate()
        +void setAttendanceRate(float)
        +String getSemester()
        +int getAcademicYear()
        +Date getWithdrawalDate()
        +void setWithdrawalDate(Date)
    }
```
