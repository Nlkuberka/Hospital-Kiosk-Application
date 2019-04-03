import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.Map;
import java.util.HashMap;

public class UIController {
    public static final String LOGIN_MAIN = "LM";
    public static final String GUEST_MAIN_MENU_MAIN = "GMMM";
    public static final String USER_MAIN_MENU_MAIN = "BMMM";
    public static final String ADMIN_MAIN_MENU_MAIN = "AMMM";
    public static final String PATHFINDING_MAIN = "PFM";
    public static final String RESERVATIONS_MAIN = "RVM";
    public static final String SERVICE_REQUEST_MAIN = "SRM";
    public static final String ADMIN_TOOLS_MAIN = "ATM";
    public static final String ADMIN_TOOLS_VIEW_NODES = "ATVN";
    public static final String ADMIN_TOOLS_VIEW_EDGES = "ATVE";

    private static final int WIDTH = 900;
    private static final int HEIGHT = 600;

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

    public void onShow() {}

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

        sceneFiles.put(UIController.ADMIN_TOOLS_MAIN, "admin_tools_main.fxml");
        sceneTitles.put(UIController.ADMIN_TOOLS_MAIN, "Admin Tools - Main");

        sceneFiles.put(UIController.ADMIN_TOOLS_VIEW_EDGES, "admin_tools_view_edges.fxml");
        sceneTitles.put(UIController.ADMIN_TOOLS_VIEW_EDGES, "Admin Tools - View Edges");

        sceneFiles.put(UIController.ADMIN_TOOLS_VIEW_NODES, "admin_tools_view_nodes.fxml");
        sceneTitles.put(UIController.ADMIN_TOOLS_VIEW_NODES, "Admin Tools - View Nodes");

        sceneFiles.put(UIController.PATHFINDING_MAIN, "path_find_main.fxml");
        sceneTitles.put(UIController.PATHFINDING_MAIN, "Path Finding Main");
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

        sceneControllers.get(sceneString).onShow();
        return sceneControllers.get(sceneString);
    }

    /**
     * Switches the scene to the main menu
     * Used across all UIControllers
     */
    @FXML
    private void setHomeButton() {
        if(CurrentUser.permissions == User.GUEST_PERMISSIONS) {
            this.goToScene(UIController.GUEST_MAIN_MENU_MAIN);
        } else if(CurrentUser.permissions == User.BASIC_PERMISSIONS) {
            this.goToScene(UIController.USER_MAIN_MENU_MAIN);
        } else if(CurrentUser.permissions == User.ADMIN_PERMISSIONS) {
            this.goToScene(UIController.ADMIN_MAIN_MENU_MAIN);
        } else {
            this.goToScene(UIController.LOGIN_MAIN);
        }
    }
}
