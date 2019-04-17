package admintools;

import application.CurrentUser;
import application.UIController;
import database.DBController;
import database.DBControllerNE;
import entities.Edge;
import entities.Node;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TabPane;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Path;
import javafx.scene.text.TextAlignment;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import net.kurobako.gesturefx.GesturePane;
import pathfinding.AnchorPaneHandler;
import pathfinding.Floors;
import pathfinding.GesturePaneHandler;

import java.io.IOException;
import java.sql.Connection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/**
 * Controller for the path_find_main.fxml file
 *
 * @author panagiotisargyrakis, dimitriberardi, ryano647
 */

public class UIControllerATMV extends UIController {

    public StackPane parentPane;
    public ImageView backgroundImage;
    public Path path;
    public MenuItem backButton;
    public TabPane tabs;
    private ImageView questionMarkF2;
    private ImageView questionMarkF3;
    private ImageView questionMarkF1;
    private ImageView questionMarkGF;
    private ImageView questionMarkL1;
    private ImageView questionMarkL2;
    private LinkedList<ImageView> helperIcons;
    String previousNodeID;
    boolean isAddingEdge;
    private Group edgesGroup = new Group();
    private Group nodesGroup = new Group();
    private LinkedList<Node> currentFloorNodes = new LinkedList<>();
    private LinkedList<Edge> currentFloorEdges = new LinkedList<>();
    private List<Edge> allEdges = new LinkedList<>();
    private ImageView currentImageView;

    @FXML
    private GesturePane lowerLevel2GesturePane;
    @FXML
    private GesturePane lowerLevel1GesturePane;
    @FXML
    private GesturePane groundFloorGesturePane;
    @FXML
    private GesturePane firstFloorGesturePane;
    @FXML
    private GesturePane secondFloorGesturePane;
    @FXML
    private GesturePane thirdFloorGesturePane;

    @FXML
    private AnchorPane lowerLevel2AnchorPane;
    @FXML
    private AnchorPane lowerLevel1AnchorPane;
    @FXML
    private AnchorPane groundFloorAnchorPane;
    @FXML
    private AnchorPane firstFloorAnchorPane;
    @FXML
    private AnchorPane secondFloorAnchorPane;
    @FXML
    private AnchorPane thirdFloorAnchorPane;
    private List<AnchorPane> anchorPanes;

    @FXML
    private ImageView lowerLevel2ImageView;
    @FXML
    private ImageView lowerLevel1ImageView;
    @FXML
    private ImageView groundFloorImageView;
    @FXML
    private ImageView firstFloorImageView;
    @FXML
    private ImageView secondFloorImageView;
    @FXML
    private ImageView thirdFloorImageView;
    private List<ImageView> imageViews;

    private double mouseX;
    private double mouseY;

    private int currentFloor;

    private GesturePaneHandler gesturePaneHandler;

    @FXML
    public void initialize() {
        setScene();
        initialBindings();

        gesturePaneHandler = new GesturePaneHandler(lowerLevel2GesturePane, lowerLevel1GesturePane, groundFloorGesturePane,
                firstFloorGesturePane, secondFloorGesturePane, thirdFloorGesturePane);

        tabs.getSelectionModel().selectedItemProperty().addListener(
                (ov, t, t1) -> {
                    GesturePane oldPane = gesturePaneHandler.getGesturePanes().get(currentFloor);
                    currentFloor = Floors.getByName(t1.getText()).getIndex();
                    GesturePane pane = gesturePaneHandler.getGesturePanes().get(currentFloor);
                    pane.centreOn(oldPane.targetPointAtViewportCentre());
                    gesturePaneHandler.changeTabs(pane, oldPane);
                }
        );


        tabs.getSelectionModel().selectedItemProperty().addListener(param -> set());

        for (ImageView icon : helperIcons) {
            String helper = "Click & Drag: Move Node\n" +
                    "Double Click: Select Node\n\n" +
                    "If Adding Or Deleting Edge:\n" +
                    "Click Again (Once) to Execute\n";
            new utilities.Tooltip(icon, helper, TextAlignment.LEFT);
        }
    }

    @Override
    public void onShow() {
        set();
    }

    private void setCurrentScene() {
        Connection conn = DBController.dbConnect();
        switch (tabs.getSelectionModel().getSelectedItem().getId()) {
            case "L2":
                assert conn != null;
                currentFloorNodes = DBControllerNE.generateListOfNodes(conn, DBControllerNE.ALL_NODES_FLOOR_L2);
                setCurrentAnchorPane(lowerLevel2AnchorPane);
                currentImageView = lowerLevel2ImageView;
                break;
            case "L1":
                assert conn != null;
                currentFloorNodes = DBControllerNE.generateListOfNodes(conn, DBControllerNE.ALL_NODES_FLOOR_L1);
                setCurrentAnchorPane(lowerLevel1AnchorPane);
                currentImageView = lowerLevel1ImageView;
                break;
            case "G":
                assert conn != null;
                currentFloorNodes = DBControllerNE.generateListOfNodes(conn, DBControllerNE.ALL_NODES_FLOOR_G);
                setCurrentAnchorPane(groundFloorAnchorPane);
                currentImageView = groundFloorImageView;
                break;
            case "1":
                assert conn != null;
                currentFloorNodes = DBControllerNE.generateListOfNodes(conn, DBControllerNE.ALL_NODES_FLOOR_1);
                setCurrentAnchorPane(firstFloorAnchorPane);
                currentImageView = firstFloorImageView;
                break;
            case "2":
                assert conn != null;
                currentFloorNodes = DBControllerNE.generateListOfNodes(conn, DBControllerNE.ALL_NODES_FLOOR_2);
                setCurrentAnchorPane(secondFloorAnchorPane);
                currentImageView = secondFloorImageView;
                break;
            case "3":
                assert conn != null;
                currentFloorNodes = DBControllerNE.generateListOfNodes(conn, DBControllerNE.ALL_NODES_FLOOR_3);
                setCurrentAnchorPane(thirdFloorAnchorPane);
                currentImageView = thirdFloorImageView;
                break;
        }
        //TODO find a better spot for this if statement
        if (allEdges.isEmpty()) {
            assert conn != null;
            allEdges = DBControllerNE.generateListofEdges(conn);
        }

        assert conn != null;
        DBController.closeConnection(conn);
        setCurrentEdges();
    }

    private void setCurrentAnchorPane(AnchorPane anchorPane) {
        if ((!anchorPane.getChildren().contains(edgesGroup)) && (!anchorPane.getChildren().contains(nodesGroup))) {
            anchorPane.getChildren().add(edgesGroup);
            anchorPane.getChildren().add(nodesGroup);
        }
    }

    private void setCurrentEdges() {
        currentFloorEdges.clear();
        for (Edge edge : allEdges) {
            String tempNode1 = edge.getNode1ID();
            String tempNode2 = edge.getNode2ID();

            boolean Node1Bool = false;
            boolean Node2Bool = false;

            for (Node node : currentFloorNodes) {
                if (node.getNodeID().equals(tempNode1)) Node1Bool = true;
                if (node.getNodeID().equals(tempNode2)) Node2Bool = true;
            }

            if (Node1Bool && Node2Bool) {
                currentFloorEdges.add(edge);
            }
        }
    }

    private void draw() {
        nodesGroup.getChildren().clear();

        float x;
        float y;

        // get all XY pairs and turn them into lines
        for (Node tempNode : currentFloorNodes) {
            x = (float) tempNode.getXcoord();
            y = (float) tempNode.getYcoord();

            Circle circle = new Circle(x, y, AnchorPaneHandler.nodeSizeIdle);
            circle.setId(tempNode.getNodeID());
            new utilities.Tooltip(circle, tempNode.getShortName());


            circle.setOnMousePressed(mouseEvent -> {
                if (previousNodeID != null) {
                    if(previousNodeID.equals(tempNode.getNodeID()))
                    {
                        popupMessage("Not Valid: Same Node", true);
                    }
                    else
                    {
                        if (isAddingEdge) {
                            addEdge(previousNodeID, tempNode.getNodeID());
                            Connection conn = DBController.dbConnect();
                            assert conn != null;
                            currentFloorEdges.add(DBControllerNE.fetchEdge(previousNodeID + "_" + tempNode.getNodeID(), conn));
                            DBController.closeConnection(conn);
                        } else {
                            deleteEdge(previousNodeID, tempNode.getNodeID());
                            currentFloorEdges.remove(getEdgeFrom(currentFloorEdges, previousNodeID, tempNode.getNodeID()));
                        }
                    }
                    previousNodeID = null;
                    draw();
                } else {
                    mouseX = circle.getLayoutX() - mouseEvent.getSceneX();
                    mouseY = circle.getLayoutY() - mouseEvent.getSceneY();
                    if (mouseEvent.getClickCount() == 2) {
                        try {
                            enableChoicePopup(tempNode);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });

            circle.setOnMouseDragged(mouseEvent -> {
                circle.setLayoutX(mouseEvent.getSceneX() + mouseX);
                circle.setLayoutY(mouseEvent.getSceneY() + mouseY);
                circle.setCursor(Cursor.MOVE);
            });

            circle.setOnMouseReleased(
                    mouseEvent -> {
                tempNode.setXcoord(tempNode.getXcoord() + (int) Math.round(circle.getLayoutX()));
                tempNode.setYcoord(tempNode.getYcoord() + (int) Math.round(circle.getLayoutY()));
                Connection conn = DBController.dbConnect();
                assert conn != null;
                DBControllerNE.updateNode(tempNode, conn);
                DBController.closeConnection(conn);
                draw();
            });

            nodesGroup.getChildren().add(circle);
        }
        drawEdges();
    }

    private Edge getEdgeFrom(LinkedList<Edge> edges, String nodeID1, String nodeID2) {
        String edgeID1 = nodeID1 + "_" + nodeID2;
        String edgeID2 = nodeID2 + "_" + nodeID1;
        for (Edge e : edges) {
            if (e.getEdgeID().equals(edgeID1) || e.getEdgeID().equals(edgeID2)) {
                return e;
            }
        }
        return null;
    }

    private void drawEdges() {
        edgesGroup.getChildren().clear();

        for (Edge e : currentFloorEdges) {
            Node tempNode1 = getNodeFromID(e.getNode1ID());
            Node tempNode2 = getNodeFromID(e.getNode2ID());
            assert tempNode1 != null;
            float Node1x = (float) tempNode1.getXcoord();
            float Node1y = (float) tempNode1.getYcoord();
            assert tempNode2 != null;
            float Node2x = (float) tempNode2.getXcoord();
            float Node2y = (float) tempNode2.getYcoord();
            Line tempLine = new Line(Node1x, Node1y, Node2x, Node2y);
            tempLine.setStrokeWidth(10);
            edgesGroup.getChildren().add(tempLine);
        }
    }

    private void initialBindings() {
        // bind background image size to window size
        // ensures auto resize works
        backgroundImage.fitHeightProperty().bind(parentPane.heightProperty());
        backgroundImage.fitWidthProperty().bind(parentPane.widthProperty());

        // bind Map to AnchorPane inside of ScrollPane
        for (int i = 0; i < imageViews.size(); i++) {
            imageViews.get(i).fitWidthProperty().bind(anchorPanes.get(i).prefWidthProperty());
            imageViews.get(i).fitHeightProperty().bind(anchorPanes.get(i).prefHeightProperty());
        }
    }

    //TODO add groups dynamically
    private void setScene() {
        //scroll_AnchorPane.getChildren().add(nodesGroup);
        //scroll_AnchorPane.getChildren().add(edgesGroup);

        anchorPanes = new LinkedList<>();
        anchorPanes.add(lowerLevel2AnchorPane);
        anchorPanes.add(lowerLevel1AnchorPane);
        anchorPanes.add(groundFloorAnchorPane);
        anchorPanes.add(firstFloorAnchorPane);
        anchorPanes.add(secondFloorAnchorPane);
        anchorPanes.add(thirdFloorAnchorPane);

        imageViews = new LinkedList<>();
        imageViews.add(lowerLevel2ImageView);
        imageViews.add(lowerLevel1ImageView);
        imageViews.add(groundFloorImageView);
        imageViews.add(firstFloorImageView);
        imageViews.add(secondFloorImageView);
        imageViews.add(thirdFloorImageView);

        helperIcons = new LinkedList<>();
        helperIcons.add(questionMarkF1);
        helperIcons.add(questionMarkF2);
        helperIcons.add(questionMarkF3);
        helperIcons.add(questionMarkGF);
        helperIcons.add(questionMarkL1);
        helperIcons.add(questionMarkL2);

    }

    private Node getNodeFromID(String nodeID) {
        for (Node n : currentFloorNodes) {
            if (n.getNodeID().equals(nodeID)) {
                return n;
            }
        }
        System.out.println("NodeID: " + nodeID);
        return null;
    }

    private HashMap<String, Float> getScale() {
        HashMap<String, Float> scales = new HashMap<>();
        float scaleFx = (float) currentImageView.getFitWidth() / 5000.0f;
        float scaleFy = (float) currentImageView.getFitHeight() / 3400.0f;
        scales.put("scaleFx", scaleFx);
        scales.put("scaleFy", scaleFy);
        return scales;
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
    public void zoom(ActionEvent actionEvent) {
        gesturePaneHandler.zoom(gesturePaneHandler.getGesturePanes().get(currentFloor));
    }

    /**
     * Allows the map to decrease in size, down to scroll_AnchorPane.getMinWidth
     *
     * @param actionEvent Triggered when zoom_button is pressed
     */
    public void unZoom(ActionEvent actionEvent) {
        gesturePaneHandler.un_zoom(gesturePaneHandler.getGesturePanes().get(currentFloor));
    }

    private void set() {
        setCurrentScene();
        draw();
    }

    @FXML
    public void addNodeOnClick(MouseEvent mouseEvent) throws IOException {
        Node tempNode = new Node();
        tempNode.setXcoord((int) (mouseEvent.getX() / getScale().get("scaleFx")));
        tempNode.setYcoord((int) (mouseEvent.getY() / getScale().get("scaleFy")));
        tempNode.setFloor(tabs.getSelectionModel().getSelectedItem().getId()); //TODO Make Auto Once Add MultiFloor Functionality
        enableAddAndEditPopup(tempNode, "ADD");
        set();
        showAddedNode(tempNode);
    }

    void editNode(Node node) throws IOException {
        enableAddAndEditPopup(node, "EDIT");
    }

    void deleteNode(Node node) {
        Connection conn = DBController.dbConnect();
        assert conn != null;
        DBControllerNE.deleteNode(node.getNodeID(), conn);
        DBController.closeConnection(conn);
        set();
    }

    private void addEdge(String node1ID, String node2ID) {
        Connection conn = DBController.dbConnect();
        Edge newEdge = new Edge(null, node1ID, node2ID);
        assert conn != null;
        DBControllerNE.addEdge(newEdge, conn);
        DBController.closeConnection(conn);
    }

    private void deleteEdge(String nodeID1, String nodeID2) {
        Connection conn = DBController.dbConnect();
        assert conn != null;
        DBControllerNE.deleteEdge(nodeID1, nodeID2, conn);
        DBController.closeConnection(conn);
    }

    void setKiosk(Node node) {
        if (node.getNodeType().equals("HALL") || node.getNodeType().equals("REST") || node.getNodeType().equals("ELEV")) {
            popupMessage("Invalid Kiosk Location", true);
        } else {
            CurrentUser.startingLocation = node.getLongName();
        }
    }

    private void enableAddAndEditPopup(Node node, String action) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/admintools/ATMV_addNode_popup.fxml"));
        Parent root = loader.load();
        UIControllerPUMVAN atmvAddNodePopupController = loader.getController();
        atmvAddNodePopupController.setNode(node, action);

        setStage(root);
    }

    private void setStage(Parent root) {
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initStyle(StageStyle.UNDECORATED);
        stage.initOwner(parentPane.getScene().getWindow());
        stage.setHeight(400);
        stage.setWidth(400);
        stage.setResizable(false);
        stage.centerOnScreen();
        stage.show();
    }

    private void enableChoicePopup(Node node) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/admintools/ATMV_selectedNodeOptions_popup.fxml"));
        Parent root = loader.load();
        UIControllerPUMVNO uiControllerPUMVNO = loader.getController();
        uiControllerPUMVNO.setUiControllerATMV(this, node);

        setStage(root);
    }

    void showAddedNode(Node node) {
        for (javafx.scene.Node nodes : nodesGroup.getChildren()) {
            if (nodes.getId().equals(node.getNodeID())) {
                ((Circle) nodes).setRadius(10);
                ((Circle) nodes).setFill(Color.GREEN);
                ((Circle) nodes).setStroke(Color.BLACK);
                ((Circle) nodes).setStrokeWidth(2);
            }
        }
    }
}

