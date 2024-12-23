package org.example.system.services;

import org.example.system.users.Student;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;

public class StudentRegistrationService implements AutoCloseable {
    private final Connection connection;

    public StudentRegistrationService(Connection connection) {
        this.connection = connection;
    }

    public void registerStudent(Student student) throws SQLException {
        connection.setAutoCommit(false);
        try {
            String userSql = "INSERT INTO USER (userSerialNumber, firstName, lastName, phoneNumber, gmail, password, birthOfDate, createdAt, role) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
            String studentSql = "INSERT INTO STUDENT (userSerialNumber, departmentNumber, schoolYear, GPA, academicStatus, isScholarship) VALUES (?, ?, ?, ?, ?, ?)";

            String serialNumber = generateStudentSerialNumber();
            student.setUserSerialNumber(serialNumber);

            try (PreparedStatement userStmt = connection.prepareStatement(userSql);
                 PreparedStatement studentStmt = connection.prepareStatement(studentSql)) {

                // Insert into USER table
                userStmt.setString(1, serialNumber);
                userStmt.setString(2, student.getFirstName());
                userStmt.setString(3, student.getLastName());
                userStmt.setString(4, student.getPhoneNumber());
                userStmt.setString(5, student.getGmail());
                userStmt.setString(6, student.getPassword());
                userStmt.setTimestamp(7, Timestamp.valueOf(student.getBirthOfDate()));
                userStmt.setTimestamp(8, Timestamp.valueOf(java.time.LocalDateTime.now()));
                userStmt.setString(9, student.getRole().name());
                userStmt.executeUpdate();

                // Insert into STUDENT table
                studentStmt.setString(1, serialNumber);
                studentStmt.setString(2, student.getDepartmentNumber());
                studentStmt.setInt(3, student.getSchoolYear());
                studentStmt.setDouble(4, 0.0); // Initial GPA
                studentStmt.setString(5, "ACTIVE"); // Initial status
                studentStmt.setBoolean(6, false); // Initial scholarship status
                studentStmt.executeUpdate();

                connection.commit();
            }
        } catch (Exception e) {
            connection.rollback();
            throw new SQLException("Failed to register student: " + e.getMessage());
        } finally {
            connection.setAutoCommit(true);
        }
    }

    private String generateStudentSerialNumber() throws SQLException {
        String sql = "SELECT COUNT(*) FROM STUDENT";
        try (var stmt = connection.prepareStatement(sql)) {
            var rs = stmt.executeQuery();
            rs.next();
            int count = rs.getInt(1);
            return String.format("STU%06d", count + 1);
        }
    }

    @Override
    public void close() throws Exception {
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }
}