import javafx.application.Application;
import javafx.stage.Stage;
import java.sql.*;
import java.util.LinkedList;
import java.util.List;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Controller controller = new Controller();
        controller.goToView(primaryStage);
    }

    public static void main(String[] args) {
        launch(args);
    }
}