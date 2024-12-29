package org.example.system.services;

import org.example.system.repositories.TeacherRepository;
import org.example.system.users.Teacher;

import java.sql.Connection;
import java.sql.SQLException;

public class TeacherService implements AutoCloseable {
    private final TeacherRepository teacherRepository;
    private final Connection connection;

    public TeacherService(Connection connection) {
        this.connection = connection;
        teacherRepository = new TeacherRepository(connection);
    }

    public void registerTeacher(Teacher teacher) throws SQLException{
        teacherRepository.registerTeacher(teacher);
    }

    @Override
    public void close() throws Exception {
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }
}
