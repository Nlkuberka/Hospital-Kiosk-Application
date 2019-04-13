package admintools;

import database.DBController;
import application.UIController;
import database.DBControllerNE;
import entities.Edge;
import entities.Node;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Path;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

/**
 * Controller for the path_find_main.fxml file
 *
 * @author panagiotisargyrakis, dimitriberardi, ryano647
 */

public class UIControllerATMV extends UIController {

    public HBox hboxForMap;
    public GridPane interfaceGrid;
    public StackPane parentPane;
    public ImageView backgroundImage;
    public Path path;
    public MenuItem backButton;
    public ScrollPane scrollPane;
    public ImageView map_imageView;
    public AnchorPane scroll_AnchorPane;
    public Button zoom_button;
    public Button unzoom_button;
    private LinkedList<Node> allNodes;
    private List<Edge> allEdges;
    private Group edgesGroup = new Group();
    private Group nodesGroup = new Group();
    private LinkedList<Node> usefulNodes;
    private LinkedList<Edge> usefulEdges;

    @FXML private ScrollPane lowerLevel2ScrollPane;
    @FXML private ScrollPane lowerLevel1ScrollPane;
    @FXML private ScrollPane groundFloorScrollPane;
    @FXML private ScrollPane firstFloorScrollPane;
    @FXML private ScrollPane secondFloorScrollPane;
    @FXML private ScrollPane thirdFloorScrollPane;
    private List<ScrollPane> scrollPanes;
    
    @FXML private AnchorPane lowerLevel2AnchorPane;
    @FXML private AnchorPane lowerLevel1AnchorPane;
    @FXML private AnchorPane groundFloorAnchorPane;
    @FXML private AnchorPane firstFloorAnchorPane;
    @FXML private AnchorPane secondFloorAnchorPane;
    @FXML private AnchorPane thirdFloorAnchorPane;
    private List<AnchorPane> anchorPanes;

    @FXML private ImageView lowerLevel2ImageView;
    @FXML private ImageView lowerLevel1ImageView;
    @FXML private ImageView groundFloorImageView;
    @FXML private ImageView firstFloorImageView;
    @FXML private ImageView secondFloorImageView;
    @FXML private ImageView thirdFloorImageView;
    private List<ImageView> imageViews;
    
    // The multiplication factor at which the map changes size
    private double zoomFactor = 1.2;

    @FXML
    public void initialize() {
        setScene();
        initialBindings();


        // Only show scroll bars if Image inside is bigger than ScrollPane
        for(ScrollPane sp : scrollPanes) {
            sp.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
            sp.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        }
    }

    @Override
    public void onShow() {
        /*usefulNodes = new LinkedList<>();
        usefulEdges = new LinkedList<>();
        Connection conn = DBController.dbConnect();
        allNodes = DBControllerNE.generateListOfNodes(conn,DBControllerNE.ALL_NODES);
        allEdges = DBControllerNE.generateListofEdges(conn);
        DBControllerNE.closeConnection(conn);

        try {
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        setUsefulNodes();
        setUsefulEdges();
        drawNodes();
        drawEdges();*/
    }


    private void initialBindings() {
        // bind background image size to window size
        // ensures auto resize works
        backgroundImage.fitHeightProperty().bind(parentPane.heightProperty());
        backgroundImage.fitWidthProperty().bind(parentPane.widthProperty());

        // bind Map to AnchorPane inside of ScrollPane
        //map_imageView.fitWidthProperty().bind(scroll_AnchorPane.prefWidthProperty());
        //map_imageView.fitHeightProperty().bind(scroll_AnchorPane.prefHeightProperty());
        for(int i = 0; i < imageViews.size(); i++) {
            imageViews.get(i).fitWidthProperty().bind(anchorPanes.get(i).prefWidthProperty());
            imageViews.get(i).fitHeightProperty().bind(anchorPanes.get(i).prefHeightProperty());
        }

        //interfaceGrid.prefHeightProperty().bind(hboxForMap.heightProperty());

        //scrollPane.prefViewportWidthProperty().bind(hboxForMap.prefWidthProperty());
    }


    private void setScene() {
        // set value to "true" to use zoom functionality
        //setZoomOn(true);
        //scroll_AnchorPane.getChildren().add(nodesGroup);
        //scroll_AnchorPane.getChildren().add(edgesGroup);
        
        scrollPanes = new LinkedList<ScrollPane>();
        scrollPanes.add(lowerLevel2ScrollPane);
        scrollPanes.add(lowerLevel1ScrollPane);
        scrollPanes.add(groundFloorScrollPane);
        scrollPanes.add(firstFloorScrollPane);
        scrollPanes.add(secondFloorScrollPane);
        scrollPanes.add(thirdFloorScrollPane);

        anchorPanes = new LinkedList<AnchorPane>();
        anchorPanes.add(lowerLevel2AnchorPane);
        anchorPanes.add(lowerLevel1AnchorPane);
        anchorPanes.add(groundFloorAnchorPane);
        anchorPanes.add(firstFloorAnchorPane);
        anchorPanes.add(secondFloorAnchorPane);
        anchorPanes.add(thirdFloorAnchorPane);

        imageViews = new LinkedList<ImageView>();
        imageViews.add(lowerLevel2ImageView);
        imageViews.add(lowerLevel1ImageView);
        imageViews.add(groundFloorImageView);
        imageViews.add(firstFloorImageView);
        imageViews.add(secondFloorImageView);
        imageViews.add(thirdFloorImageView);
        
    }

    private void drawEdges() {
        float scaleFx = getScale().get("scaleFx");
        float scaleFy = getScale().get("scaleFy");

        for (Edge e : usefulEdges) {
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

    private Node getNodeFromID(String nodeID) {
        for (Node n : usefulNodes) {
            if (n.getNodeID().equals(nodeID)) {
                return n;
            }
        }
        System.out.println("NodeID: " + nodeID);
        return null;
    }

    private HashMap<String, Float> getScale() {
        HashMap<String, Float> scales = new HashMap<>();
        float scaleFx = (float) map_imageView.getFitWidth() / 5000.0f;
        float scaleFy = (float) map_imageView.getFitHeight() / 3400.0f;
        scales.put("scaleFx", scaleFx);
        scales.put("scaleFy", scaleFy);
        return scales;
    }

    public void goBack(ActionEvent actionEvent) {
        this.goToScene(UIController.LOGIN_MAIN);
    }

    /**
     * @param bool Set in initialize() to turn on/off zoom functionality
     */
    private void setZoomOn(boolean bool) {
        //zoom_button.setVisible(bool);
        //unzoom_button.setVisible(bool);
    }

    /**
     * Allows the map to increase in size, up to scroll_AnchorPane.getMaxWidth
     *
     * @param actionEvent Triggered when zoom_button is pressed
     */
    public void zoom(ActionEvent actionEvent) {
        if (groundFloorAnchorPane.getPrefWidth() < groundFloorAnchorPane.getMaxWidth()) {
            for(AnchorPane ap : anchorPanes) {
                ap.setPrefSize(ap.getPrefWidth() * zoomFactor, ap.getPrefHeight() * zoomFactor);
            }
        }

        //resizeEdgesNodes();
    }

    /**
     * Allows the map to decrease in size, down to scroll_AnchorPane.getMinWidth
     *
     * @param actionEvent Triggered when zoom_button is pressed
     */
    public void unZoom(ActionEvent actionEvent) {
        if (groundFloorAnchorPane.getPrefWidth() > groundFloorAnchorPane.getMinWidth()) {
            for(AnchorPane ap : anchorPanes) {
                ap.setPrefSize(ap.getPrefWidth() / zoomFactor, ap.getPrefHeight() / zoomFactor);
            }
        }

        //resizeEdgesNodes();
    }

    private void resizeEdgesNodes() {
        nodesGroup.getChildren().clear();
        edgesGroup.getChildren().clear();
        drawEdges();
        drawNodes();
    }

    private void drawNodes() {
        float scaleFx = getScale().get("scaleFx");
        float scaleFy = getScale().get("scaleFy");

        float x;
        float y;

        // get all XY pairs and turn them into lines
        for (Node tempNode : usefulNodes) {
            x = (float) tempNode.getXcoord() * scaleFx;
            y = (float) tempNode.getYcoord() * scaleFy;

            Circle circle = new Circle(x, y, 3);
            circle.setId(tempNode.getNodeID());

            nodesGroup.getChildren().add(circle);
        }
    }

    private void setUsefulNodes() {
        for (Node node : allNodes) {
            if (node.getFloor().equals("2")) {
                usefulNodes.add(node);
            }
        }
    }

    private void setUsefulEdges() {
        for (Edge edge : allEdges) {
            String tempNode1 = edge.getNode1ID();
            String tempNode2 = edge.getNode2ID();

            boolean Node1Bool = false;
            boolean Node2Bool = false;

            for (Node node : usefulNodes) {
                if (node.getNodeID().equals(tempNode1)) Node1Bool = true;
                if (node.getNodeID().equals(tempNode2)) Node2Bool = true;
            }

            if (Node1Bool && Node2Bool) {
                usefulEdges.add(edge);
            }
        }
    }


    public void addNodeOnClick(MouseEvent mouseEvent) throws IOException {
        enablePopup();
        double x = mouseEvent.getX();
        double y = mouseEvent.getY();

        Node testNode = new Node();
        testNode.setXcoord((int) (mouseEvent.getX() / getScale().get("scaleFx")));
        testNode.setYcoord((int) (mouseEvent.getY() / getScale().get("scaleFy")));
        testNode.setFloor("2");
        testNode.setNodeID("TEST");
        usefulNodes.add(testNode);
        Circle newNode = new Circle((float) x, (float) y, 3);
        newNode.setRadius(5);
        newNode.setFill(Color.GREEN);
        newNode.setStroke(Color.BLACK);
        newNode.setStrokeWidth(2);
        nodesGroup.getChildren().add(newNode);
    }

    private void enablePopup() throws IOException {
        Stage stage = new Stage();
        Parent root = FXMLLoader.load(getClass().getResource("/ATMV_addNode_popup.fxml"));
        stage.setScene(new Scene(root));
        stage.initModality(Modality.APPLICATION_MODAL);
       // stage.initStyle(StageStyle.UNIFIED);
        stage.initOwner(parentPane.getScene().getWindow());
        stage.setHeight(400);
        stage.setWidth(600);
        stage.setResizable(false);
        stage.centerOnScreen();
        stage.showAndWait();
    }
}

