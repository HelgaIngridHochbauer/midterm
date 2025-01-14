package org.example.demo;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.example.demo.model.Application1;

public class AdminDashboard extends Application {

    private AdminService adminService;

    public AdminDashboard(Application1 app) {
        // Initialize the AdminService with your database connection here
        // Replace `null` with your valid connection
        this.adminService = new AdminService(DatabaseConnection.getConnection());
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Admin Dashboard");

        // Create GridPane for layout
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));

        // Title
        Label titleLabel = new Label("Admin Dashboard");
        titleLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");
        grid.add(titleLabel, 0, 0, 2, 1);

        // Create Admin Section
        Label userIdLabel = new Label("User ID:");
        TextField userIdField = new TextField();
        Button promoteToAdminButton = new Button("Promote to Admin");
        promoteToAdminButton.setOnAction(event -> {
            try {
                int userId = Integer.parseInt(userIdField.getText());
                adminService.promoteUserToAdmin(userId);
                showAlert(Alert.AlertType.INFORMATION, "User promoted successfully!");
            } catch (NumberFormatException e) {
                showAlert(Alert.AlertType.ERROR, "Invalid User ID. Please enter a valid integer.");
            } catch (Exception e) {
                showAlert(Alert.AlertType.ERROR, "Error promoting user to admin: " + e.getMessage());
            }
        });

        HBox promoteBox = new HBox(10, userIdLabel, userIdField, promoteToAdminButton);
        promoteBox.setAlignment(Pos.CENTER);
        grid.add(promoteBox, 0, 1, 2, 1);

        // Delete User Section
        Label deleteUserIdLabel = new Label("User ID:");
        TextField deleteUserIdField = new TextField();
        Button deleteUserButton = new Button("Delete User");
        deleteUserButton.setOnAction(event -> {
            try {
                int userId = Integer.parseInt(deleteUserIdField.getText());
                adminService.deleteUserById(userId);
                showAlert(Alert.AlertType.INFORMATION, "User deleted successfully!");
            } catch (NumberFormatException e) {
                showAlert(Alert.AlertType.ERROR, "Invalid User ID. Please enter a valid integer.");
            } catch (Exception e) {
                showAlert(Alert.AlertType.ERROR, "Error deleting user: " + e.getMessage());
            }
        });

        HBox deleteBox = new HBox(10, deleteUserIdLabel, deleteUserIdField, deleteUserButton);
        deleteBox.setAlignment(Pos.CENTER);
        grid.add(deleteBox, 0, 2, 2, 1);

        // Empty Database Section
        Button emptyDatabaseButton = new Button("Empty Database");
        emptyDatabaseButton.setOnAction(event -> {
            try {
                Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to empty the database?", ButtonType.YES, ButtonType.NO);
                confirmAlert.setHeaderText(null);
                confirmAlert.setTitle("Confirm Action");
                confirmAlert.showAndWait().ifPresent(response -> {
                    if (response == ButtonType.YES) {
                        adminService.emptyDatabase();
                        showAlert(Alert.AlertType.INFORMATION, "Database emptied successfully!");
                    }
                });
            } catch (Exception e) {
                showAlert(Alert.AlertType.ERROR, "Error emptying the database: " + e.getMessage());
            }
        });

        HBox emptyDatabaseBox = new HBox(10, emptyDatabaseButton);
        emptyDatabaseBox.setAlignment(Pos.CENTER);
        grid.add(emptyDatabaseBox, 0, 3, 2, 1);

        // Set the Scene
        Scene scene = new Scene(grid, 600, 400);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void showAlert(Alert.AlertType alertType, String message) {
        Alert alert = new Alert(alertType);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }

    public VBox createAdminDashboardPane(Stage primaryStage) {
        return null;
    }
}