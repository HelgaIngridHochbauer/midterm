package org.example.demo;


import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.List;
import java.util.Map;

public class PostMigration {
    public static void main(String[] args) {
        // Path to your JSON file containing posts data
        String postsJsonFile = "posts.json"; // Replace with the actual path to posts.json

        try (Connection connection = DatabaseConnection.getConnection()) {
            // Select the database
            String useDatabaseQuery = "USE PR;"; // Replace your_database with your actual DB name
            try (PreparedStatement useStatement = connection.prepareStatement(useDatabaseQuery)) {
                useStatement.execute();
            }

            // Parse JSON data
            ObjectMapper objectMapper = new ObjectMapper();
            List<Map<String, Object>> posts = objectMapper.readValue(new File(postsJsonFile), List.class);

            // Insert SQL query
            String insertQuery = "INSERT INTO Posts (id, content, date, expectedLikes, currentLikes, " +
                    "previousLikes, comments, currentFollowers, previousFollowers, campaignName, influencerId, expectedComments) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

            try (PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {
                for (Map<String, Object> post : posts) {
                    // Extract post data
                    String id = post.get("id").toString();
                    String content = post.get("content").toString();
                    long date = Long.parseLong(post.get("date").toString()); // Assume Unix timestamp in milliseconds
                    int expectedLikes = (int) post.get("expectedLikes");
                    int currentLikes = (int) post.get("currentLikes");
                    int previousLikes = (int) post.get("previousLikes");
                    int comments = (int) post.get("comments");
                    int currentFollowers = (int) post.get("currentFollowers");
                    int previousFollowers = (int) post.get("previousFollowers");
                    String campaignName = post.get("campaignName").toString();
                    String influencerId = post.get("influencerId").toString();
                    int expectedComments = (int) post.get("expectedComments");

                    // Set values for prepared statement
                    preparedStatement.setString(1, id);
                    preparedStatement.setString(2, content);
                    preparedStatement.setLong(3, date);
                    preparedStatement.setInt(4, expectedLikes);
                    preparedStatement.setInt(5, currentLikes);
                    preparedStatement.setInt(6, previousLikes);
                    preparedStatement.setInt(7, comments);
                    preparedStatement.setInt(8, currentFollowers);
                    preparedStatement.setInt(9, previousFollowers);
                    preparedStatement.setString(10, campaignName);
                    preparedStatement.setString(11, influencerId);
                    preparedStatement.setInt(12, expectedComments);

                    // Execute the insert
                    preparedStatement.executeUpdate();
                }
                System.out.println("Posts inserted successfully!");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}