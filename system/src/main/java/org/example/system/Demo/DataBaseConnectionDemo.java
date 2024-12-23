package org.example.system.Demo;

import java.sql.Connection;
import java.sql.DriverManager;

public class DataBaseConnectionDemo {
    private static final String URL = "jdbc:mysql://localhost:3306/EduMatrix";
    private static final String USER = "root";
    private static final String PASSWORD = "root";

    public static void testConnection() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("Database connected successfully");
            connection.close();
        } catch (Exception e) {
            System.err.println("Connection failed: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        testConnection();
    }
}