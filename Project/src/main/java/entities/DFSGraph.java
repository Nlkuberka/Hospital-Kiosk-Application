package entities;

import java.util.LinkedList;
import java.util.Stack;

public class DFSGraph extends SearchGraph {
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

    @Override
    protected void addNodeToRelax(int node, double[] distanceFromStart, int targetIndex) {
        stack.push(node);
    }

    @Override
    protected int getNodeToRelax(int targetIndex) {
        return stack.pop();
    }

    @Override
    protected boolean finishedSearch() {
        return stack.empty();
    }
}