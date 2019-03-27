import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.sql.SQLException;
import java.util.List;


public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Controller controller = new Controller(primaryStage);
        DBController dbController = new DBController();

//        List<Node> list = CSVHandler.readFile("PrototypeNodes.csv");
//        dbController.enterData(list);
        controller.setDBController(dbController);

        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {

                try {
                    dbController.connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }

            }
        });
        controller.goToScene(controller.VIEW_STRING);
    }

    public static void main(String[] args) {
        launch(args);
    }
}