package org.example.system.users;


import org.example.system.models.Course;
import org.example.system.models.Department;
import org.example.system.models.User;
import org.example.system.enums.Role;
import org.example.system.repositories.TeacherRepository;


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
    private TeacherRepository teacherRepository;

    public Teacher(Connection connection, String firstName, String lastName, String phoneNumber, String gmail, String password, LocalDateTime birthOfDate, Role role, String departmentName) {
        super(connection, firstName, lastName, phoneNumber, gmail, password, birthOfDate, role);
        this.departmentName = departmentName;
        this.departmentNumber = Department.getDepartmentName(connection, departmentName);
        this.teacherRepository = new TeacherRepository(connection);
        this.teachingCourses = teacherRepository.getTeachingCourses(this);
    }
    public Teacher(Connection connection) {
        super(connection);
        this.teachingCourses = new ArrayList<>();
        this.departmentName = Department.getDepartmentName(connection, departmentNumber);
    }
    public Teacher(Connection connection, User user) {
        super(connection,user.getFirstName(),user.getLastName(),user.getPhoneNumber(),user.getGmail(),user.getPassword(),user.getBirthOfDate(),user.getRole());
        this.teacherRepository = new TeacherRepository(connection);
        this.teachingCourses = teacherRepository.getTeachingCourses(this);
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
        return teachingCourses;
    }

    public String getDepartmentName() {
        return departmentName;
    }
}