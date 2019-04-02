import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.HashMap;

/**
 * The UIController Superclass
 * This controller handles all of the other UIControllers as well as
 * storing the EditableTableCell which is used in adminTools
 * @author Jonathan Chang
 * @version interation1
 */
public class UIController {
    // The various scenes that this UIController handles
    public static final String LOGIN_MAIN = "LM";
    public static final String GUEST_MAIN_MENU_MAIN = "GMMM";
    public static final String USER_MAIN_MENU_MAIN = "UMMM";
    public static final String ADMIN_MAIN_MENU_MAIN = "AMMM";
    public static final String PATHFINDING_MAIN = "PFM";
    public static final String RESERVATIONS_MAIN = "RVM";
    public static final String SERVICE_REQUEST_MAIN = "SRM";
    public static final String ADMIN_TOOLS_MAIN = "ATM";
    public static final String ADMIN_TOOLS_VIEW_NODES = "ATVN";
    public static final String ADMIN_TOOLS_VIEW_EDGES = "ATVE";
    public static final String ADMIN_TOOLS_VIEW_SERVICE_REQUESTS = "ATVSR";

    // The starting width and height of the window
    private static final int WIDTH = 900;
    private static final int HEIGHT = 600;

    // Data storage about the stage
    private static Parent root;
    private static Stage primaryStage;

    //Data storage about each scene
    private static Map<String, Scene> scenes;
    private static Map<String, UIController> sceneControllers;
    private static Map<String, String> sceneFiles;
    private static Map<String, String> sceneTitles;

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
    public UIController(Stage stage) {
        primaryStage = stage;
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

        sceneFiles.put(UIController.LOGIN_MAIN, "login_main.fxml");
        sceneTitles.put(UIController.LOGIN_MAIN, "Login Screen");

        sceneFiles.put(UIController.GUEST_MAIN_MENU_MAIN, "guest_main_menu_main.fxml");
        sceneTitles.put(UIController.GUEST_MAIN_MENU_MAIN, "Main Menu");

        sceneFiles.put(UIController.USER_MAIN_MENU_MAIN, "user_main_menu_main.fxml");
        sceneTitles.put(UIController.USER_MAIN_MENU_MAIN, "Main Menu");

        sceneFiles.put(UIController.ADMIN_MAIN_MENU_MAIN, "admin_main_menu_main.fxml");
        sceneTitles.put(UIController.ADMIN_MAIN_MENU_MAIN, "Main Menu");

        sceneFiles.put(UIController.ADMIN_TOOLS_MAIN, "admin_tools_main.fxml");
        sceneTitles.put(UIController.ADMIN_TOOLS_MAIN, "Admin Tools - Main");

        sceneFiles.put(UIController.ADMIN_TOOLS_VIEW_EDGES, "admin_tools_view_edges.fxml");
        sceneTitles.put(UIController.ADMIN_TOOLS_VIEW_EDGES, "Admin Tools - View Edges");

        sceneFiles.put(UIController.ADMIN_TOOLS_VIEW_NODES, "admin_tools_view_nodes.fxml");
        sceneTitles.put(UIController.ADMIN_TOOLS_VIEW_NODES, "Admin Tools - View Nodes");

        sceneFiles.put(UIController.ADMIN_TOOLS_VIEW_SERVICE_REQUESTS, "admin_tools_view_service_request.fxml");
        sceneTitles.put(UIController.ADMIN_TOOLS_VIEW_SERVICE_REQUESTS, "Admin Tools - View Service Requests");
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
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(sceneFiles.get(sceneString)));
                Parent root = fxmlLoader.load();
                sceneControllers.put(sceneString, fxmlLoader.getController());
                scenes.put(sceneString, new Scene(root, WIDTH, HEIGHT));
                scene = scenes.get(sceneString);
            } catch(Exception e) {
                e.printStackTrace();
            }
        }

        // Show the scene
        primaryStage.setTitle(sceneTitles.get(sceneString));
        primaryStage.setScene(scene);
        primaryStage.show();

        // Run the onShow function and return the controller
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

    /**
     * Runs the given getter on the giveb object and puts the data in the given label
     * @param object The object to get from
     * @param methodName The method name of the getter
     * @param label The label to put the value into
     */
    protected void runStringGetter(Object object, String methodName, Label label) {
        try {
            Method method = object.getClass().getMethod(methodName);
            label.setText((String) method.invoke(object));
        } catch (Exception e) {
            //e.printStackTrace();
        }
    }

    /**
     * Runs the given getter on the giveb object and puts the data in the given label and textField
     * @param object The object to get from
     * @param methodName The method name of the getter
     * @param label The label to put the value into
     * @param textField The textField to put the value into
     */
    protected void runStringGetterEditable(Object object, String methodName, Label label, TextField textField) {
        try {
            Method method = object.getClass().getMethod(methodName);
            textField.setText((String) method.invoke(object));
            runStringGetter(object, methodName, label);
        } catch (Exception e) {
            //e.printStackTrace();
        }
    }

    /**
     * Runs the setter on the given object and gets the value and class from the givven arguments
     * @param object The object to run the setter on
     * @param methodName The methodName of the setter
     * @param className The class of the argument for the setter
     * @param argument The argument of the given class to set to
     */
    protected void runSetter(Object object, String methodName, Class className, Object argument) {
        try {
            Method method = object.getClass().getMethod(methodName, className);
            method.invoke(object, argument);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * An object that extends TableCell and allows String edits to the cell
     * @param <T> The object that is being displayed in the TableView
     * @param <S> The object that is being displayed in the TableView
     */
    protected class EditableTextCell<T, S> extends TableCell<T, S> {
        protected TextField textField = new TextField("TEST"); /**< The Textfield to edit*/
        protected Label label = new Label("TEST"); /**< The Label to display*/
        protected TableColumn column; /**< The column that the cell is in, used for width properties*/
        protected int index; /**< The Column index, used for per column commands*/

        /**
         * Constructor
         * Construts with the column and index
         * @param tableColumn The TableColumn that this cell is in
         * @param indexOut The index of the column
         */
        public EditableTextCell(TableColumn<T, S> tableColumn, int indexOut) {
            this.column = tableColumn;
            this.index = indexOut;
        }

        /**
         * Updates the object when edits are made
         * @param s The object
         * @param empty Whether the cell is currently empty
         */
        @Override
        protected void updateItem(S s, boolean empty) {
            super.updateItem(s, empty);

            // Expand elements to the entire cell width
            textField.prefWidthProperty().bind(column.prefWidthProperty());
            label.prefWidthProperty().bind(column.prefWidthProperty());

            setGraphic(label);
            // When user leaves textField, switch to label and discard changes
            textField.focusedProperty().addListener(new ChangeListener<Boolean>() {
                @Override
                public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                    if(!newValue) {
                        setGraphic(label);
                        textField.setText(label.getText());
                    }
                }
            });

            // When the user double clicks the label switch to textField
            label.setOnMouseClicked(et -> {
                if(et.getClickCount() == 2) {
                    setGraphic(textField);
                    textField.setText(label.getText());
                    textField.positionCaret(label.getText().length() - 1);
                }
            });
        }
    }
}
