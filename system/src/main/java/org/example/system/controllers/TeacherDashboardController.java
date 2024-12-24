package org.example.system.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import org.example.system.config.DatabaseConfig;
import org.example.system.models.Course;
import org.example.system.models.Department;
import org.example.system.users.Teacher;
import org.example.system.utils.AlertUtils;
import org.example.system.utils.SessionManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;
import java.util.Map;

public class TeacherDashboardController {
    private Teacher teacher;
    private Connection connection;

    @FXML private Label teacherNameLabel;
    @FXML private Label teacherEmailLabel;
    @FXML private Label teacherPhoneLabel;
    @FXML private Label birthDateLabel;
    @FXML private Label departmentLabel;
    @FXML private Label coursesCountLabel;

    @FXML private TableView<Course> coursesTable;
    @FXML private TableColumn<Course, String> courseCodeColumn;
    @FXML private TableColumn<Course, String> courseNameColumn;
    @FXML private TableColumn<Course, Integer> studentsCountColumn;
    @FXML private TableColumn<Course, Button> actionsColumn;

    @FXML
    private void initialize() {
        try {
            teacher = (Teacher) SessionManager.getInstance().getCurrentUser();
            connection = DatabaseConfig.getConnection();

            if (teacher != null && connection != null) {
                loadTeacherData();
                setupTableColumns();
                setupActionsColumn();
            }
        } catch (Exception e) {
            AlertUtils.showError("Error", "Failed to initialize: " + e.getMessage());
        }
    }

    private void setupTableColumns() {
        try {
            courseCodeColumn.setCellValueFactory(new PropertyValueFactory<>("courseCode"));
            courseNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
            studentsCountColumn.setCellValueFactory(new PropertyValueFactory<>("studentsCount"));
        } catch (Exception e) {
            AlertUtils.showError("Error", "Failed to setup table columns: " + e.getMessage());
        }
    }

    private void setupActionsColumn() {
        actionsColumn.setCellFactory(param -> new TableCell<>() {
            private final Button viewButton = new Button("View Students");

            @Override
            protected void updateItem(Button item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(viewButton);
                    viewButton.setOnAction(event -> {
                        Course course = getTableView().getItems().get(getIndex());
                        showEnrolledStudents(course);
                    });
                }
            }
        });
    }

    private void showEnrolledStudents(Course course) {
        String sql = """
        SELECT u.userSerialNumber, CONCAT(u.firstName, ' ', u.lastName) as fullName,
               s.schoolYear, s.GPA,
               sc.grade, sc.attendanceRate,
               d.name as departmentName
        FROM STUDENT_COURSE sc
        JOIN USER u ON sc.userSerialNumber = u.userSerialNumber
        JOIN STUDENT s ON s.userSerialNumber = u.userSerialNumber
        JOIN DEPARTMENT d ON s.departmentNumber = d.departmentNumber
        WHERE sc.courseCode = ?
        ORDER BY u.lastName, u.firstName
    """;

        Dialog<Void> dialog = createDialog(course);
        TableView<StudentRecord> studentTable = createTableView();

        loadStudentData(sql, course, studentTable);

        configureAndShowDialog(dialog, studentTable);
    }

    private Dialog<Void> createDialog(Course course) {
        Dialog<Void> dialog = new Dialog<>();
        dialog.setTitle("Students in " + course.getName());
        dialog.setHeaderText("Course Code: " + course.getCourseCode());
        return dialog;
    }

    private TableView<StudentRecord> createTableView() {
        TableView<StudentRecord> table = new TableView<>();

        // Create each column individually for better control
        TableColumn<StudentRecord, String> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<StudentRecord, String> nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("fullName"));

        TableColumn<StudentRecord, Integer> yearCol = new TableColumn<>("Year");
        yearCol.setCellValueFactory(new PropertyValueFactory<>("schoolYear"));

        TableColumn<StudentRecord, String> deptCol = new TableColumn<>("Department");
        deptCol.setCellValueFactory(new PropertyValueFactory<>("departmentName"));

        TableColumn<StudentRecord, Double> gpaCol = new TableColumn<>("GPA");
        gpaCol.setCellValueFactory(new PropertyValueFactory<>("gpa"));
        gpaCol.setCellFactory(col -> new TableCell<StudentRecord, Double>() {
            @Override
            protected void updateItem(Double item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : String.format("%.2f", item));
            }
        });

        TableColumn<StudentRecord, Double> gradeCol = new TableColumn<>("Grade");
        gradeCol.setCellValueFactory(new PropertyValueFactory<>("grade"));
        gradeCol.setCellFactory(col -> new TableCell<StudentRecord, Double>() {
            @Override
            protected void updateItem(Double item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : String.format("%.2f", item));
            }
        });

        TableColumn<StudentRecord, Double> attendanceCol = new TableColumn<>("Attendance");
        attendanceCol.setCellValueFactory(new PropertyValueFactory<>("attendanceRate"));
        attendanceCol.setCellFactory(col -> new TableCell<StudentRecord, Double>() {
            @Override
            protected void updateItem(Double item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : String.format("%.1f%%", item));
            }
        });

        table.getColumns().addAll(
                idCol, nameCol, yearCol, deptCol, gpaCol, gradeCol, attendanceCol
        );

        return table;
    }


    private void loadStudentData(String sql, Course course, TableView<StudentRecord> table) {
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, course.getCourseCode());



            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {


                table.getItems().add(new StudentRecord(
                        rs.getString("userSerialNumber"),
                        rs.getString("fullName"),
                        rs.getInt("schoolYear"),
                        rs.getDouble("GPA"),
                        rs.getDouble("grade"),
                        rs.getDouble("attendanceRate"),
                        rs.getString("DepartmentName")
                ));
            }
        } catch (SQLException e) {
            AlertUtils.showError("Error", "Failed to load students: " + e.getMessage());
        }
    }

    private void configureAndShowDialog(Dialog<Void> dialog, TableView<StudentRecord> table) {
        dialog.getDialogPane().setContent(table);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);
        dialog.getDialogPane().setMinWidth(800);
        dialog.getDialogPane().setMinHeight(400);
        dialog.show();
    }


    private void loadTeacherData() {
        try {
            teacherNameLabel.setText(teacher.getFullName());
            teacherEmailLabel.setText(teacher.getGmail());
            teacherPhoneLabel.setText(teacher.getPhoneNumber());
            departmentLabel.setText(Department.getDepartmentName(connection, teacher.getDepartmentNumber()));
            birthDateLabel.setText(teacher.getBirthOfDate().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")));
            coursesCountLabel.setText("Total Courses: " + teacher.getTeachingCourses().size());
            refreshCoursesTable();
        } catch (Exception e) {
            AlertUtils.showError("Error", "Failed to load teacher data: " + e.getMessage());
        }
    }

    private void refreshCoursesTable() {
        try {
            coursesTable.getItems().clear();
            coursesTable.getItems().addAll(teacher.getTeachingCourses());
        } catch (Exception e) {
            AlertUtils.showError("Error", "Failed to refresh courses table: " + e.getMessage());
        }
    }
}