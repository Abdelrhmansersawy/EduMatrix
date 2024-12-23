package org.example.system.courses;

import org.example.system.models.Course;
import org.example.system.enums.Semester;
import java.time.LocalDate;

public class StudentCourse extends Course {
    private String studentSerialNumber;
    private float grade;
    private LocalDate enrollmentYear;
    private float attendanceRate;
    private Semester semester;
    private int academicYear;
    private LocalDate withdrawalDate;

    public StudentCourse() {
        super();
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

    @Override
    public Semester getSemester() {
        return semester;
    }

    @Override
    public void setSemester(Semester semester) {
        this.semester = semester;
    }

    @Override
    public int getAcademicYear() {
        return academicYear;
    }

    @Override
    public void setAcademicYear(int academicYear) {
        this.academicYear = academicYear;
    }

    public LocalDate getWithdrawalDate() {
        return withdrawalDate;
    }

    public void setWithdrawalDate(LocalDate withdrawalDate) {
        this.withdrawalDate = withdrawalDate;
    }

    // Helper method for enum conversion
    public void setSemesterFromString(String semesterStr) {
        try {
            this.semester = Semester.valueOf(semesterStr.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid semester value: " + semesterStr);
        }
    }

    public String getSemesterAsString() {
        return semester != null ? semester.name() : null;
    }
}