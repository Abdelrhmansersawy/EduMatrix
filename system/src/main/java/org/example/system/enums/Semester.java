package org.example.system.enums;

public enum Semester {
    FALL("FALL"),
    SPRING("SPRING"),
    SUMMER("SUMMER"),;

    private final String value;
    Semester(String value) {
        this.value = value;
    }
    @Override
    public String toString() {
        return value;
    }
}