package application;

import entities.User;

import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        UIController controller = new UIController(primaryStage);

<<<<<<< Updated upstream
        controller.goToScene(UIController.LOGIN_MAIN);

        System.out.println("Collaborator is " + "X");
        CurrentUser.currentAlgorithm = CurrentUser.AALOGRITHM;
=======
        controller.goToScene(UIController.ADMIN_TOOLS_MAIN);


        System.out.println("Collaborator is " + "X");

>>>>>>> Stashed changes
        //DBController.initializeAppDB();
    }

    public static void main(String[] args) {
        launch(args);
    }
}