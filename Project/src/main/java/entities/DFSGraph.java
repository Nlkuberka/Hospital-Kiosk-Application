package entities;

import java.util.LinkedList;
import java.util.Stack;

public class DFSGraph extends Graph {
    private Stack<Integer> stack;

    protected DFSGraph() {
        super();
    }

    /**
     * Performs any operations needed before beginning the search.
     */
    @Override
    protected void initialize() {
        stack = new Stack<>();
    }

    protected void addNodeToRelax(int node, double distanceFromStart, int targetIndex) {
        stack.push(node);
    }

    protected int getNodeToRelax() {
        return stack.pop();
    }

    protected boolean finishedSearch() {
        return stack.empty();
    }
}