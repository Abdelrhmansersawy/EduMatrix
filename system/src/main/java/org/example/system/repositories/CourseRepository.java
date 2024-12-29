package org.example.system.repositories;

import org.example.system.models.Course;

import java.sql.Connection;
import java.util.List;

public class CourseRepository {
    private final Connection connection;
    public CourseRepository(Connection connection) {
        this.connection = connection;
    }

//    public List<Course> showEnrolledStudents(){
//
//    }
}
