package org.example.system.services;

import org.example.system.models.User;
import java.sql.Connection;
import java.util.Optional;

public class AuthenticationService {
    private final UserService userService;
    private final Connection connection;

    public AuthenticationService(Connection connection) {
        this.connection = connection;
        this.userService = new UserService(connection);
    }

    public Optional<User> login(String gmail, String password) {
        return userService.findByGmail(gmail)
                .filter(user -> validatePassword(password, user.getPassword()));
    }

    private boolean validatePassword(String inputPassword, String storedPassword) {
        // TODO: Implement proper password hashing and validation
        return inputPassword != null && inputPassword.equals(storedPassword);
    }
}