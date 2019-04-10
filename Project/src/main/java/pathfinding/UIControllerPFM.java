package pathfinding;

import application.DBController;
import application.UIController;
import com.jfoenix.controls.JFXSlider;
import entities.Edge;
import entities.Graph;
import entities.Node;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.shape.Rectangle;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Controller for the path_find_main.fxml file
 * @author panagiotisargyrakis, dimitriberardi, ryano647
 */

public class UIControllerPFM extends UIController {

    public enum Floors {
        FIRST("First Floor", "1"), GROUND("Ground Floor", "G"), LL1("Lower Level 1", "L1"),
        LL2("Lower Level 2", "L2"), SECOND("Second Floor", "2"), THIRD("Third Floor", "3");

        private final String name;
        private final String ID;

        Floors(String name, String ID) {
            this.name = name;
            this.ID = ID;
        }

        public String getName() {
            return this.name;
        }

        public String getID() {
            return this.name;
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

    private LinkedList<Path> pathList = new LinkedList<>();
    private LinkedList<ImageView> mapList = new LinkedList<>();
    private List<Node> currentPath;
    private Path path;
    private ImageView map;
    private AnchorPane pane;
    private LinkedList<AnchorPane> paneList = new LinkedList<>();

    @FXML
    public void initialize() {
        mapList.add(map_002);
        mapList.add(map_001);
        mapList.add(map_00);
        mapList.add(map_01);
        mapList.add(map_02);
        mapList.add(map_03);

        pathList.add(p_002);
        pathList.add(p_001);
        pathList.add(p_00);
        pathList.add(p_01);
        pathList.add(p_02);
        pathList.add(p_03);

        paneList.add(pane_002);
        paneList.add(pane_001);
        paneList.add(pane_00);
        paneList.add(pane_01);
        paneList.add(pane_02);
        paneList.add(pane_03);

        floorSlider.setMax((double) mapList.size() - 1.0);
        floorSlider.setValue(2.0);
        setOpacity(2);
        floorLabel.setText(Floors.values()[(int) floorSlider.getValue()].getName());

        // bind background image size to window size
        // ensures auto resize works
        backgroundImage.fitHeightProperty().bind(parentPane.heightProperty());
        backgroundImage.fitWidthProperty().bind(parentPane.widthProperty());

        // bind Map to AnchorPane inside of ScrollPane
        for (int i = 0; i < mapList.size(); i++) {
            ImageView map = mapList.get(i);
            AnchorPane pane = paneList.get(i);
            map.fitWidthProperty().bind(pane.prefWidthProperty());
            map.fitHeightProperty().bind(pane.prefHeightProperty());
        }

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

        floorSlider.valueProperty().addListener(new ChangeListener() {

            @Override
            public void changed(ObservableValue arg0, Object arg1, Object arg2) {
                floorLabel.setText(Floors.values()[(int) floorSlider.getValue()].getName());
                setOpacity(floorSlider.getValue());
            }
        });

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
            if (node.getFloor().equals("2")) {
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
    }



    @FXML
    public void initLocChanged(ActionEvent actionEvent) {
        System.out.println("Initial location selected: " + initialLocationSelect.getValue());
        Connection connection = DBController.dbConnect();
        initialID = DBController.IDfromLongName(initialLocationSelect.getValue(), connection);

        getPath();
    }

    @FXML
    public void destLocChanged(ActionEvent actionEvent) {
        System.out.println("Initial location: " + initialLocationSelect.getValue());
        System.out.println("Destination selected: " + destinationSelect.getValue());

        Connection connection = DBController.dbConnect();
        destID = DBController.IDfromLongName(destinationSelect.getValue(), connection);

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

        if(dest == null || dest.length() == 0 || init == null || init.length() == 0)
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

    private void drawPath() {
        float scaleFx = (float) pane.getPrefWidth() / 5000.0f;
        float scaleFy = (float) pane.getPrefHeight() / 3400.0f;

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

        if (pane.getPrefWidth() < pane.getMaxWidth()) {
            this.setAllPaneSize(pane.getPrefWidth() * zoomFactor, pane.getPrefHeight() * zoomFactor);
        }
        if (this.currentPath != null)
            drawPath();
    }

    /**
     * Allows the map to decrease in size, down to scroll_AnchorPane.getMinWidth
     * @param actionEvent Triggered when zoom_button is pressed
     */
    public void unZoom(ActionEvent actionEvent) {

        if (pane.getPrefWidth() > pane.getMinWidth()) {
            this.setAllPaneSize(pane.getPrefWidth() / zoomFactor, pane.getPrefHeight() / zoomFactor);
        }
        if (this.currentPath != null)
            drawPath();
    }

    private void setOpacity(double value) {
        for (int i = 0; i < mapList.size(); i++) {
            double opacity = getOpacity((double) i, value);
            mapList.get(i).setOpacity(opacity);
            pathList.get(i).setOpacity(opacity);
        }
        this.path = pathList.get((int) value);
        this.map = mapList.get((int) value);
        this.pane = paneList.get((int) value);
    }

    private void setAllPaneSize(double width, double height) {
        for (AnchorPane pane : this.paneList) {
            pane.setPrefSize(width, height);
        }
    }

    private double getOpacity(double a, double b) {
//        double result = a - b;
//        result = result > 0.0 ? result : -result; // get abs value
//        result = result > 1.0 ? 0.0 : 1.0 - result; // clip to bounds
//        result = result < 0.5 ? 0.0 : 1.0;

        double result = (int) a == (int) b ? 1.0 : 0.0;
        return result;
    }
}
