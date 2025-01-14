package org.example.demo;

import java.sql.Connection;
import java.sql.DriverManager;

public class DatabaseConnection {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/PR";  // Replace with your database name
    private static final String DB_USER = "root"; // Replace with your MySQL username
    private static final String DB_PASSWORD = "23122003"; // Replace with your MySQL password

    public static Connection getConnection() {
        Connection connection = null;
        try {
            // Register MySQL driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Establish connection
            connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            System.out.println("Database connected successfully!");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Database connection failed!");
        }
        return connection;
    }
}