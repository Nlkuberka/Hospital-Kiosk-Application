package edu.wpi.cs3733.d19.teamD;

import com.jfoenix.controls.JFXTextField;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * The edu.wpi.cs3733.d19.teamD.UIController Superclass
 * This controller handles all of the other UIControllers as well as
 * storing the EditableTableCell which is used in adminTools
 * @author Isabel Morales
 * @version interation3
 */
public class UIController {

    // The various scenes that this edu.wpi.cs3733.d19.teamD.UIController handles
    public static final String POPUP_DIRECTIONS = "PUD";

    public static final String SERVICE_REQUEST_MAIN = "SRM";
    public static final String SERVICE_REQUEST_BABYSITTING = "SRB";

    public static final String USER_ADD = "UA";
    public static final String USERS_VIEW = "UV";

    // The starting width and height of the window
    private static final int WIDTH = 1280;
    private static final int HEIGHT = 800;

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

    private String cssPath;

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
        stage.setMinHeight(800);
        stage.setMinWidth(1280);
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
        sceneTitles.put(UIController.SERVICE_REQUEST_BABYSITTING, "Service Request - edu.wpi.cs3733.d19.teamD.Babysitting");

        sceneFiles.put(UIController.USER_ADD, "/add_user");
        sceneTitles.put(UIController.USER_ADD, "Add edu.wpi.cs3733.d19.teamD.User");

        sceneFiles.put(UIController.USERS_VIEW, "/view_users");
        sceneTitles.put(UIController.USERS_VIEW, "View Users");

        // Popups
        sceneFiles.put(UIController.POPUP_DIRECTIONS, "/direction_popup.fxml");
        sceneTitles.put(UIController.POPUP_DIRECTIONS, "Popup Window For Directions");

        sceneFiles.put(UIController.SERVICE_REQUEST_MAIN, "/overall.fxml");
        sceneTitles.put(UIController.SERVICE_REQUEST_MAIN, "Service Request - edu.wpi.cs3733.d19.teamD.Babysitting");
    }

    /**
     * Switches the primary stage to the given scene
     * If the scene has not yet been created, creates that scene
     * @param sceneString String representation of the scene to swtich to
     * @return The edu.wpi.cs3733.d19.teamD.UIController for that particular new scene
     */
    @FXML
    protected UIController goToScene(String sceneString, String cssPath) {
        Scene scene = scenes.get(sceneString);

        if(this.cssPath == null) {
            this.cssPath = cssPath;
        }

        // If the scene has not yet been created
        if(scene == null) {
            try {
                //FXMLLoader fxmlLoader = new FXMLLoader(new File(System.getProperty("user.dir") + "/resources" + sceneFiles.get(sceneString)).toURI().toURL());
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(sceneFiles.get(sceneString)));
                Parent root = fxmlLoader.load();
                sceneParents.put(sceneString, root);
                sceneControllers.put(sceneString, fxmlLoader.getController());
                scenes.put(sceneString, new Scene(root, WIDTH, HEIGHT));
                scene = scenes.get(sceneString);
                final ObservableList<String> stylesheets = scene.getStylesheets();
                stylesheets.addAll(getClass().getResource(this.cssPath).toExternalForm());
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

    /**
     * Runs the given getter on the giveb object and puts the data in the given label
     * @param object The object to get from
     * @param methodName The method name of the getter
     * @param label The label to put the value into
     */
    public void runStringGetter(Object object, String methodName, Label label) {
        try {
            Method method = object.getClass().getMethod(methodName);
            label.setText("" + method.invoke(object));
        } catch (Exception e) {
            //e.printStackTrace();
            label.setText("");
        }
    }

    /**
     * Runs the given getter on the giveb object and puts the data in the given label and textField
     * @param object The object to get from
     * @param methodName The method name of the getter
     * @param label The label to put the value into
     * @param textField The textField to put the value into
     */
    public void runStringGetterEditable(Object object, String methodName, Label label, TextField textField) {
        try {
            Method method = object.getClass().getMethod(methodName);
            textField.setText("" + method.invoke(object));
            runStringGetter(object, methodName, label);
        } catch (Exception e) {
            //e.printStackTrace();
            textField.setText("");
        }
    }

    /**
     * Runs the setter on the given object and gets the value and class from the givven arguments
     * @param object The object to run the setter on
     * @param methodName The methodName of the setter
     * @param className The class of the argument for the setter
     * @param argument The argument of the given class to set to
     */
    public void runSetter(Object object, String methodName, Class className, Object argument) {
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
        protected JFXTextField textField = new JFXTextField(); /**< The Textfield to edit*/
        protected Label label = new Label(); /**< The Label to display*/
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
            if (s == null) {
                return;
            }
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
