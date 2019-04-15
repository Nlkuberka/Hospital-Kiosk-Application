package admintools;

import application.CurrentUser;
import application.UIController;
import database.DBController;
import database.DBControllerNE;
import entities.Edge;
import entities.Node;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TabPane;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Path;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import pathfinding.UIControllerPFM;

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

    public HBox hboxForMap;
    public StackPane parentPane;
    public ImageView backgroundImage;
    public Path path;
    public MenuItem backButton;
    public ScrollPane scrollPane;
    public ImageView map_imageView;
    public AnchorPane scroll_AnchorPane;
    public Button zoom_button;
    public Button unzoom_button;
    public TabPane tabs;
    private Group edgesGroup = new Group();
    private Group nodesGroup = new Group();
    private LinkedList<Node> currentFloorNodes = new LinkedList<>();
    private LinkedList<Edge> currentFloorEdges = new LinkedList<>();
    private List<Edge> allEdges = new LinkedList<>();
    private ImageView currentImageView;

    @FXML
    private ScrollPane lowerLevel2ScrollPane;
    @FXML
    private ScrollPane lowerLevel1ScrollPane;
    @FXML
    private ScrollPane groundFloorScrollPane;
    @FXML
    private ScrollPane firstFloorScrollPane;
    @FXML
    private ScrollPane secondFloorScrollPane;
    @FXML
    private ScrollPane thirdFloorScrollPane;
    private List<ScrollPane> scrollPanes;

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

    // The multiplication factor at which the map changes size
    private double zoomFactor = 1.2;
    private double mouseX;
    private double mouseY;

    @FXML
    public void initialize() {
        setScene();
        initialBindings();


        // Only show scroll bars if Image inside is bigger than ScrollPane
        for (ScrollPane sp : scrollPanes) {
            sp.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
            sp.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        }

        tabs.getSelectionModel().selectedItemProperty().addListener(param -> {
            set();
        });
    }


    @Override
    public void onShow() {
        set();
    }

    private void setCurrentScene() {
        Connection conn = DBController.dbConnect();
        switch (tabs.getSelectionModel().getSelectedItem().getId()) {
            case "L2":
                currentFloorNodes = DBControllerNE.generateListOfNodes(conn, DBControllerNE.ALL_NODES_FLOOR_L2);
                setCurrentAnchorPane(lowerLevel2AnchorPane);
                currentImageView = lowerLevel2ImageView;
                break;
            case "L1":
                currentFloorNodes = DBControllerNE.generateListOfNodes(conn, DBControllerNE.ALL_NODES_FLOOR_L1);
                setCurrentAnchorPane(lowerLevel1AnchorPane);
                currentImageView = lowerLevel1ImageView;
                break;
            case "G":
                currentFloorNodes = DBControllerNE.generateListOfNodes(conn, DBControllerNE.ALL_NODES_FLOOR_G);
                setCurrentAnchorPane(groundFloorAnchorPane);
                currentImageView = groundFloorImageView;
                break;
            case "1":
                currentFloorNodes = DBControllerNE.generateListOfNodes(conn, DBControllerNE.ALL_NODES_FLOOR_1);
                setCurrentAnchorPane(firstFloorAnchorPane);
                currentImageView = firstFloorImageView;
                break;
            case "2":
                currentFloorNodes = DBControllerNE.generateListOfNodes(conn, DBControllerNE.ALL_NODES_FLOOR_2);
                setCurrentAnchorPane(secondFloorAnchorPane);
                currentImageView = secondFloorImageView;
                break;
            case "3":
                currentFloorNodes = DBControllerNE.generateListOfNodes(conn, DBControllerNE.ALL_NODES_FLOOR_3);
                setCurrentAnchorPane(thirdFloorAnchorPane);
                currentImageView = thirdFloorImageView;
                break;
        }
        //TODO find a better spot for this if statement
        if (allEdges.isEmpty()) {
            allEdges = DBControllerNE.generateListofEdges(conn);
        }

        DBControllerNE.closeConnection(conn);
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
        float scaleFx = getScale().get("scaleFx");
        float scaleFy = getScale().get("scaleFy");

        float x;
        float y;

        // get all XY pairs and turn them into lines
        for (Node tempNode : currentFloorNodes) {
            x = (float) tempNode.getXcoord() * scaleFx;
            y = (float) tempNode.getYcoord() * scaleFy;

            Circle circle = new Circle(x, y, 7);
            circle.setId(tempNode.getNodeID());

            circle.setOnMouseClicked(event -> {
                try {
                    enableChoicePopup(tempNode);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });

            circle.setOnMousePressed(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    mouseX = circle.getLayoutX() - mouseEvent.getSceneX();
                    mouseY = circle.getLayoutY() - mouseEvent.getSceneY();
                    circle.setCursor(Cursor.MOVE);
                }
            });

            circle.setOnMouseDragged(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    circle.setLayoutX(mouseEvent.getSceneX() + mouseX);
                    circle.setLayoutY(mouseEvent.getSceneY() + mouseY);

                }
            });

            circle.setOnMouseReleased(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    tempNode.setXcoord(tempNode.getXcoord() + (int) Math.round(circle.getLayoutX() / scaleFx));
                    tempNode.setYcoord(tempNode.getYcoord() + (int) Math.round(circle.getLayoutY() / scaleFy));
                    Connection conn = DBController.dbConnect();
                    DBControllerNE.updateNode(tempNode, conn);
                    DBController.closeConnection(conn);
                    draw();
                }
            });

            nodesGroup.getChildren().add(circle);
        }
        drawEdges();
    }

    private void drawEdges() {
        edgesGroup.getChildren().clear();
        float scaleFx = getScale().get("scaleFx");
        float scaleFy = getScale().get("scaleFy");

        for (Edge e : currentFloorEdges) {
            Node tempNode1 = getNodeFromID(e.getNode1ID());
            Node tempNode2 = getNodeFromID(e.getNode2ID());
            float Node1x = (float) tempNode1.getXcoord() * scaleFx;
            float Node1y = (float) tempNode1.getYcoord() * scaleFy;
            float Node2x = (float) tempNode2.getXcoord() * scaleFx;
            float Node2y = (float) tempNode2.getYcoord() * scaleFy;
            Line tempLine = new Line(Node1x, Node1y, Node2x, Node2y);
            tempLine.setStrokeWidth(3);
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

        scrollPanes = new LinkedList<>();
        scrollPanes.add(lowerLevel2ScrollPane);
        scrollPanes.add(lowerLevel1ScrollPane);
        scrollPanes.add(groundFloorScrollPane);
        scrollPanes.add(firstFloorScrollPane);
        scrollPanes.add(secondFloorScrollPane);
        scrollPanes.add(thirdFloorScrollPane);

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
        this.goToScene(UIController.LOGIN_MAIN);
    }

    /**
     * Allows the map to increase in size, up to scroll_AnchorPane.getMaxWidth
     *
     * @param actionEvent Triggered when zoom_button is pressed
     */
    public void zoom(ActionEvent actionEvent) {
        if (groundFloorAnchorPane.getPrefWidth() < groundFloorAnchorPane.getMaxWidth()) {
            for (AnchorPane ap : anchorPanes) {
                ap.setPrefSize(ap.getPrefWidth() * zoomFactor, ap.getPrefHeight() * zoomFactor);
            }
        }
        draw();
    }

    /**
     * Allows the map to decrease in size, down to scroll_AnchorPane.getMinWidth
     *
     * @param actionEvent Triggered when zoom_button is pressed
     */
    public void unZoom(ActionEvent actionEvent) {
        if (groundFloorAnchorPane.getPrefWidth() > groundFloorAnchorPane.getMinWidth()) {
            for (AnchorPane ap : anchorPanes) {
                ap.setPrefSize(ap.getPrefWidth() / zoomFactor, ap.getPrefHeight() / zoomFactor);
            }
        }
        draw();
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

    private void editNode(Node node) throws IOException {
        enableAddAndEditPopup(node, "EDIT");
    }

    private void deleteNode(Node node) {
        Connection conn = DBControllerNE.dbConnect();
        DBControllerNE.deleteNode(node.getNodeID(), conn);
        DBControllerNE.closeConnection(conn);
        set();
    }

    private void setKiosk(Node node) {
        if (node.getNodeType().equals("HALL") || node.getNodeType().equals("REST") || node.getNodeType().equals("ELEV")){
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
        stage.setWidth(600);
        stage.setResizable(false);
        stage.centerOnScreen();
        stage.showAndWait();
    }

    private void enableChoicePopup(Node node) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/admintools/ATMV_selectedNodeOptions_popup.fxml"));
        Parent root = loader.load();
        UIControllerPUMVNO uiControllerPUMVNO = loader.getController();
        setStage(root);

        // TODO switch to listeners if there is time
        switch (uiControllerPUMVNO.getStatus()) {
            case "EDIT-NODE":
                editNode(node);
                break;
            case "SET-KIOSK":
                setKiosk(node);
                break;
            case "ADD-EDGE":
                break;
            case "DELETE-NODE":
                deleteNode(node);
                break;
            case "DELETE-EDGE":
                break;
            default:
                break;
        }
    }

    private void showAddedNode(Node node) {
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

