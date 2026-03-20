package hostelmanagement;

import hostelmanagement.app.SceneManager;
import hostelmanagement.db.DatabaseInitializer;
import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        DatabaseInitializer.initializeDatabase();

        SceneManager sceneManager = new SceneManager(primaryStage);
        sceneManager.showLoginView();

        primaryStage.setTitle("Hostel Management System");
        primaryStage.setMinWidth(1100);
        primaryStage.setMinHeight(700);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
