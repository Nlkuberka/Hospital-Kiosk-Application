package pathfinding;

import entities.Node;
import javafx.geometry.Point2D;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.stage.Stage;

import java.util.LinkedList;
import java.util.List;

public class PathHandler {
    private LinkedList<Path> pathList = new LinkedList<>();
    private List<List<List<Node>>> latestPath;
    private Node latestStartingNode;
    private Stage primaryStage;

    public PathHandler(Path p1, Path p2, Path p3, Path p4, Path p5, Path p6, Path p7, Stage stage) {
        this.pathList.add(p1);
        this.pathList.add(p2);
        this.pathList.add(p3);
        this.pathList.add(p4);
        this.pathList.add(p5);
        this.pathList.add(p6);
        this.pathList.add(p7);
        this.primaryStage = stage;

        // set stroke width
        for (int i = 0; i < this.pathList.size(); i++) {
            this.pathList.get(i).setStrokeWidth(10);
        }

    }

    private void addToPath(Path path, List<Node> nodes) {

        float x = (float) nodes.get(0).getXcoord();
        float y = (float) nodes.get(0).getYcoord();

        path.getElements().add(new MoveTo(x, y)); // move path to initLocation

        // get all XY pairs and turn them into lines
        for (int i = 1; i < nodes.size(); i++) {
            Node node = nodes.get(i);

            x = (float) node.getXcoord();
            y = (float) node.getYcoord();

            //System.out.println(node);
            //System.out.println("NodeX: " + x + "  NodeY: " + y);

            path.getElements().add(new LineTo(x, y));
        }
    }

    private void clearLatestPath() {
        this.latestPath = null;
        this.latestStartingNode = null;
    }

    private void clearAndHidePath(Path path) {
        path.getElements().clear();
        path.setVisible(false);
    }

    private double calculateOpacity(double a, double b) {
        double result = (int) a == (int) b ? 1.0 : 0.0;
        return result;
    }

    List<Path> getPaths() {
        return this.pathList;
    }

    boolean isActive() {
        return latestPath != null;
    }

    private void updatePaths(List<List<List<Node>>> list) {
        if (list.size() != Floors.values().length)
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

    private void hidePaths() {
        for (Path path : this.pathList) {
            path.setVisible(false);
        }
    }

    private void showPaths() {
        for (Path path : this.pathList) {
            path.setVisible(true);
        }
    }

    void cancel() {
        clearAndHideAllPaths();
        clearLatestPath();
    }

    void displayNewPath(List<List<List<Node>>> list, Node startingNode) {
        this.latestPath = list;
        this.latestStartingNode = startingNode;

        // update the paths
        updatePaths(list);

        showPaths();
    }

    Point2D getinitialNodeCoord() {
        return new Point2D(latestStartingNode.getXcoord(), latestStartingNode.getYcoord());
    }

    List<Point2D> getPathExtremaOnInitFloor() {
        return getPathExtremaOnFloor(Floors.getByID(this.latestStartingNode.getFloor()).getIndex());
    }

    List<Point2D> getPathExtremaOnFloor(int floorIndex) {
        double minX = 6000, maxX = 0, minY = 6000, maxY = 0;
        List<List<Node>> list = this.latestPath.get(floorIndex);
        for (List<Node> nodeList : list) {
            for (Node node : nodeList) {
                double nodeX = node.getXcoord();
                double nodeY = node.getYcoord();
                minX = minX > nodeX ? nodeX : minX; // update bounds
                maxX = maxX < nodeX ? nodeX : maxX;
                minY = minY > nodeY ? nodeY : minY;
                maxY = maxY < nodeY ? nodeY : maxY;
            }
        }
        List<Point2D> result = new LinkedList<>();
        result.add(new Point2D(minX, minY));
        result.add(new Point2D(maxX, maxY));
        return result;
    }

    List<Integer> getFloorsUsed() {
        List<Integer> result = new LinkedList<>();
        int i = 0;
        for (List<List<Node>> floor : this.latestPath) {
            if (floor.size() > 0)
                result.add(i);
            i++;
        }
        return result;
    }


}
