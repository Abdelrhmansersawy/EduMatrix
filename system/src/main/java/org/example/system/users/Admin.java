package org.example.system.users;

import org.example.system.models.User;
import org.example.system.enums.Role;

import java.sql.Connection;
import java.util.ArrayList;

public class Admin extends User {
    public Admin(Connection connection) {
        super(connection);
    }
    @Override
    public Role getRole() {
        return Role.ADMIN;
    }
}
