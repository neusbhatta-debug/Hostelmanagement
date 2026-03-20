package hostelmanagement.view;

import hostelmanagement.app.SceneManager;
import hostelmanagement.controller.AuthController;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.GridPane;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class LoginView {
    private final SceneManager sceneManager;
    private final AuthController authController;

    public LoginView(SceneManager sceneManager) {
        this.sceneManager = sceneManager;
        this.authController = new AuthController(sceneManager);
    }

    public Scene createScene() {
        Label titleLabel = new Label("Hostel Management System");
        titleLabel.getStyleClass().add("page-title");

        Label subtitleLabel = new Label("Login as Admin or Student");
        subtitleLabel.getStyleClass().add("subtitle");

        TextField usernameField = new TextField();
        usernameField.setPromptText("Enter username");

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Enter password");

        ComboBox<String> roleComboBox = new ComboBox<>();
        roleComboBox.getItems().addAll("ADMIN", "STUDENT");
        roleComboBox.setValue("ADMIN");
        roleComboBox.setMaxWidth(Double.MAX_VALUE);

        Button loginButton = new Button("Login");
        loginButton.getStyleClass().add("primary-button");
        loginButton.setMaxWidth(Double.MAX_VALUE);

        Label hintLabel = new Label("Demo Accounts: admin/admin123, sita/sita123, ram/ram123");
        hintLabel.getStyleClass().add("hint-label");

        loginButton.setOnAction(event -> authController.login(
                usernameField.getText().trim(),
                passwordField.getText().trim(),
                roleComboBox.getValue()
        ));

        GridPane formGrid = new GridPane();
        formGrid.setHgap(12);
        formGrid.setVgap(12);
        formGrid.add(new Label("Username"), 0, 0);
        formGrid.add(usernameField, 1, 0);
        formGrid.add(new Label("Password"), 0, 1);
        formGrid.add(passwordField, 1, 1);
        formGrid.add(new Label("Role"), 0, 2);
        formGrid.add(roleComboBox, 1, 2);
        formGrid.add(loginButton, 1, 3);

        VBox card = new VBox(15, titleLabel, subtitleLabel, formGrid, hintLabel);
        card.setAlignment(Pos.CENTER);
        card.setPadding(new Insets(30));
        card.getStyleClass().add("login-card");
        card.setMaxWidth(500);

        StackPane root = new StackPane(card);
        root.setPadding(new Insets(40));
        root.getStyleClass().add("login-root");

        Scene scene = new Scene(root, 1000, 650);
        scene.getStylesheets().add(getClass().getResource("/hostelmanagement/resources/css/login.css").toExternalForm());
        return scene;
    }
}
