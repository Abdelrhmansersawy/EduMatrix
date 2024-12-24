package org.example.system.controllers;

public class StudentRecord {
    private final String id;
    private final String fullName;
    private final int schoolYear;
    private final double gpa;
    private final double grade;
    private final double attendanceRate;
    private final String departmentName;

    public StudentRecord(String id, String fullName, int schoolYear,
                         double gpa, double grade, double attendanceRate,
                         String departmentName) {
        this.id = id;
        this.fullName = fullName;
        this.schoolYear = schoolYear;
        this.gpa = gpa;
        this.grade = grade;
        this.attendanceRate = attendanceRate;
        this.departmentName = departmentName;
    }

    // Public getters
    public String getId() { return id; }
    public String getFullName() { return fullName; }
    public int getSchoolYear() { return schoolYear; }
    public double getGpa() { return gpa; }
    public double getGrade() { return grade; }
    public double getAttendanceRate() { return attendanceRate; }
    public String getDepartmentName() { return departmentName; }
    @Override
    public String toString() {
        return String.format("StudentRecord{id='%s', name='%s', year=%d, gpa=%.2f, grade=%.2f, attendance=%.1f, dept='%s'}",
                id, fullName, schoolYear, gpa, grade, attendanceRate, departmentName);
    }
}