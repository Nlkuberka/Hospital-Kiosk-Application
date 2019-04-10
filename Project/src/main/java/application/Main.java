package application;

import entities.User;

import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        UIController controller = new UIController(primaryStage);

        controller.goToScene(UIController.ADMIN_TOOLS_VIEW_USERS);


        System.out.println("Collaborator is " + "X");

        //DBController.initializeAppDB();
    }

    public static void main(String[] args) {
        launch(args);
    }
}