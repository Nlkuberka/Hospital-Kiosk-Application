import javafx.application.Application;
import javafx.stage.Stage;
import java.util.*;
import java.sql.Connection;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        CurrentUser.userID = "Guest";
        CurrentUser.permissions = User.GUEST_PERMISSIONS;
        UIController userIntController = new UIController(primaryStage);
        userIntController.goToScene(UIController.PATHFINDING_MAIN);

        System.out.println("Collaborator is " + "X");

        DBController dbController = new DBController();
        Connection connection = DBController.dbConnect();

        List<Node> list = CSVHandler.readFile("PrototypeNodes.csv");
        dbController.enterData(list, connection);
    }


    public static void main(String[] args) {
        launch(args);
    }
}