package org.example.system.services;

import org.example.system.courses.StudentCourse;
import org.example.system.repositories.StudentRepository;
import org.example.system.repositories.UserRepository;
import org.example.system.users.Student;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class StudentService implements AutoCloseable {
    private final StudentRepository studentRepository;
    private final UserRepository userRepository;
    private final Connection connection;

    public StudentService(Connection connection) {
        this.connection = connection;
        this.studentRepository = new StudentRepository(connection);
        this.userRepository = new UserRepository(connection);
    }

    public void registerStudent(Student student) throws SQLException {
        userRepository.save(student);
    }
    public void updateStudent(Student student) throws SQLException {
        userRepository.update(student);
    }

    public List<StudentCourse> findEnrolledCourses(String userSerialNumber){
        return studentRepository.findEnrolledCourses(userSerialNumber);
    }

    @Override
    public void close() throws Exception {
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }
}
