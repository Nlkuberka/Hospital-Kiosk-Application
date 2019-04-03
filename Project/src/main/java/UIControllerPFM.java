import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.shape.Rectangle;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

public class UIControllerPFM extends UIController {

    public HBox hboxForMap;
    private Graph graph;
    private String initialID;
    private String destID;

    @FXML
    public ChoiceBox<String> initialLocationSelect;
    public VBox leftVBox;
    public Rectangle rectangleLeft;
    public ChoiceBox<String> destinationSelect;
    public ImageView backgroundImage;
    public AnchorPane parentAnchorPane;
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
//        leftVBox.prefWidthProperty().bind(parentAnchorPane.widthProperty().multiply(0.15));

        // bind choicebox widths to pane
        initialLocationSelect.prefWidthProperty().bind(leftVBox.prefWidthProperty().multiply(0.75));
        destinationSelect.prefWidthProperty().bind(leftVBox.prefWidthProperty().multiply(0.75));

        // bind background image size to window size
        // ensures auto resize works
        backgroundImage.fitHeightProperty().bind(parentAnchorPane.heightProperty());
        backgroundImage.fitWidthProperty().bind(parentAnchorPane.widthProperty());

        // bind opaque rectangle to leftVbox width
        rectangleLeft.widthProperty().bind(leftVBox.prefWidthProperty());
        rectangleLeft.heightProperty().bind(hboxForMap.prefHeightProperty());

        // bind Map to AnchorPane inside of ScrollPane
        map_imageView.fitWidthProperty().bind(scroll_AnchorPane.prefWidthProperty());
        map_imageView.fitHeightProperty().bind(scroll_AnchorPane.prefHeightProperty());

//        primaryStage.widthProperty().addListener((obs, oldVal, newVal) -> {
//            System.out.println("Old Value Width " + oldVal);
//            System.out.println("New Value Width " + newVal);
////            scrollPane_pathfind.prefViewportWidthProperty().set(newVal.floatValue());
//        });
//
//        primaryStage.heightProperty().addListener((obs, oldVal, newVal) -> {
//            System.out.println("Old Value Height " + oldVal);
//            System.out.println("new Value Height " + newVal);
//        });


        // Only show scroll bars if Image inside is bigger than ScrollPane
        scrollPane_pathfind.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane_pathfind.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);

//        scrollPane_pathfind.prefViewportHeightProperty().bind(parentAnchorPane.prefHeightProperty());
        scrollPane_pathfind.prefViewportWidthProperty().bind(hboxForMap.prefWidthProperty());

//        scrollPane_pathfind.setFitToHeight(true);
//        scrollPane_pathfind.setFitToWidth(true);

        // set value to "true" to use zoom functionality
        setZoomOn(true);

        Connection conn = DBController.dbConnect();
        LinkedList<Node> allNodes = DBController.generateListofNodes(conn);
        List<Edge> allEdges = DBController.generateListofEdges(conn);

        try {
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        LinkedList<Node> usefulNodes = new LinkedList<>();
        for (Node node : allNodes) {
            if (node.getFloor().equals("2") && node.getBuilding().equals("Tower")) {
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
            }
            catch(IllegalArgumentException e) {

            }
        }

        // path demo code
//        path.getElements().add(new MoveTo(0.0f, 0.0f));
//        path.getElements().add(new LineTo(100.0f, 100.0f));
//        path.getElements().add(new LineTo(200.0f, 150.0f));
    }



    @FXML
    public void initLocChanged(ActionEvent actionEvent) {
        System.out.println("Initial location selected: " + initialLocationSelect.getValue());
        Connection connection = DBController.dbConnect();
        initialID = DBController.IDfromLongName(initialLocationSelect.getValue(), connection);
    }

    @FXML
    public void destLocChanged(ActionEvent actionEvent) {
        String value = destinationSelect.getValue();

        System.out.println("Initial location: " + initialLocationSelect.getValue());
        System.out.println("Destination selected: " + value);

        Connection connection = DBController.dbConnect();
        destID = DBController.IDfromLongName(destinationSelect.getValue(), connection);

        // call getPath if not null
        if (!(value == null || value.length() == 0)) {
            getPath();
        }
    }


    @FXML
    private void clearSelection(ActionEvent actionEvent) {
        initialLocationSelect.getSelectionModel().selectFirst();
        destinationSelect.getSelectionModel().clearSelection();
        clearPathOnMap();
    }

    private void getPath() {
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

    private void drawPath() {
        float scaleFx = (float) map_imageView.getFitWidth() / 5000.0f;
        float scaleFy = (float) map_imageView.getFitHeight() / 3400.0f;

        System.out.println("ScaleFx: " + scaleFx + "  ScaleFy: " + scaleFy);

        clearPathOnMap();

        float x = (float) this.currentPath.get(0).getXcoord() * scaleFx;
        float y = (float) this.currentPath.get(0).getYcoord() * scaleFy;

        path.getElements().add(new MoveTo(x, y)); // move path to initLocation

        // get all XY pairs and turn them into lines
        for (int i = 1; i < this.currentPath.size() - 1; i++) {
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
     *
     * @param bool Set in initialize() to turn on/off zoom functionality
     */
    private void setZoomOn(boolean bool) {
        zoom_button.setVisible(bool);
        unzoom_button.setVisible(bool);
    }

    /**
     * Allows the map to increase in size, up to scroll_AnchorPane.getMaxWidth
     * @param actionEvent Triggered when zoom_button is pressed
     */
    public void zoom(ActionEvent actionEvent) {

        if (scroll_AnchorPane.getPrefWidth() < scroll_AnchorPane.getMaxWidth()) {
            scroll_AnchorPane.setPrefSize(scroll_AnchorPane.getPrefWidth() * zoomFactor, scroll_AnchorPane.getPrefHeight() * zoomFactor);
        }
        if (this.currentPath != null)
            drawPath();
    }

    /**
     * Allows the map to decrease in size, down to scroll_AnchorPane.getMinWidth
     * @param actionEvent Triggered when zoom_button is pressed
     */
    public void unZoom(ActionEvent actionEvent) {

        if (scroll_AnchorPane.getPrefWidth() > scroll_AnchorPane.getMinWidth()) {
            scroll_AnchorPane.setPrefSize(scroll_AnchorPane.getPrefWidth() / zoomFactor, scroll_AnchorPane.getPrefHeight() / zoomFactor);
        }
        if (this.currentPath != null)
            drawPath();
    }
}
