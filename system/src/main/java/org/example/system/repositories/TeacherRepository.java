package org.example.system.repositories;

import org.example.system.models.Course;
import org.example.system.users.Teacher;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class TeacherRepository {
    private final Connection connection;
    private static final Logger LOGGER = Logger.getLogger(StudentRepository.class.getName());

    public TeacherRepository(Connection connection) {
        this.connection = connection;
    }
    public void save(Teacher teacher) throws SQLException {
        String sql = "INSERT INTO TEACHER (userSerialNumber, departmentNumber) VALUES (?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, teacher.getUserSerialNumber());
            stmt.setString(2, teacher.getDepartmentNumber());
            stmt.executeUpdate();
        }
    }
    public void update(Teacher teacher) {
        // TODO: implement SQL Query

    }
    public List<Course> getTeachingCourses(Teacher teacher) {
        List<Course> courses = new ArrayList<>();
        String query = "SELECT * FROM COURSE WHERE teacherSerialNumber = ? AND isActive = true";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, teacher.getUserSerialNumber());
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Course course = new Course(connection);
                course.courseCode = rs.getString("courseCode");
                course.setName(rs.getString("name"));
                course.setDescription(rs.getString("description"));
                course.setDepartmentNumber(rs.getString("departmentNumber"));
                course.setTeacherSerialNumber(rs.getString("teacherSerialNumber"));
                course.isActive = rs.getBoolean("isActive");
                course.setMaxCapacity(rs.getInt("maxCapacity"));
                course.setSemesterFromString(rs.getString("semester"));
                course.setAcademicYear(rs.getInt("academicYear"));
                course.setCourseTypeFromString(rs.getString("courseType"));
                courses.add(course);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching teaching courses: " + e.getMessage());
        }
        return courses;
    }

    public void registerTeacher(Teacher teacher) {
        // TODO: implement SQL Query
    }
}
