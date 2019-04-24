package pathfinding;

import javafx.animation.Animation;
import javafx.animation.Interpolator;
import javafx.animation.PathTransition;
import javafx.geometry.Dimension2D;
import javafx.geometry.Point2D;
import javafx.scene.input.MouseButton;
import javafx.util.Duration;
import net.kurobako.gesturefx.GesturePane;

import java.util.LinkedList;
import java.util.List;

public class GesturePaneHandler {
    private List<GesturePane> gesturePanes;
    static final Duration DURATION = Duration.millis(400);
    private CurrentObjects currentObjects;

    public GesturePaneHandler(GesturePane p1, GesturePane p2, GesturePane p3, GesturePane p4,
            GesturePane p5, GesturePane p6, GesturePane p7) {
        this.gesturePanes = new LinkedList<>();
        gesturePanes.add(p1);
        gesturePanes.add(p2);
        gesturePanes.add(p3);
        gesturePanes.add(p4);
        gesturePanes.add(p5);
        gesturePanes.add(p6);
        gesturePanes.add(p7);

        setupGesturePanes();
    }

    public void setCurrentObjects(CurrentObjects currentObjects) {
        this.currentObjects = currentObjects;
    }

    public List<GesturePane> getGesturePanes() {
        return gesturePanes;
    }

    /**
     * Sets up gesture panes. 1) adds gesture panes to list 2) sets minscale, maxscale and scroll-bar
     * 3) applies zoom bindings 4) sets event handlers for zoom 4) sets initial zoom
     *
     */
    private void setupGesturePanes() {
        // setup properties
        for(int i = 0; i < this.gesturePanes.size(); i++) {
            GesturePane pane = this.gesturePanes.get(i);
            pane.setMaxScale(1.5);
            pane.setMinScale(0.01);
            pane.setScrollBarEnabled(true);
            pane.setHBarEnabled(true);
        }

        // setup scale bindings
        for(int i = 0; i < this.gesturePanes.size()-1; i++) {
            GesturePane pane = this.gesturePanes.get(i);
            GesturePane next = this.gesturePanes.get(i+1);
            pane.currentScaleProperty().bindBidirectional(next.currentScaleProperty());
        }


        // zoom*2 on double-click
        for (GesturePane pane : this.gesturePanes) {
            pane.setOnMouseClicked(e -> {
                Point2D pivotOnTarget = pane.targetPointAt(new Point2D(e.getX(), e.getY()))
                        .orElse(pane.targetPointAtViewportCentre());
                if (e.getButton() == MouseButton.PRIMARY && e.getClickCount() == 2) {
                    if (currentObjects != null) {
                        currentObjects.clearContextMenu();
                    }
                } else if (e.getButton() == MouseButton.SECONDARY && e.getClickCount() == 1) {
                    pane.animate(DURATION)
                            .interpolateWith(Interpolator.EASE_BOTH)
                            .zoomTo(pane.getMinScale(), pivotOnTarget);
                }
            });
        }

        resetZoom();
    }

    public void resetZoom() {
        // zoom so that it looks good
        GesturePane pane = gesturePanes.get(0);
        pane.zoomTo(0.3, new Point2D(0, 0));
        pane.translateBy(new Dimension2D(500.0, 400.0));
    }

    public void changeTabs(GesturePane pane, GesturePane oldPane) {
        pane.centreOn(oldPane.targetPointAtViewportCentre());
    }

    public void zoom(GesturePane pane) {
        Point2D pivotOnTarget = pane.targetPointAtViewportCentre();
        // increment of scale makes more sense exponentially instead of linearly
        pane.animate(DURATION)
                .interpolateWith(Interpolator.EASE_BOTH)
                .zoomBy(pane.getCurrentScale()/1.66, pivotOnTarget);
    }

    public void un_zoom(GesturePane pane) {
        Point2D pivotOnTarget = pane.targetPointAtViewportCentre();
        pane.animate(DURATION)
                .interpolateWith(Interpolator.EASE_BOTH)
                .zoomBy(-0.33, pivotOnTarget);
    }

    /**
     * Generates new animation based on given path. Sets the currentAnt and currentAnimation attributes
     */
    void newAnimation(CurrentObjects currentObjects) {
        PathTransition pathTransition = new PathTransition();

        //Setting the duration of the path transition
        pathTransition.setDuration(Duration.seconds(5));
        pathTransition.setRate(0.5);

        //Setting the first for the transition
        currentObjects.setAnt();
        //currentObjects.getAnt().setFill(Color.LIGHTGREEN);
        currentObjects.getCurrentAnchorPane().getChildren().add(currentObjects.getAnt());
        pathTransition.setNode(currentObjects.getAnt());

        //Setting the path
        pathTransition.setPath(currentObjects.getCurrentPath());

        //Setting the orientation of the path
        pathTransition.setOrientation(PathTransition.OrientationType.NONE);

        //Setting auto reverse value to false
        pathTransition.setAutoReverse(false);

        pathTransition.setCycleCount(Animation.INDEFINITE);

        pathTransition.setOnFinished(e -> {
            currentObjects.clearAnimation();
        });

        if ((!(currentObjects.getInitCircle() == null)) && (!(currentObjects.getDestCircle() == null))) {
            pathTransition.play();
        }

        currentObjects.setAnimation(pathTransition);
    }

    void centerOnInitialNode(PathHandler pathHandler, GesturePane pane, int floor) {
        // center on initial first
        List<Point2D> extremaMinMax = pathHandler.getPathExtremaOnFloor(floor); // get extrema
        double centerX = (extremaMinMax.get(0).getX() + extremaMinMax.get(1).getX()) / 2; // find average
        double centerY = (extremaMinMax.get(0).getY() + extremaMinMax.get(1).getY()) / 2;

        double ySpan = extremaMinMax.get(1).getY() - extremaMinMax.get(0).getY();
        double xSpan = extremaMinMax.get(1).getX() - extremaMinMax.get(0).getX();

        double buffer = pane.getViewportWidth() * 0.7;

        double ySf = calcScaleFactor(pane.getViewportHeight(), ySpan, buffer);
        double xSf = calcScaleFactor(pane.getViewportWidth(), xSpan, buffer);

        double sf = ySf < xSf ? ySf : xSf; // get min sf so it fits

        Point2D center = new Point2D(centerX, centerY); // animate to that point

        pane.animate(DURATION)
                .interpolateWith(Interpolator.EASE_BOTH)
                .afterFinished(() -> pane.animate(DURATION)
                        .interpolateWith(Interpolator.EASE_BOTH)
                        .zoomTo(sf, center))
                .centreOn(center);



    }

    private double calcScaleFactor(double viewPort, double yield, double buffer) {
        return viewPort / (yield + buffer);
    }

    public void setPaning(boolean value) {
        for(GesturePane gp : gesturePanes) {
            gp.setGestureEnabled(value);
        }
    }
}
