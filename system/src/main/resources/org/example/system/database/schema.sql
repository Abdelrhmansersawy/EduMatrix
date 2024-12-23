-- Create database
CREATE DATABASE IF NOT EXISTS student_management_system;
USE student_management_system;

-- Create USER table
CREATE TABLE USER (
    userSerialNumber VARCHAR(50) PRIMARY KEY,
    firstName VARCHAR(50) NOT NULL,
    lastName VARCHAR(50) NOT NULL,
    phoneNumber VARCHAR(20),
    gmail VARCHAR(100) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    birthOfDate DATE,
    role ENUM('ADMIN', 'TEACHER', 'STUDENT') NOT NULL,
    createdAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Create DEPARTMENT table
CREATE TABLE DEPARTMENT (
    departmentNumber VARCHAR(20) PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE,
    description TEXT,
    location VARCHAR(100) NOT NULL
);

-- Create TEACHER table
CREATE TABLE TEACHER (
    userSerialNumber VARCHAR(50) PRIMARY KEY,
    departmentNumber VARCHAR(20) NOT NULL,
    FOREIGN KEY (userSerialNumber) REFERENCES USER(userSerialNumber),
    FOREIGN KEY (departmentNumber) REFERENCES DEPARTMENT(departmentNumber)
);

-- Create STUDENT table
CREATE TABLE STUDENT (
    userSerialNumber VARCHAR(50) PRIMARY KEY,
    departmentNumber VARCHAR(20) NOT NULL,
    schoolYear INT NOT NULL CHECK (schoolYear BETWEEN 0 AND 4),
    GPA DECIMAL(3,2) CHECK (GPA >= 0.00 AND GPA <= 4.00),
    academicStatus ENUM('ACTIVE', 'PROBATION', 'SUSPENDED', 'GRADUATED') DEFAULT 'ACTIVE',
    isScholarship BOOLEAN DEFAULT FALSE,
    FOREIGN KEY (userSerialNumber) REFERENCES USER(userSerialNumber),
    FOREIGN KEY (departmentNumber) REFERENCES DEPARTMENT(departmentNumber)
);

-- Create ADMIN table
CREATE TABLE ADMIN (
    userSerialNumber VARCHAR(50) PRIMARY KEY,
    FOREIGN KEY (userSerialNumber) REFERENCES USER(userSerialNumber)
);

-- Create COURSE table
CREATE TABLE COURSE (
    courseCode VARCHAR(20) PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    description TEXT,
    departmentNumber VARCHAR(20) NOT NULL,
    teacherSerialNumber VARCHAR(50) NOT NULL,
    isActive BOOLEAN DEFAULT TRUE,
    maxCapacity INT NOT NULL CHECK (maxCapacity > 0),
    semester ENUM('FALL', 'SPRING', 'SUMMER') NOT NULL,
    academicYear INT NOT NULL CHECK (academicYear >= 2000),
    courseType ENUM('MANDATORY', 'ELECTIVE', 'GENERAL') NOT NULL,
    FOREIGN KEY (departmentNumber) REFERENCES DEPARTMENT(departmentNumber),
    FOREIGN KEY (teacherSerialNumber) REFERENCES TEACHER(userSerialNumber)
);

-- Create STUDENT_COURSE table
CREATE TABLE STUDENT_COURSE (
    userSerialNumber VARCHAR(50),
    courseCode VARCHAR(20),
    grade DECIMAL(4,2) CHECK (grade >= 0.00 AND grade <= 100.00),
    enrollmentYear DATE NOT NULL,
    attendanceRate DECIMAL(5,2) DEFAULT 0.00 CHECK (attendanceRate >= 0.00 AND attendanceRate <= 100.00),
    semester ENUM('FALL', 'SPRING', 'SUMMER') NOT NULL,
    academicYear INT NOT NULL CHECK (academicYear >= 2000),
    withdrawalDate DATE CHECK (withdrawalDate >= enrollmentYear),
    PRIMARY KEY (userSerialNumber, courseCode, semester, academicYear),
    FOREIGN KEY (userSerialNumber) REFERENCES STUDENT(userSerialNumber),
    FOREIGN KEY (courseCode) REFERENCES COURSE(courseCode)
);
