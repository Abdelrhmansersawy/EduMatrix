package org.example.system.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.example.system.config.DatabaseConfig;
import org.example.system.services.StudentRegistrationService;
import org.example.system.users.Student;
import org.example.system.utils.AlertUtils;
import org.example.system.utils.ValidationUtils;

import java.time.LocalDateTime;

public class StudentSignupController extends BaseController {

    @FXML private TextField firstNameField;
    @FXML private TextField lastNameField;
    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;
    @FXML private PasswordField confirmPasswordField;
    @FXML private TextField phoneNumberField;
    @FXML private DatePicker birthDatePicker;
    @FXML private ComboBox<String> departmentComboBox;
    @FXML private ComboBox<Integer> schoolYearComboBox;
    @FXML private Button registerButton;
    @FXML private Button backButton;

    private StudentRegistrationService registrationService;

    public StudentSignupController() {
        try {
            this.registrationService = new StudentRegistrationService(DatabaseConfig.getConnection());
        } catch (Exception e) {
            AlertUtils.showError("Database Error", "Could not connect to database");
        }
    }

    @Override
    protected void initialize() {
        setupValidation();
        setupComboBoxes();
    }

    private void setupValidation() {
        emailField.textProperty().addListener((o, old, newVal) ->
                validateField(emailField, ValidationUtils.isValidEmail(newVal)));

        passwordField.textProperty().addListener((o, old, newVal) ->
                validateField(passwordField, ValidationUtils.isValidPassword(newVal)));

        confirmPasswordField.textProperty().addListener((o, old, newVal) ->
                validateField(confirmPasswordField, newVal.equals(passwordField.getText())));
    }

    private void setupComboBoxes() {
        departmentComboBox.getItems().addAll("CS", "IT", "IS", "DS");
        schoolYearComboBox.getItems().addAll(1, 2, 3, 4);
    }

    @FXML
    protected void handleRegister() {
        if (!validateForm()) {
            AlertUtils.showError("Validation Error", "Please fill all fields correctly");
            return;
        }

        try {
            Student student = new Student(DatabaseConfig.getConnection());
            student.setFirstName(firstNameField.getText());
            student.setLastName(lastNameField.getText());
            student.setGmail(emailField.getText());
            student.setPassword(passwordField.getText());
            student.setPhoneNumber(phoneNumberField.getText());
            student.setBirthOfDate(LocalDateTime.of(
                    birthDatePicker.getValue(),
                    java.time.LocalTime.MIDNIGHT
            ));
            student.setDepartmentNumber(departmentComboBox.getValue());
            student.setSchoolYear(schoolYearComboBox.getValue());

            registrationService.registerStudent(student);
            AlertUtils.showInfo("Success", "Registration successful");
            navigateBack();

        } catch (Exception e) {
            AlertUtils.showError("Registration Error", e.getMessage());
        }
    }

    @FXML
    protected void handleBack() {
        navigateBack();
    }

    @FXML
    protected void navigateBack(){
        navigateTo("/org/example/system/views/login.fxml", "Login", backButton);
    }

    @Override
    protected boolean validateForm() {
        return !firstNameField.getText().isEmpty() &&
                !lastNameField.getText().isEmpty() &&
                ValidationUtils.isValidEmail(emailField.getText()) &&
                ValidationUtils.isValidPassword(passwordField.getText()) &&
                passwordField.getText().equals(confirmPasswordField.getText()) &&
                birthDatePicker.getValue() != null &&
                departmentComboBox.getValue() != null &&
                schoolYearComboBox.getValue() != null;
    }

    @Override
    protected void cleanup() {
        try {
            if (registrationService != null) {
                registrationService.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void refreshView() {
        setupComboBoxes();
    }

    @Override
    protected void resetForm() {
        firstNameField.clear();
        lastNameField.clear();
        emailField.clear();
        passwordField.clear();
        confirmPasswordField.clear();
        phoneNumberField.clear();
        birthDatePicker.cancelEdit();
        departmentComboBox.cancelEdit();
        schoolYearComboBox.cancelEdit();
    }
    private void validateField(TextField field, boolean isValid) {
        if (isValid) {
            field.setStyle("-fx-border-color: green;");
        } else {
            field.setStyle("-fx-border-color: red;");
        }
    }
}