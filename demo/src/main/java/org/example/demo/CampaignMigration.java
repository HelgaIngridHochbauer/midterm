package org.example.demo;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.List;

public class CampaignMigration {
    public static void main(String[] args) {
        String campaignsJsonFile = "campaigns.json"; // Replace with actual path to campaigns.json

        try (Connection connection = DatabaseConnection.getConnection()) {
            // Parse JSON data
            ObjectMapper objectMapper = new ObjectMapper();
            List<String> campaigns = objectMapper.readValue(new File(campaignsJsonFile), List.class);

            // Insert data into Campaigns table
            String insertQuery = "INSERT INTO Campaigns (campaignName) VALUES (?)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {
                for (String campaign : campaigns) {
                    preparedStatement.setString(1, campaign);
                    preparedStatement.executeUpdate();
                }
                System.out.println("Campaigns inserted successfully!");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}