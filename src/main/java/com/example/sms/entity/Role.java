package com.example.sms.entity;

public enum Role {
    ROLE_ADMIN("Administrator"),
    ROLE_TEACHER("Teacher"),
    ROLE_STUDENT("Student");

    private final String displayName;

    Role(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
