package org.example.demo;
// Example Java methods for admin-related tasks
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class AdminService {

    private Connection connection;

    public AdminService(Connection connection) { // Pass in Database Connection
        this.connection = connection;
    }

    // DELETE USER BY ID
    public void deleteUserById(int userId) {
        String query = "DELETE FROM users WHERE id = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, userId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle errors appropriately (log or throw)
        }
    }

    // EMPTY DATABASE
    public void emptyDatabase() {
        String query = "TRUNCATE TABLE users";

        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle errors appropriately (log or throw)
        }
    }

    // PROMOTE USER TO ADMIN
    public void promoteUserToAdmin(int userId) {
        String query = "UPDATE users SET role = 'Admin' WHERE id = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, userId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle errors appropriately (log or throw)
        }
    }
}