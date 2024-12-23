package org.example.system.enums;

public enum Role {
    ADMIN("ADMIN"),
    TEACHER("TEACHER"),
    STUDENT("STUDENT");
    private final String value;
    Role(String value) {
        this.value = value;
    }
    @Override
    public String toString() {
        return value;
    }
}
