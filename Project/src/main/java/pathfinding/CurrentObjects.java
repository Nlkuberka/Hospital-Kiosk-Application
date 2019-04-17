package pathfinding;

import entities.Node;
import javafx.animation.PathTransition;
import javafx.scene.SubScene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Path;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import net.kurobako.gesturefx.GesturePane;

public class CurrentObjects {
    private int floorIndex;
    private Circle initCircle;
    private Circle destCircle;
    private PathTransition animation = null;
    private Rectangle ant = null;
    private PathHandler pathHandler;
    private AnchorPaneHandler anchorPaneHandler;
    private GesturePaneHandler gesturePaneHandler;
    private String initialID;
    private String destID;
    private SubScene contextMenu;
    private Text initNodeLabel;
    private Text destNodeLabel;

    public CurrentObjects(int floorIndex, Circle initCircle, Circle destCircle, PathTransition animation, Rectangle currentAnt,
                          PathHandler pathHandler, AnchorPaneHandler anchorPaneHandler, GesturePaneHandler gesturePaneHandler) {
        this.floorIndex = floorIndex;
        this.initCircle = initCircle;
        this.destCircle = destCircle;
        this.animation = animation;
        this.ant = currentAnt;
        this.pathHandler = pathHandler;
        this.anchorPaneHandler = anchorPaneHandler;
        this.gesturePaneHandler = gesturePaneHandler;
    }

    void cancel() {
        this.clearNodeStyle();
        this.clearInitDestIDs();
        this.clearAnimation();
    }

    void clearContextMenu() {
        if (contextMenu != null) {
            getCurrentAnchorPane().getChildren().remove(contextMenu);
        }
    }

    public SubScene getContextMenu() {
        return contextMenu;
    }

    public void setContextMenu(SubScene contextMenu) {
        this.contextMenu = contextMenu;
    }

    void clearInitDestIDs() {
        this.destID = null;
        this.initialID = null;
    }

    void setInitialID(String initialID) {
        this.initialID = initialID;
    }

    void setDestID(String destID) {
        this.destID = destID;
    }

    String getInitialID() {
        return initialID;
    }

    String getDestID() {
        return destID;
    }

    boolean anyNullEndNodes() {
        return destID == null || initialID == null;
    }

    /**
     * Clear style of currently selected circles
     */
    void clearNodeStyle() {
        if (initCircle != null) {
            initCircle.setFill(Color.BLACK);
            initCircle.setRadius(AnchorPaneHandler.nodeSizeIdle);
        }
        if (destCircle != null) {
            destCircle.setFill(Color.BLACK);
            destCircle.setRadius(AnchorPaneHandler.nodeSizeIdle);
        }
    }


    /**
     * Clears currentAnimation and currentAnt attributes and removes ant from anchorPane
     */
    void clearAnimation() {
        // remove animation
        if (this.animation != null) {
            this.animation.stop();
            this.animation = null;
        }

        // remove ant
        if (ant != null) {
            getCurrentAnchorPane().getChildren().removeAll(this.ant);
            this.ant = null;
        }
    }

    void clearLabels() {
        for (int i = 0; i < Floors.values().length; i++) {
            anchorPaneHandler.getAnchorPaneAtFloor(i).getChildren().remove(this.initNodeLabel);
            anchorPaneHandler.getAnchorPaneAtFloor(i).getChildren().remove(this.destNodeLabel);
        }
        initNodeLabel = null;
        destNodeLabel = null;
    }

    public Text getInitNodeLabel() {
        return initNodeLabel;
    }

    public Text getDestNodeLabel() {
        return destNodeLabel;
    }

    public AnchorPane getCurrentAnchorPane() {
        return this.anchorPaneHandler.getAnchorPaneAtFloor(this.floorIndex);
    }

    public Path getCurrentPath() {
        return this.pathHandler.getPaths().get(floorIndex);
    }

    public GesturePane getCurrentGesturePane() {
        return this.gesturePaneHandler.getGesturePanes().get(floorIndex);
    }

    public int getFloorIndex() {
        return floorIndex;
    }

    public void setFloorIndex(int floorIndex) {
        this.floorIndex = floorIndex;
    }

    public Circle getInitCircle() {
        return initCircle;
    }

    public void setInitCircle(Circle initCircle) {
        if (initCircle != null) {
            initCircle.setFill(Color.GREEN);
            initCircle.setRadius(AnchorPaneHandler.getNodeSizeHighlited);
        }
        this.initCircle = initCircle;
    }

    public void setInitCircle(String initCircle) {
        setInitCircle(anchorPaneHandler.getCircleFromName(initCircle));
    }

    public Circle getDestCircle() {
        return destCircle;
    }

    public void setDestCircle(Circle destCircle) {
        if (destCircle != null) {
            destCircle.setRadius(AnchorPaneHandler.getNodeSizeHighlited);
            destCircle.setFill(Color.RED);
        }
        this.destCircle = destCircle;
    }

    public void setDestCircle(String destCircle) {
        setDestCircle(anchorPaneHandler.getCircleFromName(destCircle));
    }

    public PathTransition getAnimation() {
        return animation;
    }

    public void setAnimation(PathTransition animation) {
        this.animation = animation;
    }

    public Rectangle getAnt() {
        return ant;
    }

    public void setAnt(Rectangle ant) {
        this.ant = ant;
    }

    private Text labelFactory(Node node) {
        Text text = new Text();
        text.setText(node.getLongName());
        text.setFont(Font.font(60));
        text.setLayoutX(node.getXcoord() - text.getLayoutBounds().getWidth()/2);
        text.setLayoutY(node.getYcoord() - 60);
        //text.setStyle("-fx-background-color: #ffffff;"); // does not work
        return text;
    }

    void newDestLabel(Node node) {
        Text text = labelFactory(node);
        this.destNodeLabel = text;
        anchorPaneHandler.getAnchorPaneAtFloor(Floors.getByID(node.getFloor()).getIndex()).getChildren().add(text);
    }

    void newInitLabel(Node node) {
        Text text = labelFactory(node);
        this.initNodeLabel = text;
        anchorPaneHandler.getAnchorPaneAtFloor(Floors.getByID(node.getFloor()).getIndex()).getChildren().add(text);
    }
}
