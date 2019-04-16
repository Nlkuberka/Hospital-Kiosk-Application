package pathfinding;

import javafx.animation.PathTransition;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

public class CurrentObjects {
    private int floorIndex;
    private Circle initCircle;
    private Circle destCircle;
    private PathTransition animation = null;
    private Rectangle ant = null;
    private PathHandler pathHandler;
    private AnchorPaneHandler anchorPaneHandler;

    public CurrentObjects(int floorIndex, Circle initCircle, Circle destCircle, PathTransition animation, Rectangle currentAnt,
                          PathHandler pathHandler, AnchorPaneHandler anchorPaneHandler) {
        this.floorIndex = floorIndex;
        this.initCircle = initCircle;
        this.destCircle = destCircle;
        this.animation = animation;
        this.ant = currentAnt;
        this.pathHandler = pathHandler;
        this.anchorPaneHandler = anchorPaneHandler;
    }

    public AnchorPane getCurrentAnchorPane() {
        return this.anchorPaneHandler.getAnchorPaneAtFloor(this.floorIndex);
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
        this.initCircle = initCircle;
    }

    public Circle getDestCircle() {
        return destCircle;
    }

    public void setDestCircle(Circle destCircle) {
        this.destCircle = destCircle;
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
