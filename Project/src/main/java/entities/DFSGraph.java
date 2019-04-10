package entities;

import java.util.LinkedList;
import java.util.Stack;

public class DFSGraph extends DijkstraGraph {
    Stack<Integer> stack = new Stack<>();

    public DFSGraph(LinkedList<Node> storedNodes) {
        super(storedNodes);
    }

    protected void addNodeToRelax(int node) {
        stack.push(node);
    }

    protected int getNodeToRelax() {
        return stack.pop();
    }

    protected boolean hasNodesToRelax() {
        return !stack.empty();
    }
}