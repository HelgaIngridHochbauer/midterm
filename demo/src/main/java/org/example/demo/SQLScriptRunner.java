package org.example.demo;

import java.sql.Connection;
import java.sql.Statement;

public class SQLScriptRunner {
    public static void main(String[] args) {
        // Initialize SQL for table creation
        String createCampaignsTable = "CREATE TABLE IF NOT EXISTS Campaigns (" +
                "campaignName VARCHAR(255) PRIMARY KEY" +
                ");";
        String createInfluencersTable = "CREATE TABLE IF NOT EXISTS Influencers (" +
                "id CHAR(36) PRIMARY KEY, " +
                "name VARCHAR(255), " +
                "platform VARCHAR(50), " +
                "followers INT" +
                ");";
        String createPostsTable = "CREATE TABLE IF NOT EXISTS Posts (" +
                "id CHAR(36) PRIMARY KEY, " +
                "content TEXT, " +
                "date BIGINT, " +
                "expectedLikes INT, " +
                "currentLikes INT, " +
                "previousLikes INT, " +
                "comments INT, " +
                "currentFollowers INT, " +
                "previousFollowers INT, " +
                "campaignName VARCHAR(255), " +
                "influencerId CHAR(36), " +
                "expectedComments INT, " +
                "FOREIGN KEY (campaignName) REFERENCES Campaigns(campaignName), " +
                "FOREIGN KEY (influencerId) REFERENCES Influencers(id)" +
                ");";

        // Get database connection
        try (Connection connection = DatabaseConnection.getConnection();
             Statement statement = connection.createStatement()) {

            // Execute SQL scripts
            statement.execute(createCampaignsTable);
            statement.execute(createInfluencersTable);
            statement.execute(createPostsTable);
            System.out.println("Tables created successfully!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}