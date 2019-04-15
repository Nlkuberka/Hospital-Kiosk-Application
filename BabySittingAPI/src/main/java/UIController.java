import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.HashMap;
import java.util.Map;

/**
 * The UIController Superclass
 * This controller handles all of the other UIControllers as well as
 * storing the EditableTableCell which is used in adminTools
 * @author Isabel Morales
 * @version interation3
 */
public class UIController {

    // The various scenes that this UIController handles
    public static final String POPUP_DIRECTIONS = "PUD";

    public static final String SERVICE_REQUEST_MAIN = "SRM";
    public static final String SERVICE_REQUEST_BABYSITTING = "SRB";

    // The starting width and height of the window
    private static final int WIDTH = 1280;
    private static final int HEIGHT = 720;

    private static final int WIDTH_POPUP_WARNING = 300;
    private static final int HEIGHT_POPUP_WARNING = 150;

    // Data storage about the stage
    private static Scene rootScene;
    private static BorderPane rootPane;
    protected static Stage primaryStage;
    private static Map<String, Scene> scenes;
    private static Map<String, UIController> sceneControllers;
    private static Map<String, String> sceneFiles;
    private static Map<String, String> sceneTitles;
    private static Map<String, Parent> sceneParents;

    /**
     * Constructor
     */
    public UIController() {

    }

    /**
     * Constructor
     * Sets the primary stage to the given stage and initializes the file lists
     * @param stage
     */
    public UIController(Stage stage, int width, int height) {
        primaryStage = stage;
        rootPane = new BorderPane();
        rootScene = new Scene(rootPane, width, height);
        primaryStage.setScene(rootScene);
        primaryStage.show();

        setLists();
    }

    /**
     * Function that is run on scene show
     */
    public void onShow() {}

    /**
     * Initializes the lists of titles and fxml files for each scene
     */
    private void setLists() {
        scenes = new HashMap<String, Scene>();
        sceneControllers = new HashMap<String, UIController>();
        sceneFiles = new HashMap<String, String>();
        sceneTitles = new HashMap<String, String>();
        sceneParents = new HashMap<String, Parent>();

        sceneFiles.put(UIController.SERVICE_REQUEST_BABYSITTING, "/service_request_babysitting.fxml");
        sceneTitles.put(UIController.SERVICE_REQUEST_BABYSITTING, "Service Request - Babysitting");

        // Popups
        sceneFiles.put(UIController.POPUP_DIRECTIONS, "/direction_popup.fxml");
        sceneTitles.put(UIController.POPUP_DIRECTIONS, "Popup Window For Directions");
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

        // If the scene has not yet been created
        if(scene == null) {
            try {
                //FXMLLoader fxmlLoader = new FXMLLoader(new File(System.getProperty("user.dir") + "/resources" + sceneFiles.get(sceneString)).toURI().toURL());
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(sceneFiles.get(sceneString)));
                Parent root = fxmlLoader.load();
                sceneParents.put(sceneString, root);
                sceneControllers.put(sceneString, fxmlLoader.getController());
                scenes.put(sceneString, new Scene(root, WIDTH, HEIGHT));
            } catch(Exception e) {
                e.printStackTrace();
            }
        }

        // Show the scene
        primaryStage.setTitle(sceneTitles.get(sceneString));
        rootPane.setCenter(sceneParents.get(sceneString));

        // Run the onShow function and return the controller
        sceneControllers.get(sceneString).onShow();
        return sceneControllers.get(sceneString);
    }

    /**
     * Pops up a window with the given warning that the user must acknowledge to continue
     * @param message The warning string to dispaly
     */
    @FXML
    public void popupMessage(String message, boolean isWarning) {
        Stage stage = new Stage();
        Scene scene = null;
        UIControllerPUM controller = null;
        try {
            FXMLLoader fxmlLoader = isWarning ? new FXMLLoader(getClass().getResource("/popup_main.fxml")) :  new FXMLLoader(getClass().getResource("/popup_confirm.fxml"));
            Parent root = fxmlLoader.load();
            scene = new Scene(root, WIDTH_POPUP_WARNING, HEIGHT_POPUP_WARNING);
            controller = fxmlLoader.getController();
        } catch(Exception e) {
            e.printStackTrace();
        }

        stage.initOwner(primaryStage);
        stage.initModality(Modality.APPLICATION_MODAL);

        controller.setMessage(message);

        stage.setTitle("Warning - Main");
        stage.setScene(scene);
        stage.showAndWait();
        stage.setAlwaysOnTop(true);


    }


}
