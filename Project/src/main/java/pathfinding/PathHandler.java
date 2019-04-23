package pathfinding;

import com.jfoenix.controls.JFXTabPane;
import entities.Node;
import javafx.geometry.Point2D;
import javafx.scene.shape.Circle;
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
    private LinkedList<EdgeNode> edgeNodes;

    public PathHandler(Path p1, Path p2, Path p3, Path p4, Path p5, Path p6, Path p7) {
        this.pathList.add(p1);
        this.pathList.add(p2);
        this.pathList.add(p3);
        this.pathList.add(p4);
        this.pathList.add(p5);
        this.pathList.add(p6);
        this.pathList.add(p7);

        this.edgeNodes = new LinkedList<>();

        // set stroke width
        for (int i = 0; i < this.pathList.size(); i++) {
            this.pathList.get(i).setStrokeWidth(10);
        }

    }

    public LinkedList<EdgeNode> getEdgeNodes() {
        return edgeNodes;
    }

    public void drawFloorLinks(AnchorPaneHandler anchorPaneHandler, JFXTabPane mapTabs) {
        for (int i = 0; i < this.edgeNodes.size(); i++) { // for every edge node
            EdgeNode edgeNode = this.edgeNodes.get(i);
            Circle circle = anchorPaneHandler.getCircleFromName(edgeNode.node.getLongName()); // get corresponding circle
            int nextFloor = Floors.getByID(edgeNode.next.getFloor()).getTabIndex(); // get next floor to link to
            circle.setOnMouseClicked(e -> mapTabs.getSelectionModel().select(nextFloor)); // link to it
        }
    }

    private Node addToPath(Path path, List<Node> nodes) throws Exception {

        float x = (float) nodes.get(0).getXcoord();
        float y = (float) nodes.get(0).getYcoord();

        path.getElements().add(new MoveTo(x, y)); // move path to initLocation

        // get all XY pairs and turn them into lines
        Node node = null;
        for (int i = 1; i < nodes.size(); i++) {
            node = nodes.get(i);

            x = (float) node.getXcoord();
            y = (float) node.getYcoord();

            //System.out.println(node);
            //System.out.println("NodeX: " + x + "  NodeY: " + y);

            path.getElements().add(new LineTo(x, y));
        }

        if (node == null)
            throw new Exception("addToPath: no nodes present!");

        return node;
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

        this.edgeNodes = new LinkedList<>();
        for (int floor = 0; floor < list.size(); floor++) { // get floor
            for (int pathNum = 0; pathNum < list.get(floor).size(); pathNum++) { // get specific path on that floor
                try { // will fail if a path has no nodes
                    Node lastNode = addToPath(this.pathList.get(floor), list.get(floor).get(pathNum));
                    Node firstNode = list.get(floor).get(pathNum).get(0);
                    this.edgeNodes.add(new EdgeNode(firstNode, lastNode));
                } catch (Exception e) {
                    e.printStackTrace();
                }
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
