package hostelmanagement.view;

import hostelmanagement.app.AppState;
import hostelmanagement.app.SceneManager;
import hostelmanagement.controller.AuthController;
import hostelmanagement.db.RoomDAO;
import hostelmanagement.model.Student;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

public class StudentDashboardView {
    private final SceneManager sceneManager;

    public StudentDashboardView(SceneManager sceneManager) {
        this.sceneManager = sceneManager;
    }

    public Scene createScene() {
        Student student = AppState.getLoggedInStudent();
        RoomDAO roomDAO = new RoomDAO();
        String roomText = student == null ? "Not Assigned" : roomDAO.getRoomNumberById(student.getRoomId());

        Label titleLabel = new Label("Student Dashboard");
        titleLabel.getStyleClass().add("page-title");

        Label welcomeLabel = new Label("Welcome, " + (student != null ? student.getName() : "Student"));
        Label rollLabel = new Label("Roll: " + (student != null ? student.getRoll() : "-"));
        Label roomLabel = new Label("Room: " + roomText);

        Button feeButton = new Button("View Fee Status");
        Button maintenanceButton = new Button("Maintenance / Complaints");
        Button logoutButton = new Button("Logout");

        feeButton.getStyleClass().add("dashboard-button");
        maintenanceButton.getStyleClass().add("dashboard-button");
        logoutButton.getStyleClass().add("danger-button");

        feeButton.setOnAction(event -> sceneManager.showFeeManagement());
        maintenanceButton.setOnAction(event -> sceneManager.showMaintenanceManagement());
        logoutButton.setOnAction(event -> new AuthController(sceneManager).logout());

        VBox card = new VBox(15, titleLabel, welcomeLabel, rollLabel, roomLabel, feeButton, maintenanceButton, logoutButton);
        card.setAlignment(Pos.CENTER);
        card.setPadding(new Insets(30));
        card.getStyleClass().add("dashboard-card");

        BorderPane root = new BorderPane();
        root.setCenter(card);
        root.setPadding(new Insets(30));
        root.getStyleClass().add("dashboard-root");

        Scene scene = new Scene(root, 1100, 700);
        scene.getStylesheets().add(getClass().getResource("/hostelmanagement/resources/css/dashboard.css").toExternalForm());
        return scene;
    }
}
