package org.example.system.courses;

import org.example.system.models.Course;
import org.example.system.enums.Semester;
import org.example.system.enums.CourseType;
import java.time.LocalDate;
import java.sql.Connection;

public class StudentCourse extends Course {
    private String studentSerialNumber;
    private float grade;
    private LocalDate enrollmentYear;
    private float attendanceRate;
    private LocalDate withdrawalDate;

    public StudentCourse(Connection connection) {
        super(connection);
    }

    public StudentCourse(Connection connection, String courseCode, String name, String description,
                         String departmentNumber, String teacherSerialNumber, boolean isActive,
                         int maxCapacity, Semester semester, int academicYear, CourseType courseType,
                         String studentSerialNumber, float grade, LocalDate enrollmentYear,
                         float attendanceRate, LocalDate withdrawalDate) {
        super(courseCode, name, description, departmentNumber, teacherSerialNumber,
                isActive, maxCapacity, semester, academicYear, courseType);
        this.studentSerialNumber = studentSerialNumber;
        this.grade = grade;
        this.enrollmentYear = enrollmentYear;
        this.attendanceRate = attendanceRate;
        this.withdrawalDate = withdrawalDate;
        validateStudentCourseData();
    }


    // Student-specific getters and setters
    public String getStudentSerialNumber() {
        return studentSerialNumber;
    }

    public void setStudentSerialNumber(String studentSerialNumber) {
        this.studentSerialNumber = studentSerialNumber;
    }

    public float getGrade() {
        return grade;
    }

    public void setGrade(float grade) {
        this.grade = grade;
    }

    public LocalDate getEnrollmentYear() {
        return enrollmentYear;
    }

    public void setEnrollmentYear(LocalDate enrollmentYear) {
        this.enrollmentYear = enrollmentYear;
    }

    public float getAttendanceRate() {
        return attendanceRate;
    }

    public void setAttendanceRate(float attendanceRate) {
        this.attendanceRate = attendanceRate;
    }


    public LocalDate getWithdrawalDate() {
        return withdrawalDate;
    }

    public void setWithdrawalDate(LocalDate withdrawalDate) {
        this.withdrawalDate = withdrawalDate;
    }

    private void validateStudentCourseData() {
        if (grade < 0.0 || grade > 100.0) {
            throw new IllegalArgumentException("Grade must be between 0 and 100");
        }
        if (attendanceRate < 0.0 || attendanceRate > 100.0) {
            throw new IllegalArgumentException("Attendance rate must be between 0 and 100");
        }
        if (withdrawalDate != null && withdrawalDate.isBefore(enrollmentYear)) {
            throw new IllegalArgumentException("Withdrawal date cannot be before enrollment date");
        }
    }
}