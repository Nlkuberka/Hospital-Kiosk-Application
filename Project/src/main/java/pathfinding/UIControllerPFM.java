package pathfinding;

import com.jfoenix.controls.JFXTabPane;
import database.DBController;
import application.UIController;
import application.UIControllerPUD;
import database.DBControllerNE;
import entities.Graph;
import entities.Node;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.sql.Connection;

import com.jfoenix.controls.JFXButton;
import javafx.scene.Scene;
import javafx.scene.shape.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import net.kurobako.gesturefx.GesturePane;

import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Controller for the path_find_main.fxml file
 *
 * @author panagiotisargyrakis, dimitriberardi, ryano647
 */

public class UIControllerPFM extends UIController {

    @FXML private Path pathLL2, pathLL1, pathG, path1, path2, path3;
    @FXML private JFXTabPane mapTabPane;

    @FXML
    public ChoiceBox<String> initialLocationSelect;
    @FXML
    private ChoiceBox<String> destinationSelect;
    @FXML
    private ImageView backgroundImage;


    @FXML private GesturePane lowerLevel2GesturePane;
    @FXML private GesturePane lowerLevel1GesturePane;
    @FXML private GesturePane groundFloorGesturePane;
    @FXML private GesturePane firstFloorGesturePane;
    @FXML private GesturePane secondFloorGesturePane;
    @FXML private GesturePane thirdFloorGesturePane;

    @FXML private AnchorPane lowerLevel2AnchorPane;
    @FXML private AnchorPane lowerLevel1AnchorPane;
    @FXML private AnchorPane groundFloorAnchorPane;
    @FXML private AnchorPane firstFloorAnchorPane;
    @FXML private AnchorPane secondFloorAnchorPane;
    @FXML private AnchorPane thirdFloorAnchorPane;

    // The multiplication factor at which the map changes size
    @FXML
    private JFXButton directionsRequest;

    private PathHandler pathHandler;

    private AnchorPaneHandler anchorPaneHandler;
    private CurrentObjects currentObjects;
    private GesturePaneHandler gesturePaneHandler;

    /**
     * Initialize various componets, especially panes, tabs and pathHandler
     */
    @FXML
    public void initialize() {
        backgroundImage.fitWidthProperty().bind(primaryStage.widthProperty());

        // ensures new tab has same x,y on the map and path animation changes between floors
        mapTabPane.getSelectionModel().selectedItemProperty().addListener(
                new ChangeListener<Tab>() {
                    @Override
                    public void changed(ObservableValue<? extends Tab> ov, Tab t, Tab t1) {
                        currentObjects.clearAnimation();
                        GesturePane oldPane = currentObjects.getCurrentGesturePane();
                        currentObjects.setFloorIndex(Floors.getByName(t1.getText()).getIndex());
                        GesturePane pane = currentObjects.getCurrentGesturePane();
                        pane.centreOn(oldPane.targetPointAtViewportCentre());
                        gesturePaneHandler.changeTabs(pane, oldPane);
                        if (pathHandler.isActive()) {
                            gesturePaneHandler.newAnimation(currentObjects);
                        }
                    }
                }
        );

        pathHandler = new PathHandler(pathLL2, pathLL1, pathG, path1, path2, path3, primaryStage);

        gesturePaneHandler = new GesturePaneHandler(lowerLevel2GesturePane, lowerLevel1GesturePane,
                groundFloorGesturePane, firstFloorGesturePane, secondFloorGesturePane, thirdFloorGesturePane);

        anchorPaneHandler = new AnchorPaneHandler(lowerLevel2AnchorPane, lowerLevel1AnchorPane,
                groundFloorAnchorPane, firstFloorAnchorPane, secondFloorAnchorPane, thirdFloorAnchorPane);

        currentObjects = new CurrentObjects(0, null, null, null, null,
                pathHandler, anchorPaneHandler, gesturePaneHandler);

        anchorPaneHandler.setCurrentObjects(currentObjects);

    }

    /**
     * Initialize choice boxes and setup circles as node indicators
     */
    @Override
    public void onShow() {

        // ~~~~~ init choice boxes
        Connection conn = DBControllerNE.dbConnect();

        LinkedList<LinkedList<Node>> roomsAtEachFloor = new LinkedList<>();

        roomsAtEachFloor.add(DBControllerNE.generateListOfNodes(conn, DBControllerNE.ALL_ROOMS_FLOOR_L2));
        roomsAtEachFloor.add(DBControllerNE.generateListOfNodes(conn, DBControllerNE.ALL_ROOMS_FLOOR_L1));
        roomsAtEachFloor.add(DBControllerNE.generateListOfNodes(conn, DBControllerNE.ALL_ROOMS_FLOOR_G));
        roomsAtEachFloor.add(DBControllerNE.generateListOfNodes(conn, DBControllerNE.ALL_ROOMS_FLOOR_1));
        roomsAtEachFloor.add(DBControllerNE.generateListOfNodes(conn, DBControllerNE.ALL_ROOMS_FLOOR_2));
        roomsAtEachFloor.add(DBControllerNE.generateListOfNodes(conn, DBControllerNE.ALL_ROOMS_FLOOR_3));

        DBControllerNE.closeConnection(conn);

        initialLocationSelect.getItems().clear();
        destinationSelect.getItems().clear();

        for (LinkedList<Node> list : roomsAtEachFloor) {
            for (Node node : list) {
                // update choices for initial location
                initialLocationSelect.getItems().add(node.getLongName());
                // update choices for destination location
                destinationSelect.getItems().addAll(node.getLongName());
            }
        }

        anchorPaneHandler.initCircles(roomsAtEachFloor, initialLocationSelect, destinationSelect);

    }

//    void createMenuOnNode() {
//        AnchorPane pane = anchorPanes.get(currentFloorIndex);
//        Rectangle rectangle = new Rectangle();
//    }



    /**
     * Allows for a default starting location
     * @param longName Name of starting node
     */
    private void setUpDefaultStartingLocation(String longName){
//        initialLocationSelect.setValue(longName);
    }

    /**
     * Call back for change in init location drop down
     * @param actionEvent
     */
    @FXML
    public void initLocChanged(ActionEvent actionEvent) {

        if (initialLocationSelect.getValue() == null)
            return;

        //System.out.println("Initial location selected: " + initialLocationSelect.getValue());
        Connection connection = DBController.dbConnect();
        currentObjects.setInitialID(DBController.IDfromLongName(initialLocationSelect.getValue(), connection));
        DBController.closeConnection(connection);

        currentObjects.setInitCircle(initialLocationSelect.getValue());

        getPath();
    }

    /**
     * Call back for change in dest location drop down
     * @param actionEvent
     */
    @FXML
    public void destLocChanged(ActionEvent actionEvent) {

        if (destinationSelect.getValue() == null)
            return;

        Connection connection = DBController.dbConnect();
        System.out.println(destinationSelect.getValue());
        currentObjects.setDestID(DBController.IDfromLongName(destinationSelect.getValue(), connection));
        DBController.closeConnection(connection);

        currentObjects.setDestCircle(destinationSelect.getValue());

        getPath();
    }


    /**
     * Callback for cancel. Clears path, animation, node selection and drop down menus
     * @param actionEvent
     */
    @FXML
    private void cancel(ActionEvent actionEvent) {
        pathHandler.cancel();
        clearTabColors();

        currentObjects.cancel();

        initialLocationSelect.getSelectionModel().clearSelection();
        destinationSelect.getSelectionModel().clearSelection();
    }

    /**
     * Handles the generation, display and animation of a new path
     */
    private void getPath() {

        if(currentObjects.anyNullEndNodes())
            return;

        List<String> pathIDs;
        pathIDs = Graph.getGraph().shortestPath(currentObjects.getInitialID(), currentObjects.getDestID());

        Connection connection = DBController.dbConnect();
        Node initialNode = DBControllerNE.fetchNode(currentObjects.getInitialID(), connection);
        DBController.closeConnection(connection);

        currentObjects.clearAnimation(); // reset stuff
        pathHandler.cancel(); // reset stuff

        // change tab based on initial node -- order here is important! Do not move below.
        mapTabPane.getSelectionModel().select(Floors.getByID(initialNode.getFloor()).getIndex());

        // update paths -- order here is important! Do not move above change tab.
        pathHandler.displayNewPath(Graph.getGraph().separatePathByFloor(pathIDs), initialNode);

        gesturePaneHandler.centerOnInitialNode(pathHandler, currentObjects.getCurrentGesturePane());

        List<Integer> floorsUsed = pathHandler.getFloorsUsed();
        clearTabColors();
        for (Integer floor : floorsUsed) {
            this.mapTabPane.getTabs().get(floor).setStyle("-fx-background-color: #015080");
        }

        gesturePaneHandler.newAnimation(currentObjects);

    }

    /**
     * Clear marking of tab headers
     */
    private void clearTabColors() {
        for (Tab tab : this.mapTabPane.getTabs()) {
            tab.setStyle("-fx-background-color: #FFC41E");
        }
    }

    public void goBack(ActionEvent actionEvent) {
        this.goToScene(UIController.LOGIN_MAIN);
    }

    /**
     * Allows the map to increase in size, up to scroll_AnchorPane.getMaxWidth
     *
     * @param actionEvent Triggered when zoom_button is pressed
     */
    public void zoom(ActionEvent actionEvent) {
        gesturePaneHandler.zoom(currentObjects.getCurrentGesturePane());
    }

    /**
     * Allows the map to decrease in size, down to scroll_AnchorPane.getMinWidth
     *
     * @param actionEvent Triggered when zoom_button is pressed
     */
    public void unZoom(ActionEvent actionEvent) {
        gesturePaneHandler.un_zoom(currentObjects.getCurrentGesturePane());
    }

    @FXML
    private void directionSelection() {
        String direction = Graph.getGraph().textDirections(Graph.getGraph().shortestPath(currentObjects.getInitialID(),
                currentObjects.getDestID()));
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("/directions_popup.fxml"));

            Scene popupScene = new Scene(fxmlLoader.load(), 600, 400);
            Stage popupStage = new Stage();

            popupStage.initModality(Modality.WINDOW_MODAL);
            popupStage.initOwner(this.primaryStage);

            UIControllerPUD controller = (UIControllerPUD) fxmlLoader.getController();
            controller.setDirections(direction);

            popupStage.setTitle("Directions");
            popupStage.setScene(popupScene);
            popupStage.show();
        } catch (IOException e) {
            Logger logger = Logger.getLogger((getClass().getName()));
            logger.log(Level.SEVERE, "Failed to create new window.", e);

        }
    }

    @FXML
    private void setLoginButton() {
        this.goToScene(UIController.LOGIN_MAIN);
    }

    @FXML
    private void setServiceRequestButton() {
        this.goToScene(UIController.SERVICE_REQUEST_MAIN);
    }
}



