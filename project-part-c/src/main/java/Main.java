import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.sql.SQLException;
import java.util.List;


public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Controller controller = new Controller();
        controller.goToView(primaryStage);
        DBController dbController = new DBController();
        List<Node> list = CSVHandler.readFile("PrototypeNodes.csv");
        dbController.enterData(list);
        dbController.exportData("test.csv");

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
    }

    public static void main(String[] args) {
        launch(args);
    }


}