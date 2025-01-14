package org.example.demo;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.example.demo.model.Application1;
import org.example.demo.model.CustomException;

public class Dashboard {

    private final Application1 app; // Reference to the backend Application class

    public Dashboard(Application1 app) {
        this.app = app;
    }

    public VBox createDashboardPane(Stage primaryStage) {
        VBox dashboardPane = new VBox(15);
        dashboardPane.setAlignment(Pos.CENTER);
        dashboardPane.setPadding(new javafx.geometry.Insets(20));

        Label titleLabel = new Label("PR Dashboard");
        titleLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

        Button influencerDashboardButton = new Button("Influencer Dashboard");
        influencerDashboardButton.setOnAction(event -> showInfluencerDashboard(primaryStage));

        Button createPostButton = new Button("Create Post");
        createPostButton.setOnAction(e -> {
            try {
                app.createPost();
            } catch (CustomException.InvalidNumberException ex) {
                ex.printStackTrace();
                // Optionally, display an error message in the UI
            }
        });

        Button viewCalendarButton = new Button("View Calendar");
        viewCalendarButton.setOnAction(e -> {
            try {
                app.viewCalendar();
            } catch (CustomException.InvalidNumberException ex) {
                ex.printStackTrace();
                // Optionally, display an error message in the UI
            }
        });

        Button calculateFollowerGrowthButton = new Button("Calculate Follower Growth");
        calculateFollowerGrowthButton.setOnAction(e -> app.calculateFollowerGrowth());

        Button compareEngagementButton = new Button("Compare Engagement");
        compareEngagementButton.setOnAction(e -> {
            try {
                app.compareEngagement();
            } catch (CustomException.InvalidNumberException ex) {
                ex.printStackTrace();
                // Optionally, display an error message in the UI
            }
        });

        Button compareFollowerGrowthButton = new Button("Compare Follower Growth");
        compareFollowerGrowthButton.setOnAction(e -> {
            try {
                app.compareFollowerGrowth();
            } catch (CustomException.InvalidNumberException ex) {
                ex.printStackTrace();
                // Optionally, display an error message in the UI
            }
        });

        Button addInfluencerButton = new Button("Add Influencer");
        addInfluencerButton.setOnAction(e -> {
            AddInfluencerPage addInfluencerPage = new AddInfluencerPage(app);
            VBox addInfluencerPane = addInfluencerPage.createAddInfluencerPane(primaryStage);

            // Create a new scene with the addInfluencerPane
            Scene addInfluencerScene = new Scene(addInfluencerPane, 480, 800);

            // Reapply the CSS stylesheet to the new scene
            String css = this.getClass().getResource("/style.css").toExternalForm();
            addInfluencerScene.getStylesheets().add(css);

            // Set the scene to the primary stage
            primaryStage.setScene(addInfluencerScene);
        });

        Button sortInfluencersButton = new Button("Sort Influencers");
        sortInfluencersButton.setOnAction(e -> {
            // Create an instance of SortInfluencer and pass the current Dashboard Scene
            SortInfluencer sortInfluencerPage = new SortInfluencer(app, primaryStage.getScene());
            sortInfluencerPage.displaySortInfluencer(primaryStage); // Transition to the Sort Influencer view
        });

        Button setSortCriterionButton = new Button("Set Sort Criterion");
        setSortCriterionButton.setOnAction(e -> {
            SetSortCriterion setSortCriterionPage = new SetSortCriterion(app, primaryStage.getScene());
            setSortCriterionPage.displaySetSortCriterion(primaryStage);
        });

        Button groupByPlatformButton = new Button("Group Influencers by Platform");
        groupByPlatformButton.setOnAction(e -> {
            // Create an instance of GroupByPlatform and pass the current Dashboard Scene
            GroupByPlatform groupByPlatformPage = new GroupByPlatform(app, primaryStage.getScene());
            groupByPlatformPage.displayGroupByPlatform(primaryStage); // Transition to the Group By Platform view
        });

        Button groupByCelebrityLevelButton = new Button("Group Influencers by Celebrity Level");
        groupByCelebrityLevelButton.setOnAction(e -> {
            // Create an instance of GroupByCelebrityLevel and pass the current Dashboard Scene
            GroupByCelebrityLevel groupByCelebrityLevelPage = new GroupByCelebrityLevel(app, primaryStage.getScene());
            groupByCelebrityLevelPage.displayGroupByCelebrityLevel(primaryStage); // Transition to the view
        });

        Button groupByCampaignButton = new Button("Group Posts by Campaign");
        groupByCampaignButton.setOnAction(e -> {
            // Create an instance of GroupByCampaign and pass the current Dashboard Scene
            GroupByCampaign groupByCampaignPage = new GroupByCampaign(app, primaryStage.getScene());
            groupByCampaignPage.displayGroupByCampaign(primaryStage); // Transition to the view
        });

        Button exitButton = new Button("Exit");
        exitButton.setOnAction(e -> primaryStage.close());

        dashboardPane.getChildren().addAll(
                titleLabel,
                influencerDashboardButton,
                createPostButton,
                viewCalendarButton,
                calculateFollowerGrowthButton,
                compareEngagementButton,
                compareFollowerGrowthButton,
                addInfluencerButton,
                sortInfluencersButton,
                setSortCriterionButton,
                groupByPlatformButton,
                groupByCelebrityLevelButton,
                groupByCampaignButton,
                exitButton
        );

        return dashboardPane;
    }

    // Method to display the Influencer Dashboard
    private void showInfluencerDashboard(Stage primaryStage) {
        InfluencerDashboard influencerDashboard = new InfluencerDashboard();
        VBox influencerPane = influencerDashboard.createInfluencerDashboardPane(primaryStage);
        Scene influencerScene = new Scene(influencerPane, 480, 800);

        String css = this.getClass().getResource("/style.css").toExternalForm();
        influencerScene.getStylesheets().add(css);
        primaryStage.setScene(influencerScene);
    }
}