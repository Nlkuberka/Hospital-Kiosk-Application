package entities;

import java.util.LinkedList;
import java.util.List;

public class DijkstraGraph extends SearchGraph {

    private List<Integer> priorityQueue;
    private boolean finishedSearch;

    /**
     * Performs any operations needed before beginning the search.
     */
    @Override
    protected void initialize() {
        priorityQueue = new LinkedList<>();
        finishedSearch = false;
    }

    /**
     * Adds a node to a set of nodes that need to be relaxed.
     * @param node the node to be added
     * @param distanceFromStart the distances of the shortest known paths from start to each node in the graph
     * @param targetIndex the index in adj of the target node
     */
    @Override
    protected void addNodeToRelax(int node, double[] distanceFromStart, int targetIndex) {
        int index = 0;
        for(int queuedNode : priorityQueue) {
            if(distanceFromStart[node] < distanceFromStart[queuedNode]) {
                break;
            }
            index++;
        }
        priorityQueue.add(index, node);
    }

    /**
     * Removes a node from a set of nodes that need to be relaxed, and returns it.
     * @param targetIndex the index in adj of the target node
     * @return the node to be relaxed
     */
    @Override
    protected int getNodeToRelax(int targetIndex) {
        int returnValue = priorityQueue.remove(0);
        if(returnValue == targetIndex) {
            finishedSearch = true;
        }
        return returnValue;
    }

    /**
     * Checks if the algorithm has already found the shortest path from start to target.
     *
     * @return true if the shortest path has been found, false otherwise
     */
    @Override
    protected boolean finishedSearch() {
        return priorityQueue.isEmpty() || finishedSearch;
    }
}
