package org.example.system.services;

import org.example.system.users.Student;
import org.example.system.users.Teacher;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;

public class TeacherRegistrationService implements AutoCloseable {
    private final Connection connection;

    public TeacherRegistrationService(Connection connection) {
        this.connection = connection;
    }

    public void registerTeacher(Teacher teacher) throws SQLException {
        try {
            connection.setAutoCommit(false);

            teacher.createNewUser();

            String sql = "INSERT INTO TEACHER (userSerialNumber, departmentNumber) " +
                    "VALUES (?, ?)";

            try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                stmt.setString(1, teacher.getUserSerialNumber());
                stmt.setString(2, teacher.getDepartmentNumber());
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