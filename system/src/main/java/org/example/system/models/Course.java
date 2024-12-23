package org.example.system.models;

import org.example.system.enums.CourseType;
import org.example.system.enums.Semester;
import java.time.LocalDateTime;

public class Course {
    private String courseCode;
    private String name;
    private String description;
    private String departmentNumber;
    private boolean isActive;
    private int maxCapacity;
    private Semester semester;
    private int academicYear;
    private CourseType courseType;
    private String classRoom;
    private LocalDateTime scheduleTime;

    // Getters and Setters
    public String getCourseCode() {
        return courseCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDepartmentNumber() {
        return departmentNumber;
    }

    public void setDepartmentNumber(String departmentNumber) {
        this.departmentNumber = departmentNumber;
    }

    public boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
    }

    public int getMaxCapacity() {
        return maxCapacity;
    }

    public void setMaxCapacity(int maxCapacity) {
        this.maxCapacity = maxCapacity;
    }

    public Semester getSemester() {
        return semester;
    }

    public void setSemester(Semester semester) {
        this.semester = semester;
    }

    public int getAcademicYear() {
        return academicYear;
    }

    public void setAcademicYear(int academicYear) {
        this.academicYear = academicYear;
    }

    public CourseType getCourseType() {
        return courseType;
    }

    public void setCourseType(CourseType courseType) {
        this.courseType = courseType;
    }

    public String getClassRoom() {
        return classRoom;
    }

    public void setClassRoom(String classRoom) {
        this.classRoom = classRoom;
    }

    public LocalDateTime getScheduleTime() {
        return scheduleTime;
    }

    public void setScheduleTime(LocalDateTime scheduleTime) {
        this.scheduleTime = scheduleTime;
    }

    // Additional helper methods for enum conversions
    public void setSemesterFromString(String semesterStr) {
        try {
            this.semester = Semester.valueOf(semesterStr.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid semester value: " + semesterStr);
        }
    }

    public void setCourseTypeFromString(String courseTypeStr) {
        try {
            this.courseType = CourseType.valueOf(courseTypeStr.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid course type value: " + courseTypeStr);
        }
    }

    public String getSemesterAsString() {
        return semester != null ? semester.name() : null;
    }

    public String getCourseTypeAsString() {
        return courseType != null ? courseType.name() : null;
    }
}