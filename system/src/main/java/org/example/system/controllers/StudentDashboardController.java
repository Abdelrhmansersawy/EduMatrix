package org.example.system.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import org.example.system.users.Student;
import org.example.system.courses.StudentCourse;
import org.example.system.utils.SessionManager;
import java.util.Collections;
import java.util.logging.Logger;
import java.util.logging.Level;

public class StudentDashboardController {
    private static final Logger LOGGER = Logger.getLogger(StudentDashboardController.class.getName());
    private static final double MAX_GPA = 4.0;

    @FXML private Label nameLabel;
    @FXML private Label departmentLabel;
    @FXML private Label yearLabel;
    @FXML private Label emailLabel;
    @FXML private Label gpaLabel;
    @FXML private Label statusLabel;
    @FXML private Label scholarshipLabel;
    @FXML private ProgressBar gpaProgress;

    @FXML private TableView<StudentCourse> coursesTable;
    @FXML private TableColumn<StudentCourse, String> codeColumn;
    @FXML private TableColumn<StudentCourse, Float> gradeColumn;
    @FXML private TableColumn<StudentCourse, Float> attendanceColumn;
    @FXML private TableColumn<StudentCourse, String> semesterColumn;

    private Student student;

    @FXML
    private void initialize() {
        try {
            Object currentUser = SessionManager.getInstance().getCurrentUser();
            if (!(currentUser instanceof Student)) {
                throw new ClassCastException("Current user is not a Student");
            }
            student = (Student) currentUser;
            setupTableColumns();
            updateUI();
        } catch (ClassCastException e) {
            LOGGER.log(Level.SEVERE, "Error: Current user is not a Student", e);
            clearUI();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error initializing dashboard", e);
            clearUI();
        }
    }


    private void setupTableColumns() {
        try {
            codeColumn.setCellValueFactory(new PropertyValueFactory<>("courseCode"));
            gradeColumn.setCellValueFactory(new PropertyValueFactory<>("grade"));
            attendanceColumn.setCellValueFactory(new PropertyValueFactory<>("attendanceRate"));
            semesterColumn.setCellValueFactory(new PropertyValueFactory<>("semester"));

            gradeColumn.setCellFactory(column -> new TableCell<StudentCourse, Float>() {
                @Override
                protected void updateItem(Float item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) {
                        setText(null);
                    } else {
                        setText(String.format("%.2f", item));
                    }
                }
            });

            attendanceColumn.setCellFactory(column -> new TableCell<StudentCourse, Float>() {
                @Override
                protected void updateItem(Float item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) {
                        setText(null);
                    } else {
                        setText(String.format("%.1f%%", item * 100));
                    }
                }
            });

        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error setting up table columns", e);
        }
    }

    public void setStudent(Student student) {
        if (student == null) {
            LOGGER.warning("Attempted to set null student");
            clearUI();
            return;
        }
        this.student = student;
        updateUI();
    }

    private void updateUI() {
        if (student == null) {
            clearUI();
            return;
        }

        try {
            nameLabel.setText(formatName(student.getFirstName(), student.getLastName()));
            departmentLabel.setText(student.getDepartmentName() != null ?
                    student.getDepartmentName() : "");
            yearLabel.setText(String.valueOf(student.getSchoolYear()));
            emailLabel.setText(student.getGmail() != null ? student.getGmail() : "");

            double gpa = student.getGPA();
            gpaLabel.setText(String.format("%.2f", gpa));
            statusLabel.setText(student.getAcademicStatus() != null ?
                    student.getAcademicStatus() : "");
            scholarshipLabel.setText(student.isScholarship() ? "Yes" : "No");

            double normalizedGPA = Math.min(Math.max(gpa / MAX_GPA, 0.0), 1.0);
            gpaProgress.setProgress(normalizedGPA);

            updateCoursesTable();

        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error updating UI", e);
            clearUI();
        }
    }

    private void updateCoursesTable() {
        try {
            coursesTable.getItems().clear(); // Clear existing items first
            student.getEnrolledCourses().ifPresent(courses -> {
                coursesTable.getItems().addAll(courses);
            });
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error loading enrolled courses", e);
            coursesTable.getItems().clear();
        }
    }

    private String formatName(String firstName, String lastName) {
        StringBuilder name = new StringBuilder();
        if (firstName != null && !firstName.trim().isEmpty()) {
            name.append(firstName.trim());
        }
        if (lastName != null && !lastName.trim().isEmpty()) {
            if (name.length() > 0) {
                name.append(" ");
            }
            name.append(lastName.trim());
        }
        return name.toString();
    }

    private void clearUI() {
        nameLabel.setText("");
        departmentLabel.setText("");
        yearLabel.setText("");
        emailLabel.setText("");
        gpaLabel.setText("");
        statusLabel.setText("");
        scholarshipLabel.setText("");
        gpaProgress.setProgress(0);
        coursesTable.getItems().clear();
    }

    public void refresh() {
        updateUI();
    }
}