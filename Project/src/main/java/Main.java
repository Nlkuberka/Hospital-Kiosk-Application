
import javafx.application.Application;
import javafx.stage.Stage;

import java.sql.Connection;





public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        CurrentUser.userID = "Guest";
        CurrentUser.permissions = User.GUEST_PERMISSIONS;
        UIController controller = new UIController(primaryStage);
        controller.goToScene(UIController.RESERVATIONS_MAIN);

        System.out.println("Collaborator is " + "X");

// if Database not loaded use Create tables on the Drive, do not push binary files


        Connection conn = DBController.dbConnect();

    }

    public static void main(String[] args) {
        launch(args);
    }
}