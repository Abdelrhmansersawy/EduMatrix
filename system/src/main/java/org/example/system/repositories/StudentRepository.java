package org.example.system.repositories;
import org.example.system.courses.StudentCourse;
import org.example.system.enums.CourseType;
import org.example.system.enums.Semester;
import org.example.system.exceptions.DatabaseException;
import org.example.system.users.Student;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

public class StudentRepository {
    private final Connection connection;
    private static final Logger LOGGER = Logger.getLogger(StudentRepository.class.getName());

    public StudentRepository(Connection connection) {
        this.connection = connection;
    }

    public void save(Student student) throws SQLException {
        String sql = """
            INSERT INTO STUDENT 
                (userSerialNumber, departmentNumber, schoolYear, GPA, 
                academicStatus, isScholarship)
            VALUES (?, ?, ?, ?, ?, ?)
            """;

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, student.getUserSerialNumber());
            stmt.setString(2, student.getDepartmentNumber());
            stmt.setInt(3, student.getSchoolYear());
            stmt.setDouble(4, student.getGPA());
            stmt.setString(5, student.getAcademicStatus());
            stmt.setBoolean(6, student.isScholarship());
            stmt.executeUpdate();
        }
    }
    public void update(Student student) {
        String sql = "UPDATE STUDENT SET GPA = ?, departmentNumber = ?, schoolYear = ?, " +
                "academicStatus = ?, isScholarship = ? WHERE userSerialNumber = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setDouble(1, student.getGPA());
            stmt.setString( 2, student.getDepartmentNumber());
            stmt.setInt(3, student.getSchoolYear());
            stmt.setString(4, student.getAcademicStatus());
            stmt.setBoolean(5, student.isScholarship());
            stmt.setString(6, student.getUserSerialNumber());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException("Error saving student", e);
        }


        // TODO: Update user data of student
    }
    public List<StudentCourse> findEnrolledCourses(String userSerialNumber) {
        String sql = """
            SELECT sc.*, c.name, c.description, c.departmentNumber, c.teacherSerialNumber, 
                   c.isActive, c.maxCapacity, c.semester, c.academicYear, c.courseType 
            FROM STUDENT_COURSE sc
            JOIN COURSE c ON sc.courseCode = c.courseCode
            WHERE sc.userSerialNumber = ?
        """;

        List<StudentCourse> studentCourses = new ArrayList<>();
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, userSerialNumber);
            var rs = stmt.executeQuery();
            while (rs.next()) {
                try {
                    studentCourses.add(mapResultSetToStudentCourse(rs, userSerialNumber));
                } catch (IllegalArgumentException e) {
                    LOGGER.log(Level.WARNING,
                            "Skipping invalid course data: " + rs.getString("courseCode"), e);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(
                    "Error loading enrolled courses for student: " + userSerialNumber, e
            );
        }
        return studentCourses;
    }
    private StudentCourse mapResultSetToStudentCourse(ResultSet rs, String userSerialNumber) throws SQLException {
        return new StudentCourse(
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
                userSerialNumber,
                rs.getFloat("grade"),
                Optional.ofNullable(rs.getDate("enrollmentYear"))
                        .map(Date::toLocalDate)
                        .orElse(null),
                rs.getFloat("attendanceRate"),
                Optional.ofNullable(rs.getDate("withdrawalDate"))
                        .map(Date::toLocalDate)
                        .orElse(null)
        );
    }
}