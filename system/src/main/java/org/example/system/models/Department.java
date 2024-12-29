package org.example.system.models;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Department {
    private String departmentNumber;
    private String name;
    private String description;
    private String location;
    private Connection connection;
    // Getters and Setters
    public String getDepartmentNumber() {
        return departmentNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void loadDepartmentByName(String departmentName) {
        String sql = "SELECT * FROM DEPARTMENT WHERE name = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, departmentName);
            var rs = stmt.executeQuery();

            if (rs.next()) {
                String newDepartmentNumber = rs.getString("departmentNumber");
                this.departmentNumber = newDepartmentNumber;

                this.name = rs.getString("name");
                this.departmentNumber = rs.getString("description");
                this.location = rs.getString("location");

            }
        } catch (SQLException e) {
            throw new RuntimeException("Error loading department data", e);
        }
    }

    public static String getDepartmentNumber(Connection connection, String departmentName) {
        String sql = "SELECT * FROM DEPARTMENT WHERE name = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, departmentName);
            var rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getString("departmentNumber");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error loading department number", e);
        }
        return null;
    }
    public static String getDepartmentName(Connection connection, String departmentNumber) {
        String sql = "SELECT name FROM DEPARTMENT WHERE departmentNumber = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, departmentNumber);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getString("name");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error loading department name", e);
        }
        return null;
    }
    public static List<String> getAllDepartmentNames(Connection connection) {
        List<String> departments = new ArrayList<>();
        String sql = "SELECT name FROM DEPARTMENT ORDER BY name";

        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                departments.add(rs.getString("name"));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error loading departments", e);
        }
        return departments;
    }

}