package org.example.demo;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.example.demo.model.Application1;

public class SortInfluencer {
    private final Application1 app;
    private final Scene dashboardScene; // Reference to the Dashboard scene

    // Constructor
    public SortInfluencer(Application1 app, Scene dashboardScene) {
        // Check if 'app' is valid during initialization
        if (app == null) {
            throw new IllegalArgumentException("Application1 instance (app) cannot be null.");
        }
        this.app = app;
        this.dashboardScene = dashboardScene; // Store the Dashboard scene
    }

    // Create the UI Pane for the Sort Influencer Page
    public VBox createSortInfluencerPane(Stage primaryStage) {
        // Layout for the page
        VBox sortInfluencerPane = new VBox(10);
        sortInfluencerPane.setPadding(new Insets(20));
        sortInfluencerPane.setAlignment(Pos.CENTER);

        // TextArea to display influencers
        TextArea outputTextArea = new TextArea();
        outputTextArea.setEditable(false); // Make it read-only
        outputTextArea.setPrefHeight(600);
        outputTextArea.setPrefWidth(440);
        outputTextArea.setWrapText(true);

        // Fetch the list of influencers and safely display the results
        String influencerList;
        try {
            influencerList = app.listInfluencers(); // Fetch the influencer list
            if (influencerList == null || influencerList.isEmpty()) {
                influencerList = "No influencers found."; // Fallback when the list is empty
            }
        } catch (Exception ex) {
            // Handle unexpected errors and display a message in the TextArea
            influencerList = "Error loading influencers: " + ex.getMessage();
            ex.printStackTrace();
        }

        // Display data in the TextArea
        outputTextArea.setText(influencerList);

        // Back button to navigate to the dashboard
        Button backButton = new Button("Back");
        backButton.setOnAction(e -> primaryStage.setScene(dashboardScene)); // Switch to Dashboard

        // Add components to the pane
        sortInfluencerPane.getChildren().addAll(outputTextArea, backButton);
        return sortInfluencerPane;
    }

    // Method to apply scene and attach the pane to the Stage
    public void displaySortInfluencer(Stage primaryStage) {
        VBox sortInfluencerPane = createSortInfluencerPane(primaryStage); // Get the page content

        // Create a new scene with Sort Influencer Pane
        Scene sortInfluencerScene = new Scene(sortInfluencerPane, 480, 800);

        // Apply CSS
        String cssFile = this.getClass().getResource("/style.css") != null
                ? this.getClass().getResource("/style.css").toExternalForm()
                : null;
        if (cssFile != null) {
            sortInfluencerScene.getStylesheets().add(cssFile);
        } else {
            System.err.println("CSS file not found. Styles will not be applied.");
        }

        // Set the scene to the stage
        primaryStage.setScene(sortInfluencerScene);
    }
}