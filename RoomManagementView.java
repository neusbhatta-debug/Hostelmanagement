package hostelmanagement.view;

import hostelmanagement.app.SceneManager;
import hostelmanagement.controller.RoomController;
import hostelmanagement.db.StudentDAO;
import hostelmanagement.model.Room;
import hostelmanagement.model.Student;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class RoomManagementView {
    private final SceneManager sceneManager;
    private final RoomController roomController;
    private final StudentDAO studentDAO;

    public RoomManagementView(SceneManager sceneManager) {
        this.sceneManager = sceneManager;
        this.roomController = new RoomController();
        this.studentDAO = new StudentDAO();
    }

    public Scene createScene() {
        Label titleLabel = new Label("Room Management");
        titleLabel.getStyleClass().add("page-title");

        TableView<Room> roomTable = new TableView<>();
        roomTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<Room, Integer> idColumn = new TableColumn<>("ID");
        idColumn.setCellValueFactory(data -> new SimpleIntegerProperty(data.getValue().getId()).asObject());

        TableColumn<Room, String> roomNoColumn = new TableColumn<>("Room No");
        roomNoColumn.setCellValueFactory(new PropertyValueFactory<>("roomNo"));

        TableColumn<Room, String> statusColumn = new TableColumn<>("Status");
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));

        roomTable.getColumns().addAll(idColumn, roomNoColumn, statusColumn);
        refreshTable(roomTable);

        TextField roomNoField = new TextField();
        roomNoField.setPromptText("Room Number");

        ComboBox<Student> studentComboBox = new ComboBox<>();
        studentComboBox.getItems().addAll(studentDAO.getAllStudents());
        studentComboBox.setPromptText("Select Student");

        ComboBox<Room> roomComboBox = new ComboBox<>();
        refreshRoomCombo(roomComboBox);

        Button addRoomButton = new Button("Add Room");
        Button allocateButton = new Button("Allocate Room");
        Button refreshButton = new Button("Refresh");
        Button backButton = new Button("Back");

        addRoomButton.getStyleClass().add("primary-button");
        allocateButton.getStyleClass().add("secondary-button");
        refreshButton.getStyleClass().add("secondary-button");
        backButton.getStyleClass().add("secondary-button");

        Label summaryLabel = new Label();
        updateSummary(summaryLabel);

        addRoomButton.setOnAction(event -> {
            roomController.addRoom(roomNoField.getText().trim());
            roomNoField.clear();
            refreshTable(roomTable);
            refreshRoomCombo(roomComboBox);
            updateSummary(summaryLabel);
        });

        allocateButton.setOnAction(event -> {
            Student selectedStudent = studentComboBox.getValue();
            Room selectedRoom = roomComboBox.getValue();
            if (selectedStudent != null) {
                roomController.allocateRoom(selectedStudent.getId(), selectedRoom);
                refreshTable(roomTable);
                refreshRoomCombo(roomComboBox);
                studentComboBox.getItems().setAll(studentDAO.getAllStudents());
                updateSummary(summaryLabel);
            }
        });

        refreshButton.setOnAction(event -> {
            refreshTable(roomTable);
            refreshRoomCombo(roomComboBox);
            studentComboBox.getItems().setAll(studentDAO.getAllStudents());
            updateSummary(summaryLabel);
        });

        backButton.setOnAction(event -> sceneManager.showAdminDashboard());

        GridPane formGrid = new GridPane();
        formGrid.setHgap(10);
        formGrid.setVgap(10);
        formGrid.add(new Label("New Room"), 0, 0);
        formGrid.add(roomNoField, 1, 0);
        formGrid.add(new Label("Student"), 0, 1);
        formGrid.add(studentComboBox, 1, 1);
        formGrid.add(new Label("Available Room"), 0, 2);
        formGrid.add(roomComboBox, 1, 2);

        VBox rightPane = new VBox(15, titleLabel, summaryLabel, formGrid, new HBox(10, addRoomButton, allocateButton, refreshButton, backButton));
        rightPane.setPadding(new Insets(20));
        rightPane.setPrefWidth(420);

        BorderPane root = new BorderPane();
        root.setLeft(roomTable);
        root.setCenter(rightPane);
        root.setPadding(new Insets(20));
        BorderPane.setMargin(roomTable, new Insets(0, 20, 0, 0));
        root.getStyleClass().add("module-root");

        Scene scene = new Scene(root, 1150, 700);
        scene.getStylesheets().add(getClass().getResource("/hostelmanagement/resources/css/module.css").toExternalForm());
        return scene;
    }

    private void refreshTable(TableView<Room> table) {
        table.setItems(roomController.getRooms());
    }

    private void refreshRoomCombo(ComboBox<Room> comboBox) {
        comboBox.getItems().setAll(roomController.getAvailableRooms());
        comboBox.setPromptText("Available Room");
    }

    private void updateSummary(Label label) {
        long total = roomController.getRooms().size();
        long occupied = roomController.getRooms().stream().filter(r -> "OCCUPIED".equalsIgnoreCase(r.getStatus())).count();
        long available = roomController.getRooms().stream().filter(r -> "AVAILABLE".equalsIgnoreCase(r.getStatus())).count();
        label.setText("Total: " + total + " | Occupied: " + occupied + " | Available: " + available);
    }
}
