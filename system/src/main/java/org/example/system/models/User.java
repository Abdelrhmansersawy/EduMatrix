package org.example.system.models;

import org.example.system.enums.Role;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;

public abstract class User {
    private String userSerialNumber;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String gmail;
    private String password;
    private LocalDate birthOfDate;
    private LocalDate createdAt;

    protected Connection connection; // For database operations

    public User() {}

    public User(Connection connection) {
        this.connection = connection;
    }

    public String getUserSerialNumber() {
        return userSerialNumber;
    }

    abstract public Role getRole();

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
            e.printStackTrace();
            throw new RuntimeException("Error updating firstName");
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
            e.printStackTrace();
            throw new RuntimeException("Error updating lastName");
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
            e.printStackTrace();
            throw new RuntimeException("Error updating phoneNumber");
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
            e.printStackTrace();
            throw new RuntimeException("Error updating gmail");
        }
    }

    public void setPassword(String password) {
        String sql = "UPDATE USER SET password = ? WHERE userSerialNumber = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            // In a real application, you should hash the password before storing it
            stmt.setString(1, password);
            stmt.setString(2, this.userSerialNumber);
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                this.password = password;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error updating password");
        }
    }

    public LocalDate getBirthOfDate() {
        return birthOfDate;
    }

    public void setBirthOfDate(LocalDate birthOfDate) {
        String sql = "UPDATE USER SET birthOfDate = ? WHERE userSerialNumber = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setObject(1, birthOfDate);
            stmt.setString(2, this.userSerialNumber);
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                this.birthOfDate = birthOfDate;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error updating birthOfDate");
        }
    }

    public LocalDate getCreated() {
        return createdAt;
    }

    public String getPassword() {
        return password;
    }

    // Protected setters for use by factory methods or builders
    protected void setUserSerialNumber(String userSerialNumber) {
        this.userSerialNumber = userSerialNumber;
    }

    protected void setCreatedAt(LocalDate createdAt) {
        this.createdAt = createdAt;
    }
}