package pathfinding;

import application.CurrentUser;
import application.UIController;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTabPane;
import com.jfoenix.controls.JFXToggleButton;
import database.DBController;
import database.DBControllerNE;
import edu.wpi.cs3733.d19.teamE.api.Main_Registration;
import entities.Direction;
import entities.Graph;
import entities.Node;
import entities.User;
import helper.RoomCategoryFilterHelper;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Point2D;
import javafx.scene.Scene;
import javafx.scene.control.Accordion;
import javafx.scene.control.Menu;
import javafx.scene.control.Tab;
import javafx.scene.control.TitledPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.paint.Color;
import javafx.scene.shape.Path;
import javafx.stage.Modality;
import javafx.stage.Stage;
import net.kurobako.gesturefx.GesturePane;

import java.io.IOException;
import java.sql.Connection;
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

    @FXML private AnchorPane topAnchorPane;
    @FXML private Path pathLL2, pathLL1, pathG, path1, path2, path3, path4;
    @FXML private JFXTabPane mapTabPane;
    @FXML private Menu homeMenu;

    @FXML public JFXComboBox<String> initialLocationCombo;
    @FXML public JFXComboBox<String> destinationCombo;
    private RoomCategoryFilterHelper initialFilterHelper;
    private RoomCategoryFilterHelper destinationFilterHelper;

    @FXML
    private ImageView backgroundImage;


    @FXML private GesturePane lowerLevel2GesturePane;
    @FXML private GesturePane lowerLevel1GesturePane;
    @FXML private GesturePane groundFloorGesturePane;
    @FXML private GesturePane firstFloorGesturePane;
    @FXML private GesturePane secondFloorGesturePane;
    @FXML private GesturePane thirdFloorGesturePane;
    @FXML private GesturePane fourthFloorGesturePane;

    @FXML private AnchorPane lowerLevel2AnchorPane;
    @FXML private AnchorPane lowerLevel1AnchorPane;
    @FXML private AnchorPane groundFloorAnchorPane;
    @FXML private AnchorPane firstFloorAnchorPane;
    @FXML private AnchorPane secondFloorAnchorPane;
    @FXML private AnchorPane thirdFloorAnchorPane;
    @FXML private AnchorPane fourthFloorAnchorPane;

    @FXML private JFXButton reservationButton;
    @FXML private JFXButton resolveRequestButton;
    @FXML private Accordion menu;
    @FXML private TitledPane userToolsTitledPane;
    @FXML private TitledPane pathfindingTitledPane;

    // The multiplication factor at which the map changes size
    @FXML private JFXButton directionsRequest;
    @FXML JFXToggleButton noStairsButton;

    private PathHandler pathHandler;

    private AnchorPaneHandler anchorPaneHandler;
    private CurrentObjects currentObjects;
    private GesturePaneHandler gesturePaneHandler;

    private Boolean open = true;

    /**
     * Initialize various componets, especially panes, tabs and pathHandler
     */
    @FXML
    public void initialize() {
       // pathfindingTitledPane.setExpanded(true);
        // titledPane.prefHeightProperty().bind(primaryStage.heightProperty());
        backgroundImage.fitWidthProperty().bind(primaryStage.widthProperty());

        // ensures new tab has same x,y on the map and path animation changes between floors
        mapTabPane.getSelectionModel().selectedItemProperty().addListener(
                (ov, t, t1) -> {
                    currentObjects.clearAnimation(); // clear animation on current floor
                    GesturePane oldPane = currentObjects.getCurrentGesturePane(); // save old index
                    currentObjects.setFloorIndex(Floors.getByID(t1.getId()).getIndex()); // change floor
                    GesturePane pane = currentObjects.getCurrentGesturePane();
                    pane.centreOn(oldPane.targetPointAtViewportCentre());
                    gesturePaneHandler.changeTabs(pane, oldPane);
                    currentObjects.clearContextMenu();
                    if (pathHandler.isActive()) {
                        gesturePaneHandler.newAnimation(currentObjects);
                        gesturePaneHandler.centerOnInitialNode(pathHandler, currentObjects.getCurrentGesturePane(),
                                currentObjects.getFloorIndex());
                    }
                }
        );


        pathHandler = new PathHandler(pathLL2, pathLL1, pathG, path1, path2, path3, path4);

        gesturePaneHandler = new GesturePaneHandler(lowerLevel2GesturePane, lowerLevel1GesturePane,
                groundFloorGesturePane, firstFloorGesturePane, secondFloorGesturePane, thirdFloorGesturePane,
                fourthFloorGesturePane);

        anchorPaneHandler = new AnchorPaneHandler(lowerLevel2AnchorPane, lowerLevel1AnchorPane,
                groundFloorAnchorPane, firstFloorAnchorPane, secondFloorAnchorPane, thirdFloorAnchorPane, fourthFloorAnchorPane,
                topAnchorPane, this);

        currentObjects = new CurrentObjects(0, null, null, null, null,
                pathHandler, anchorPaneHandler, gesturePaneHandler);

        anchorPaneHandler.setCurrentObjects(currentObjects);
        gesturePaneHandler.setCurrentObjects(currentObjects);

        directionsRequest.setDisable(true);

        mapTabPane.getSelectionModel().select(4); // sets default to ground floor
    }

    /**
     * Initialize choice boxes and setup circles as first indicators
     */
    @Override
    public void onShow() {

        if (initialLocationCombo.getItems().size() == 0) {

            // ~~~~~ init choice boxes
            Connection conn = DBController.dbConnect();


            LinkedList<LinkedList<Node>> roomsAtEachFloor = new LinkedList<>();

            assert conn != null;
            roomsAtEachFloor.add(DBControllerNE.generateListOfNodes(conn, DBControllerNE.ALL_ROOMS_FLOOR_L2));
            roomsAtEachFloor.add(DBControllerNE.generateListOfNodes(conn, DBControllerNE.ALL_ROOMS_FLOOR_L1));
            roomsAtEachFloor.add(DBControllerNE.generateListOfNodes(conn, DBControllerNE.ALL_ROOMS_FLOOR_G));
            roomsAtEachFloor.add(DBControllerNE.generateListOfNodes(conn, DBControllerNE.ALL_ROOMS_FLOOR_1));
            roomsAtEachFloor.add(DBControllerNE.generateListOfNodes(conn, DBControllerNE.ALL_ROOMS_FLOOR_2));
            roomsAtEachFloor.add(DBControllerNE.generateListOfNodes(conn, DBControllerNE.ALL_ROOMS_FLOOR_3));
            roomsAtEachFloor.add(DBControllerNE.generateListOfNodes(conn, DBControllerNE.ALL_ROOMS_FLOOR_4));

            DBController.closeConnection(conn);

            initialFilterHelper = new RoomCategoryFilterHelper(initialLocationCombo, param -> {
                if (initialFilterHelper.getLongName() == null)
                    return;

                currentObjects.setInitialID(initialFilterHelper.getNodeID());

                currentObjects.setInitCircle(initialFilterHelper.getLongName());

                getPath();
            }, true);
            destinationFilterHelper = new RoomCategoryFilterHelper(destinationCombo, param -> {
                if (destinationFilterHelper.getLongName() == null)
                    return;

                currentObjects.setDestID(destinationFilterHelper.getNodeID());

                currentObjects.setDestCircle(destinationFilterHelper.getLongName());

                getPath();
            }, true);

            anchorPaneHandler.initCircles(roomsAtEachFloor, initialLocationCombo, destinationCombo);
        }

        userToolsTitledPane.collapsibleProperty().setValue(false);
        userToolsTitledPane.setMouseTransparent(true);
        if(CurrentUser.user.getPermissions() == User.BASIC_PERMISSIONS) {
            userToolsTitledPane.collapsibleProperty().setValue(true);
            userToolsTitledPane.setMouseTransparent(false);
        }
        menu.setExpandedPane(pathfindingTitledPane);

        menu.getPanes().get(0).setExpanded(true);

        cancel();

        // set init location -- do not put this before cancel()
        initialLocationCombo.getSelectionModel().select(CurrentUser.startingLocation);
        currentObjects.setInitialID(CurrentUser.startingLocationID);
        currentObjects.setInitCircle(anchorPaneHandler.getCircleFromName(CurrentUser.startingLocation));

        Connection connection = DBController.dbConnect();
        assert connection != null;
        Node node = DBControllerNE.fetchNode(CurrentUser.getStartingLocationID(), connection);
        DBController.closeConnection(connection);

        Floors floor = Floors.getByID(node.getFloor());
        currentObjects.setFloorIndex(floor.getIndex());
        mapTabPane.getSelectionModel().select(floor.getTabIndex());

        gesturePaneHandler.resetZoomTo(new Point2D(node.getXcoord(), node.getYcoord()));

    }

    void setInitialLocation(String longName) {
        initialLocationCombo.getSelectionModel().select(longName);
        currentObjects.clearContextMenu();
    }

    void setDestinationLocation(String longName) {
        destinationCombo.getSelectionModel().select(longName);
        currentObjects.clearContextMenu();
    }

    /**
     *
     */
    @FXML
    private void setTitledPane() {
     //   pathfindingTitledPane.setExpanded(true);
        if (pathfindingTitledPane.isExpanded()==true) {
            final Color color2 = Color.web("#ffc41e");
            pathfindingTitledPane.setBackground(new Background(new BackgroundFill(color2, null, null)));
        } else {
            final Color color = Color.TRANSPARENT;
            pathfindingTitledPane.setBackground(new Background(new BackgroundFill(color, null, null)));
        }

    }

    /**
     * Allows for a default starting location
     * @param longName Name of starting first
     */
    private void setUpDefaultStartingLocation(String longName){
        setInitialLocation(CurrentUser.startingLocation);
    }


    /**
     * Callback for cancel. Clears path, animation, first selection and drop down menus
     */
    @FXML
    private void cancel(ActionEvent actionEvent) {
        cancel();
    }

    public void cancel() {
        pathHandler.cancel();
        pathHandler.removeFloorLinks(anchorPaneHandler);
        clearTabColors();

        currentObjects.cancel();

        directionsRequest.setDisable(true);

        initialLocationCombo.getSelectionModel().clearSelection();
        destinationCombo.getSelectionModel().clearSelection();
        setInitialLocation(CurrentUser.startingLocation);
    }

    /**
     * Handles the generation, display and animation of a new path
     */
    private void getPath() {

        if (currentObjects.anyNullEndNodes())
            return;

        List<String> pathIDs;
        pathIDs = Graph.getGraph().shortestPath(currentObjects.getInitialID(), currentObjects.getDestID());

        Connection connection = DBController.dbConnect();
        assert connection != null;
        Node initialNode = DBControllerNE.fetchNode(currentObjects.getInitialID(), connection);
        Node destNode = DBControllerNE.fetchNode(currentObjects.getDestID(), connection);

        DBController.closeConnection(connection);

        currentObjects.clearAnimation(); // reset stuff
        pathHandler.cancel(); // reset stuff
        pathHandler.removeFloorLinks(anchorPaneHandler);

        if (pathIDs == null) {
            clearTabColors();
            popupMessage("There is no path between these two nodes.", true);
        } else {
            // change tab based on initial node -- order here is important! Do not move below.
            int index = Floors.getByID(initialNode.getFloor()).getTabIndex();
            mapTabPane.getSelectionModel().select(index);

            // update paths -- order here is important! Do not move above change tab.
            pathHandler.displayNewPath(Graph.getGraph().separatePathByFloor(pathIDs), initialNode);

            connection = DBController.dbConnect();
            assert connection != null;

            LinkedList<EdgeNodePair> edgeNodes = new LinkedList<>();

            Node previousNode = DBControllerNE.fetchNode(pathIDs.get(0), connection);
            for (int i = 1; i < pathIDs.size(); i++) {
                Node node = DBControllerNE.fetchNode(pathIDs.get(i), connection);
                if ((previousNode.getNodeType().equals("STAI") || previousNode.getNodeType().equals("ELEV"))
                        && (node.getNodeType().equals("STAI") || node.getNodeType().equals("ELEV"))
                        && !node.getFloor().equals(previousNode.getFloor())) {
                    edgeNodes.add(new EdgeNodePair(previousNode, node, mapTabPane));
                }
                previousNode = node;
            }
            DBController.closeConnection(connection);

            pathHandler.drawFloorLinks(edgeNodes, anchorPaneHandler);

            gesturePaneHandler.centerOnInitialNode(pathHandler, currentObjects.getCurrentGesturePane(),
                    currentObjects.getFloorIndex());

            List<Integer> floorsUsed = pathHandler.getFloorsUsed();
            clearTabColors();
            for (int i = 0; i < Floors.values().length; i++) {
                int floor = Floors.getByIndex(i).getTabIndex();
                if (floorsUsed.contains(i)) {
                    if(Floors.getByID(destNode.getFloor()).getTabIndex() == floor){
                        mapTabPane.getTabs().get(floor).setStyle("-fx-background-color: #ff0000");
                        mapTabPane.getTabs().get(floor).setDisable(false);
                    }
                    else if(Floors.getByID(initialNode.getFloor()).getTabIndex() == floor)
                    {
                        mapTabPane.getTabs().get(floor).setStyle("-fx-background-color: #008000");
                        mapTabPane.getTabs().get(floor).setDisable(false);
                    }
                    else {
                        mapTabPane.getTabs().get(floor).setStyle("-fx-background-color: #efffff");
                        mapTabPane.getTabs().get(floor).setDisable(false);
                    }
                } else {
                    mapTabPane.getTabs().get(floor).setStyle("-fx-background-color: #003454");
                    mapTabPane.getTabs().get(floor).setDisable(true);
                }
            }
        }

        currentObjects.clearLabels();

        currentObjects.newInitLabel(initialNode);
        currentObjects.newDestLabel(destNode);

        gesturePaneHandler.newAnimation(currentObjects);

        directionsRequest.setDisable(false);

        if (menu.getExpandedPane() != null) {
            menu.getExpandedPane().setExpanded(false);
        }

    }

    /**
     * Clear marking of tab headers
     */
    private void clearTabColors() {
        for (Tab tab : mapTabPane.getTabs()) {
            tab.setStyle("-fx-background-color: #015080");
            tab.setDisable(false);
        }
    }

    @FXML
    public void goBack(ActionEvent actionEvent) {
        goToScene(UIController.LOGIN_MAIN);
    }

    /**
     * Allows the map to increase in size, up to scroll_AnchorPane.getMaxWidth
     *
     * @param actionEvent Triggered when zoom_button is pressed
     */
    @FXML
    public void zoom(ActionEvent actionEvent) {
        gesturePaneHandler.zoom(currentObjects.getCurrentGesturePane());
    }

    /**
     * Allows the map to decrease in size, down to scroll_AnchorPane.getMinWidth
     *
     * @param actionEvent Triggered when zoom_button is pressed
     */
    @FXML
    public void unZoom(ActionEvent actionEvent) {
        gesturePaneHandler.un_zoom(currentObjects.getCurrentGesturePane());
    }

    @FXML
    private void directionSelection() {

        List<String> shortPath = Graph.getGraph().shortestPath(currentObjects.getInitialID(), currentObjects.getDestID());
        List<List<List<Direction>>> direction = Graph.getGraph().textDirections(Graph.getGraph().separatePathByFloor(shortPath));

        UIControllerPUD controller = (UIControllerPUD) popupScene(POPUP_DIRECTIONS, 600, 400, true);

        controller.setAddedPath(shortPath);
        controller.populateDirections(direction);
        controller.setDirections();
    }

    @FXML
    private void setBackButton() {
        goToScene(UIController.LOGIN_MAIN);
    }

    @FXML
    private void setAboutButton() {
        goToScene(UIController.ABOUT_PAGE);
    }

    @FXML
    private void noStairsToggled() {
        Graph.noStairsIsOn = noStairsButton.isSelected();
        getPath();
    }

    @FXML
    private void setReservationButton() {
        goToScene(UIController.RESERVATIONS_MAIN_MENU);
    }

    @FXML
    private void setResolveRequestButton() {
        goToScene(UIController.USER_RESOLVE_SERVICE_REQUESTS);
    }

    @FXML
    private void setHomeMenuPF() {
        int permission = CurrentUser.user.getPermissions();
        switch (permission) {
            case 1:
                this.goToScene(UIController.LOGIN_MAIN);
                break;
            case 2:
                this.goToScene(UIController.LOGIN_MAIN);
                break;
            case 3:
                this.goToScene(UIController.ADMIN_TOOLS_MAIN);
                break;
            default:
                this.goToScene(UIController.LOGIN_MAIN);
                break;
        }
    }

    @FXML
    private void setFlowerButton() {
        this.popupScene(UIController.SERVICE_REQUEST_FLOWER_DELIVERY, 900, 600, false);
    }

    @FXML
    private void setBabyButton() {
        this.popupScene(UIController.SERVICE_REQUEST_BABYSITTING, 900, 600, false);
    }

    @FXML
    private void setReligiousButton() {
        this.popupScene(UIController.SERVICE_REQUEST_RELIGIOUS_SERVICES, 900, 600, false);
    }

    @FXML
    private void setPatientButton(){
        Main_Registration.main(null);
    }


    @FXML
    private void setOtherButton() {
        this.popupScene(UIController.SERVICE_REQUEST_OTHER_MAIN, 900, 600, false);
    }
}



