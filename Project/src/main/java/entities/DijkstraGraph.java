package entities;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public abstract class DijkstraGraph extends Graph {
    public DijkstraGraph(LinkedList<Node> storedNodes) {
        super(storedNodes);
    }

    /**
     * Finds the shortest path between two nodes
     * @param startID the String ID of the starting node
     * @param targetID the String ID of the desired finish node
     * @return returns an LinkedList<List<String>> of the shortest path between those two points
     */
    public List<String> shortestPath(String startID, String targetID) {
        int startIndex = mapNodeIDToIndex(startID);
        int targetIndex = mapNodeIDToIndex(targetID);
        int current = startIndex;
        double [] distance = new double [storedNodes.size()]; //stored distance from start node to node at index
        for(int i = 0; i < storedNodes.size(); i++) {
            distance[i] = Double.MAX_VALUE;
        }
        distance[startIndex] = 0;
        addNodeToRelax(startIndex);

        // BFS of nodes and get the distances of each node
        while(hasNodesToRelax()) {
            current = getNodeToRelax();
            for(int i = 0; i < adj.get(current).size(); i++) {
                int nextNode = adj.get(current).get(i);
                double currentDistance = distance[current] + adjWeights.get(current).get(i);
                if(currentDistance < distance[nextNode]) {
                    distance[nextNode] = currentDistance;
                    addNodeToRelax(nextNode);
                }
            }
        }
        if(distance[targetIndex] == Double.MAX_VALUE) {
            return null;
        }
        List<String> path = new LinkedList<>();
        path.add(targetID);
        current = targetIndex;
        while(current != startIndex) {
            for (int i = 0; i < adj.get(current).size(); i++) {
                int previousNode = adj.get(current).get(i);
                if (adjWeights.get(current).get(i) + distance[previousNode] == distance[current]) {
                    path.add(0, mapIndexToNode(previousNode).getNodeID());
                    current = previousNode;
                    break;
                }
            }
        }
        return path;
    }

    /**
     * Adds a node to a set of nodes that need to be relaxed.
     * @param node the node to be added
     */
    protected abstract void addNodeToRelax(int node);

    /**
     * Removes a node from a set of nodes that need to be relaxed, and returns it.
     * @return the node to be relaxed
     */
    protected abstract int getNodeToRelax();

    /**
     * Checks if there are nodes that need to be relaxed (i.e. the aforementioned set is not empty)
     * @return true if there is at least one node to be relaxed, false otherwise
     */
    protected abstract boolean hasNodesToRelax();
}