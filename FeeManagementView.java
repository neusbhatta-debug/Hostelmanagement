package hostelmanagement.view;

import hostelmanagement.app.AppState;
import hostelmanagement.app.SceneManager;
import hostelmanagement.controller.FeeController;
import hostelmanagement.model.Fee;
import hostelmanagement.model.Student;
import hostelmanagement.util.AlertHelper;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class FeeManagementView {
    private final SceneManager sceneManager;
    private final FeeController feeController;

    public FeeManagementView(SceneManager sceneManager) {
        this.sceneManager = sceneManager;
        this.feeController = new FeeController();
    }

    public Scene createScene() {
        Label titleLabel = new Label("Fee Management");
        titleLabel.getStyleClass().add("page-title");

        TableView<Fee> feeTable = new TableView<>();
        feeTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<Fee, Integer> idColumn = new TableColumn<>("Fee ID");
        idColumn.setCellValueFactory(data -> new SimpleIntegerProperty(data.getValue().getId()).asObject());

        TableColumn<Fee, Integer> studentIdColumn = new TableColumn<>("Student ID");
        studentIdColumn.setCellValueFactory(data -> new SimpleIntegerProperty(data.getValue().getStudentId()).asObject());

        TableColumn<Fee, Double> amountColumn = new TableColumn<>("Amount");
        amountColumn.setCellValueFactory(data -> new SimpleDoubleProperty(data.getValue().getAmount()).asObject());

        TableColumn<Fee, String> statusColumn = new TableColumn<>("Status");
        statusColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getStatus()));

        feeTable.getColumns().addAll(idColumn, studentIdColumn, amountColumn, statusColumn);

        VBox rightPane;

        if (AppState.isAdminLoggedIn()) {
            refreshAdminTable(feeTable);

            TextField studentIdField = new TextField();
            TextField amountField = new TextField();
            studentIdField.setPromptText("Student ID");
            amountField.setPromptText("Amount");

            ComboBox<String> statusComboBox = new ComboBox<>();
            statusComboBox.getItems().addAll("PAID", "UNPAID");
            statusComboBox.setValue("PAID");

            Button recordButton = new Button("Record Payment");
            Button refreshButton = new Button("Refresh");
            Button backButton = new Button("Back");

            recordButton.getStyleClass().add("primary-button");
            refreshButton.getStyleClass().add("secondary-button");
            backButton.getStyleClass().add("secondary-button");

            recordButton.setOnAction(event -> {
                try {
                    int studentId = Integer.parseInt(studentIdField.getText().trim());
                    double amount = Double.parseDouble(amountField.getText().trim());
                    feeController.recordPayment(studentId, amount, statusComboBox.getValue());
                    refreshAdminTable(feeTable);
                    studentIdField.clear();
                    amountField.clear();
                    statusComboBox.setValue("PAID");
                } catch (NumberFormatException exception) {
                    AlertHelper.showError("Validation Error", "Student ID and Amount must be numbers.");
                }
            });

            refreshButton.setOnAction(event -> refreshAdminTable(feeTable));
            backButton.setOnAction(event -> sceneManager.showAdminDashboard());

            GridPane form = new GridPane();
            form.setHgap(10);
            form.setVgap(10);
            form.add(new Label("Student ID"), 0, 0);
            form.add(studentIdField, 1, 0);
            form.add(new Label("Amount"), 0, 1);
            form.add(amountField, 1, 1);
            form.add(new Label("Status"), 0, 2);
            form.add(statusComboBox, 1, 2);

            rightPane = new VBox(15, titleLabel, form, new HBox(10, recordButton, refreshButton, backButton));
        } else {
            Student student = AppState.getLoggedInStudent();
            refreshStudentTable(feeTable, student);

            Button refreshButton = new Button("Refresh");
            Button backButton = new Button("Back");
            refreshButton.getStyleClass().add("secondary-button");
            backButton.getStyleClass().add("secondary-button");

            refreshButton.setOnAction(event -> refreshStudentTable(feeTable, AppState.getLoggedInStudent()));
            backButton.setOnAction(event -> sceneManager.showStudentDashboard());

            rightPane = new VBox(15, titleLabel, new Label("Your payment history is shown below."), new HBox(10, refreshButton, backButton));
        }

        rightPane.setPadding(new Insets(20));
        rightPane.setPrefWidth(360);

        BorderPane root = new BorderPane();
        root.setLeft(feeTable);
        root.setCenter(rightPane);
        root.setPadding(new Insets(20));
        BorderPane.setMargin(feeTable, new Insets(0, 20, 0, 0));
        root.getStyleClass().add("module-root");

        Scene scene = new Scene(root, 1150, 700);
        scene.getStylesheets().add(getClass().getResource("/hostelmanagement/resources/css/module.css").toExternalForm());
        return scene;
    }

    private void refreshAdminTable(TableView<Fee> table) {
        table.setItems(feeController.getAllFees());
    }

    private void refreshStudentTable(TableView<Fee> table, Student student) {
        if (student != null) {
            table.setItems(feeController.getFeesByStudentId(student.getId()));
        }
    }
}
