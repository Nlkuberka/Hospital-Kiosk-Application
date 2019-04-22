package edu.wpi.cs3733.d19.teamD;

import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class Babysitting extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Connection conn = DBControllerAPI.dbConnect();
        DBControllerAPI.initializeAppDB(conn);
        DBControllerAPI.closeConnection(conn);

        run(0,0,600,600,"/css/background.css");

    }


    public void run(int xcoord, int ycoord, int windowWidth, int windowLength, String cssPath) throws Exception {
        Stage primaryStage = new Stage();
        primaryStage.setX(xcoord);
        primaryStage.setY(ycoord);
        primaryStage.setTitle("Service Request - Babysitter");

        UIController controller = new UIController(primaryStage,windowWidth,windowLength);
        controller.goToScene(UIController.SERVICE_REQUEST_MAIN,cssPath);
    }



    public static void main(String[] args) {
        launch(args);
    }
}