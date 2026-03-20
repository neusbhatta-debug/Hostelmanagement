package hostelmanagement.view;

import hostelmanagement.app.SceneManager;
import hostelmanagement.controller.AuthController;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

public class AdminDashboardView {
    private final SceneManager sceneManager;

    public AdminDashboardView(SceneManager sceneManager) {
        this.sceneManager = sceneManager;
    }

    public Scene createScene() {
        Label titleLabel = new Label("Admin Dashboard");
        titleLabel.getStyleClass().add("page-title");

        Button studentButton = new Button("Student Management");
        Button roomButton = new Button("Room Management");
        Button feeButton = new Button("Fee Management");
        Button maintenanceButton = new Button("Maintenance Management");
        Button logoutButton = new Button("Logout");

        studentButton.getStyleClass().add("dashboard-button");
        roomButton.getStyleClass().add("dashboard-button");
        feeButton.getStyleClass().add("dashboard-button");
        maintenanceButton.getStyleClass().add("dashboard-button");
        logoutButton.getStyleClass().add("danger-button");

        studentButton.setOnAction(event -> sceneManager.showStudentManagement());
        roomButton.setOnAction(event -> sceneManager.showRoomManagement());
        feeButton.setOnAction(event -> sceneManager.showFeeManagement());
        maintenanceButton.setOnAction(event -> sceneManager.showMaintenanceManagement());
        logoutButton.setOnAction(event -> new AuthController(sceneManager).logout());

        VBox menuBox = new VBox(15, titleLabel, studentButton, roomButton, feeButton, maintenanceButton, logoutButton);
        menuBox.setAlignment(Pos.CENTER);
        menuBox.setPadding(new Insets(30));
        menuBox.getStyleClass().add("dashboard-card");

        BorderPane root = new BorderPane();
        root.setCenter(menuBox);
        root.setPadding(new Insets(30));
        root.getStyleClass().add("dashboard-root");

        Scene scene = new Scene(root, 1100, 700);
        scene.getStylesheets().add(getClass().getResource("/hostelmanagement/resources/css/dashboard.css").toExternalForm());
        return scene;
    }
}
