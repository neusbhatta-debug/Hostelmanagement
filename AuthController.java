package hostelmanagement.controller;

import hostelmanagement.app.AppState;
import hostelmanagement.app.SceneManager;
import hostelmanagement.db.UserDAO;
import hostelmanagement.model.User;
import hostelmanagement.util.AlertHelper;

public class AuthController {
    private final SceneManager sceneManager;
    private final UserDAO userDAO;

    public AuthController(SceneManager sceneManager) {
        this.sceneManager = sceneManager;
        this.userDAO = new UserDAO();
    }

    public void login(String username, String password, String role) {
        User user = userDAO.login(username, password, role);

        if (user == null) {
            AlertHelper.showError("Login Failed", "Invalid username, password, or role.");
            return;
        }

        AppState.setCurrentUser(user);
        AlertHelper.showInformation("Login Successful", "Welcome, " + user.getUsername() + "!");

        if ("ADMIN".equalsIgnoreCase(user.getRole())) {
            sceneManager.showAdminDashboard();
        } else {
            sceneManager.showStudentDashboard();
        }
    }

    public void logout() {
        AppState.clearSession();
        sceneManager.showLoginView();
    }
}
