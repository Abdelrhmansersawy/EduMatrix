package org.example.system.utils;

public class ValidationUtils {
    public static boolean isValidInput(String email, String password) {
        return isValidEmail(email) && isValidPassword(password);
    }

    public static boolean isValidEmail(String email) {
        if (email == null || email.isEmpty()) {
            return false;
        }
        // Add email validation regex
        return email.matches("^[A-Za-z0-9+_.-]+@(.+)$");
    }

    public static boolean isValidPassword(String password) {
        return password != null && password.length() >= 6;
    }
}