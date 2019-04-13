package pathfinding;

import application.DBController;
import application.UIController;
import application.UIControllerPUD;
import com.jfoenix.controls.JFXSlider;
import entities.AStarGraph;
import entities.Edge;
import entities.Graph;
import entities.Node;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Path;

import java.io.IOException;
import java.sql.Connection;

import com.jfoenix.controls.JFXButton;
import javafx.animation.PathTransition;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Controller for the path_find_main.fxml file
 *
 * @author panagiotisargyrakis, dimitriberardi, ryano647
 */

public class UIControllerPFM extends UIController {

    public enum Floors {
        FIRST("First Floor", "1", 3), GROUND("Ground Floor", "G", 2), LL1("Lower Level 1", "L1", 1),
        LL2("Lower Level 2", "L2", 0), SECOND("Second Floor", "2", 4), THIRD("Third Floor", "3", 5);

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
    @FXML
    private JFXSlider floorSlider;

    private Graph graph;
    private String initialID;
    private String destID;

    @FXML
    public ChoiceBox<String> initialLocationSelect;
    @FXML
    private Label floorLabel;
    @FXML
    private ChoiceBox<String> destinationSelect;
    @FXML
    private ImageView backgroundImage;
    @FXML
    private MenuItem backButton;
    @FXML
    private ScrollPane scrollPane_pathfind;
    @FXML
    private ImageView map_02, map_01, map_03, map_00, map_001, map_002;
    @FXML
    private Path p_002, p_001, p_00, p_01, p_02, p_03;
    @FXML
    private AnchorPane pane_002, pane_001, pane_00, pane_01, pane_02, pane_03;
    @FXML
    private Button zoom_button;
    @FXML
    private Button unzoom_button;

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

    private LinkedList<LinkedList<Node>> roomsAtEachFloor = new LinkedList<>();

    private MapHandler mapHandler;

    @FXML
    public void initialize() {

        this.mapHandler = new MapHandler(p_002, p_001, p_00, p_01, p_02, p_03,
                map_002, map_001, map_00, map_01, map_02, map_03,
                pane_002, pane_001, pane_00, pane_01, pane_02, pane_03,
                Floors.SECOND);

        initialBindings();
        setScene();


        floorSlider.setMax(5.0); // number of floors - 1
        floorSlider.setValue(2.0);
        floorLabel.setText(Floors.getByIndex((int) floorSlider.getValue()).getName());


        // Only show scroll bars if Image inside is bigger than ScrollPane
        scrollPane_pathfind.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane_pathfind.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);


        scrollPane_pathfind.prefViewportWidthProperty().bind(hboxForMap.prefWidthProperty());
//        scrollPane_pathfind.prefViewportHeightProperty().bind(hboxForMap.prefHeightProperty());

        // set value to "true" to use zoom functionality
        setZoomOn(true);

        floorSlider.valueProperty().addListener(new ChangeListener() {

            @Override
            public void changed(ObservableValue arg0, Object arg1, Object arg2) {
                floorLabel.setText(Floors.getByIndex((int) floorSlider.getValue()).getName());
                circleGroup.getChildren().clear();
                mapHandler.changeToFloor(floorSlider.getValue());
                drawNodes(roomsAtEachFloor.get(mapHandler.currentFloor.getIndex()));
                focusNodes();
            }
        });
    }

    @Override
    public void onShow() {
        Connection conn = DBController.dbConnect();
        LinkedList<Node> allNodes = DBController.generateListofNodes(conn);
        LinkedList<Node> allRooms = DBController.fetchAllRooms(conn);
        List<Edge> allEdges = DBController.generateListofEdges(conn);

        roomsAtEachFloor.add(DBController.getRoomsforFloor(conn, Floors.LL2.getID()));
        roomsAtEachFloor.add(DBController.getRoomsforFloor(conn, Floors.LL1.getID()));
        roomsAtEachFloor.add(DBController.getRoomsforFloor(conn, Floors.GROUND.getID()));
        roomsAtEachFloor.add(DBController.getRoomsforFloor(conn, Floors.FIRST.getID()));
        roomsAtEachFloor.add(DBController.getRoomsforFloor(conn, Floors.SECOND.getID()));
        roomsAtEachFloor.add(DBController.getRoomsforFloor(conn, Floors.THIRD.getID()));

        DBController.closeConnection(conn);

        initialLocationSelect.getItems().clear();
        destinationSelect.getItems().clear();

        for (Node node : allRooms) {
            // update choices for initial location
            initialLocationSelect.getItems().add(node.getLongName());
            // update choices for destination location
            destinationSelect.getItems().addAll(node.getLongName());
        }

        this.graph = new AStarGraph(allNodes);

        for (Edge edge : allEdges) {
            try {
                graph.addBiEdge(edge.getNode1ID(), edge.getNode2ID());
            } catch (IllegalArgumentException e) {
            }
        }

        drawNodes(roomsAtEachFloor.get(mapHandler.currentFloor.getIndex()));
    }

    private void initialBindings() {
        // bind background image size to window size
        // ensures auto resize works
        backgroundImage.fitHeightProperty().bind(parentPane.heightProperty());
        backgroundImage.fitWidthProperty().bind(parentPane.widthProperty());

        interfaceGrid.prefHeightProperty().bind(hboxForMap.heightProperty());

        scrollPane_pathfind.prefViewportWidthProperty().bind(hboxForMap.prefWidthProperty());
    }

    private void setScene() {
        // set value to "true" to use zoom functionality
        setZoomOn(true);
        mapHandler.getTopPane().getChildren().add(circleGroup);
    }

    @FXML
    public void initLocChanged(ActionEvent actionEvent) {
        if (!(pathTransition == null)) {
            pathTransition.stop();
            mapHandler.getTopPane().getChildren().remove(pathTransition.getNode());
        }

        //System.out.println("Initial location selected: " + initialLocationSelect.getValue());
        Connection connection = DBController.dbConnect();
        initialID = DBController.IDfromLongName(initialLocationSelect.getValue(), connection);
        DBController.closeConnection(connection);

        focusNodes();

        getPath();

        pathAnimation();
    }

    @FXML
    public void destLocChanged(ActionEvent actionEvent) {
        if (!(pathTransition == null)) {
            pathTransition.stop();
            mapHandler.getTopPane().getChildren().remove(pathTransition.getNode());
        }

        //System.out.println("Initial location: " + initialLocationSelect.getValue());
        //System.out.println("Destination selected: " + destinationSelect.getValue());

        Connection connection = DBController.dbConnect();
        System.out.println(destinationSelect.getValue());
        destID = DBController.IDfromLongName(destinationSelect.getValue(), connection);
        DBController.closeConnection(connection);

        focusNodes();

        // call getPath if not null
        getPath();

        pathAnimation();
    }

    @FXML
    private void clearSelection(ActionEvent actionEvent) {
        setNodesVisible(true);
        setZoomOn(true);
        initialLocationSelect.setDisable(false);
        destinationSelect.setDisable(false);
        currentPath = null;
        destinationSelect.getSelectionModel().clearSelection();
        mapHandler.cancel();
    }

    private void getPath() {

        if(initialID == null || destID == null)
            return;

        List<String> pathIDs;
        pathIDs = graph.shortestPath(initialID, destID);

        Connection connection = DBController.dbConnect();
        Node initialNode = DBController.fetchNode(initialID, connection);
        DBController.closeConnection(connection);

        mapHandler.displayNewPath(graph.separatePathByFloor(pathIDs), initialNode);
    }


    private HashMap<String, Float> getScale() {
        HashMap<String, Float> scales = new HashMap<>();
        float scaleFx = (float) mapHandler.getCurrentMap().getFitWidth() / 5000.0f;
        float scaleFy = (float) mapHandler.getCurrentMap().getFitHeight() / 3400.0f;
        scales.put("scaleFx", scaleFx);
        scales.put("scaleFy", scaleFy);
        return scales;
    }

    private void pathAnimation() {
        pathTransition = new PathTransition();

        //Setting the duration of the path transition
        pathTransition.setDuration(Duration.seconds(4));

        //Setting the node for the transition
        Rectangle ant = new Rectangle(8, 3);
        ant.setFill(Color.LIGHTGREEN);
        mapHandler.getTopPane().getChildren().add(ant);
        pathTransition.setNode(ant);

        //Setting the path
        pathTransition.setPath(mapHandler.getCurrentPath());

        //Setting the orientation of the path
        pathTransition.setOrientation(PathTransition.OrientationType.ORTHOGONAL_TO_TANGENT);

        //Setting auto reverse value to false
        pathTransition.setAutoReverse(false);

        pathTransition.setCycleCount(1);

        pathTransition.setOnFinished(e -> {
            mapHandler.getTopPane().getChildren().remove(ant);
            setZoomOn(true);
            setNodesVisible(true);
            initialLocationSelect.setDisable(false);
            destinationSelect.setDisable(false);
        });

        if ((!(currentDestCircle == null)) && (!(currentInitCircle == null))) {
            setZoomOn(false);
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
            focusNodes();
        }
    }



    public void goBack(ActionEvent actionEvent) {
        this.goToScene(UIController.LOGIN_MAIN);
    }

    /**
     * @param bool Set in initialize() to turn on/off zoom functionality
     */
    private void setZoomOn(boolean bool) {
        zoom_button.setVisible(bool);
        unzoom_button.setVisible(bool);
    }

    /**
     * Allows the map to increase in size, up to scroll_AnchorPane.getMaxWidth
     *
     * @param actionEvent Triggered when zoom_button is pressed
     */
    public void zoom(ActionEvent actionEvent) {

        mapHandler.zoomIn(zoomFactor);

        circleGroup.getChildren().clear();
        drawNodes(roomsAtEachFloor.get(mapHandler.currentFloor.getIndex()));
        focusNodes();
    }

    /**
     * Allows the map to decrease in size, down to scroll_AnchorPane.getMinWidth
     *
     * @param actionEvent Triggered when zoom_button is pressed
     */
    public void unZoom(ActionEvent actionEvent) {

        mapHandler.zoomOut(zoomFactor);

        circleGroup.getChildren().clear();
        drawNodes(roomsAtEachFloor.get(mapHandler.currentFloor.getIndex()));
        focusNodes();
    }

    public void drawNodes(LinkedList<Node> nodes) {
        float scaleFx = getScale().get("scaleFx");
        float scaleFy = getScale().get("scaleFy");

        float x;
        float y;

        // get all XY pairs and turn them into lines
        for (Node tempNode : nodes) {

            x = (float) tempNode.getXcoord() * scaleFx;
            y = (float) tempNode.getYcoord() * scaleFy;


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

    private void focusNodes() {
        if (initialLocationSelect.getValue() == null && !(currentInitCircle == null)) {
            currentInitCircle.setFill(Color.BLACK);
            currentInitCircle.setRadius(3);
            currentInitCircle = null;
        }
        if (destinationSelect.getValue() == null && !(currentDestCircle == null)) {
            currentDestCircle.setFill(Color.BLACK);
            currentDestCircle.setRadius(3);
            currentDestCircle = null;
        }

        for (javafx.scene.Node n : circleGroup.getChildren()) {
            //if (!(currentInitCircle == null)) {
            if (n.getId().equals(initialID)) {
                currentInitCircle = ((Circle) n);
                currentInitCircle.setRadius(5);
                currentInitCircle.setFill(Color.LIGHTGREEN);
            } else if (n.getId().equals(destID)) {
                currentDestCircle = ((Circle) n);
                currentDestCircle.setRadius(5);
                currentDestCircle.setFill(Color.RED);
            } else {
                ((Circle) n).setFill(Color.BLACK);
                ((Circle) n).setRadius(3);
            }
        }
    }

    @FXML
    private void directionSelection() {
        String direction = graph.textDirections(graph.shortestPath(initialID, destID));
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

}
