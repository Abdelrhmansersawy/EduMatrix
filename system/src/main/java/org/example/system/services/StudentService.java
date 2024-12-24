package org.example.system.services;

import org.example.system.repositories.CourseRepository;
import org.example.system.repositories.StudentRepository;
import org.example.system.users.Student;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class StudentService implements AutoCloseable {
    private final StudentRepository studentRepository;
    private final Connection connection;

    public StudentService(Connection connection) {
        this.connection = connection;
        this.studentRepository = new StudentRepository(connection);
    }

    public void registerStudent(Student student) throws SQLException {
        try {
            connection.setAutoCommit(false);
            student.createNewUser();
            String sql = "INSERT INTO STUDENT (userSerialNumber, departmentNumber, schoolYear, GPA, academicStatus, isScholarship) " +
                    "VALUES (?, ?, ?, ?, ?, ?)";

            try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                stmt.setString(1, student.getUserSerialNumber());
                stmt.setString(2, student.getDepartmentNumber());
                stmt.setInt(3, student.getSchoolYear());
                stmt.setDouble(4, student.getGPA());
                stmt.setString(5, student.getAcademicStatus());
                stmt.setBoolean(6, student.isScholarship());
                stmt.executeUpdate();
            }

            connection.commit();
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException rollbackEx) {
                throw new RuntimeException("Error rolling back transaction", rollbackEx);
            }
            throw new RuntimeException("Error creating student: " + e.getMessage(), e);
        }
    }

    @Override
    public void close() throws Exception {
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }
}
