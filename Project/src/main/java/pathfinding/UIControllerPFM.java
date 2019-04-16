package pathfinding;

import com.jfoenix.controls.JFXTabPane;
import database.DBController;
import application.UIController;
import application.UIControllerPUD;
import database.DBControllerNE;
import entities.Graph;
import entities.Node;

import javafx.animation.Interpolator;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Point2D;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;

import java.io.IOException;
import java.sql.Connection;

import com.jfoenix.controls.JFXButton;
import javafx.animation.PathTransition;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;
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

    public enum Floors {
        LL2("Lower Level 2", "L2", 0), LL1("Lower Level 1", "L1", 1),
        GROUND("Ground Floor", "G", 2), FIRST("First Floor", "1", 3),
        SECOND("Second Floor", "2", 4), THIRD("Third Floor", "3", 5);

        private final String name;
        private final String ID;
        private final int index;

        Floors(String name, String ID, int index) {
            this.name = name;
            this.ID = ID;
            this.index = index;
        }

        public String getName() {
            return this.name;
        }

        public String getID() {
            return this.ID;
        }

        public int getIndex() {
            return this.index;
        }

        public static Floors getByID(String ID) {
            if(ID.equals("1")) {
                return FIRST;
            }
            if(ID.equals("2")) {
                return SECOND;
            }
            if(ID.equals("3")) {
                return THIRD;
            }
            if(ID.equals("G")) {
                return GROUND;
            }
            if(ID.equals("L1")) {
                return LL1;
            }
            return LL2;
        }

        public static Floors getByName(String ID) {
            if(ID.equals(FIRST.name)) {
                return FIRST;
            }
            if(ID.equals(SECOND.name)) {
                return SECOND;
            }
            if(ID.equals(THIRD.name)) {
                return THIRD;
            }
            if(ID.equals(GROUND.name)) {
                return GROUND;
            }
            if(ID.equals(LL1.name)) {
                return LL1;
            }
            return LL2;
        }

        public static Floors getByIndex(int index) {
            switch (index) {
                case (0): {
                    return LL2;
                } case (1): {
                    return LL1;
                } case (2): {
                    return GROUND;
                } case (3): {
                    return FIRST;
                } case (4): {
                    return SECOND;
                }case (5): {
                    return THIRD;
                } default:
                    return SECOND;
            }
        }
    }

    @FXML
    private HBox hboxForMap;
    @FXML
    private GridPane interfaceGrid;
    @FXML
    private StackPane parentPane;

    private String initialID;
    private String destID;

    @FXML
    public ChoiceBox<String> initialLocationSelect;
    @FXML
    private ChoiceBox<String> destinationSelect;
    @FXML
    private ImageView backgroundImage;
    @FXML
    private MenuItem backButton;
    @FXML
    private ScrollPane scrollPane_pathfind;
    @FXML
    private JFXButton loginButton;
    @FXML
    private JFXButton serviceRequestButton;


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
                        gesturePaneHandler.changeTabs(pathHandler,
                                UIControllerPFM.Floors.getByName(t1.getText()).getIndex());
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

        gesturePaneHandler.setCurrentObjects(currentObjects);
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
        initialID = DBController.IDfromLongName(initialLocationSelect.getValue(), connection);
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
        destID = DBController.IDfromLongName(destinationSelect.getValue(), connection);
        DBController.closeConnection(connection);

        currentObjects.setDestCircle(destinationSelect.getValue());

        // call getPath if not null
        getPath();
    }


    /**
     * Callback for cancel. Clears path, animation, node selection and drop down menus
     * @param actionEvent
     */
    @FXML
    private void cancel(ActionEvent actionEvent) {
        pathHandler.cancel();
        currentObjects.clearNodeStyle();
        clearTabColors();

        initialID = null;
        destID = null;

        currentObjects.clearAnimation();

        initialLocationSelect.getSelectionModel().clearSelection();
        destinationSelect.getSelectionModel().clearSelection();
    }

    /**
     * Handles the generation, display and animation of a new path
     */
    private void getPath() {

        if(initialID == null || destID == null)
            return;

        List<String> pathIDs;
        pathIDs = Graph.getGraph().shortestPath(initialID, destID);

        Connection connection = DBController.dbConnect();
        Node initialNode = DBControllerNE.fetchNode(initialID, connection);
        DBController.closeConnection(connection);

        currentObjects.clearAnimation(); // reset stuff
        pathHandler.cancel(); // reset stuff

        // change tab based on initial node -- order here is important! Do not move below.
        mapTabPane.getSelectionModel().select(Floors.getByID(initialNode.getFloor()).getIndex());

        // update paths -- order here is important! Do not move above change tab.
        pathHandler.displayNewPath(Graph.getGraph().separatePathByFloor(pathIDs), initialNode);

        gesturePaneHandler.centerOnInitialNode(pathHandler);

        List<Integer> floorsUsed = pathHandler.getFloorsUsed();
        clearTabColors();
        for (Integer floor : floorsUsed) {
            this.mapTabPane.getTabs().get(floor).setStyle("-fx-background-color: #015080");
        }

        gesturePaneHandler.newAnimation();

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
        gesturePaneHandler.zoom();
    }

    /**
     * Allows the map to decrease in size, down to scroll_AnchorPane.getMinWidth
     *
     * @param actionEvent Triggered when zoom_button is pressed
     */
    public void unZoom(ActionEvent actionEvent) {
        gesturePaneHandler.zoom();
    }

    @FXML
    private void directionSelection() {
        String direction = Graph.getGraph().textDirections(Graph.getGraph().shortestPath(initialID, destID));
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



