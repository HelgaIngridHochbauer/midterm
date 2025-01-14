package org.example.demo;

import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class RegistrationForm {

    private static final String FILE_NAME = "user_credentials.txt";

    // Method to create the Registration GridPane
    public GridPane createRegistrationPane() {
        TextField nameField = new TextField();
        TextField emailField = new TextField();
        PasswordField passwordField = new PasswordField();
        Button registerButton = new Button("Register");
        Label messageLabel = new Label();

        nameField.setPromptText("Enter your name");
        emailField.setPromptText("Enter your email");
        passwordField.setPromptText("Enter your password");
        messageLabel.setStyle("-fx-text-fill: red;");

        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(20));
        gridPane.setVgap(10);
        gridPane.setHgap(10);
        gridPane.getStyleClass().add("root");

        gridPane.add(new Label("Name:"), 0, 0);
        gridPane.add(nameField, 1, 0);
        gridPane.add(new Label("Email:"), 0, 1);
        gridPane.add(emailField, 1, 1);
        gridPane.add(new Label("Password:"), 0, 2);
        gridPane.add(passwordField, 1, 2);
        gridPane.add(registerButton, 1, 3);
        gridPane.add(messageLabel, 1, 4);

        registerButton.setOnAction(event -> {
            String name = nameField.getText();
            String email = emailField.getText();
            String password = passwordField.getText();

            if (validateInput(name, email, password)) {
                saveCredentialsToTextFile(name, email, password, messageLabel);
            } else {
                messageLabel.setText("Invalid input. Please check your details.");
            }
        });

        return gridPane;
    }

    private void saveCredentialsToTextFile(String name, String email, String password, Label messageLabel) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME, true))) {
            writer.write("Name: " + name + ", Email: " + email + ", Password: " + password);
            writer.newLine();
            messageLabel.setStyle("-fx-text-fill: green;");
            messageLabel.setText("Registration successful!");
        } catch (IOException e) {
            messageLabel.setText("Error saving credentials.");
            e.printStackTrace();
        }
    }

    private boolean validateInput(String name, String email, String password) {
        return !name.isBlank() && isValidEmail(email) && !password.isBlank();
    }

    private boolean isValidEmail(String email) {
        return email.contains("@");
    }
}