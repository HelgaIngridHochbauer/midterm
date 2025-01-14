package org.example.demo;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.example.demo.model.Application1;
import org.example.demo.model.Influencer;
import org.example.demo.model.Post;

import java.sql.Date;
import java.time.LocalDate;

public class CreatePost {

    private final Application1 app; // Reference to the application instance
    private final Scene dashboardScene; // Scene reference for navigation after success

    // Constructor initializes both app and dashboardScene
    public CreatePost(Application1 app, Scene dashboardScene) {
        if (app == null || dashboardScene == null) {
            throw new IllegalArgumentException("Application1 instance and DashboardScene cannot be null.");
        }
        this.app = app;
        this.dashboardScene = dashboardScene;
    }

    public VBox createCreatePostPane(Stage primaryStage) {
        VBox createPostPane = new VBox(10);
        createPostPane.setAlignment(Pos.TOP_CENTER);
        createPostPane.setPadding(new Insets(20));

        // Title Label
        Label titleLabel = new Label("Create a New Post");
        titleLabel.getStyleClass().add("title-label");

        // Influencer Selection ComboBox
        Label influencerLabel = new Label("Select an Influencer:");
        ComboBox<Influencer> influencerComboBox = new ComboBox<>();
        influencerComboBox.getItems().addAll(app.getInfluencers()); // Populate from app

        // Content TextArea
        Label contentLabel = new Label("Post Content:");
        TextArea contentTextArea = new TextArea();
        contentTextArea.setPromptText("Enter post content...");
        contentTextArea.setPrefRowCount(5);

        // Expected Likes Field
        Label likesLabel = new Label("Expected Likes:");
        TextField likesTextField = new TextField();
        likesTextField.setPromptText("Enter number of expected likes...");

        // Expected Comments Field
        Label commentsLabel = new Label("Expected Comments:");
        TextField commentsTextField = new TextField();
        commentsTextField.setPromptText("Enter number of expected comments...");

        // Date Picker
        Label dateLabel = new Label("Post Date:");
        DatePicker datePicker = new DatePicker();

        // Campaign Selection Options
        ToggleGroup campaignToggleGroup = new ToggleGroup();
        RadioButton existingCampaignBtn = new RadioButton("Use Existing Campaign");
        RadioButton newCampaignBtn = new RadioButton("Create New Campaign");
        existingCampaignBtn.setToggleGroup(campaignToggleGroup);
        newCampaignBtn.setToggleGroup(campaignToggleGroup);
        existingCampaignBtn.setSelected(true);

        ComboBox<String> campaignComboBox = new ComboBox<>();

        // Fix: Extract campaign names from the map
        campaignComboBox.getItems().addAll(app.getCampaigns().keySet()); // Updated here

        TextField newCampaignField = new TextField();
        newCampaignField.setPromptText("Enter new campaign name...");

        // Conditional visibility for campaign options
        campaignComboBox.setDisable(false);
        newCampaignField.setDisable(true);
        existingCampaignBtn.setOnAction(e -> {
            campaignComboBox.setDisable(false);
            newCampaignField.setDisable(true);
        });
        newCampaignBtn.setOnAction(e -> {
            campaignComboBox.setDisable(true);
            newCampaignField.setDisable(false);
        });

        // Buttons
        Button schedulePostButton = new Button("Schedule Post");
        Button backToDashboardButton = new Button("Back to Dashboard");

        // Error Label
        Label errorLabel = new Label();
        errorLabel.setStyle("-fx-text-fill: red;");

        // Back to Dashboard Button Action
        backToDashboardButton.setOnAction(e -> primaryStage.setScene(dashboardScene));

        // Schedule Post Button Action
        schedulePostButton.setOnAction(e -> {
            try {
                // Validate inputs
                if (influencerComboBox.getValue() == null) {
                    errorLabel.setText("Please select an influencer.");
                    return;
                }
                if (contentTextArea.getText().isEmpty()) {
                    errorLabel.setText("Post content cannot be empty.");
                    return;
                }
                if (likesTextField.getText().isEmpty() || !likesTextField.getText().matches("\\d+")) {
                    errorLabel.setText("Expected likes must be a valid number.");
                    return;
                }
                if (commentsTextField.getText().isEmpty() || !commentsTextField.getText().matches("\\d+")) {
                    errorLabel.setText("Expected comments must be a valid number.");
                    return;
                }
                if (datePicker.getValue() == null || datePicker.getValue().isBefore(LocalDate.now())) {
                    errorLabel.setText("Please select a valid post date.");
                    return;
                }

                // Determine Campaign Name
                String campaignName;
                if (existingCampaignBtn.isSelected()) {
                    if (campaignComboBox.getValue() == null) {
                        errorLabel.setText("Please select an existing campaign.");
                        return;
                    }
                    campaignName = campaignComboBox.getValue();
                } else {
                    if (newCampaignField.getText().isEmpty()) {
                        errorLabel.setText("Please enter a new campaign name.");
                        return;
                    }
                    campaignName = newCampaignField.getText();
                }

                // Create and schedule the Post
                Influencer selectedInfluencer = influencerComboBox.getValue();
                String content = contentTextArea.getText();
                int expectedLikes = Integer.parseInt(likesTextField.getText());
                int expectedComments = Integer.parseInt(commentsTextField.getText());
                LocalDate postDate = datePicker.getValue();

                Post post = new Post(
                        content,
                        Date.valueOf(postDate),
                        expectedLikes,
                        0,
                        0,
                        0,
                        app.getCurrentFollowers(),
                        app.getPreviousFollowers(),
                        campaignName);

                post.setExpectedComments(expectedComments);
                post.setInfluencer(selectedInfluencer);

                app.schedulePost(post, campaignName);

                errorLabel.setStyle("-fx-text-fill: green;");
                errorLabel.setText("Post scheduled successfully!");

                Platform.runLater(() -> primaryStage.setScene(dashboardScene));

            } catch (Exception ex) {
                errorLabel.setText("Error: " + ex.getMessage());
            }
        });

        createPostPane.getChildren().addAll(
                titleLabel,
                influencerLabel, influencerComboBox,
                contentLabel, contentTextArea,
                likesLabel, likesTextField,
                commentsLabel, commentsTextField,
                dateLabel, datePicker,
                existingCampaignBtn, campaignComboBox,
                newCampaignBtn, newCampaignField,
                schedulePostButton, backToDashboardButton,
                errorLabel
        );

        return createPostPane;
    }
}