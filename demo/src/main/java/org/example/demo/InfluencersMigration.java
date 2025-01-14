package org.example.demo;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.List;
import java.util.Map;

public class InfluencersMigration {
    public static void main(String[] args) {
        // Path to your JSON file containing influencer data
        String influencersJsonFile = "influencers.json"; // Replace with actual path to influencers.json

        try (Connection connection = DatabaseConnection.getConnection()) {
            // Parse JSON data
            ObjectMapper objectMapper = new ObjectMapper();
            List<Map<String, Object>> influencers = objectMapper.readValue(new File(influencersJsonFile), List.class);

            // Prepare SQL query for inserting data
            String insertQuery = "INSERT INTO Influencers (id, name, platform, followers) VALUES (?, ?, ?, ?)";

            try (PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {
                // Iterate through all influencer entries in the JSON array
                for (Map<String, Object> influencer : influencers) {
                    preparedStatement.setString(1, influencer.get("id").toString());           // Set id
                    preparedStatement.setString(2, influencer.get("name").toString());         // Set name
                    preparedStatement.setString(3, influencer.get("platform").toString());     // Set platform
                    preparedStatement.setInt(4, (int) influencer.get("followers"));            // Set followers

                    // Execute the insert statement
                    preparedStatement.executeUpdate();
                }
                System.out.println("Influencers inserted successfully!");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}