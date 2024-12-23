package org.example.system.users;


import org.example.system.models.Course;
import org.example.system.models.User;
import org.example.system.enums.Role;


import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

public class Teacher extends User {
    private String departmentNumber;
    private List<Course> teachingCourses;

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
        throw null;
    }

    public void addTeachingCourse(Course course) {

    }

    public void removeTeachingCourse(Course course) {

    }
}