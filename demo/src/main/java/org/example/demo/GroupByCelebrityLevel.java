package org.example.demo;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.example.demo.model.Application1;

public class GroupByCelebrityLevel {
    private final Application1 app;
    private final Scene dashboardScene; // Reference to the Dashboard scene

    // Constructor
    public GroupByCelebrityLevel(Application1 app, Scene dashboardScene) {
        this.app = app;
        this.dashboardScene = dashboardScene; // Store the Dashboard scene
    }

    // Create the UI Pane for grouping influencers by celebrity level
    public VBox createGroupByCelebrityLevelPane(Stage primaryStage) {
        // Layout for the page
        VBox groupByCelebrityLevelPane = new VBox(10);
        groupByCelebrityLevelPane.setPadding(new Insets(20));
        groupByCelebrityLevelPane.setAlignment(Pos.CENTER);

        // Title or description
        TextArea outputTextArea = new TextArea();
        outputTextArea.setEditable(false); // Make it read-only
        outputTextArea.setPrefHeight(600);
        outputTextArea.setPrefWidth(440);
        outputTextArea.setWrapText(true);

        // Fetch and display the grouped celebrity data
        String groupedByCelebrityLevel = app.getGroupedInfluencersByCelebrityLevel(); // Call the backend method
        outputTextArea.setText(groupedByCelebrityLevel);                              // Populate the TextArea

        // Back button to navigate to the dashboard
        Button backButton = new Button("Back");
        backButton.setOnAction(e -> primaryStage.setScene(dashboardScene)); // Switch to Dashboard

        // Add components to the pane
        groupByCelebrityLevelPane.getChildren().addAll(outputTextArea, backButton);
        return groupByCelebrityLevelPane;
    }

    // Method to apply scene and attach the pane to the Stage
    public void displayGroupByCelebrityLevel(Stage primaryStage) {
        VBox groupByCelebrityLevelPane = createGroupByCelebrityLevelPane(primaryStage); // Get the page content

        // Create a new scene with Group By Celebrity Level Pane
        Scene groupByCelebrityLevelScene = new Scene(groupByCelebrityLevelPane, 480, 800);

        // Apply CSS
        String cssFile = this.getClass().getResource("/style.css") != null
                ? this.getClass().getResource("/style.css").toExternalForm()
                : null;
        if (cssFile != null) {
            groupByCelebrityLevelScene.getStylesheets().add(cssFile);
        } else {
            System.err.println("CSS file not found. Styles will not be applied.");
        }

        // Set the scene to the stage
        primaryStage.setScene(groupByCelebrityLevelScene);
    }
}