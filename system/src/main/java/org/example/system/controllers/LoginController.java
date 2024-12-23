package org.example.system.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Button;
import org.example.system.services.AuthenticationService;
import org.example.system.utils.AlertUtils;
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
    private Button signUpButton;

    private AuthenticationService authService;
    private Connection connection;

    public LoginController() {
        // TODO: Get connection from your database connection manager
        // this.connection = DatabaseConnectionManager.getConnection();
        // this.authService = new AuthenticationService(connection);
    }

    @Override
    protected void initialize() {
        emailField.setText("");
        passwordField.setText("");
    }

    @Override
    protected boolean validateForm() {
        return ValidationUtils.isValidInput(emailField.getText(), passwordField.getText());
    }

    @Override
    protected void resetForm() {
        emailField.clear();
        passwordField.clear();
    }

    @FXML
    protected void handleLogin() {
        if (!validateForm()) {
            AlertUtils.showError("Validation Error", "Please fill in all fields correctly");
            return;
        }

        String gmail = emailField.getText();
        String password = passwordField.getText();

        authService.login(gmail, password)
                .ifPresentOrElse(
                        user -> {
                            navigateTo("/org/example/system/views/dashboard.fxml", "Dashboard", loginButton);
                        },
                        () -> {
                            AlertUtils.showError("Login Failed", "Invalid credentials");
                            resetForm();
                        }
                );
    }

    @FXML
    protected void handleSignUp() {
        navigateTo("/org/example/system/views/signup.fxml", "Sign Up", signUpButton);
    }

    @Override
    protected void cleanup() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (Exception e) {
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