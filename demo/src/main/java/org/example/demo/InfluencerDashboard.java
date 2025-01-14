package org.example.demo;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.example.demo.model.Application1;
import org.example.demo.utils.InputDevice;

import java.util.Optional;

public class InfluencerDashboard {

    private InputDevice inputDevice;
    private org.example.demo.utils.OutputDevice outputDevice;

    public VBox createInfluencerDashboardPane(Stage primaryStage) {
        VBox influencerPane = new VBox(10);
        influencerPane.setAlignment(Pos.CENTER);
        influencerPane.setPadding(new javafx.geometry.Insets(10));

        // Prompt for influencer name
        TextInputDialog textInputDialog = new TextInputDialog();
        textInputDialog.setTitle("Influencer Name");
        textInputDialog.setHeaderText("Enter Influencer's Name");
        textInputDialog.setContentText("Name:");

        Optional<String> result = textInputDialog.showAndWait();
        result.ifPresentOrElse(influencerName -> {
            // Display Influencer's Name
            Label influencerNameLabel = new Label("Dashboard Options for " + influencerName);
            influencerNameLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

            // Create the buttons
            Button viewCampaignsPostsButton = new Button("View Campaigns and Posts");
            // Similarly handle actions
            Button editPostButton = new Button("Edit Post");
            Button editInfluencerDetailsButton = new Button("Edit Influencer Details");
            Button viewInfluencerDetailsButton = new Button("View Influencer Details");
            Button exitButton = new Button("Exit Dashboard");

            exitButton.setOnAction(e -> {
                Application1 app = new Application1(inputDevice, outputDevice);
                Dashboard dashboard = new Dashboard(app);
                VBox dashboardPane = dashboard.createDashboardPane(primaryStage);
                Scene dashboardScene = new Scene(dashboardPane, 480, 800);
                dashboardScene.getStylesheets().add(this.getClass().getResource("/style.css").toExternalForm());
                primaryStage.setScene(dashboardScene);
            });


            influencerPane.getChildren().addAll(
                    influencerNameLabel,
                    viewCampaignsPostsButton,
                    editPostButton,
                    editInfluencerDetailsButton,
                    viewInfluencerDetailsButton,
                    exitButton
            );

        }, () -> {
            // If no name is entered, return to main dashboard
            Application1 app = new Application1(inputDevice, outputDevice);
            Dashboard dashboard = new Dashboard(app);
            VBox dashboardPane = dashboard.createDashboardPane(primaryStage);
            Scene dashboardScene = new Scene(dashboardPane, 500, 1000);
            dashboardScene.getStylesheets().add(this.getClass().getResource("/style.css").toExternalForm());
            primaryStage.setScene(dashboardScene);
        });

        Scene influencerScene = new Scene(influencerPane, 500, 1000);

        // Add the stylesheet
        String css = this.getClass().getResource("/style.css").toExternalForm();
        influencerScene.getStylesheets().add(css);

        // Set the scene on the stage
        primaryStage.setScene(influencerScene);
        return influencerPane;
    }
}