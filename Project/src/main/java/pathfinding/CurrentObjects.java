package pathfinding;

import javafx.animation.PathTransition;
import javafx.scene.SubScene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Path;
import javafx.scene.shape.Rectangle;
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
        this.destID = null;
        this.initialID = null;
        this.contextMenu = null;
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
}
