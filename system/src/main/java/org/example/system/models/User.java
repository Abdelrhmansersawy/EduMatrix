package org.example.system.models;

import org.example.system.enums.Role;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDateTime;

public abstract class User {
    private String userSerialNumber;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String gmail;
    private String password;
    private LocalDateTime birthOfDate;
    private LocalDateTime createdAt;
    private Role role;

    protected Connection connection;

    public User() {}

    public User(Connection connection) {
        this.connection = connection;
    }

    public String getUserSerialNumber() {
        return userSerialNumber;
    }

    public void setUserSerialNumber(String userSerialNumber) {
        this.userSerialNumber = userSerialNumber;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        String sql = "UPDATE USER SET firstName = ? WHERE userSerialNumber = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, firstName);
            stmt.setString(2, this.userSerialNumber);
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                this.firstName = firstName;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error updating firstName", e);
        }
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        String sql = "UPDATE USER SET lastName = ? WHERE userSerialNumber = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, lastName);
            stmt.setString(2, this.userSerialNumber);
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                this.lastName = lastName;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error updating lastName", e);
        }
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        String sql = "UPDATE USER SET phoneNumber = ? WHERE userSerialNumber = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, phoneNumber);
            stmt.setString(2, this.userSerialNumber);
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                this.phoneNumber = phoneNumber;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error updating phoneNumber", e);
        }
    }

    public String getGmail() {
        return gmail;
    }

    public void setGmail(String gmail) {
        String sql = "UPDATE USER SET gmail = ? WHERE userSerialNumber = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, gmail);
            stmt.setString(2, this.userSerialNumber);
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                this.gmail = gmail;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error updating gmail", e);
        }
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        String sql = "UPDATE USER SET password = ? WHERE userSerialNumber = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, password); // Should hash password before storing
            stmt.setString(2, this.userSerialNumber);
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                this.password = password;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error updating password", e);
        }
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        String sql = "UPDATE USER SET role = ? WHERE userSerialNumber = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, role.name());
            stmt.setString(2, this.userSerialNumber);
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                this.role = role;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error updating role", e);
        }
    }

    public LocalDateTime getBirthOfDate() {
        return birthOfDate;
    }

    public void setBirthOfDate(LocalDateTime birthOfDate) {
        String sql = "UPDATE USER SET birthOfDate = ? WHERE userSerialNumber = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setTimestamp(1, java.sql.Timestamp.valueOf(birthOfDate));
            stmt.setString(2, this.userSerialNumber);
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                this.birthOfDate = birthOfDate;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error updating birthOfDate", e);
        }
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public void loadUser(String userSerialNumber) {
        String sql = "SELECT * FROM USER WHERE userSerialNumber = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, userSerialNumber);
            var rs = stmt.executeQuery();
            if (rs.next()) {
                this.userSerialNumber = rs.getString("userSerialNumber");
                this.firstName = rs.getString("firstName");
                this.lastName = rs.getString("lastName");
                this.phoneNumber = rs.getString("phoneNumber");
                this.gmail = rs.getString("gmail");
                this.password = rs.getString("password");
                this.birthOfDate = rs.getTimestamp("birthOfDate").toLocalDateTime();
                this.createdAt = rs.getTimestamp("createdAt").toLocalDateTime();
                this.role = Role.valueOf(rs.getString("role"));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error loading user", e);
        }
    }
}