import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.sql.SQLException;
import java.util.List;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        User.userID = "Guest";
        User.permissions = User.GUEST_PERMISSIONS;
        UIController controller = new UIController(primaryStage);
        controller.goToScene(UIController.ADMIN_TOOLS_MAIN);

        System.out.println("Collaborator is " + "X");
    }

    public static void main(String[] args) {
        launch(args);
    }
}