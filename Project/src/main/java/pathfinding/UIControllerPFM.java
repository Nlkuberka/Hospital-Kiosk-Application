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


    private MapHandler mapHandler;

    @FXML
    public void initialize() {
        this.mapHandler = new MapHandler(p_002, p_001, p_00, p_01, p_02, p_03,
                map_002, map_001, map_00, map_01, map_02, map_03,
                pane_002, pane_001, pane_00, pane_01, pane_02, pane_03,
                Floors.SECOND);

        floorSlider.setMax(5.0); // number of floors - 1
        floorSlider.setValue(2.0);
        floorLabel.setText(Floors.values()[(int) floorSlider.getValue()].getName());

        // bind background image size to window size
        // ensures auto resize works
        backgroundImage.fitHeightProperty().bind(parentPane.heightProperty());
        backgroundImage.fitWidthProperty().bind(parentPane.widthProperty());


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
                mapHandler.changeToFloor(floorSlider.getValue());
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

        for (Node node : allNodes) {
            // update choices for initial location
            initialLocationSelect.getItems().add(node.getLongName());
            // update choices for destination location
            destinationSelect.getItems().addAll(node.getLongName());
        }

        this.graph = new Graph(allNodes);

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
        DBController.closeConnection(connection);

        getPath();
    }

    @FXML
    public void destLocChanged(ActionEvent actionEvent) {
        System.out.println("Initial location: " + initialLocationSelect.getValue());
        System.out.println("Destination selected: " + destinationSelect.getValue());

        Connection connection = DBController.dbConnect();
        destID = DBController.IDfromLongName(destinationSelect.getValue(), connection);
        DBController.closeConnection(connection);

        // call getPath if not null
        getPath();
    }


    @FXML
    private void clearSelection(ActionEvent actionEvent) {
        initialLocationSelect.getSelectionModel().selectFirst();
        destinationSelect.getSelectionModel().clearSelection();
        mapHandler.cancel();
    }

    private void getPath() {

        if(initialID == null || destID == null)
            return;

        Connection connection = DBController.dbConnect();
        List<String> pathIDs;
        pathIDs = graph.shortestPath(initialID, destID);

        Node initialNode = DBController.fetchNode(initialID, connection);
        DBController.closeConnection(connection);

        mapHandler.displayNewPath(graph.separatePathByFloor(pathIDs), initialNode);
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

        mapHandler.zoomIn(zoomFactor);
    }

    /**
     * Allows the map to decrease in size, down to scroll_AnchorPane.getMinWidth
     * @param actionEvent Triggered when zoom_button is pressed
     */
    public void unZoom(ActionEvent actionEvent) {

        mapHandler.zoomOut(zoomFactor);
    }
}
