package org.example.system.users;

import org.example.system.enums.CourseType;
import org.example.system.enums.Semester;
import org.example.system.models.User;
import org.example.system.enums.Role;
import org.example.system.enums.AcademicStatus;
import org.example.system.courses.StudentCourse;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.ArrayList;

public class Student extends User {
    private String departmentNumber;
    private int schoolYear;
    private double GPA;
    private AcademicStatus academicStatus;
    private boolean isScholarship;
    private List<StudentCourse> enrolledCourses;

    public Student(Connection connection) {
        super(connection);
        this.enrolledCourses = new ArrayList<>();
        this.academicStatus = AcademicStatus.ACTIVE;
    }

    @Override
    public Role getRole() {
        return Role.STUDENT;
    }

    public String getDepartmentNumber() {
        return departmentNumber;
    }

    public void setDepartmentNumber(String departmentNumber) {
        String sql = "UPDATE STUDENT SET departmentNumber = ? WHERE userSerialNumber = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, departmentNumber);
            stmt.setString(2, getUserSerialNumber());
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                this.departmentNumber = departmentNumber;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error updating department number", e);
        }
    }

    public int getSchoolYear() {
        return schoolYear;
    }

    public void setSchoolYear(int schoolYear) {
        if (schoolYear < 0 || schoolYear > 4) {
            throw new IllegalArgumentException("School year must be between 0 and 4");
        }
        String sql = "UPDATE STUDENT SET schoolYear = ? WHERE userSerialNumber = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, schoolYear);
            stmt.setString(2, getUserSerialNumber());
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                this.schoolYear = schoolYear;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error updating school year", e);
        }
    }

    public double getGPA() {
        return GPA;
    }

    public void setGPA(double GPA) {
        if (GPA < 0.0 || GPA > 4.0) {
            throw new IllegalArgumentException("GPA must be between 0.0 and 4.0");
        }
        String sql = "UPDATE STUDENT SET GPA = ? WHERE userSerialNumber = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setDouble(1, GPA);
            stmt.setString(2, getUserSerialNumber());
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                this.GPA = GPA;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error updating GPA", e);
        }
    }

    public String getAcademicStatus() {
        return academicStatus.toString();
    }

    public void setAcademicStatus(AcademicStatus academicStatus) {
        String sql = "UPDATE STUDENT SET academicStatus = ? WHERE userSerialNumber = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, academicStatus.name());
            stmt.setString(2, getUserSerialNumber());
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                this.academicStatus = academicStatus;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error updating academic status", e);
        }
    }

    public boolean isScholarship() {
        return isScholarship;
    }

    public void setScholarship(boolean scholarship) {
        String sql = "UPDATE STUDENT SET isScholarship = ? WHERE userSerialNumber = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setBoolean(1, scholarship);
            stmt.setString(2, getUserSerialNumber());
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                this.isScholarship = scholarship;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error updating scholarship status", e);
        }
    }

    public List<StudentCourse> getEnrolledCourses() {
        return new ArrayList<>(enrolledCourses);
    }

    public void loadEnrolledCourses() {
        String sql = "SELECT * FROM STUDENT_COURSE WHERE userSerialNumber = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, getUserSerialNumber());
            var rs = stmt.executeQuery();
            enrolledCourses.clear();
            while (rs.next()) {
                StudentCourse course = new StudentCourse(
                        connection,
                        rs.getString("courseCode"),
                        rs.getString("name"),
                        rs.getString("description"),
                        rs.getString("departmentNumber"),
                        rs.getString("teacherSerialNumber"),
                        rs.getBoolean("isActive"),
                        rs.getInt("maxCapacity"),
                        Semester.valueOf(rs.getString("semester")),
                        rs.getInt("academicYear"),
                        CourseType.valueOf(rs.getString("courseType")),
                        getUserSerialNumber(),
                        rs.getFloat("grade"),
                        rs.getDate("enrollmentYear").toLocalDate(),
                        rs.getFloat("attendanceRate"),
                        rs.getDate("withdrawalDate") != null ?
                                rs.getDate("withdrawalDate").toLocalDate() : null
                );
                enrolledCourses.add(course);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error loading enrolled courses", e);
        }
    }

    public void setAcademicStatus(String status) {
        setAcademicStatus(AcademicStatus.valueOf(status.toUpperCase()));
    }

    protected void setStudentData(String departmentNumber, int schoolYear, double GPA,
                                  String academicStatus, boolean isScholarship) {
        this.departmentNumber = departmentNumber;
        this.schoolYear = schoolYear;
        this.GPA = GPA;
        this.academicStatus = AcademicStatus.valueOf(academicStatus);
        this.isScholarship = isScholarship;
    }
}