package org.example.system.services;

import org.example.system.models.User;
import org.example.system.enums.Role;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserService {
    private final Connection connection;

    public UserService(Connection connection) {
        this.connection = connection;
    }

    public Optional<User> findByGmail(String gmail) {
        String sql = "SELECT * FROM USER WHERE gmail = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, gmail);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return Optional.of(mapResultSetToUser(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    public Optional<User> findBySerialNumber(String serialNumber) {
        String sql = "SELECT * FROM USER WHERE userSerialNumber = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, serialNumber);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return Optional.of(mapResultSetToUser(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    public List<User> findByRole(Role role) {
        List<User> users = new ArrayList<>();
        String sql = "SELECT * FROM USER WHERE role = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, role.toString());
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                users.add(mapResultSetToUser(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }

    public void createUser(User user) {
        String sql = "INSERT INTO USER (userSerialNumber, firstName, lastName, phoneNumber, " +
                "gmail, password, birthOfDate, role, createdAt) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, NOW())";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, user.getUserSerialNumber());
            stmt.setString(2, user.getFirstName());
            stmt.setString(3, user.getLastName());
            stmt.setString(4, user.getPhoneNumber());
            stmt.setString(5, user.getGmail());
            stmt.setString(6, user.getPassword());
            stmt.setObject(7, user.getBirthOfDate());
            stmt.setString(8, user.getRole().toString());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new IllegalArgumentException("Error creating user");
        }
    }

    public void updateUser(String serialNumber, User user) {
        String sql = "UPDATE USER SET firstName = ?, lastName = ?, phoneNumber = ?, " +
                "gmail = ?, password = ?, birthOfDate = ? " +
                "WHERE userSerialNumber = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, user.getFirstName());
            stmt.setString(2, user.getLastName());
            stmt.setString(3, user.getPhoneNumber());
            stmt.setString(4, user.getGmail());
            stmt.setString(5, user.getPassword());
            stmt.setObject(6, user.getBirthOfDate());
            stmt.setString(7, serialNumber);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new IllegalArgumentException("Error updating user");
        }
    }

    public void deleteUser(String serialNumber) {
        String sql = "DELETE FROM USER WHERE userSerialNumber = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, serialNumber);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new IllegalArgumentException("Error deleting user");
        }
    }

    public List<User> searchUsers(String query) {
        List<User> users = new ArrayList<>();
        String sql = "SELECT * FROM USER WHERE " +
                "firstName LIKE ? OR lastName LIKE ? OR " +
                "gmail LIKE ? OR userSerialNumber LIKE ?";
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
            e.printStackTrace();
        }
        return users;
    }

    private User mapResultSetToUser(ResultSet rs) throws SQLException {
        // This method needs to be implemented based on your User factory or creation method
        // Since User is abstract, you'll need to create the appropriate concrete class
        // based on the role from the database
        Role role = Role.valueOf(rs.getString("role"));
        // Create and return appropriate User subclass based on role
        return null; // Placeholder - implement actual mapping
    }

    public void changePassword(String serialNumber, String newPassword) {
        String sql = "UPDATE USER SET password = ? WHERE userSerialNumber = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, newPassword);
            stmt.setString(2, serialNumber);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new IllegalArgumentException("Error changing password");
        }
    }
}