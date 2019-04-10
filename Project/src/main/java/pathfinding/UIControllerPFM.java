package pathfinding;

import application.DBController;
import application.UIController;
import application.UIControllerPUD;
import application.UIControllerPUM;
import com.jfoenix.controls.JFXButton;
import entities.Edge;
import entities.Graph;
import entities.Node;
import javafx.animation.PathTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
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

    public HBox hboxForMap;
    public GridPane interfaceGrid;
    public StackPane parentPane;
    @FXML
    public ChoiceBox<String> initialLocationSelect;
    public ChoiceBox<String> destinationSelect;
    public ImageView backgroundImage;
    public Path path;
    public MenuItem backButton;
    public ScrollPane scrollPane_pathfind;
    public ImageView map_imageView;
    public AnchorPane scroll_AnchorPane;
    public Button zoom_button;
    public Button unzoom_button;
    private Graph graph;
    private String initialID;
    private String destID;
    private Group circles = new Group();
    private Circle currentInitCircle;
    private Circle currentDestCircle;
    private PathTransition pathTransition;
    private Random random = new Random(System.currentTimeMillis());
    private LinkedList<Node> usefulNodes;
    private List<Node> currentPath;
    // The multiplication factor at which the map changes size
    private double zoomFactor = 1.2;
    @FXML
    private JFXButton directionsRequest;

    @FXML
    public void initialize() {
        initialBindings();
        setScene();

//        primaryStage.heightProperty().addListener((obs, oldVal, newVal) -> {
//            primaryStage.show();
//        });
//
//        primaryStage.widthProperty().addListener((obs, oldVal, newVal) -> {
//            primaryStage.show();
//        });

        // Only show scroll bars if Image inside is bigger than ScrollPane
        scrollPane_pathfind.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane_pathfind.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
    }

    @Override
    public void onShow() {
        usefulNodes = new LinkedList<>();
        Connection conn = DBController.dbConnect();
        LinkedList<Node> allNodes = DBController.generateListofNodes(conn);
        List<Edge> allEdges = DBController.generateListofEdges(conn);

        try {
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        initialLocationSelect.getItems().clear();
        destinationSelect.getItems().clear();

        for (Node node : allNodes) {
            if (node.getFloor().equals("2") && !node.getNodeType().equals("HALL") && !node.getNodeType().equals("STAI")) {
                // update choices for initial location
                initialLocationSelect.getItems().add(node.getLongName());
                // update choices for destination location
                destinationSelect.getItems().addAll(node.getLongName());
                usefulNodes.add(node);
            }
        }

        this.graph = new Graph(allNodes);

        for (Edge edge : allEdges) {
            try {
                graph.addBiEdge(edge.getNode1ID(), edge.getNode2ID());
            } catch (IllegalArgumentException e) {
            }
        }

        drawNodes();
    }

    private void initialBindings() {
        // bind background image size to window size
        // ensures auto resize works
        backgroundImage.fitHeightProperty().bind(parentPane.heightProperty());
        backgroundImage.fitWidthProperty().bind(parentPane.widthProperty());

        // bind Map to AnchorPane inside of ScrollPane
        map_imageView.fitWidthProperty().bind(scroll_AnchorPane.prefWidthProperty());
        map_imageView.fitHeightProperty().bind(scroll_AnchorPane.prefHeightProperty());

        interfaceGrid.prefHeightProperty().bind(hboxForMap.heightProperty());

        scrollPane_pathfind.prefViewportWidthProperty().bind(hboxForMap.prefWidthProperty());
    }

    private void setScene() {
        // set value to "true" to use zoom functionality
        setZoomOn(true);
        scroll_AnchorPane.getChildren().add(circles);
        path.setStrokeWidth(3);
    }

    @FXML
    public void initLocChanged(ActionEvent actionEvent) {
        if (!(pathTransition == null)) {
            pathTransition.stop();
            scroll_AnchorPane.getChildren().remove(pathTransition.getNode());
        }

        System.out.println("Initial location selected: " + initialLocationSelect.getValue());
        Connection connection = DBController.dbConnect();
        initialID = DBController.IDfromLongName(initialLocationSelect.getValue(), connection);

        focusNodes();

        getPath();

        pathAnimation();
    }

    @FXML
    public void destLocChanged(ActionEvent actionEvent) {
        if (!(pathTransition == null)) {
            pathTransition.stop();
            scroll_AnchorPane.getChildren().remove(pathTransition.getNode());
        }

        System.out.println("Initial location: " + initialLocationSelect.getValue());
        System.out.println("Destination selected: " + destinationSelect.getValue());

        Connection connection = DBController.dbConnect();
        destID = DBController.IDfromLongName(destinationSelect.getValue(), connection);

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
        initialLocationSelect.getSelectionModel().selectFirst();
        clearPathOnMap();
    }

    private void getPath() {
        String dest = destinationSelect.getValue();
        String init = initialLocationSelect.getValue();

        if (dest == null || dest.length() == 0 || init == null || init.length() == 0)
            return;

        System.out.println("getPath called");
        Connection connection = DBController.dbConnect();
        List<String> pathIDs;
        pathIDs = graph.shortestPath(initialID, destID);
        LinkedList<Node> pathNodes = DBController.multiNodeFetch(pathIDs, connection);
        drawPath(pathNodes);
    }

    // TODO: list of all nodes that have: names, XY coords
    private void drawPath(List<Node> nodes) {
        this.currentPath = nodes;
        drawPath();
    }

    private HashMap<String, Float> getScale() {
        HashMap<String, Float> scales = new HashMap<>();
        float scaleFx = (float) map_imageView.getFitWidth() / 5000.0f;
        float scaleFy = (float) map_imageView.getFitHeight() / 3400.0f;
        scales.put("scaleFx", scaleFx);
        scales.put("scaleFy", scaleFy);
        return scales;
    }

    private void drawPath() {
        float scaleFx = getScale().get("scaleFx");
        float scaleFy = getScale().get("scaleFy");

        System.out.println("ScaleFx: " + scaleFx + "  ScaleFy: " + scaleFy);

        clearPathOnMap();
        float x = (float) this.currentPath.get(0).getXcoord() * scaleFx;
        float y = (float) this.currentPath.get(0).getYcoord() * scaleFy;

        path.getElements().add(new MoveTo(x, y)); // move path to initLocation

        // get all XY pairs and turn them into lines
        for (int i = 1; i < this.currentPath.size(); i++) {
            Node node = this.currentPath.get(i);

            x = (float) node.getXcoord() * scaleFx;
            y = (float) node.getYcoord() * scaleFy;

            System.out.println(node);
            System.out.println("NodeX: " + x + "  NodeY: " + y);

            path.getElements().add(new LineTo(x, y));
        }


        path.setStroke(Color.rgb(random.nextInt(255), 0, random.nextInt(255)));
        // draw lines
        path.setVisible(true); //must be the very last thing done once lines are drawn


        //Playing the animation
        //setNodesVisible(false);
        //pathTransition.play();
    }

    private void pathAnimation() {
        pathTransition = new PathTransition();

        //Setting the duration of the path transition
        pathTransition.setDuration(Duration.seconds(4));

        //Setting the node for the transition
        Rectangle circle = new Rectangle(8, 3);
        circle.setFill(Color.LIGHTGREEN);
        scroll_AnchorPane.getChildren().add(circle);
        pathTransition.setNode(circle);

        //Setting the path
        pathTransition.setPath(path);

        //Setting the orientation of the path
        pathTransition.setOrientation(PathTransition.OrientationType.ORTHOGONAL_TO_TANGENT);

        //Setting the cycle count for the transition
        pathTransition.setCycleCount(50);

        //Setting auto reverse value to false
        pathTransition.setAutoReverse(false);

        pathTransition.setCycleCount(1);

        pathTransition.setOnFinished(e -> {
            scroll_AnchorPane.getChildren().remove(circle);
            setZoomOn(true);
            setNodesVisible(true);
            initialLocationSelect.setDisable(false);
            destinationSelect.setDisable(false);
            //setNodesVisible(true);
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
        for (javafx.scene.Node n : circles.getChildren()) {
            if (!currentDestCircle.equals(n) && !currentInitCircle.equals(n)) {
                n.setVisible(bool);
            }
        }
        if (bool) {
            focusNodes();
        }
    }

    private void clearPathOnMap() {
        path.getElements().clear();
        path.setVisible(false);
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

        if (scroll_AnchorPane.getPrefWidth() < scroll_AnchorPane.getMaxWidth()) {
            scroll_AnchorPane.setPrefSize(scroll_AnchorPane.getPrefWidth() * zoomFactor, scroll_AnchorPane.getPrefHeight() * zoomFactor);
        }
        if (this.currentPath != null)
            drawPath();

        circles.getChildren().clear();
        drawNodes();
        focusNodes();
    }

    /**
     * Allows the map to decrease in size, down to scroll_AnchorPane.getMinWidth
     *
     * @param actionEvent Triggered when zoom_button is pressed
     */
    public void unZoom(ActionEvent actionEvent) {

        if (scroll_AnchorPane.getPrefWidth() > scroll_AnchorPane.getMinWidth()) {
            scroll_AnchorPane.setPrefSize(scroll_AnchorPane.getPrefWidth() / zoomFactor, scroll_AnchorPane.getPrefHeight() / zoomFactor);
        }
        if (this.currentPath != null)
            drawPath();

        circles.getChildren().clear();
        drawNodes();
        focusNodes();
    }

    private void drawNodes() {
        float scaleFx = getScale().get("scaleFx");
        float scaleFy = getScale().get("scaleFy");

        float x;
        float y;

        // get all XY pairs and turn them into lines
        for (Node tempNode : usefulNodes) {
            //if (tempNode.getFloor().equals("2") && !tempNode.getNodeType().equals("HALL") && !tempNode.getNodeType().equals("STAI")) {

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

            circles.getChildren().add(circle);
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

        for (javafx.scene.Node n : circles.getChildren()) {
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
        } catch (IOException e){
            Logger logger = Logger.getLogger((getClass().getName()));
            logger.log(Level.SEVERE, "Failed to create new window.", e);

        }

    }

}
