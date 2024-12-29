package org.example.system.repositories;

import org.example.system.enums.Role;
import org.example.system.exceptions.DatabaseException;
import org.example.system.models.User;
import org.example.system.users.Admin;
import org.example.system.users.Student;
import org.example.system.users.Teacher;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 * Repository class for managing user data in the EduMatrix system.
 * Handles CRUD operations for all user types (Student, Teacher, Admin) and provides
 * search functionality. This class manages the base user data in the USER table
 * and coordinates with specific repositories for role-based operations.
 *
 * @author Abdelrhman Sersawy
 * @version 1.0
 */
public class UserRepository {
    private final Connection connection;
    private static final Logger LOGGER = Logger.getLogger(UserRepository.class.getName());
    private final StudentRepository studentRepository;
    private final TeacherRepository teacherRepository;
    private final AdminRepository adminRepository;
    /**
     * Base SQL query for selecting user data with role-specific information.
     * Joins USER table with STUDENT and TEACHER tables to retrieve all relevant data.
     */
    private static final String BASE_SELECT_QUERY = """
            SELECT u.*, s.departmentNumber, s.schoolYear, s.GPA, s.academicStatus, s.isScholarship,
                   t.departmentNumber as teacherDepartment
            FROM USER u
            LEFT JOIN STUDENT s ON u.userSerialNumber = s.userSerialNumber
            LEFT JOIN TEACHER t ON u.userSerialNumber = t.userSerialNumber
            """;
    /**
     * Constructs a new UserRepository with the specified database connection.
     * Initializes role-specific repositories for handling specialized operations.
     *
     * @param connection The database connection to be used for all operations
     */
    public UserRepository(Connection connection) {
        this.connection = connection;
        studentRepository = new StudentRepository(connection);
        teacherRepository = new TeacherRepository(connection);
        adminRepository = new AdminRepository(connection);
    }
    /**
     * Finds a user by their Gmail address.
     * Retrieves complete user information including role-specific data.
     *
     * @param gmail The Gmail address to search for
     * @return Optional containing the User if found, empty otherwise
     * @throws DatabaseException if there's an error executing the query
     */
    public Optional<User> findByGmail(String gmail) {
        String sql = BASE_SELECT_QUERY + "WHERE u.gmail = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, gmail);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return Optional.of(mapResultSetToUser(rs));
            }
            LOGGER.log(Level.INFO, "No user found with gmail: {0}", gmail);
            return Optional.empty();
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error finding user by gmail: " + gmail, e);
            throw new DatabaseException("Error finding user by gmail", e);
        }
    }
    /**
     * Finds a user by their serial number.
     * Retrieves complete user information including role-specific data.
     *
     * @param serialNumber The user's serial number
     * @return Optional containing the User if found, empty otherwise
     * @throws DatabaseException if there's an error executing the query
     */
    public Optional<User> findBySerialNumber(String serialNumber) {
        String sql = BASE_SELECT_QUERY + "WHERE u.userSerialNumber = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, serialNumber);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return Optional.of(mapResultSetToUser(rs));
            }
            LOGGER.log(Level.INFO, "No user found with serial number: {0}", serialNumber);
            return Optional.empty();
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error finding user by serial number: " + serialNumber, e);
            throw new DatabaseException("Error finding user by serial number", e);
        }
    }
    /**
     * Finds all users with a specific role.
     *
     * @param role The role to search for (STUDENT, TEACHER, or ADMIN)
     * @return List of users with the specified role
     * @throws DatabaseException if there's an error executing the query
     */
    public List<User> findByRole(Role role) {
        List<User> users = new ArrayList<>();
        String sql = BASE_SELECT_QUERY + "WHERE u.role = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, role.toString());
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                users.add(mapResultSetToUser(rs));
            }
            LOGGER.log(Level.INFO, "Found {0} users with role: {1}", new Object[]{users.size(), role});
            return users;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error finding users by role: " + role, e);
            throw new DatabaseException("Error finding users by role", e);
        }
    }
    /**
     * Searches for users based on a query string.
     * Searches across firstName, lastName, gmail, and userSerialNumber fields.
     *
     * @param query The search query
     * @return List of users matching the search criteria
     * @throws DatabaseException if there's an error executing the query
     */
    public List<User> searchUsers(String query) {
        List<User> users = new ArrayList<>();
        String sql = BASE_SELECT_QUERY +
                "WHERE u.firstName LIKE ? OR u.lastName LIKE ? OR u.gmail LIKE ? OR u.userSerialNumber LIKE ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            String searchPattern = "%" + query + "%";
            for (int i = 1; i <= 4; i++) {
                stmt.setString(i, searchPattern);
            }
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                users.add(mapResultSetToUser(rs));
            }
            LOGGER.log(Level.INFO, "Found {0} users matching query: {1}", new Object[]{users.size(), query});
            return users;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error searching users with query: " + query, e);
            throw new DatabaseException("Error searching users", e);
        }
    }
    /**
     * Saves a new user to the database.
     * Handles the creation of both base user data and role-specific information.
     * Uses transaction to ensure data consistency across tables.
     *
     * @param user The user object to save
     * @throws SQLException if there's an error during the save operation
     */
    public void save(User user) throws SQLException {
        try {
            connection.setAutoCommit(false);
            user.createNewUser();

            // Insert into role-specific table
            switch (user.getRole()) {
                case STUDENT -> studentRepository.save((Student) user);
                case TEACHER -> teacherRepository.save((Teacher) user);
                case ADMIN -> adminRepository.save((Admin) user);
            }

            connection.commit();
            LOGGER.log(Level.INFO, "Successfully saved user with serial number: {0}", user.getUserSerialNumber());
        } catch (SQLException e) {
            try {
                connection.rollback();
                LOGGER.log(Level.WARNING, "Transaction rolled back for user: " + user.getUserSerialNumber());
            } catch (SQLException ex) {
                LOGGER.log(Level.SEVERE, "Error rolling back transaction", ex);
                throw new DatabaseException("Error rolling back transaction", ex);
            }
            LOGGER.log(Level.SEVERE, "Error saving user: " + user.getUserSerialNumber(), e);
            throw e; // Re-throw the original exception
        }
    }
    /**
     * Updates user information in the database and delegates role-specific updates
     * to the appropriate repository (Student, Teacher, or Admin).
     * Updates both base user data and role-specific information in a single transaction.
     *
     * @param user The user object containing updated information (can be Student, Teacher, or Admin)
     * @throws SQLException if there's an error during the database update
     */
    public void update(User user) throws SQLException {
        try {
            connection.setAutoCommit(false);

            String userSql = """
            UPDATE USER 
            SET firstName = ?, 
                lastName = ?, 
                phoneNumber = ?, 
                gmail = ?, 
                password = ?, 
                birthOfDate = ?
            WHERE userSerialNumber = ?
            """;

            try (PreparedStatement stmt = connection.prepareStatement(userSql)) {
                stmt.setString(1, user.getFirstName());
                stmt.setString(2, user.getLastName());
                stmt.setString(3, user.getPhoneNumber());
                stmt.setString(4, user.getGmail());
                stmt.setString(5, user.getPassword());
                stmt.setTimestamp(6, Timestamp.valueOf(user.getBirthOfDate()));
                stmt.setString(7, user.getUserSerialNumber());
                stmt.executeUpdate();
            }

            switch (user.getRole()) {
                case STUDENT -> studentRepository.update((Student) user);
                case TEACHER -> teacherRepository.update((Teacher) user);
                case ADMIN -> adminRepository.update((Admin) user);
            }

            connection.commit();
            LOGGER.log(Level.INFO, "Successfully updated user: {0}", user.getUserSerialNumber());
        } catch (SQLException e) {
            try {
                connection.rollback();
                LOGGER.log(Level.WARNING, "Transaction rolled back for user update: " + user.getUserSerialNumber());
            } catch (SQLException ex) {
                LOGGER.log(Level.SEVERE, "Error rolling back transaction", ex);
                throw new DatabaseException("Error rolling back transaction", ex);
            }
            LOGGER.log(Level.SEVERE, "Error updating user: " + user.getUserSerialNumber(), e);
            throw e;
        }
    }
    /**
     * Deletes a user from the database.
     * Due to CASCADE constraints, this will also delete associated role-specific data.
     *
     * @param serialNumber The serial number of the user to delete
     * @throws DatabaseException if there's an error during the delete operation
     */
    public void delete(String serialNumber) {
        String sql = "DELETE FROM USER WHERE userSerialNumber = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, serialNumber);
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                LOGGER.log(Level.INFO, "Successfully deleted user: {0}", serialNumber);
            } else {
                LOGGER.log(Level.WARNING, "No user found to delete with serial number: {0}", serialNumber);
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error deleting user: " + serialNumber, e);
            throw new DatabaseException("Error deleting user", e);
        }
    }
    /**
     * Updates a user's password.
     *
     * @param serialNumber The serial number of the user
     * @param newPassword The new password (should be pre-hashed if required)
     * @throws DatabaseException if there's an error updating the password
     */
    public void updatePassword(String serialNumber, String newPassword) {
        String sql = "UPDATE USER SET password = ? WHERE userSerialNumber = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, newPassword);
            stmt.setString(2, serialNumber);
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                LOGGER.log(Level.INFO, "Successfully updated password for user: {0}", serialNumber);
            } else {
                LOGGER.log(Level.WARNING, "No user found to update password with serial number: {0}", serialNumber);
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error updating password for user: " + serialNumber, e);
            throw new DatabaseException("Error updating password", e);
        }
    }
    /**
     * Maps a database result set to the appropriate User object type.
     * Creates and populates Student, Teacher, or Admin object based on the user's role.
     *
     * @param rs The ResultSet containing user data
     * @return User object of the appropriate type
     * @throws SQLException if there's an error reading from the ResultSet
     */
    private User mapResultSetToUser(ResultSet rs) throws SQLException {
        Role role = Role.valueOf(rs.getString("role"));
        User user;

        switch (role) {
            case STUDENT -> {
                Student student = new Student(connection);
                student.setDepartmentNumber(rs.getString("departmentNumber"));
                student.setSchoolYear(rs.getInt("schoolYear"));
                student.setGPA(rs.getDouble("GPA"));
                student.setAcademicStatus(rs.getString("academicStatus"));
                student.setScholarship(rs.getBoolean("isScholarship"));
                user = student;
            }
            case TEACHER -> {
                Teacher teacher = new Teacher(connection);
                teacher.setDepartmentNumber(rs.getString("teacherDepartment"));
                user = teacher;
            }
            case ADMIN -> user = new Admin(connection);
            default -> throw new IllegalArgumentException("Unknown role: " + role);
        }

        user.setUserSerialNumber(rs.getString("userSerialNumber"));
        user.setFirstName(rs.getString("firstName"));
        user.setLastName(rs.getString("lastName"));
        user.setPhoneNumber(rs.getString("phoneNumber"));
        user.setGmail(rs.getString("gmail"));
        user.setPassword(rs.getString("password"));
        user.setBirthOfDate(rs.getTimestamp("birthOfDate").toLocalDateTime());
        user.setCreatedAt(rs.getTimestamp("createdAt").toLocalDateTime());
        user.setRole(role);

        return user;
    }
}