package application;

import entities.User;

import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        UIController controller = new UIController(primaryStage);

        controller.goToScene(UIController.LOGIN_MAIN);

        System.out.println("Collaborator is " + "X");
        CurrentUser.currentAlgorithm = CurrentUser.AALOGRITHM;
        //DBController.initializeAppDB();
    }

    public static void main(String[] args) {
        launch(args);
    }
}