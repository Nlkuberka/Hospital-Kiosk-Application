package pathfinding;

import entities.Node;

public class EdgeNode {
    Node node;
    Node next;

    public EdgeNode(Node node, Node next) {
        this.node = node;
        this.next = next;
    }
}
