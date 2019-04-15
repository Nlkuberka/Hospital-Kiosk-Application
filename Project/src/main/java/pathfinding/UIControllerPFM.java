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
import javafx.geometry.Dimension2D;
import javafx.geometry.Point2D;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;

import java.io.IOException;
import java.sql.Connection;

import com.jfoenix.controls.JFXButton;
import javafx.animation.PathTransition;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;
import net.kurobako.gesturefx.GesturePane;

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

    @FXML
    public void initialize() {
        backgroundImage.fitWidthProperty().bind(primaryStage.widthProperty());

        setupGesturePanes();
        setupAnchorPanes();

        // ensures new tab has same x,y on the map
        mapTabPane.getSelectionModel().selectedItemProperty().addListener(
                new ChangeListener<Tab>() {
                    @Override
                    public void changed(ObservableValue<? extends Tab> ov, Tab t, Tab t1) {
                        currentFloorIndex = Floors.getByName(t1.getText()).getIndex();
                        int indexOld = Floors.getByName(t.getText()).getIndex();
                        GesturePane pane = gesturePanes.get(currentFloorIndex);
                        GesturePane oldPane = gesturePanes.get(indexOld);
                        pane.centreOn(oldPane.targetPointAtViewportCentre());
                    }
                }
        );

        mapHandler = new MapHandler(pathLL2, pathLL1, pathG, path1, path2, path3, primaryStage);

    }

    @Override
    public void onShow() {
        System.out.println(startingLocation);
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

        // setup circles for nodes
        for (int i = 0; i < this.groupsForNodes.size(); i++) {
            Group group = this.groupsForNodes.get(i);

            for (Node node : roomsAtEachFloor.get(i)) {
                float x = (float) node.getXcoord();
                float y = (float) node.getYcoord();

                Circle circle = new Circle(x, y, 13);
                circle.setId(node.getNodeID());


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

        //drawNodes(roomsAtEachFloor.get(mapHandler.currentFloor.getIndex()));
    }

    private void setupGesturePanes() {
        this.gesturePanes = new LinkedList<GesturePane>();
        gesturePanes.add(lowerLevel2GesturePane);
        gesturePanes.add(lowerLevel1GesturePane);
        gesturePanes.add(groundFloorGesturePane);
        gesturePanes.add(firstFloorGesturePane);
        gesturePanes.add(secondFloorGesturePane);
        gesturePanes.add(thirdFloorGesturePane);

        for(int i = 0; i < this.gesturePanes.size(); i++) {
            GesturePane pane = this.gesturePanes.get(i);
            pane.setMaxScale(1.3);
            pane.setMinScale(0.1);
            pane.setScrollBarEnabled(true);
            pane.setHBarEnabled(true);
        }

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

        GesturePane pane = this.gesturePanes.get(currentFloorIndex);
        pane.zoomTo(0.3, pane.viewportCentre());
        pane.translateBy(new Dimension2D(500.0, 400.0));
    }

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

    private void setUpDefaultStartingLocation(String longName){
        initialLocationSelect.setValue(longName);
    }

    @FXML
    public void initLocChanged(ActionEvent actionEvent) {
        if (!(pathTransition == null)) {
            pathTransition.stop();
//            mapHandler.getTopPane().getChildren().remove(pathTransition.getNode());
        }

        if (initialLocationSelect.getValue() == null)
            return;

        //System.out.println("Initial location selected: " + initialLocationSelect.getValue());
        Connection connection = DBController.dbConnect();
        initialID = DBController.IDfromLongName(initialLocationSelect.getValue(), connection);
        DBController.closeConnection(connection);

        getPath();

        // pathAnimation();
    }

    @FXML
    public void destLocChanged(ActionEvent actionEvent) {
//        if (!(pathTransition == null)) {
//            pathTransition.stop();
//            mapHandler.getTopPane().getChildren().remove(pathTransition.getNode());
//        }

        //System.out.println("Initial location: " + initialLocationSelect.getValue());
        //System.out.println("Destination selected: " + destinationSelect.getValue());

        if (destinationSelect.getValue() == null)
            return;

        Connection connection = DBController.dbConnect();
        System.out.println(destinationSelect.getValue());
        destID = DBController.IDfromLongName(destinationSelect.getValue(), connection);
        DBController.closeConnection(connection);

        // focusNodes();

        // call getPath if not null
        getPath();

        pathAnimation();
    }

    @FXML
    private void cancel(ActionEvent actionEvent) {
        mapHandler.cancel();
        clearNodes();
        clearTabColors();

        initialLocationSelect.getSelectionModel().clearSelection();
        destinationSelect.getSelectionModel().clearSelection();
    }

    private void clearNodes() {
        currentInitCircle.setFill(Color.BLACK);
        currentInitCircle.setRadius(13);
        currentDestCircle.setFill(Color.BLACK);
        currentDestCircle.setRadius(13);
    }

    private void getPath() {

        if(initialID == null || destID == null)
            return;

        List<String> pathIDs;
        pathIDs = Graph.getGraph().shortestPath(initialID, destID);

        Connection connection = DBController.dbConnect();
        Node initialNode = DBControllerNE.fetchNode(initialID, connection);
        DBController.closeConnection(connection);

        // update paths
        mapHandler.displayNewPath(Graph.getGraph().separatePathByFloor(pathIDs), initialNode);

        // change tab based on initial node
        mapTabPane.getSelectionModel().select(Floors.getByID(initialNode.getFloor()).getIndex());

        // center on initial node
        List<Point2D> extremaMinMax = mapHandler.getPathExtremaOnInitFloor(); // get extrema
        double centerX = (extremaMinMax.get(0).getX() + extremaMinMax.get(1).getX()) / 2; // find average
        double centerY = (extremaMinMax.get(0).getY() + extremaMinMax.get(1).getY()) / 2;

        GesturePane pane = getCurrentPane(); // save pane for efficiency

        double ySpan = extremaMinMax.get(1).getY() - extremaMinMax.get(0).getY();
        ySpan = map(ySpan, 0, 3400, pane.getMaxScale(), pane.getMinScale());

        Point2D center = new Point2D(centerX, centerY); // animate to that point
        pane.animate(DURATION)
                .interpolateWith(Interpolator.EASE_BOTH)
                .zoomTo(ySpan / 2.0,  center);

        pane.centreOn(center);

//        pane.animate(DURATION)
//                .interpolateWith(Interpolator.EASE_BOTH)
//                .centreOn(center);

        List<Integer> floorsUsed = mapHandler.getFloorsUsed();
        clearTabColors();
        for (Integer floor : floorsUsed) {
            this.mapTabPane.getTabs().get(floor).setStyle("-fx-background-color: #015080");
        }

    }

    private double map(double x, double in_min, double in_max, double out_min, double out_max) {
        return (x - in_min) * (out_max - out_min) / (in_max - in_min) + out_min;
    }

    private GesturePane getCurrentPane() {
        return this.gesturePanes.get(currentFloorIndex);
    }

    private void clearTabColors() {
        for (Tab tab : this.mapTabPane.getTabs()) {
            tab.setStyle("-fx-background-color: #FFC41E");
        }
    }


    private HashMap<String, Float> getScale() {
        HashMap<String, Float> scales = new HashMap<>();
//        float scaleFx = (float) mapHandler.getCurrentMap().getFitWidth() / 5000.0f;
//        float scaleFy = (float) mapHandler.getCurrentMap().getFitHeight() / 3400.0f;
//        scales.put("scaleFx", scaleFx);
//        scales.put("scaleFy", scaleFy);
        return scales;
    }

    private void pathAnimation() {
        pathTransition = new PathTransition();

        //Setting the duration of the path transition
        pathTransition.setDuration(Duration.seconds(4));

        //Setting the node for the transition
        Rectangle ant = new Rectangle(8, 3);
        ant.setFill(Color.LIGHTGREEN);
//        mapHandler.getTopPane().getChildren().add(ant);
        pathTransition.setNode(ant);

        //Setting the path
//        pathTransition.setPath(mapHandler.getCurrentPath());

        //Setting the orientation of the path
        pathTransition.setOrientation(PathTransition.OrientationType.ORTHOGONAL_TO_TANGENT);

        //Setting auto reverse value to false
        pathTransition.setAutoReverse(false);

        pathTransition.setCycleCount(1);

        pathTransition.setOnFinished(e -> {
//            mapHandler.getTopPane().getChildren().remove(ant);
            setNodesVisible(true);
            initialLocationSelect.setDisable(false);
            destinationSelect.setDisable(false);
        });

        if ((!(currentDestCircle == null)) && (!(currentInitCircle == null))) {
            setNodesVisible(false);
            initialLocationSelect.setDisable(true);
            destinationSelect.setDisable(true);
            pathTransition.play();
        }
    }

    private void setNodesVisible(boolean bool) {
        for (javafx.scene.Node n : circleGroup.getChildren()) {
            if(!(currentDestCircle == null) && !(currentInitCircle == null)) {
                if (!currentDestCircle.equals(n) && !currentInitCircle.equals(n)) {
                    n.setVisible(bool);
                }
            }
        }
        if (bool) {
            // focusNodes();
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

    public void drawNodes(LinkedList<Node> nodes) {

        float x;
        float y;

        // get all XY pairs and turn them into lines
        for (Node tempNode : nodes) {

            x = (float) tempNode.getXcoord();
            y = (float) tempNode.getYcoord();


            Circle circle = new Circle(x, y, 3);
            circle.setId(tempNode.getNodeID());

            circle.setOnMouseClicked(e -> {
                if ((initialLocationSelect.getValue() == null)) {
                    currentInitCircle = circle;
                    currentInitCircle.setFill(Color.GREEN);
                    currentInitCircle.setRadius(5);
                    initialLocationSelect.setValue(tempNode.getLongName());
                } else //if ((destinationSelect.getValue() == null))
                {
                    if (!(currentDestCircle == null)) {
                        currentDestCircle.setFill(Color.BLACK);
                        currentDestCircle.setRadius(3);
                    }
                    currentDestCircle = circle;
                    currentDestCircle.setFill(Color.RED);
                    currentDestCircle.setRadius(5);
                    destinationSelect.setValue(tempNode.getLongName());
                }
            });

            circleGroup.getChildren().add(circle);
        }
    }
    //}

//    private void focusNodes() {
//        if (initialLocationSelect.getValue() == null && !(currentInitCircle == null)) {
//            currentInitCircle.setFill(Color.BLACK);
//            currentInitCircle.setRadius(3);
//            currentInitCircle = null;
//        }
//        if (destinationSelect.getValue() == null && !(currentDestCircle == null)) {
//            currentDestCircle.setFill(Color.BLACK);
//            currentDestCircle.setRadius(3);
//            currentDestCircle = null;
//        }
//
//        for (javafx.scene.Node n : circleGroup.getChildren()) {
//            //if (!(currentInitCircle == null)) {
//            if (n.getId().equals(initialID)) {
//                currentInitCircle = ((Circle) n);
//                currentInitCircle.setRadius(5);
//                currentInitCircle.setFill(Color.LIGHTGREEN);
//            } else if (n.getId().equals(destID)) {
//                currentDestCircle = ((Circle) n);
//                currentDestCircle.setRadius(5);
//                currentDestCircle.setFill(Color.RED);
//            } else {
//                ((Circle) n).setFill(Color.BLACK);
//                ((Circle) n).setRadius(3);
//            }
//        }
//    }

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



