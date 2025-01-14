package org.example.demo;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.function.Consumer;

public class LoginForm {

    private Consumer<String> onLoginSuccessful; // Role-handling callback

    public VBox createLoginPane() {
        VBox loginPane = new VBox(15);
        loginPane.setAlignment(Pos.CENTER);
        loginPane.setPadding(new Insets(20));

        // Email label and text field
        Label emailLabel = new Label("Email:");
        TextField emailField = new TextField();
        emailField.setPromptText("Enter your email");
        emailField.setMaxWidth(250);

        // Password label and password field
        Label passwordLabel = new Label("Password:");
        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Enter your password");
        passwordField.setMaxWidth(250);

        // Login message label
        Label loginMessageLabel = new Label();

        // Image view to display welcome image
        ImageView welcomeImageView = new ImageView();
        welcomeImageView.setFitHeight(200);
        welcomeImageView.setFitWidth(200);
        welcomeImageView.setVisible(false);

        // Login button
        Button loginButton = new Button("Log In");

        // Handle login button action
        loginButton.setOnAction(event -> {
            String email = emailField.getText().trim();
            String password = passwordField.getText().trim();
            String role = authenticate(email, password); // Role-based authentication
            if (role != null) {
                loginMessageLabel.setText("Login successful! Welcome back as " + role + "!");
                loginMessageLabel.setStyle("-fx-text-fill: green;");
                //welcomeImageView.setVisible(true);
                //welcomeImageView.setImage(new Image(getClass().getResource("/welcome.jpg").toExternalForm())); // Update with correct image path

                if (onLoginSuccessful != null) {
                    onLoginSuccessful.accept(role); // Pass the role ("Admin" or "User")
                }
            } else {
                loginMessageLabel.setText("Invalid email or password.");
                loginMessageLabel.setStyle("-fx-text-fill: red;");
                welcomeImageView.setVisible(false);
            }
        });

        // Add components to login pane
        loginPane.getChildren().addAll(
                emailLabel,
                emailField,
                passwordLabel,
                passwordField,
                loginButton,
                loginMessageLabel,
                welcomeImageView
        );

        return loginPane;
    }

    /**
     * Role-based authentication method.
     *
     * @param email    the email entered by the user.
     * @param password the password entered by the user.
     * @return "Admin" if the user is an admin, "User" if the user is a regular user, or null if authentication fails.
     */
    private String authenticate(String email, String password) {
        String fileName = "C:\\Users\\helga\\IdeaProjects\\demo\\user_credentials.txt"; // Update with your actual file path

        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                String foundEmail = null;
                String foundPassword = null;
                String foundRole = "User"; // Default role is "User"

                for (String part : parts) {
                    String[] keyValue = part.split(":");
                    if (keyValue.length == 2) {
                        String key = keyValue[0].trim();
                        String value = keyValue[1].trim();

                        if ("Email".equalsIgnoreCase(key)) {
                            foundEmail = value;
                        } else if ("Password".equalsIgnoreCase(key)) {
                            foundPassword = value;
                        } else if ("Role".equalsIgnoreCase(key)) {
                            foundRole = value; // Fetch the role from the file
                        }
                    }
                }

                // If both email and password match, return the found role
                if (email.equals(foundEmail) && password.equals(foundPassword)) {
                    return foundRole;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null; // Authentication failed
    }

    /**
     * Set a callback to handle successful login, passing the user's role ("Admin" or "User").
     *
     * @param onLoginSuccessful the callback to execute on successful login.
     */
    public void setOnLoginSuccessful(Consumer<String> onLoginSuccessful) {
        this.onLoginSuccessful = onLoginSuccessful;
    }
}