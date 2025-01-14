package org.example.demo;

import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TxtToTableMigration {
    public static void main(String[] args) {
        String filePath = "user_credentials.txt"; // Replace with your actual file path

        // Regex pattern to extract Name, Email, and Password
        String regexPattern = "Name:\\s*(.*?),\\s*Email:\\s*(.*?),\\s*Password:\\s*(.*?)(?:\\s|$)";

        try (Connection connection = DatabaseConnection.getConnection();
             BufferedReader reader = new BufferedReader(new FileReader(filePath))) {

            // Use the correct database
            String useDatabaseQuery = "USE PR;"; // Replace 'demo' with your database name
            try (PreparedStatement useStatement = connection.prepareStatement(useDatabaseQuery)) {
                useStatement.execute();
            }

            // Insert statement for Users table
            String insertQuery = "INSERT INTO Users (name, email, password) VALUES (?, ?, ?)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {

                String line;
                Pattern pattern = Pattern.compile(regexPattern);

                // Read each line from the file
                while ((line = reader.readLine()) != null) {
                    Matcher matcher = pattern.matcher(line);

                    // Match regex pattern and extract groups
                    while (matcher.find()) {
                        String name = matcher.group(1).trim();
                        String email = matcher.group(2).trim();
                        String password = matcher.group(3).trim();

                        // Insert into the table
                        preparedStatement.setString(1, name);
                        preparedStatement.setString(2, email);
                        preparedStatement.setString(3, password);
                        preparedStatement.executeUpdate();
                    }
                }

                System.out.println("Data successfully migrated to the database!");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}