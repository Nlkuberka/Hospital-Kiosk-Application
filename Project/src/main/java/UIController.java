import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.util.Map;
import java.util.HashMap;

public class UIController {
    public static final String LOGIN_MAIN = "LM";
    public static final String MAIN_MENU_MAIN = "MMM";
    public static final String PATHFINDING_MAIN = "PFM";
    public static final String RESERVATIONS_MAIN = "RVM";
    public static final String SERVICE_REQUEST_MAIN = "SRM";
    public static final String ADMIN_TOOLS_MAIN = "ATM";
    public static final String ADMIN_TOOLS_VIEW_NODES = "ATVN";
    public static final String ADMIN_TOOLS_VIEW_EDGES = "ATVE";

    private static final int WIDTH = 700;
    private static final int HEIGHT = 700;

    private static Stage primaryStage;
    private static Map<String, Scene> scenes;
    private static Map<String, UIController> sceneControllers;
    private static Map<String, String> sceneFiles;
    private static Map<String, String> sceneTitles;

    public UIController() {

    }

    /**
     * Constructor
     * Sets the primary stage to the given stage and initializes the file lists
     * @param stage
     */
    public UIController(Stage stage) {
        primaryStage = stage;
        setLists();
    }

    /**
     * Initializes the lists of titles and fxml files for each scene
     */
    private void setLists() {
        scenes = new HashMap<String, Scene>();
        sceneControllers = new HashMap<String, UIController>();
        sceneFiles = new HashMap<String, String>();
        sceneTitles = new HashMap<String, String>();

        sceneFiles.put(UIController.LOGIN_MAIN, "login_main.fxml");
        sceneTitles.put(UIController.LOGIN_MAIN, "Login Screen");
    }

    /**
     * Switches the primary stage to the given scene
     * If the scene has not yet been created, creates that scene
     * @param sceneString String representation of the scene to swtich to
     * @return The UIController for that particular new scene
     */
    @FXML
    protected UIController goToScene(String sceneString) {
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

    /**
     * Switches the scene to the main menu
     * Used across all UIControllers
     */
    @FXML
    private void setHomeButton() {
        this.goToScene(UIController.MAIN_MENU_MAIN);
    }
}
