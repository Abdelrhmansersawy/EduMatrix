package org.example.system.users;

import org.example.system.models.Department;
import org.example.system.models.User;
import org.example.system.enums.*;
import org.example.system.courses.StudentCourse;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.ArrayList;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Student extends User {
    private String departmentName;
    private String departmentNumber;
    private int schoolYear;
    private double GPA;
    private AcademicStatus academicStatus;
    private boolean isScholarship;
    private List<StudentCourse> enrolledCourses;
    private static final Logger LOGGER = Logger.getLogger(Student.class.getName());
    public Student(Connection connection) {
        super(connection);
        this.enrolledCourses = new ArrayList<>();
        this.academicStatus = AcademicStatus.ACTIVE;
    }
    public Student(Connection connection, String firstName, String lastName, String phoneNumber,
                   String gmail, String password, LocalDateTime birthOfDate,
                   String departmentName, int schoolYear) {
        super(connection, firstName, lastName, phoneNumber, gmail, password, birthOfDate, Role.STUDENT);

        // Set student-specific attributes
        this.departmentName = departmentName;
        this.departmentNumber = Department.getDepartmentNumber(connection, departmentName);
        this.schoolYear = schoolYear;
        this.GPA = 0.0; // Default initial GPA
        this.academicStatus = AcademicStatus.ACTIVE;
        this.isScholarship = false;
        this.enrolledCourses = new ArrayList<>();

    }


    @Override
    public Role getRole() {
        return Role.STUDENT;
    }

    public String getDepartmentNumber() {
        return departmentNumber;
    }
    public String getDepartmentName() {
        return departmentName;
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

    public Optional<List<StudentCourse>> getEnrolledCourses() {
        if(enrolledCourses.isEmpty()) loadEnrolledCourses();
        return Optional.of(enrolledCourses);
    }


    public void loadEnrolledCourses() {
        if (connection == null) {
            throw new IllegalStateException("Database connection is null");
        }

        String sql = """
            SELECT sc.*, c.name, c.description, c.departmentNumber, c.teacherSerialNumber, 
                   c.isActive, c.maxCapacity, c.semester, c.academicYear, c.courseType 
            FROM STUDENT_COURSE sc
            JOIN COURSE c ON sc.courseCode = c.courseCode
            WHERE sc.userSerialNumber = ?
        """;

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, getUserSerialNumber());
            var rs = stmt.executeQuery();

            enrolledCourses.clear();
            while (rs.next()) {
                try {
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
                            rs.getDate("enrollmentYear") != null ?
                                    rs.getDate("enrollmentYear").toLocalDate() : null,
                            rs.getFloat("attendanceRate"),
                            rs.getDate("withdrawalDate") != null ?
                                    rs.getDate("withdrawalDate").toLocalDate() : null
                    );
                    enrolledCourses.add(course);
                } catch (IllegalArgumentException e) {
                    LOGGER.log(Level.WARNING,
                            "Skipping invalid course data: " + rs.getString("courseCode"), e);
                    continue;
                }
            }
        } catch (SQLException e) {
            String errorMsg = "Error loading enrolled courses for student: " + getUserSerialNumber();
            throw new RuntimeException(errorMsg, e);
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
    @Override
    public String toString() {
        return "Student{" +
                super.toString() +
                ", departmentNumber='" + departmentNumber + '\'' +
                ", schoolYear=" + schoolYear +
                ", GPA=" + GPA +
                ", academicStatus=" + academicStatus +
                ", isScholarship=" + isScholarship +
                ", enrolledCourses.size=" + enrolledCourses.size() +
                '}';
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public void setGrade(double grade) {
        this.GPA = grade;
    }
}