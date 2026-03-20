package hostelmanagement.view;

import hostelmanagement.app.SceneManager;
import hostelmanagement.controller.StudentController;
import hostelmanagement.db.RoomDAO;
import hostelmanagement.model.Room;
import hostelmanagement.model.Student;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class StudentManagementView {
    private final SceneManager sceneManager;
    private final StudentController studentController;
    private final RoomDAO roomDAO;

    public StudentManagementView(SceneManager sceneManager) {
        this.sceneManager = sceneManager;
        this.studentController = new StudentController();
        this.roomDAO = new RoomDAO();
    }

    public Scene createScene() {
        Label titleLabel = new Label("Student Management");
        titleLabel.getStyleClass().add("page-title");

        TableView<Student> studentTable = new TableView<>();
        studentTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<Student, Integer> idColumn = new TableColumn<>("ID");
        idColumn.setCellValueFactory(data -> new SimpleIntegerProperty(data.getValue().getId()).asObject());

        TableColumn<Student, String> nameColumn = new TableColumn<>("Name");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<Student, String> rollColumn = new TableColumn<>("Roll");
        rollColumn.setCellValueFactory(new PropertyValueFactory<>("roll"));

        TableColumn<Student, String> contactColumn = new TableColumn<>("Contact");
        contactColumn.setCellValueFactory(new PropertyValueFactory<>("contact"));

        TableColumn<Student, String> roomColumn = new TableColumn<>("Room");
        roomColumn.setCellValueFactory(data -> new SimpleStringProperty(roomDAO.getRoomNumberById(data.getValue().getRoomId())));

        TableColumn<Student, String> usernameColumn = new TableColumn<>("Username");
        usernameColumn.setCellValueFactory(new PropertyValueFactory<>("username"));

        studentTable.getColumns().addAll(idColumn, nameColumn, rollColumn, contactColumn, roomColumn, usernameColumn);
        refreshTable(studentTable);

        TextField nameField = new TextField();
        TextField rollField = new TextField();
        TextField contactField = new TextField();
        TextField usernameField = new TextField();
        PasswordField passwordField = new PasswordField();

        nameField.setPromptText("Name");
        rollField.setPromptText("Roll");
        contactField.setPromptText("Contact");
        usernameField.setPromptText("Username");
        passwordField.setPromptText("Password");

        ComboBox<Room> roomComboBox = new ComboBox<>();
        refreshRoomCombo(roomComboBox);

        Button addButton = new Button("Add Student");
        Button updateButton = new Button("Update Student");
        Button deleteButton = new Button("Delete Student");
        Button refreshButton = new Button("Refresh");
        Button backButton = new Button("Back");

        addButton.getStyleClass().add("primary-button");
        updateButton.getStyleClass().add("secondary-button");
        deleteButton.getStyleClass().add("danger-button");
        refreshButton.getStyleClass().add("secondary-button");
        backButton.getStyleClass().add("secondary-button");

        final String[] oldUsername = {null};

        studentTable.getSelectionModel().selectedItemProperty().addListener((obs, oldValue, selected) -> {
            if (selected != null) {
                nameField.setText(selected.getName());
                rollField.setText(selected.getRoll());
                contactField.setText(selected.getContact());
                usernameField.setText(selected.getUsername());
                passwordField.setText(selected.getPassword());
                oldUsername[0] = selected.getUsername();

                refreshRoomCombo(roomComboBox);
                if (selected.getRoomId() != null) {
                    Room currentRoom = roomDAO.getRoomById(selected.getRoomId());
                    if (currentRoom != null && !roomComboBox.getItems().contains(currentRoom)) {
                        roomComboBox.getItems().add(0, currentRoom);
                    }
                    roomComboBox.setValue(currentRoom);
                } else {
                    roomComboBox.setValue(null);
                }
            }
        });

        addButton.setOnAction(event -> {
            Integer roomId = roomComboBox.getValue() == null ? null : roomComboBox.getValue().getId();
            Student student = new Student(0, nameField.getText().trim(), rollField.getText().trim(),
                    contactField.getText().trim(), roomId, usernameField.getText().trim(), passwordField.getText().trim());
            studentController.addStudent(student);
            clearForm(nameField, rollField, contactField, usernameField, passwordField, roomComboBox, oldUsername);
            refreshTable(studentTable);
            refreshRoomCombo(roomComboBox);
        });

        updateButton.setOnAction(event -> {
            Student selected = studentTable.getSelectionModel().getSelectedItem();
            if (selected != null) {
                Integer roomId = roomComboBox.getValue() == null ? null : roomComboBox.getValue().getId();
                Student updatedStudent = new Student(selected.getId(), nameField.getText().trim(), rollField.getText().trim(),
                        contactField.getText().trim(), roomId, usernameField.getText().trim(), passwordField.getText().trim());
                studentController.updateStudent(updatedStudent, oldUsername[0]);
                clearForm(nameField, rollField, contactField, usernameField, passwordField, roomComboBox, oldUsername);
                refreshTable(studentTable);
                refreshRoomCombo(roomComboBox);
            }
        });

        deleteButton.setOnAction(event -> {
            studentController.deleteStudent(studentTable.getSelectionModel().getSelectedItem());
            clearForm(nameField, rollField, contactField, usernameField, passwordField, roomComboBox, oldUsername);
            refreshTable(studentTable);
            refreshRoomCombo(roomComboBox);
        });

        refreshButton.setOnAction(event -> {
            refreshTable(studentTable);
            refreshRoomCombo(roomComboBox);
        });

        backButton.setOnAction(event -> sceneManager.showAdminDashboard());

        GridPane formGrid = new GridPane();
        formGrid.setHgap(10);
        formGrid.setVgap(10);
        formGrid.add(new Label("Name"), 0, 0);
        formGrid.add(nameField, 1, 0);
        formGrid.add(new Label("Roll"), 0, 1);
        formGrid.add(rollField, 1, 1);
        formGrid.add(new Label("Contact"), 0, 2);
        formGrid.add(contactField, 1, 2);
        formGrid.add(new Label("Room"), 0, 3);
        formGrid.add(roomComboBox, 1, 3);
        formGrid.add(new Label("Username"), 0, 4);
        formGrid.add(usernameField, 1, 4);
        formGrid.add(new Label("Password"), 0, 5);
        formGrid.add(passwordField, 1, 5);

        VBox rightPane = new VBox(15, titleLabel, formGrid, new HBox(10, addButton, updateButton, deleteButton, refreshButton, backButton));
        rightPane.setPadding(new Insets(20));
        rightPane.setPrefWidth(420);

        BorderPane root = new BorderPane();
        root.setLeft(studentTable);
        root.setCenter(rightPane);
        root.setPadding(new Insets(20));
        BorderPane.setMargin(studentTable, new Insets(0, 20, 0, 0));
        root.getStyleClass().add("module-root");

        Scene scene = new Scene(root, 1200, 700);
        scene.getStylesheets().add(getClass().getResource("/hostelmanagement/resources/css/module.css").toExternalForm());
        return scene;
    }

    private void refreshTable(TableView<Student> table) {
        table.setItems(studentController.getStudents());
    }

    private void refreshRoomCombo(ComboBox<Room> roomComboBox) {
        roomComboBox.getItems().clear();
        roomComboBox.getItems().addAll(roomDAO.getAvailableRooms());
        roomComboBox.setPromptText("Select Room (Optional)");
    }

    private void clearForm(TextField nameField, TextField rollField, TextField contactField,
                           TextField usernameField, PasswordField passwordField,
                           ComboBox<Room> roomComboBox, String[] oldUsername) {
        nameField.clear();
        rollField.clear();
        contactField.clear();
        usernameField.clear();
        passwordField.clear();
        roomComboBox.setValue(null);
        oldUsername[0] = null;
    }
}
