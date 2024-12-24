package org.example.system.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.example.system.config.DatabaseConfig;
import org.example.system.models.Department;
import org.example.system.services.StudentService;
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
    @FXML private Button backButton;

    private StudentService studentService;

    public StudentSignupController() {
        try {
            this.studentService = new StudentService(DatabaseConfig.getConnection());
        } catch (Exception e) {
            AlertUtils.showError("Database Error", "Could not connect to database");
        }
    }

    @FXML
    public void initialize() throws Exception {
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

    private void setupComboBoxes() throws Exception {
        departmentComboBox.getItems().clear();
        schoolYearComboBox.getItems().clear();
        departmentComboBox.getItems().addAll(Department.getAllDepartmentNames(DatabaseConfig.getConnection()));

        schoolYearComboBox.getItems().addAll(0,1,2,3,4);
    }

    @FXML
    protected void handleRegister() {
        if (!validateForm()) {
            AlertUtils.showError("Validation Error", "Please fill all fields correctly");
            return;
        }
        try {
            Student student = new Student(
                    DatabaseConfig.getConnection(),
                    firstNameField.getText(),
                    lastNameField.getText(),
                    phoneNumberField.getText(),
                    emailField.getText(),
                    passwordField.getText(),
                    LocalDateTime.of(birthDatePicker.getValue(), java.time.LocalTime.MIDNIGHT),
                    departmentComboBox.getValue(),
                    schoolYearComboBox.getValue()
            );
            studentService.registerStudent(student);
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
    protected void cleanup() throws Exception {
        studentService.close();
    }

    @Override
    protected void refreshView() throws Exception {
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