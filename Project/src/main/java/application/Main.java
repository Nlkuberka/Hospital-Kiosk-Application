package application;

import database.DBController;

import javafx.application.Application;
import javafx.stage.Stage;


import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        UIController controller = new UIController(primaryStage);

        controller.goToScene(UIController.PATHFINDING_MAIN);


        System.out.println("Collaborator is " + "X");

        Connection conn = DBController.dbConnect();
        DatabaseMetaData dbmd = conn.getMetaData();
        ResultSet rs = dbmd.getTables(null, null, "RESERVATIONS",null);
        if(!rs.next()){
            DBController.initializeAppDB();
        }

        CurrentUser.currentAlgorithm = CurrentUser.AALOGRITHM;
        //DBController.initializeAppDB();
    }

    public static void main(String[] args) {
        launch(args);
    }
}