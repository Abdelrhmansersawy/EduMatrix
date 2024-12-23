package org.example.system.enums;

public enum CourseType {
    MANDATORY("MANDATORY"),
    ELECTIVE("ELECTIVE"),
    GENERAL("GENERAL");

    private final String value;
    CourseType(String value) {
        this.value = value;
    }
    @Override
    public String toString() {
        return value;
    }
}