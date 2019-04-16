package pathfinding;

import application.UIController;
import application.UIControllerPUD;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTabPane;
import database.DBController;
import database.DBControllerNE;
import entities.Graph;
import entities.Node;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.PathTransition;
import javafx.animation.Timeline;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Dimension2D;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Path;
import javafx.scene.shape.Rectangle;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;
import net.kurobako.gesturefx.GesturePane;

import java.io.IOException;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

import static application.CurrentUser.startingLocation;

/**
 * Controller for the path_find_main.fxml file
 *
 * @author panagiotisargyrakis, dimitriberardi, ryano647
 */

public class UIControllerPFM extends UIController {

    static final Duration DURATION = Duration.millis(300);
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
    private List<GesturePane> gesturePanes;

    @FXML private AnchorPane lowerLevel2AnchorPane;
    @FXML private AnchorPane lowerLevel1AnchorPane;
    @FXML private AnchorPane groundFloorAnchorPane;
    @FXML private AnchorPane firstFloorAnchorPane;
    @FXML private AnchorPane secondFloorAnchorPane;
    @FXML private AnchorPane thirdFloorAnchorPane;
    private List<AnchorPane> anchorPanes;
    private List<Group> groupsForNodes;

    private Group circleGroup = new Group();
    private Circle currentInitCircle;
    private Circle currentDestCircle;
    private PathTransition pathTransition;
    private Random random = new Random(System.currentTimeMillis());
    private List<Node> currentPath;
    // The multiplication factor at which the map changes size
    private double zoomFactor = 1.2;
    @FXML
    private JFXButton directionsRequest;

    private MapHandler mapHandler;

    private int currentFloorIndex = 0;

    private PathTransition currentAnimation = null;
    private Rectangle currentAnt = null;

    private HashMap<String, Circle> circleFromName;

    /**
     * Initialize various componets, especially panes, tabs and mapHandler
     */
    @FXML
    public void initialize() {
        backgroundImage.fitWidthProperty().bind(primaryStage.widthProperty());

        setupGesturePanes();
        setupAnchorPanes();

        // ensures new tab has same x,y on the map and path animation changes between floors
        mapTabPane.getSelectionModel().selectedItemProperty().addListener(
                new ChangeListener<Tab>() {
                    @Override
                    public void changed(ObservableValue<? extends Tab> ov, Tab t, Tab t1) {
                        clearPathTransition();
                        currentFloorIndex = Floors.getByName(t1.getText()).getIndex();
                        int indexOld = Floors.getByName(t.getText()).getIndex();
                        GesturePane pane = gesturePanes.get(currentFloorIndex);
                        GesturePane oldPane = gesturePanes.get(indexOld);
                        pane.centreOn(oldPane.targetPointAtViewportCentre());
                        if (mapHandler.isActive()) {
                            newAnimation(mapHandler.getPaths().get(currentFloorIndex), anchorPanes.get(currentFloorIndex));
                        }
                    }
                }
        );

        mapHandler = new MapHandler(pathLL2, pathLL1, pathG, path1, path2, path3, primaryStage);

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

        // ~~~~~~ init circles

        this.circleFromName = new HashMap<>(); // map to get corresponding circles from longnames

        // setup circles for nodes
        for (int i = 0; i < this.groupsForNodes.size(); i++) {
            Group group = this.groupsForNodes.get(i);

            for (Node node : roomsAtEachFloor.get(i)) {
                float x = (float) node.getXcoord();
                float y = (float) node.getYcoord();

                Circle circle = new Circle(x, y, 13);
                circle.setId(node.getNodeID());
                Tooltip tooltip = new Tooltip(node.getShortName());
                hackTooltipStartTiming(tooltip);
                Tooltip.install(circle, tooltip);

                this.circleFromName.put(node.getLongName(), circle); // setup hashmap

                circle.setOnMouseClicked(e -> {
                    if ((initialLocationSelect.getValue() == null)) {
                        currentInitCircle = circle;
                        currentInitCircle.setFill(Color.GREEN);
                        currentInitCircle.setRadius(16);
                        initialLocationSelect.setValue(node.getLongName());
                    } else if ((destinationSelect.getValue() == null))
                    {
                        currentDestCircle = circle;
                        currentDestCircle.setFill(Color.RED);
                        currentDestCircle.setRadius(16);
                        destinationSelect.setValue(node.getLongName());
                    }
                });

                setUpDefaultStartingLocation(startingLocation);

                group.getChildren().add(circle);
            }
            group.setVisible(true);
        }

    }

//    void createMenuOnNode() {
//        AnchorPane pane = anchorPanes.get(currentFloorIndex);
//        Rectangle rectangle = new Rectangle();
//    }

    /**
     * Sets up gesture panes. 1) adds gesture panes to list 2) sets minscale, maxscale and scroll-bar
     * 3) applies zoom bindings 4) sets event handlers for zoom 4) sets initial zoom
     *
     */
    private void setupGesturePanes() {
        this.gesturePanes = new LinkedList<GesturePane>();
        gesturePanes.add(lowerLevel2GesturePane);
        gesturePanes.add(lowerLevel1GesturePane);
        gesturePanes.add(groundFloorGesturePane);
        gesturePanes.add(firstFloorGesturePane);
        gesturePanes.add(secondFloorGesturePane);
        gesturePanes.add(thirdFloorGesturePane);

        // setup properties
        for(int i = 0; i < this.gesturePanes.size(); i++) {
            GesturePane pane = this.gesturePanes.get(i);
            pane.setMaxScale(1.3);
            pane.setMinScale(0.1);
            pane.setScrollBarEnabled(true);
            pane.setHBarEnabled(true);
        }

        // setup scale bindings
        for(int i = 0; i < this.gesturePanes.size()-1; i++) {
            GesturePane pane = this.gesturePanes.get(i);
            GesturePane next = this.gesturePanes.get(i+1);
            pane.currentScaleProperty().bindBidirectional(next.currentScaleProperty());
        }


        // zoom*2 on double-click
        for (GesturePane pane : this.gesturePanes) {
            pane.setOnMouseClicked(e -> {
                Point2D pivotOnTarget = pane.targetPointAt(new Point2D(e.getX(), e.getY()))
                        .orElse(pane.targetPointAtViewportCentre());
                if (e.getButton() == MouseButton.PRIMARY && e.getClickCount() == 2) {
                    // increment of scale makes more sense exponentially instead of linearly
                    pane.animate(DURATION)
                            .interpolateWith(Interpolator.EASE_BOTH)
                            .zoomBy(pane.getCurrentScale(), pivotOnTarget);
                } else if (e.getButton() == MouseButton.SECONDARY && e.getClickCount() == 1) {
                    pane.animate(DURATION)
                            .interpolateWith(Interpolator.EASE_BOTH)
                            .zoomTo(pane.getMinScale(), pivotOnTarget);
                }
            });
        }

        // zoom so that it looks good
        GesturePane pane = this.gesturePanes.get(currentFloorIndex);
        pane.zoomTo(0.3, pane.viewportCentre());
        pane.translateBy(new Dimension2D(500.0, 400.0));
    }

    /**
     * Setup anchor panes such that they are in a list and have groups for the node circles
     */
    private void setupAnchorPanes() {
        this.anchorPanes = new LinkedList<AnchorPane>();
        anchorPanes.add(lowerLevel2AnchorPane);
        anchorPanes.add(lowerLevel1AnchorPane);
        anchorPanes.add(groundFloorAnchorPane);
        anchorPanes.add(firstFloorAnchorPane);
        anchorPanes.add(secondFloorAnchorPane);
        anchorPanes.add(thirdFloorAnchorPane);

        this.groupsForNodes = new LinkedList<>(); // add groups for circles
        for (AnchorPane anchorPane : this.anchorPanes) {
            Group group = new Group();
            this.groupsForNodes.add(group);
            anchorPane.getChildren().add(group);
        }

    }

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

        this.currentInitCircle = circleFromName.get(initialLocationSelect.getValue());

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

        this.currentDestCircle = circleFromName.get(destinationSelect.getValue());

        // call getPath if not null
        getPath();
    }

    /**
     * Clears currentAnimation and currentAnt attributes and removes ant from anchorPane
     */
    private void clearPathTransition() {
        // remove animation
        if (currentAnimation != null) {
            currentAnimation.stop();
            currentAnimation = null;
        }

        // remove ant
        if (this.currentAnt != null) {
            this.anchorPanes.get(currentFloorIndex).getChildren().removeAll(this.currentAnt);
            this.currentAnt = null;
        }
    }

    /**
     * Callback for cancel. Clears path, animation, node selection and drop down menus
     * @param actionEvent
     */
    @FXML
    private void cancel(ActionEvent actionEvent) {
        mapHandler.cancel();
        clearNodes();
        clearTabColors();

        initialID = null;
        destID = null;

        clearPathTransition();

        initialLocationSelect.getSelectionModel().clearSelection();
        destinationSelect.getSelectionModel().clearSelection();
    }

    /**
     * Clear style of currently selected circles
     */
    private void clearNodes() {
        currentInitCircle.setFill(Color.BLACK);
        currentInitCircle.setRadius(13);
        currentDestCircle.setFill(Color.BLACK);
        currentDestCircle.setRadius(13);
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

        clearPathTransition(); // reset stuff
        mapHandler.cancel(); // reset stuff

        // change tab based on initial node -- order here is important! Do not move below.
        mapTabPane.getSelectionModel().select(Floors.getByID(initialNode.getFloor()).getIndex());

        // update paths -- order here is important! Do not move above change tab.
        mapHandler.displayNewPath(Graph.getGraph().separatePathByFloor(pathIDs), initialNode);

        // center on initial node
        List<Point2D> extremaMinMax = mapHandler.getPathExtremaOnInitFloor(); // get extrema
        double centerX = (extremaMinMax.get(0).getX() + extremaMinMax.get(1).getX()) / 2; // find average
        double centerY = (extremaMinMax.get(0).getY() + extremaMinMax.get(1).getY()) / 2;

        GesturePane pane = getCurrentPane(); // save pane for efficiency

        double ySpan = extremaMinMax.get(1).getY() - extremaMinMax.get(0).getY();
        ySpan = map(ySpan, 0, 3400, pane.getMaxScale(), pane.getMinScale());

        Point2D center = new Point2D(centerX, centerY); // animate to that point
//        pane.animate(DURATION)
//                .interpolateWith(Interpolator.EASE_BOTH)
//                .zoomTo(ySpan / 2.0,  center);

//        pane.centreOn(center);

        pane.animate(DURATION)
                .interpolateWith(Interpolator.EASE_BOTH)
                .centreOn(center);

        List<Integer> floorsUsed = mapHandler.getFloorsUsed();
        clearTabColors();
        for (Integer floor : floorsUsed) {
            this.mapTabPane.getTabs().get(floor).setStyle("-fx-background-color: #015080");
        }

        newAnimation(mapHandler.getPaths().get(currentFloorIndex), anchorPanes.get(currentFloorIndex));

    }

    /**
     * Linearly map a variable from one range to another
     * @param x
     * @param in_min
     * @param in_max
     * @param out_min
     * @param out_max
     * @return
     */
    private double map(double x, double in_min, double in_max, double out_min, double out_max) {
        return (x - in_min) * (out_max - out_min) / (in_max - in_min) + out_min;
    }

    /**
     * Returns gesture pane of current floor
     * @return
     */
    private GesturePane getCurrentPane() {
        return this.gesturePanes.get(currentFloorIndex);
    }

    /**
     * Clear marking of tab headers
     */
    private void clearTabColors() {
        for (Tab tab : this.mapTabPane.getTabs()) {
            tab.setStyle("-fx-background-color: #FFC41E");
        }
    }


    /**
     * Generates new animation based on given path. Sets the currentAnt and currentAnimation attributes
     * @param path the path to be animated
     * @param pane the pane on which to animate
     */
    private void newAnimation(Path path, AnchorPane pane) {
        pathTransition = new PathTransition();

        //Setting the duration of the path transition
        pathTransition.setDuration(Duration.seconds(3));

        //Setting the node for the transition
        this.currentAnt = new Rectangle(55, 20);
        this.currentAnt.setFill(Color.LIGHTGREEN);
        this.anchorPanes.get(currentFloorIndex).getChildren().add(this.currentAnt);
        pathTransition.setNode(this.currentAnt);

        //Setting the path
        pathTransition.setPath(mapHandler.getPaths().get(currentFloorIndex));

        //Setting the orientation of the path
        pathTransition.setOrientation(PathTransition.OrientationType.ORTHOGONAL_TO_TANGENT);

        //Setting auto reverse value to false
        pathTransition.setAutoReverse(false);

        pathTransition.setCycleCount(99);

        pathTransition.setOnFinished(e -> {
            clearPathTransition();
        });

        if ((!(currentDestCircle == null)) && (!(currentInitCircle == null))) {
            pathTransition.play();
        }

        this.currentAnimation = pathTransition;
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
        GesturePane pane = this.gesturePanes.get(this.currentFloorIndex);
        Point2D pivotOnTarget = pane.targetPointAtViewportCentre();
        // increment of scale makes more sense exponentially instead of linearly
        pane.animate(DURATION)
                .interpolateWith(Interpolator.EASE_BOTH)
                .zoomBy(pane.getCurrentScale()/1.66, pivotOnTarget);
    }

    /**
     * Allows the map to decrease in size, down to scroll_AnchorPane.getMinWidth
     *
     * @param actionEvent Triggered when zoom_button is pressed
     */
    public void unZoom(ActionEvent actionEvent) {
        GesturePane pane = this.gesturePanes.get(this.currentFloorIndex);
        Point2D pivotOnTarget = pane.targetPointAtViewportCentre();
        pane.animate(DURATION)
                .interpolateWith(Interpolator.EASE_BOTH)
                .zoomBy(-0.33, pivotOnTarget);
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

    private static void hackTooltipStartTiming(Tooltip tooltip) {
        try {
            Field fieldBehavior = tooltip.getClass().getDeclaredField("BEHAVIOR");
            fieldBehavior.setAccessible(true);
            Object objBehavior = fieldBehavior.get(tooltip);

            Field fieldTimer = objBehavior.getClass().getDeclaredField("activationTimer");
            fieldTimer.setAccessible(true);
            Timeline objTimer = (Timeline) fieldTimer.get(objBehavior);

            objTimer.getKeyFrames().clear();
            objTimer.getKeyFrames().add(new KeyFrame(new Duration(0)));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}



