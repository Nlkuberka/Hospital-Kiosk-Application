import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.sql.SQLException;
import java.util.List;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        UIController controller = new UIController(primaryStage);

        List<Node> list = ;


        controller.goToScene(UIController.SERVICE_REQUEST_BABYSITTING);
    }


    public void run(int xcoord, int ycoord, int windowWidth, int windowLength, String cssPath, String destNodeID, String originNodeID) throws Exception {

        AnchorPane root = (AnchorPane) new UIControllerSRB(destNodeID, originNodeID).getContentView();

        Stage primaryStage = new Stage();
        primaryStage.setX(xcoord);
        primaryStage.setY(ycoord);
        primaryStage.setTitle("Service Request - Babysitter");

        Scene mainScene = new Scene(root, windowWidth, windowLength);
        final ObservableList<String> stylesheets = mainScene.getStylesheets();
        stylesheets.addAll(getClass().getResource(cssPath).toExternalForm());

        primaryStage.setScene(mainScene);
        primaryStage.show();
    }



    public static void main(String[] args) {
        launch(args);
    }
}