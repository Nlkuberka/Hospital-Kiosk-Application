package pathfinding;

import application.DBController;
import application.UIController;
import entities.Edge;
import entities.Graph;
import entities.Node;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/**
 * Controller for the path_find_main.fxml file
 *
 * @author panagiotisargyrakis, dimitriberardi, ryano647
 */

public class UIControllerPFM extends UIController {

    public HBox hboxForMap;
    public GridPane interfaceGrid;
    public StackPane parentPane;
    private Graph graph;
    private String initialID;
    private String destID;
    private Group circles = new Group();
    Circle currentInitCircle;
    Circle currentDestCircle;

    @FXML
    public ChoiceBox<String> initialLocationSelect;
    //public VBox leftVBox;
    public ChoiceBox<String> destinationSelect;
    public ImageView backgroundImage;
    public Path path;
    public MenuItem backButton;
    public ScrollPane scrollPane_pathfind;
    public ImageView map_imageView;
    public AnchorPane scroll_AnchorPane;
    public Button zoom_button;
    public Button unzoom_button;
    private List<Node> currentPath;

    @FXML
    public void initialize() {

        // bind background image size to window size
        // ensures auto resize works
        backgroundImage.fitHeightProperty().bind(parentPane.heightProperty());
        backgroundImage.fitWidthProperty().bind(parentPane.widthProperty());

        // bind Map to AnchorPane inside of ScrollPane
        map_imageView.fitWidthProperty().bind(scroll_AnchorPane.prefWidthProperty());
        map_imageView.fitHeightProperty().bind(scroll_AnchorPane.prefHeightProperty());

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

        interfaceGrid.prefHeightProperty().bind(hboxForMap.heightProperty());


        scrollPane_pathfind.prefViewportWidthProperty().bind(hboxForMap.prefWidthProperty());
//        scrollPane_pathfind.prefViewportHeightProperty().bind(hboxForMap.prefHeightProperty());

        // set value to "true" to use zoom functionality
        setZoomOn(true);


        // path demo code
//        path.getElements().add(new MoveTo(0.0f, 0.0f));
//        path.getElements().add(new LineTo(100.0f, 100.0f));
//        path.getElements().add(new LineTo(200.0f, 150.0f));
        scroll_AnchorPane.getChildren().add(circles);
        drawNodes();
    }

    @Override
    public void onShow() {
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

        LinkedList<Node> usefulNodes = new LinkedList<>();
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

        List<Edge> usefulEdges = new LinkedList<>();
        for (Edge edge : allEdges) {
            try {
                graph.addBiEdge(edge.getNode1ID(), edge.getNode2ID());
            } catch (IllegalArgumentException e) {

            }
        }
    }


    @FXML
    public void initLocChanged(ActionEvent actionEvent) {
        System.out.println("Initial location selected: " + initialLocationSelect.getValue());
        Connection connection = DBController.dbConnect();
        initialID = DBController.IDfromLongName(initialLocationSelect.getValue(), connection);

        for (javafx.scene.Node n : circles.getChildren()) {
            if (n.getId().equals(initialID)) {
                if (!(currentInitCircle == null)) {
                    currentInitCircle.setFill(Color.BLACK);
                    currentInitCircle.setRadius(3);
                }
                currentInitCircle = ((Circle) n);
                currentInitCircle.setRadius(5);
                currentInitCircle.setFill(Color.LIGHTGREEN);
            }
        }

        getPath();
    }

    @FXML
    public void destLocChanged(ActionEvent actionEvent) {
        System.out.println("Initial location: " + initialLocationSelect.getValue());
        System.out.println("Destination selected: " + destinationSelect.getValue());

        Connection connection = DBController.dbConnect();
        destID = DBController.IDfromLongName(destinationSelect.getValue(), connection);

        for (javafx.scene.Node n : circles.getChildren()) {
            if (n.getId().equals(destID)) {
                if (!(currentDestCircle == null)) {
                    currentDestCircle.setFill(Color.BLACK);
                    currentDestCircle.setRadius(3);
                }
                currentDestCircle = ((Circle) n);
                currentDestCircle.setRadius(5);
                currentDestCircle.setFill(Color.RED);
            }
        }

        // call getPath if not null
        getPath();
    }


    @FXML
    private void clearSelection(ActionEvent actionEvent) {
        initialLocationSelect.getSelectionModel().selectFirst();
        destinationSelect.getSelectionModel().clearSelection();
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

        // draw lines
        path.setVisible(true); //must be the very last thing done once lines are drawn
    }

    private void clearPathOnMap() {
        path.getElements().clear();
        path.setVisible(false);
    }

    public void goBack(ActionEvent actionEvent) {
        this.goToScene(UIController.LOGIN_MAIN);
    }

    // The multiplication factor at which the map changes size
    private double zoomFactor = 1.2;

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
    }

    public void drawNodes() {
        Connection conn = DBController.dbConnect();
        LinkedList<Node> allNodes = DBController.generateListofNodes(conn);

        try {
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        float scaleFx = getScale().get("scaleFx");
        float scaleFy = getScale().get("scaleFy");

        float x;
        float y;

        // get all XY pairs and turn them into lines
        for (int i = 0; i < allNodes.size(); i++) {
            Node nodeCopy = allNodes.get(i);

            if (nodeCopy.getFloor().equals("2") && !nodeCopy.getNodeType().equals("HALL") && !nodeCopy.getNodeType().equals("STAI")) {

                x = (float) nodeCopy.getXcoord() * scaleFx;
                y = (float) nodeCopy.getYcoord() * scaleFy;
                Circle circle = new Circle(x, y, 3);
                circle.setId(nodeCopy.getNodeID());

                circle.setOnMouseClicked(e -> {
                    if ((initialLocationSelect.getValue() == null)) {
                        currentInitCircle = circle;
                        currentInitCircle.setFill(Color.GREEN);
                        currentInitCircle.setRadius(5);
                        initialLocationSelect.setValue(nodeCopy.getLongName());
                    } else //if ((destinationSelect.getValue() == null))
                    {
                        if (!(currentDestCircle == null)) {
                            currentDestCircle.setFill(Color.BLACK);
                            currentDestCircle.setRadius(3);
                        }
                        currentDestCircle = circle;
                        currentDestCircle.setFill(Color.RED);
                        currentDestCircle.setRadius(5);
                        destinationSelect.setValue(nodeCopy.getLongName());
                    }
                });

                circles.getChildren().add(circle);
            }
        }
    }


}
