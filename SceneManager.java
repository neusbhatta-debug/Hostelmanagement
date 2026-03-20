package hostelmanagement.app;

import hostelmanagement.view.AdminDashboardView;
import hostelmanagement.view.FeeManagementView;
import hostelmanagement.view.LoginView;
import hostelmanagement.view.MaintenanceManagementView;
import hostelmanagement.view.RoomManagementView;
import hostelmanagement.view.StudentDashboardView;
import hostelmanagement.view.StudentManagementView;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class SceneManager {
    private final Stage stage;

    public SceneManager(Stage stage) {
        this.stage = stage;
    }

    public void showLoginView() {
        stage.setScene(new LoginView(this).createScene());
    }

    public void showAdminDashboard() {
        stage.setScene(new AdminDashboardView(this).createScene());
    }

    public void showStudentDashboard() {
        stage.setScene(new StudentDashboardView(this).createScene());
    }

    public void showStudentManagement() {
        stage.setScene(new StudentManagementView(this).createScene());
    }

    public void showRoomManagement() {
        stage.setScene(new RoomManagementView(this).createScene());
    }

    public void showFeeManagement() {
        stage.setScene(new FeeManagementView(this).createScene());
    }

    public void showMaintenanceManagement() {
        stage.setScene(new MaintenanceManagementView(this).createScene());
    }
}
