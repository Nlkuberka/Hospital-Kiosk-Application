import javafx.application.Application;
import javafx.stage.Stage;

import java.util.List;


public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Controller controller = new Controller(primaryStage);
        DBController dbController = new DBController();

        List<Node> list = CSVHandler.readFile("PrototypeNodes.csv");
        dbController.enterData(list);
        System.out.println(dbController);
        System.out.println(dbController.connection);
        controller.setDBController(dbController);

        controller.goToScene(controller.VIEW_STRING);
    }

    public static void main(String[] args) {
        launch(args);
    }
}