import javafx.application.Application;
import javafx.stage.Stage;
import java.sql.*;

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