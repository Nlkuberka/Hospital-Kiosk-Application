import javafx.application.Application;
import javafx.stage.Stage;

import java.util.List;


public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Controller controller = new Controller();
        controller.goToView(primaryStage);
        DBController dbController = new DBController();
        List<Node> list = CSVHandler.readFile("PrototypeNodes.csv");
        dbController.enterData(list);
    }

    public static void main(String[] args) {
        launch(args);
    }
}