package org.example.demo;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.example.demo.model.Application1;
import org.example.demo.model.CustomException.InvalidNumberException;

public class SetSortCriterion {
    private final Application1 app;
    private final Scene previousScene; // Reference to the previous scene (e.g., the dashboard)

    // Constructor
    public SetSortCriterion(Application1 app, Scene previousScene) {
        this.app = app;
        this.previousScene = previousScene; // Keep a reference to navigate back
    }

    // Creates the UI layout for setting a sort criterion
    public VBox createSetSortCriterionPane(Stage primaryStage) {
        // Layout: Use a VBox with a vertical spacing of 10
        VBox setSortCriterionPane = new VBox(10);
        setSortCriterionPane.setPadding(new Insets(20));
        setSortCriterionPane.setAlignment(Pos.CENTER);

        // Button for "Sort by Name"
        Button sortByNameButton = new Button("Sort by Name");
        sortByNameButton.setOnAction(e -> handleSortCriterion(1, primaryStage)); // Pass choice 1

        // Button for "Sort Descendingly by Followers"
        Button sortByFollowersDescButton = new Button("Sort Descendingly by Followers");
        sortByFollowersDescButton.setOnAction(e -> handleSortCriterion(2, primaryStage)); // Pass choice 2

        // Button for "Sort Ascendingly by Followers"
        Button sortByFollowersAscButton = new Button("Sort Ascendingly by Followers");
        sortByFollowersAscButton.setOnAction(e -> handleSortCriterion(3, primaryStage)); // Pass choice 3

        // Back button to navigate to the previous scene
        Button backButton = new Button("Back");
        backButton.setOnAction(e -> primaryStage.setScene(previousScene)); // Go back to the dashboard or previous screen

        // Add all buttons to the layout
        setSortCriterionPane.getChildren().addAll(
                sortByNameButton,
                sortByFollowersDescButton,
                sortByFollowersAscButton,
                backButton
        );

        return setSortCriterionPane;
    }

    // Handle selection and update sort criterion based on user's choice
    private void handleSortCriterion(int choice, Stage primaryStage) {
        try {
            // Call the backend method to update the sort criterion
            app.updateSortCriterion(choice);

            // Show success message after updating the criterion
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Success");
            alert.setHeaderText(null);
            alert.setContentText("Sort criterion updated successfully!");
            alert.showAndWait();

            // Navigate back to the previous screen
            primaryStage.setScene(previousScene);
        } catch (InvalidNumberException ex) {
            // Show error message for invalid choice
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Invalid choice. Please try again!");
            alert.showAndWait();
        }
    }

    // Display the SetSortCriterion interface
    public void displaySetSortCriterion(Stage primaryStage) {
        // Create the content pane
        VBox setSortCriterionPane = createSetSortCriterionPane(primaryStage);

        // Create a new scene with the pane
        Scene setSortCriterionScene = new Scene(setSortCriterionPane, 480, 800);

        // Apply CSS styles if a stylesheet exists
        String cssFile = this.getClass().getResource("/style.css") != null
                ? this.getClass().getResource("/style.css").toExternalForm()
                : null;
        if (cssFile != null) {
            setSortCriterionScene.getStylesheets().add(cssFile);
        } else {
            System.err.println("CSS file not found. Styles will not be applied.");
        }

        // Set the new scene for the primary stage
        primaryStage.setScene(setSortCriterionScene);
    }
}