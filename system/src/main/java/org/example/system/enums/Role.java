package org.example.system.enums;

public enum Role {
    ADMIN("admin"),
    TEACHER("teacher"),
    STUDENT("student");
    private final String value;
    Role(String value) {
        this.value = value;
    }
    @Override
    public String toString() {
        return value;
    }
}
