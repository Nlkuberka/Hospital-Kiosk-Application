package pathfinding;

import entities.Node;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.SubScene;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import utilities.Tooltip;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;


public class AnchorPaneHandler {
    public static double nodeSizeIdle = 25;
    public static double getNodeSizeHighlited = 35;
    List<AnchorPane> anchorPanes;
    private List<Group> groupsForNodes;
    private HashMap<String, Circle> circleFromName;
    private CurrentObjects currentObjects;
    private AnchorPane topAnchorPane;
    UIControllerPFM controller;

    /**
     * Setup anchor panes such that they are in a list and have groups for the node circles
     */
    AnchorPaneHandler(AnchorPane p1, AnchorPane p2, AnchorPane p3, AnchorPane p4, AnchorPane p5, AnchorPane p6, AnchorPane p7,
                      AnchorPane topAnchorPane, UIControllerPFM controller) {
        this.anchorPanes = new LinkedList<AnchorPane>();
        anchorPanes.add(p1);
        anchorPanes.add(p2);
        anchorPanes.add(p3);
        anchorPanes.add(p4);
        anchorPanes.add(p5);
        anchorPanes.add(p6);
        anchorPanes.add(p7);

        this.controller = controller;

        this.topAnchorPane = topAnchorPane;

        this.groupsForNodes = new LinkedList<>(); // add groups for circles
        for (AnchorPane anchorPane : this.anchorPanes) {
            Group group = new Group();
            this.groupsForNodes.add(group);
            anchorPane.getChildren().add(group);
        }
    }

    void setCurrentObjects(CurrentObjects currentObjects) {
        this.currentObjects = currentObjects;
    }

    AnchorPane getAnchorPaneAtFloor(int floor) {
        return this.anchorPanes.get(floor);
    }

    Circle getCircleFromName(String string) {
        return this.circleFromName.get(string);
    }

    private void newContextMenuAtLocation(Circle circle, String name, String longName) {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/pathfinding/path_finding_context_menu.fxml"));
        Parent node;
        try {
            node = fxmlLoader.load();
            node.setPickOnBounds(true);
            node.setMouseTransparent(false);

            SubScene contextMenu = new SubScene(node, 600, 400);
            contextMenu.setLayoutX(circle.getCenterX() - 300);
            contextMenu.setLayoutY(circle.getCenterY() - 450);
//            contextMenu.setLayoutX(100);
//            contextMenu.setLayoutY(100);
            contextMenu.setVisible(true);

            currentObjects.clearContextMenu();

            currentObjects.setContextMenu(contextMenu);

            ((UIControllerPFCM) fxmlLoader.getController()).setController(controller);
            ((UIControllerPFCM) fxmlLoader.getController()).setText(name);
            ((UIControllerPFCM) fxmlLoader.getController()).setNodeLongName(longName);

            currentObjects.getCurrentAnchorPane().getChildren().add(contextMenu);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void initCircles(LinkedList<LinkedList<Node>> roomsAtEachFloor, ComboBox initialLocationSelect,
                     ComboBox destinationSelect) {
        // ~~~~~~ init circles

        this.circleFromName = new HashMap<>(); // map to get corresponding circles from longnames

        // setup circles for nodes
        for (int i = 0; i < this.groupsForNodes.size(); i++) {
            Group group = this.groupsForNodes.get(i);

            for (Node node : roomsAtEachFloor.get(i)) {
                float x = (float) node.getXcoord();
                float y = (float) node.getYcoord();

                Circle circle = new Circle(x, y, nodeSizeIdle);
                circle.setFill(Color.web("015080"));
                circle.setId(node.getNodeID());

                new Tooltip(circle, node.getShortName());

                this.circleFromName.put(node.getLongName(), circle); // setup hashmap

                circle.setOnMouseClicked(e -> newContextMenuAtLocation(circle, node.getShortName(), node.getLongName()));
                group.getChildren().add(circle);
            }
            group.setVisible(true);
        }
    }
}
