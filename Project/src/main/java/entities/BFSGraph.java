package entities;

import java.util.LinkedList;
import java.util.Queue;

public class BFSGraph extends Graph {
    private Queue<Integer> queue;

    protected BFSGraph() {
        super();
    }

    /**
     * Performs any operations needed before beginning the search.
     */
    @Override
    protected void initialize() {
        queue = new LinkedList<>();
    }

    @Override
    protected void addNodeToRelax(int node, double[] distanceFromStart, int targetIndex) {
        queue.add(node);
    }

    @Override
    protected int getNodeToRelax(int  targetIndex) {
        return queue.remove();
    }

    @Override
    protected boolean finishedSearch() {
        return queue.size() == 0;
    }
}
