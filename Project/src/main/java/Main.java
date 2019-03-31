import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        CurrentUser.userID = "Guest";
        CurrentUser.permissions = User.GUEST_PERMISSIONS;
        UIController controller = new UIController(primaryStage);
        controller.goToScene(UIController.SERVICE_REQUEST_MAIN);

        System.out.println("Collaborator is " + "X");
    }

    public static void main(String[] args) {
        launch(args);
    }
}