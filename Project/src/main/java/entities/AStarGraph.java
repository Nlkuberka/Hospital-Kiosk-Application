package entities;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class AStarGraph extends Graph {
    private List<Double> totalPathDistance;
    private List<Integer> priorityQueue;
    private boolean finishedSearch;

    public AStarGraph(LinkedList<Node> storedNodes) {
        super(storedNodes);
    }

    /**
     * Performs any operations needed before beginning the search.
     */
    @Override
    protected void initialize() {
        totalPathDistance = new ArrayList<>(storedNodes.size());
        for(int i = 0; i < storedNodes.size(); i++) {
            totalPathDistance.add(Double.MAX_VALUE);
        }
        priorityQueue = new LinkedList<>();
        finishedSearch = false;
    }

    /**
     * Adds a node to a set of nodes that need to be relaxed.
     *
     * @param node the node to be added
     */
    @Override
    protected void addNodeToRelax(int node, double distanceFromStart, int targetIndex) {
        totalPathDistance.set(node, distanceFromStart + getDistanceToTarget(node, targetIndex));
        int index = 0;
        for (int queuedNode : priorityQueue) {
            if (totalPathDistance.get(node) < totalPathDistance.get(queuedNode)) {
                break;
            }
            index++;
        }
        priorityQueue.add(index, node);
        if(node == targetIndex) {
            finishedSearch = true;
        }
    }

    /**
     * Removes a node from a set of nodes that need to be relaxed, and returns it.
     *
     * @return the node to be relaxed
     */
    @Override
    protected int getNodeToRelax() {
        return priorityQueue.remove(0);
    }

    /**
     * Checks if the algorithm has already found the shortest path from start to target.
     * @return true if the shortest path has been found, false otherwise
     */
    @Override
    protected boolean finishedSearch() {
        return priorityQueue.isEmpty() || finishedSearch;
    }

    /**
     * Estimates the distance of the shortest path from an arbitrary node to the target.
     * Calculates the Euclidean distance between these two points, using the Pythagorean theorem.
     * @param nodeIndex the index in storedNodes of the arbitrary node
     * @param targetIndex the index in storedNodes of the target node
     * @return the estimated distance between these nodes
     */
    private double getDistanceToTarget(int nodeIndex, int targetIndex) {
        Node startNode = storedNodes.get(nodeIndex);
        Node targetNode = storedNodes.get(targetIndex);
        double xDifference = startNode.getXcoord() - targetNode.getXcoord();
        double yDifference = startNode.getYcoord() - targetNode.getYcoord();
        return Math.sqrt(xDifference * xDifference + yDifference * yDifference);
    }
}
