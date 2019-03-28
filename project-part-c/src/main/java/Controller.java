import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.Map;
import java.util.HashMap;

public class Controller {
    public static final String VIEW_STRING = "VIEW";
    public static final String EDIT_STRING= "EDIT";
    public static final String DOWNLOAD_STRING = "DOWNLOAD";

    private static final int WIDTH = 700;
    private static final int HEIGHT = 700;

    private static Stage primaryStage;
    private static Map<String, Scene> scenes;
    private static Map<String, Controller> sceneControllers;
    private static Map<String, String> sceneFiles;
    private static Map<String, String> sceneTitles;

    protected static DBController dbController;

    public Controller() {

    }

    public Controller(Stage stage) {
        primaryStage = stage;
        setLists();
    }

    private void setLists() {
        scenes = new HashMap<String, Scene>();
        sceneControllers = new HashMap<String, Controller>();
        sceneFiles = new HashMap<String, String>();
        sceneTitles = new HashMap<String, String>();

        sceneFiles.put(this.VIEW_STRING, "view.fxml");
        sceneTitles.put(this.VIEW_STRING, "Database Viewer");

        sceneFiles.put(this.EDIT_STRING, "editNode.fxml");
        sceneTitles.put(this.EDIT_STRING, "Database Node Editor");

        sceneFiles.put(this.DOWNLOAD_STRING, "download.fxml");
        sceneTitles.put(this.DOWNLOAD_STRING, "Database Download");
    }

    @FXML
    protected Controller goToScene(String sceneString) {
        Scene scene = scenes.get(sceneString);

        if(scene == null) {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(sceneFiles.get(sceneString)));
                Parent root = fxmlLoader.load();
                sceneControllers.put(sceneString, fxmlLoader.getController());
                scenes.put(sceneString, new Scene(root, WIDTH, HEIGHT));
                scene = scenes.get(sceneString);
            } catch(Exception e) {
                e.printStackTrace();
            }
        }

        primaryStage.setTitle(sceneTitles.get(sceneString));
        primaryStage.setScene(scene);
        primaryStage.show();

        return sceneControllers.get(sceneString);
    }

    public void setDBController(DBController dbController) {
        this.dbController = dbController;
    }
}
