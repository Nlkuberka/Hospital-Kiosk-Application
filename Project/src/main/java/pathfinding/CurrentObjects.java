package pathfinding;

import javafx.animation.PathTransition;
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

    /**
     * Clear style of currently selected circles
     */
    void clearNodeStyle() {
        initCircle.setFill(Color.BLACK);
        initCircle.setRadius(13);
        destCircle.setFill(Color.BLACK);
        destCircle.setRadius(13);
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
        this.initCircle = initCircle;
    }

    public void setInitCircle(String initCircle) {
        this.initCircle = anchorPaneHandler.getCircleFromName(initCircle);
    }

    public Circle getDestCircle() {
        return destCircle;
    }

    public void setDestCircle(Circle destCircle) {
        this.destCircle = destCircle;
    }

    public void setDestCircle(String destCircle) {
        this.destCircle = anchorPaneHandler.getCircleFromName(destCircle);
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
