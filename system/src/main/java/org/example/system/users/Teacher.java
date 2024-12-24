package org.example.system.users;


import org.example.system.models.Course;
import org.example.system.models.Department;
import org.example.system.models.User;
import org.example.system.enums.Role;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Teacher extends User {
    private String departmentNumber;
    private String departmentName;
    private List<Course> teachingCourses;

    public Teacher(Connection connection, String firstName, String lastName, String phoneNumber, String gmail, String password, LocalDateTime birthOfDate, Role role, String departmentName) {
        super(connection, firstName, lastName, phoneNumber, gmail, password, birthOfDate, role);
        this.departmentName = departmentName;
        this.departmentNumber = Department.getDepartmentName(connection, departmentName);
        teachingCourses = new ArrayList<>();
    }

    public Teacher() {
        super();
        this.teachingCourses = new ArrayList<>();
    }
    public Teacher(Connection connection) {
        super(connection);
        this.teachingCourses = new ArrayList<>();
    }

    @Override
    public Role getRole() {
        return Role.TEACHER;
    }

    public String getDepartmentNumber() {
        return departmentNumber;
    }

    public void setDepartmentNumber(String departmentNumber) {
        this.departmentNumber = departmentNumber;
    }

    public List<Course> getTeachingCourses() {
        List<Course> courses = new ArrayList<>();
        String query = "SELECT * FROM COURSE WHERE teacherSerialNumber = ? AND isActive = true";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, getUserSerialNumber());
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

    public void addTeachingCourse(Course course) {

    }

    public void removeTeachingCourse(Course course) {

    }

    public void updateProfile(Connection connection) {

    }
}