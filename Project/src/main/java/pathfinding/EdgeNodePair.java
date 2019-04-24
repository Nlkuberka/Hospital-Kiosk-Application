package pathfinding;

import com.jfoenix.controls.JFXTabPane;
import entities.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import utilities.Tooltip;

public class EdgeNodePair {
    Node first;
    Node next;
    Circle firstCircle;
    Circle nextCircle;

    public EdgeNodePair(Node first, Node next, JFXTabPane mapTabs) {
        this.first = first;
        this.next = next;

        firstCircle = new Circle();
        firstCircle.setCenterX(first.getXcoord());
        firstCircle.setCenterY(first.getYcoord());

        nextCircle = new Circle();
        nextCircle.setCenterX(next.getXcoord());
        nextCircle.setCenterY(next.getYcoord());

        firstCircle.setRadius(30);
        firstCircle.setFill(Color.web("FFC41E"));
        firstCircle.toFront();

        nextCircle.setRadius(30);
        nextCircle.setFill(Color.web("FFC41E"));
        nextCircle.toFront();

        new Tooltip(firstCircle, "Go to " + getNextFloor().getName());
        new Tooltip(nextCircle, "Go to " + getFirstFloor().getName());

        int firstFloor = getFirstFloor().getTabIndex();
        int nextFloor = getNextFloor().getTabIndex(); // get next floor to link to

        firstCircle.setOnMouseClicked(e -> mapTabs.getSelectionModel().select(nextFloor)); // link to it
        nextCircle.setOnMouseClicked(e -> mapTabs.getSelectionModel().select(firstFloor)); // link to it
    }

    Floors getFirstFloor() {
        return Floors.getByID(first.getFloor());
    }

    Floors getNextFloor() {
        return Floors.getByID(next.getFloor());
    }
}
