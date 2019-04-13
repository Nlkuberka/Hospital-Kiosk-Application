package entities;

import java.util.LinkedList;
import java.util.Queue;

public class BFSGraph extends Graph {
    private Queue<Integer> queue;

    public BFSGraph(LinkedList<Node> storedNodes) {
        super(storedNodes);
    }

    /**
     * Performs any operations needed before beginning the search.
     */
    @Override
    protected void initialize() {
        queue = new LinkedList<>();
    }

    protected void addNodeToRelax(int node, double distanceFromStart, int targetIndex) {
        queue.add(node);
    }

    protected int getNodeToRelax() {
        return queue.remove();
    }

    protected boolean finishedSearch() {
        return queue.size() == 0;
    }
}
