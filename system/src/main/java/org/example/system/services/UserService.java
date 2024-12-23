package org.example.system.services;

import org.example.system.users.Admin;
import org.example.system.users.Student;
import org.example.system.users.Teacher;
import org.example.system.models.User;
import org.example.system.enums.Role;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserService {
    private final Connection connection;

    public UserService(Connection connection) {
        this.connection = connection;
    }

    public Optional<User> findByGmail(String gmail) {
        String sql = "SELECT u.*, s.departmentNumber, s.schoolYear, s.GPA, s.academicStatus, s.isScholarship, " +
                "t.departmentNumber as teacherDepartment " +
                "FROM USER u " +
                "LEFT JOIN STUDENT s ON u.userSerialNumber = s.userSerialNumber " +
                "LEFT JOIN TEACHER t ON u.userSerialNumber = t.userSerialNumber " +
                "WHERE u.gmail = ?";
        return getUser(gmail, sql);
    }

    public Optional<User> findBySerialNumber(String serialNumber) {
        String sql = "SELECT u.*, s.departmentNumber, s.schoolYear, s.GPA, s.academicStatus, s.isScholarship, " +
                "t.departmentNumber as teacherDepartment " +
                "FROM USER u " +
                "LEFT JOIN STUDENT s ON u.userSerialNumber = s.userSerialNumber " +
                "LEFT JOIN TEACHER t ON u.userSerialNumber = t.userSerialNumber " +
                "WHERE u.userSerialNumber = ?";
        return getUser(serialNumber, sql);
    }

    private Optional<User> getUser(String value, String sql) {
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, value);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return Optional.of(mapResultSetToUser(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error retrieving user", e);
        }
        return Optional.empty();
    }

    public List<User> findByRole(Role role) {
        List<User> users = new ArrayList<>();
        String sql = "SELECT u.*, s.departmentNumber, s.schoolYear, s.GPA, s.academicStatus, s.isScholarship, " +
                "t.departmentNumber as teacherDepartment " +
                "FROM USER u " +
                "LEFT JOIN STUDENT s ON u.userSerialNumber = s.userSerialNumber " +
                "LEFT JOIN TEACHER t ON u.userSerialNumber = t.userSerialNumber " +
                "WHERE u.role = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, role.toString());
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                users.add(mapResultSetToUser(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding users by role", e);
        }
        return users;
    }

    private User mapResultSetToUser(ResultSet rs) throws SQLException {
        Role role = Role.valueOf(rs.getString("role"));
        User user;

        switch (role) {
            case STUDENT -> {
                Student student = new Student(connection);
                student.setDepartmentNumber(rs.getString("departmentNumber"));
                student.setSchoolYear(rs.getInt("schoolYear"));
                student.setGPA(rs.getDouble("GPA"));
                student.setAcademicStatus(rs.getString("academicStatus"));
                student.setScholarship(rs.getBoolean("isScholarship"));
                user = student;
            }
            case TEACHER -> {
                Teacher teacher = new Teacher(connection);
                teacher.setDepartmentNumber(rs.getString("teacherDepartment"));
                user = teacher;
            }
            case ADMIN -> user = new Admin(connection);
            default -> throw new IllegalArgumentException("Unknown role: " + role);
        }

        user.setUserSerialNumber(rs.getString("userSerialNumber"));
        user.setFirstName(rs.getString("firstName"));
        user.setLastName(rs.getString("lastName"));
        user.setPhoneNumber(rs.getString("phoneNumber"));
        user.setGmail(rs.getString("gmail"));
        user.setPassword(rs.getString("password"));
        user.setBirthOfDate(rs.getTimestamp("birthOfDate").toLocalDateTime());
        user.setCreatedAt(rs.getTimestamp("createdAt").toLocalDateTime());
        user.setRole(role);

        return user;
    }

    private void createStudent(Student student) throws SQLException {
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
    }


    private void createTeacher(Teacher teacher) throws SQLException {
        String sql = "INSERT INTO TEACHER (userSerialNumber, departmentNumber) VALUES (?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, teacher.getUserSerialNumber());
            stmt.setString(2, teacher.getDepartmentNumber());
            stmt.executeUpdate();
        }
    }

    private void createAdmin(Admin admin) throws SQLException {
        String sql = "INSERT INTO ADMIN (userSerialNumber) VALUES (?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, admin.getUserSerialNumber());
            stmt.executeUpdate();
        }
    }

    public void updateUser(String serialNumber, User user) throws SQLException {
        connection.setAutoCommit(false);
        try {
            String sql = "UPDATE USER SET firstName = ?, lastName = ?, phoneNumber = ?, " +
                    "gmail = ?, password = ?, birthOfDate = ? " +
                    "WHERE userSerialNumber = ?";
            try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                stmt.setString(1, user.getFirstName());
                stmt.setString(2, user.getLastName());
                stmt.setString(3, user.getPhoneNumber());
                stmt.setString(4, user.getGmail());
                stmt.setString(5, user.getPassword());
                stmt.setTimestamp(6, java.sql.Timestamp.valueOf(user.getBirthOfDate()));
                stmt.setString(7, serialNumber);
                stmt.executeUpdate();
            }

            switch (user.getRole()) {
                case STUDENT -> updateStudent(serialNumber, (Student) user);
                case TEACHER -> updateTeacher(serialNumber, (Teacher) user);
            }

            connection.commit();
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException ex) {
                throw new RuntimeException("Error rolling back transaction", ex);
            }
            throw new RuntimeException("Error updating user", e);
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException e) {
                throw new RuntimeException("Error resetting auto-commit", e);
            }
        }
    }

    private void updateStudent(String serialNumber, Student student) throws SQLException {
        String sql = "UPDATE STUDENT SET departmentNumber = ?, schoolYear = ?, GPA = ?, " +
                "academicStatus = ?, isScholarship = ? WHERE userSerialNumber = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, student.getDepartmentNumber());
            stmt.setInt(2, student.getSchoolYear());
            stmt.setDouble(3, student.getGPA());
            stmt.setString(4, student.getAcademicStatus());
            stmt.setBoolean(5, student.isScholarship());
            stmt.setString(6, serialNumber);
            stmt.executeUpdate();
        }
    }

    private void updateTeacher(String serialNumber, Teacher teacher) throws SQLException {
        String sql = "UPDATE TEACHER SET departmentNumber = ? WHERE userSerialNumber = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, teacher.getDepartmentNumber());
            stmt.setString(2, serialNumber);
            stmt.executeUpdate();
        }
    }

    public void deleteUser(String serialNumber) {
        String sql = "DELETE FROM USER WHERE userSerialNumber = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, serialNumber);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting user", e);
        }
    }

    public List<User> searchUsers(String query) {
        List<User> users = new ArrayList<>();
        String sql = "SELECT u.*, s.departmentNumber, s.schoolYear, s.GPA, s.academicStatus, s.isScholarship, " +
                "t.departmentNumber as teacherDepartment " +
                "FROM USER u " +
                "LEFT JOIN STUDENT s ON u.userSerialNumber = s.userSerialNumber " +
                "LEFT JOIN TEACHER t ON u.userSerialNumber = t.userSerialNumber " +
                "WHERE u.firstName LIKE ? OR u.lastName LIKE ? OR " +
                "u.gmail LIKE ? OR u.userSerialNumber LIKE ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            String searchPattern = "%" + query + "%";
            stmt.setString(1, searchPattern);
            stmt.setString(2, searchPattern);
            stmt.setString(3, searchPattern);
            stmt.setString(4, searchPattern);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                users.add(mapResultSetToUser(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error searching users", e);
        }
        return users;
    }

    public void changePassword(String serialNumber, String newPassword) {
        String sql = "UPDATE USER SET password = ? WHERE userSerialNumber = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, newPassword); // Should hash password before storing
            stmt.setString(2, serialNumber);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error changing password", e);
        }
    }
}