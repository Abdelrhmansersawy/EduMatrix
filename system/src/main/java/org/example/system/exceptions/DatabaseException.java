package org.example.system.exceptions;

import java.sql.SQLException;

public class DatabaseException extends RuntimeException {
    public DatabaseException(String errorSavingStudent, SQLException e) {
        super(errorSavingStudent, e);
    }
}
