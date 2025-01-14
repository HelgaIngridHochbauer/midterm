package org.example.demo;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.example.demo.model.Application1;

public class AddInfluencerPage {
    private final Application1 app;

    public AddInfluencerPage(Application1 app) {
        this.app = app;
    }

    public VBox createAddInfluencerPane(Stage primaryStage) {
        VBox addInfluencerPane = new VBox(10);
        addInfluencerPane.setPadding(new Insets(20));
        addInfluencerPane.setAlignment(Pos.CENTER);

        Label nameLabel = new Label("Name:");
        TextField nameField = new TextField();

        Label platformLabel = new Label("Platform:");
        TextField platformField = new TextField();

        Label followersLabel = new Label("Number of Followers:");
        TextField followersField = new TextField();

        Button saveButton = new Button("Save");
        saveButton.setOnAction(e -> {
            try {
                String name = nameField.getText();
                String platform = platformField.getText();
                int followers = Integer.parseInt(followersField.getText());

                // Call the method in the application to add an influencer
                app.addInfluencer(name, platform, followers);

                // Go back to the dashboard or display a confirmation message
                Scene dashboardScene = new Scene(new Dashboard(app).createDashboardPane(primaryStage), 480, 800);
                String css = this.getClass().getResource("/style.css").toExternalForm();
                dashboardScene.getStylesheets().add(css);

                primaryStage.setScene(dashboardScene);
            } catch (Exception ex) {
                ex.printStackTrace();
                // Handle errors (e.g. invalid input) or show a validation message
            }
        });

        addInfluencerPane.getChildren().addAll(nameLabel, nameField, platformLabel, platformField, followersLabel, followersField, saveButton);

        // Create the add influencer scene
        Scene addInfluencerScene = new Scene(addInfluencerPane, 480, 800);

        // Apply CSS
        String css = this.getClass().getResource("/style.css").toExternalForm();
        addInfluencerScene.getStylesheets().add(css);

        primaryStage.setScene(addInfluencerScene);

        return addInfluencerPane;
    }
}