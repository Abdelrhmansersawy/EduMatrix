package org.example.system.models;

public class Department {
    private String departmentNumber;
    private String name;
    private String description;
    private String location;

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
}