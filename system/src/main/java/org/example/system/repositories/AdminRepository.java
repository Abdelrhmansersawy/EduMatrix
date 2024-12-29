package org.example.system.repositories;

import org.example.system.users.Admin;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Logger;

public class AdminRepository {
    private final Connection connection;
    private static final Logger LOGGER = Logger.getLogger(UserRepository.class.getName());
    public AdminRepository(Connection connection) {
        this.connection = connection;
    }

    public void save(Admin admin) {
        // TODO: Implement SQL query
    }
    private void saveAdmin(Admin admin) throws SQLException {
        String sql = "INSERT INTO ADMIN (userSerialNumber) VALUES (?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, admin.getUserSerialNumber());
            stmt.executeUpdate();
        }
    }

    public void update(Admin user) {
        // TODO: Implement SQL query
    }
}
