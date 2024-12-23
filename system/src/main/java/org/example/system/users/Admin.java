package org.example.system.users;

import org.example.system.models.User;
import org.example.system.enums.Role;

public class Admin extends User {
    @Override
    public Role getRole() {
        return Role.ADMIN;
    }
}
