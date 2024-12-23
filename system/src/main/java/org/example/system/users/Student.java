package org.example.system.users;

import org.example.system.models.User;
import org.example.system.enums.Role;
import org.example.system.enums.AcademicStatus;
import org.example.system.courses.StudentCourse;
import java.util.List;
import java.util.ArrayList;

public class Student extends User {
    private String departmentNumber;
    private int schoolYear;
    private float GPA;
    private AcademicStatus academicStatus;
    private boolean isScholarship;
    private List<StudentCourse> enrolledCourses;

    public Student() {
        super();
        this.enrolledCourses = new ArrayList<>();
        this.academicStatus = AcademicStatus.ACTIVE; // Default status
    }

    @Override
    public Role getRole() {
        return Role.STUDENT;
    }

    public String getDepartmentNumber() {
        return departmentNumber;
    }

    public void setDepartmentNumber(String departmentNumber) {
        this.departmentNumber = departmentNumber;
    }

    public int getSchoolYear() {
        return schoolYear;
    }

    public void setSchoolYear(int schoolYear) {
        if (schoolYear < 0 || schoolYear > 4) {
            throw new IllegalArgumentException("School year must be between 0 and 4");
        }
        this.schoolYear = schoolYear;
    }

    public float getGPA() {
        return GPA;
    }

    public void setGPA(float GPA) {
        if (GPA < 0.0 || GPA > 4.0) {
            throw new IllegalArgumentException("GPA must be between 0.0 and 4.0");
        }
        this.GPA = GPA;
    }

    public AcademicStatus getAcademicStatus() {
        return academicStatus;
    }

    public void setAcademicStatus(AcademicStatus academicStatus) {
        this.academicStatus = academicStatus;
    }

    public boolean isScholarship() {
        return isScholarship;
    }

    public void setScholarship(boolean scholarship) {
        isScholarship = scholarship;
    }

    public List<StudentCourse> getEnrolledCourses() {
        return new ArrayList<>(enrolledCourses);
    }

    public void addEnrolledCourse(StudentCourse course) {
        if (course != null) {
            enrolledCourses.add(course);
        }
    }

    public void removeEnrolledCourse(StudentCourse course) {
        if (course != null) {
            enrolledCourses.remove(course);
        }
    }

    // Helper method for AcademicStatus conversion
    public void setAcademicStatusFromString(String status) {
        try {
            this.academicStatus = AcademicStatus.valueOf(status.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid academic status: " + status);
        }
    }

    public String getAcademicStatusAsString() {
        return academicStatus != null ? academicStatus.name() : null;
    }
}