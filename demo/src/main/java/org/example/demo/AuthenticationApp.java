package org.example.demo;

import org.example.demo.model.Application1;
import org.example.demo.utils.InputDevice;
import org.example.demo.utils.OutputDevice;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class AuthenticationApp extends Application {

    private Application1 app; // Declare Application1 instance here

    public AuthenticationApp() {
        // No-argument constructor, required by JavaFX
    }

    @Override
    public void start(Stage primaryStage) {
        // Instantiate InputDevice and OutputDevice
        InputDevice inputDevice = new InputDevice();   // Replace with actual implementation or mock
        OutputDevice outputDevice = new OutputDevice(); // Replace with actual implementation or mock

        // Initialize the Application1 instance with the required devices
        app = new Application1(inputDevice, outputDevice);

        // Instantiate the components
        LoginForm loginForm = new LoginForm();
        RegistrationForm registrationForm = new RegistrationForm();
        Dashboard userDashboard = new Dashboard(app); // This is for the user interface
        AdminDashboard adminDashboard = new AdminDashboard(app); // This is for the admin-specific dashboard

        // Create the User Dashboard Pane

        VBox userDashboardPane = userDashboard.createDashboardPane(primaryStage);
        Scene userDashboardScene = new Scene(userDashboardPane, 480, 700);
        if (userDashboardPane == null) {
            throw new RuntimeException("UserDashboardPane is not properly initialized.");
        }

        // Create the Admin Dashboard Pane
        VBox adminDashboardPane = adminDashboard.createAdminDashboardPane(primaryStage);
        Scene adminDashboardScene = new Scene(adminDashboardPane, 480, 700);

        // Instantiate the CreatePost feature and associate it with the User Dashboard
        CreatePost createPost = new CreatePost(app, userDashboardScene); // Pass both app and user dashboard scene
        VBox createPostPane = createPost.createCreatePostPane(primaryStage);
        Scene createPostScene = new Scene(createPostPane, 480, 700);

        // Add a "Create Post" button to the User Dashboard's pane
        Button createPostButton = new Button("Create Post");
        createPostButton.setOnAction(e -> primaryStage.setScene(createPostScene));
        userDashboardPane.getChildren().add(createPostButton);

        // Create the Login Pane using the LoginForm class
        VBox loginPane = loginForm.createLoginPane();

        // Create the Registration Pane using the RegistrationForm class
        GridPane registrationPane = registrationForm.createRegistrationPane();

        // Create the initial choice pane
        VBox choicePane = new VBox(10);
        choicePane.setAlignment(Pos.CENTER);
        Button logInButton = new Button("Log In");
        Button signUpButton = new Button("Sign Up");

        // Add buttons to the choice pane
        choicePane.getChildren().addAll(logInButton, signUpButton);

        // Define the scenes
        Scene choiceScene = new Scene(choicePane, 480, 700);
        Scene loginScene = new Scene(loginPane, 480, 700);
        Scene registerScene = new Scene(registrationPane, 480, 700);

        // Add CSS styling
        String css = this.getClass().getResource("/style.css").toExternalForm();
        choiceScene.getStylesheets().add(css);
        loginScene.getStylesheets().add(css);
        registerScene.getStylesheets().add(css);
        userDashboardScene.getStylesheets().add(css);
        adminDashboardScene.getStylesheets().add(css); // Add style for Admin Dashboard
        createPostScene.getStylesheets().add(css);

        // Button actions to switch scenes
        logInButton.setOnAction(e -> primaryStage.setScene(loginScene));
        signUpButton.setOnAction(e -> primaryStage.setScene(registerScene));

        // Start with the choice screen
        primaryStage.setScene(choiceScene);
        primaryStage.setTitle("PR app");
        primaryStage.show();

        // Set action for successful login to navigate to respective dashboard
        loginForm.setOnLoginSuccessful((role) -> {
            if ("Admin".equalsIgnoreCase(role)) {
                primaryStage.setScene(adminDashboardScene); // Admin navigates here
            } else if ("User".equalsIgnoreCase(role)) {
                primaryStage.setScene(userDashboardScene); // User navigates here
            } else {
                // Handle unexpected or null roles (optional, e.g., show an error alert)
                System.err.println("Invalid role: " + role);
            }
        });
    }

    private void switchToDashboard(Stage primaryStage, String role) {
        if ("Admin".equalsIgnoreCase(role)) {
            // Create Admin dashboard scene
            VBox adminDashboard = new VBox();
            Scene adminScene = new Scene(adminDashboard, 800, 600);
            primaryStage.setScene(adminScene);
        } else if ("User".equalsIgnoreCase(role)) {
            // Create User dashboard scene
            VBox userDashboard = new VBox();
            Scene userScene = new Scene(userDashboard, 800, 600);
            primaryStage.setScene(userScene);
        } else {
            System.err.println("Unknown role: " + role);
        }
    }

    public VBox createDashboardPane(Stage primaryStage) {
        VBox root;

        try {
            root = new VBox(); // Initialize the VBox
            // Optional: Add children or configure VBox based on some logic
            // root.getChildren().add(new Label("Dashboard content goes here"));
        } catch (Exception e) {
            e.printStackTrace();
            // If anything happens, ensure root is still initialized
            root = new VBox();
        }

        return root;
    }

    public static void main(String[] args) {
        launch(args);
    }
}