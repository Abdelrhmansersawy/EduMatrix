package org.example.system.enums;

public enum AcademicStatus {
    ACTIVE("ACTIVE"),
    PROBATION("PROBATION"),
    SUSPENDED("SUSPENDED"),
    GRADUATED("GRADUATED"),;

    private final String value;
    AcademicStatus(String value) {
        this.value = value;
    }
    @Override
    public String toString() {
        return value;
    }
}