package org.example.system.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Button;
import org.example.system.config.DatabaseConfig;
import org.example.system.services.AuthenticationService;
import org.example.system.utils.AlertUtils;
import org.example.system.utils.SessionManager;
import org.example.system.utils.ValidationUtils;
import java.sql.Connection;

public class LoginController extends BaseController {
    @FXML
    private TextField emailField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Button loginButton;
    @FXML
    private Button teacherSignUpButton;
    @FXML
    private Button studentSignUpButton;

    private AuthenticationService authService;
    private Connection connection;

    public LoginController() {
        try {
            this.connection = DatabaseConfig.getConnection();
            this.authService = new AuthenticationService(connection);
        } catch (Exception e) {
            AlertUtils.showError("Database Error", "Could not connect to database");
            e.printStackTrace();
        }
    }

    @Override
    protected void initialize() {
        emailField.setText("");
        passwordField.setText("");

        // Add event listeners for real-time validation
        emailField.textProperty().addListener((observable, oldValue, newValue) -> {
            validateField(emailField, ValidationUtils.isValidEmail(newValue));
        });

        passwordField.textProperty().addListener((observable, oldValue, newValue) -> {
            validateField(passwordField, ValidationUtils.isValidPassword(newValue));
        });
    }

    private void validateField(TextField field, boolean isValid) {
        if (isValid) {
            field.setStyle("-fx-border-color: green;");
        } else {
            field.setStyle("-fx-border-color: red;");
        }
    }

    @Override
    protected boolean validateForm() {
        String email = emailField.getText();
        String password = passwordField.getText();
        return ValidationUtils.isValidEmail(email) && ValidationUtils.isValidPassword(password);
    }

    @Override
    protected void resetForm() {
        emailField.clear();
        passwordField.clear();
        emailField.setStyle(null);
        passwordField.setStyle(null);
    }

    @FXML
    protected void handleLogin() {
        if (!validateForm()) {
            AlertUtils.showError("Validation Error", "Please enter a valid email and password");
            return;
        }

        try {
            authService.login(emailField.getText(), passwordField.getText())
                    .ifPresentOrElse(
                            user -> {
                                // Store user session
                                SessionManager.getInstance().setCurrentUser(user);
                                System.out.println("Logged in successfully");
                                switch (user.getRole()){
                                    case ADMIN ->  navigateTo("/org/example/system/views/adminDashboard.fxml", "Dashboard", loginButton);
                                    case TEACHER -> navigateTo("/org/example/system/views/teacherDashboard.fxml", "Dashboard", loginButton);
                                    case STUDENT -> navigateTo("/org/example/system/views/studentDashboard.fxml", "Dashboard", loginButton);
                                }

                            },
                            () -> {
                                AlertUtils.showError("Login Failed", "Invalid credentials");
                                resetForm();
                            }
                    );
        } catch (Exception e) {
            AlertUtils.showError("Login Error", "An error occurred during login");
            e.printStackTrace();
        }
    }

    @FXML
    private void handleTeacherSignUp() {
        navigateTo("/org/example/system/views/teacherSignup.fxml", "Teacher Sign Up", teacherSignUpButton);
    }

    @FXML
    private void handleStudentSignUp() {
        navigateTo("/org/example/system/views/studentSignup.fxml", "Student Sign Up", studentSignUpButton);
    }


    @Override
    protected void cleanup() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (Exception e) {
            AlertUtils.showError("Database Error", "Error closing database connection");
            e.printStackTrace();
        }
        authService = null;
    }

    @Override
    protected void refreshView() {
        resetForm();
        initialize();
    }
}