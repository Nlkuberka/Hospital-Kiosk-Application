package pathfinding;

import entities.Node;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;

import java.util.LinkedList;
import java.util.List;

public class MapHandler {
    private LinkedList<Path> pathList = new LinkedList<>();
    private LinkedList<ImageView> mapList = new LinkedList<>();
    private LinkedList<AnchorPane> paneList = new LinkedList<>();
    UIControllerPFM.Floors currentFloor;
    private List<List<List<Node>>> latestPath;

    private void addToPath(Path path, List<Node> nodes) {
        AnchorPane pane = getCurrentPane();

        float scaleFx = (float) pane.getPrefWidth() / 5000.0f;
        float scaleFy = (float) pane.getPrefHeight() / 3400.0f;

        System.out.println("ScaleFx: " + scaleFx + "  ScaleFy: " + scaleFy);

//        clearPathOnMap();

        float x = (float) nodes.get(0).getXcoord() * scaleFx;
        float y = (float) nodes.get(0).getYcoord() * scaleFy;

        path.getElements().add(new MoveTo(x, y)); // move path to initLocation

        // get all XY pairs and turn them into lines
        for (int i = 1; i < nodes.size(); i++) {
            Node node = nodes.get(i);

            x = (float) node.getXcoord() * scaleFx;
            y = (float) node.getYcoord() * scaleFy;

            System.out.println(node);
            System.out.println("NodeX: " + x + "  NodeY: " + y);

            path.getElements().add(new LineTo(x, y));
        }
    }

    private AnchorPane getCurrentPane() {
        return this.paneList.get(currentFloor.ordinal());
    }

    private ImageView getCurrentMap() {
        return this.mapList.get(currentFloor.ordinal());
    }

    private Path getCurrentPath() {
        return this.pathList.get(currentFloor.ordinal());
    }

    private void clearLatestPath() {
        this.latestPath = null;
    }

    private void clearAndHidePath(Path path) {
        path.getElements().clear();
        path.setVisible(false);
    }

    void setAllPaneSize(double width, double height) {
        for (AnchorPane pane : this.paneList) {
            pane.setPrefSize(width, height);
        }
    }

    private double calculateOpacity(double a, double b) {
        double result = (int) a == (int) b ? 1.0 : 0.0;
        return result;
    }

    private void updatePaths(List<List<List<Node>>> list) {
        if (list.size() != UIControllerPFM.Floors.values().length)
            System.out.println("WARNING: Did not receive a node list for each floor");

        clearAndHideAllPaths();

        for (int floor = 0; floor < list.size(); floor++) { // get floor
            for (int pathNum = 0; pathNum < list.get(floor).size(); pathNum++) { // get specific path on that floor
                addToPath(this.pathList.get(floor), list.get(floor).get(pathNum));
            }
        }
    }

    private void clearAndHideAllPaths() {
        for (Path path : this.pathList) {
            clearAndHidePath(path);
        }
    }

    private void disablePaths() {
        for (Path path : this.pathList) {
            path.setVisible(false);
        }
    }

    private void enablePaths() {
        for (Path path : this.pathList) {
            path.setVisible(true);
        }
    }

    void changeToFloor(double floor) {
        for (int i = 0; i < mapList.size(); i++) {
            double opacity = calculateOpacity((double) i, floor);
            mapList.get(i).setOpacity(opacity);
            pathList.get(i).setOpacity(opacity);
        }
        this.currentFloor = UIControllerPFM.Floors.values()[(int) floor];
    }

    void cancel() {
        clearAndHideAllPaths();
        clearLatestPath();
    }

    void zoomIn(double zoomFactor) {
        if (getCurrentPane().getPrefWidth() < getCurrentPane().getMaxWidth()) {
            this.setAllPaneSize(getCurrentPane().getPrefWidth() * zoomFactor,
                    getCurrentPane().getPrefHeight() * zoomFactor);
        }
        this.redrawPathIfNeeded();
    }

    void zoomOut(double zoomFactor) {
        if (getCurrentPane().getPrefWidth() > getCurrentPane().getMinWidth()) {
            this.setAllPaneSize(getCurrentPane().getPrefWidth() / zoomFactor,
                    getCurrentPane().getPrefHeight() / zoomFactor);
        }
        this.redrawPathIfNeeded();
    }

    private void redrawPathIfNeeded() {
        if (this.latestPath != null) {
            updatePaths(this.latestPath);
            changeToFloor(this.currentFloor.ordinal());
            this.enablePaths();
        }
    }

    void displayNewPath(List<List<List<Node>>> list, UIControllerPFM.Floors floor) {
        updatePaths(list);
        this.latestPath = list;
        changeToFloor(floor.ordinal());
        enablePaths();
    }
}
