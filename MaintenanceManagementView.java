package hostelmanagement.view;

import hostelmanagement.app.AppState;
import hostelmanagement.app.SceneManager;
import hostelmanagement.controller.MaintenanceController;
import hostelmanagement.model.MaintenanceRequest;
import hostelmanagement.model.Student;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class MaintenanceManagementView {
    private final SceneManager sceneManager;
    private final MaintenanceController maintenanceController;

    public MaintenanceManagementView(SceneManager sceneManager) {
        this.sceneManager = sceneManager;
        this.maintenanceController = new MaintenanceController();
    }

    public Scene createScene() {
        Label titleLabel = new Label("Maintenance Management");
        titleLabel.getStyleClass().add("page-title");

        TableView<MaintenanceRequest> requestTable = new TableView<>();
        requestTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<MaintenanceRequest, Integer> idColumn = new TableColumn<>("Request ID");
        idColumn.setCellValueFactory(data -> new SimpleIntegerProperty(data.getValue().getRequestId()).asObject());

        TableColumn<MaintenanceRequest, Integer> studentIdColumn = new TableColumn<>("Student ID");
        studentIdColumn.setCellValueFactory(data -> new SimpleIntegerProperty(data.getValue().getStudentId()).asObject());

        TableColumn<MaintenanceRequest, String> descriptionColumn = new TableColumn<>("Description");
        descriptionColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getDescription()));

        TableColumn<MaintenanceRequest, String> statusColumn = new TableColumn<>("Status");
        statusColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getStatus()));

        requestTable.getColumns().addAll(idColumn, studentIdColumn, descriptionColumn, statusColumn);

        VBox rightPane;

        if (AppState.isAdminLoggedIn()) {
            refreshAdminTable(requestTable);

            ComboBox<String> statusComboBox = new ComboBox<>();
            statusComboBox.getItems().addAll("PENDING", "IN_PROGRESS", "RESOLVED");
            statusComboBox.setValue("IN_PROGRESS");

            Button updateButton = new Button("Update Status");
            Button refreshButton = new Button("Refresh");
            Button backButton = new Button("Back");

            updateButton.getStyleClass().add("primary-button");
            refreshButton.getStyleClass().add("secondary-button");
            backButton.getStyleClass().add("secondary-button");

            updateButton.setOnAction(event -> {
                maintenanceController.updateStatus(requestTable.getSelectionModel().getSelectedItem(), statusComboBox.getValue());
                refreshAdminTable(requestTable);
            });

            refreshButton.setOnAction(event -> refreshAdminTable(requestTable));
            backButton.setOnAction(event -> sceneManager.showAdminDashboard());

            rightPane = new VBox(15, titleLabel, new Label("Select a request and update its status."), statusComboBox, new HBox(10, updateButton, refreshButton, backButton));
        } else {
            Student student = AppState.getLoggedInStudent();
            refreshStudentTable(requestTable, student);

            TextArea descriptionArea = new TextArea();
            descriptionArea.setPromptText("Describe your complaint");
            descriptionArea.setWrapText(true);
            descriptionArea.setPrefRowCount(6);

            Button submitButton = new Button("Submit Complaint");
            Button refreshButton = new Button("Refresh");
            Button backButton = new Button("Back");

            submitButton.getStyleClass().add("primary-button");
            refreshButton.getStyleClass().add("secondary-button");
            backButton.getStyleClass().add("secondary-button");

            submitButton.setOnAction(event -> {
                if (student != null) {
                    maintenanceController.submitRequest(student.getId(), descriptionArea.getText().trim());
                    descriptionArea.clear();
                    refreshStudentTable(requestTable, student);
                }
            });

            refreshButton.setOnAction(event -> refreshStudentTable(requestTable, AppState.getLoggedInStudent()));
            backButton.setOnAction(event -> sceneManager.showStudentDashboard());

            rightPane = new VBox(15, titleLabel, descriptionArea, new HBox(10, submitButton, refreshButton, backButton));
        }

        rightPane.setPadding(new Insets(20));
        rightPane.setPrefWidth(360);

        BorderPane root = new BorderPane();
        root.setLeft(requestTable);
        root.setCenter(rightPane);
        root.setPadding(new Insets(20));
        BorderPane.setMargin(requestTable, new Insets(0, 20, 0, 0));
        root.getStyleClass().add("module-root");

        Scene scene = new Scene(root, 1150, 700);
        scene.getStylesheets().add(getClass().getResource("/hostelmanagement/resources/css/module.css").toExternalForm());
        return scene;
    }

    private void refreshAdminTable(TableView<MaintenanceRequest> table) {
        table.setItems(maintenanceController.getAllRequests());
    }

    private void refreshStudentTable(TableView<MaintenanceRequest> table, Student student) {
        if (student != null) {
            table.setItems(maintenanceController.getRequestsByStudentId(student.getId()));
        }
    }
}
