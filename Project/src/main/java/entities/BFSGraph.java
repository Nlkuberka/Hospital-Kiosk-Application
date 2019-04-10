package entities;

import java.util.LinkedList;
import java.util.Queue;

public class BFSGraph extends DijkstraGraph {
    Queue<Integer> queue = new LinkedList<>();

    public BFSGraph(LinkedList<Node> storedNodes) {
        super(storedNodes);
    }

    protected void addNodeToRelax(int node) {
        queue.add(node);
    }

    protected int getNodeToRelax() {
        return queue.remove();
    }

    protected boolean hasNodesToRelax() {
        return queue.size() > 0;
    }
}
