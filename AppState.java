package hostelmanagement.app;

import hostelmanagement.model.Admin;
import hostelmanagement.model.Student;
import hostelmanagement.model.User;

public class AppState {
    private static User currentUser;

    private AppState() {
    }

    public static void setCurrentUser(User user) {
        currentUser = user;
    }

    public static User getCurrentUser() {
        return currentUser;
    }

    public static boolean isAdminLoggedIn() {
        return currentUser instanceof Admin;
    }

    public static boolean isStudentLoggedIn() {
        return currentUser instanceof Student;
    }

    public static Student getLoggedInStudent() {
        if (currentUser instanceof Student) {
            return (Student) currentUser;
        }
        return null;
    }

    public static void clearSession() {
        currentUser = null;
    }
}
