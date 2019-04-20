package entities;

import java.util.List;

public abstract class SearchGraph extends Graph {
    /**
     * Finds the shortest path between two nodes
     * @param startID the String ID of the starting node
     * @param targetID the String ID of the desired finish node
     * @return returns an LinkedList<List<String>> of the shortest path between those two points
     */
    public List<String> shortestPath(String startID, String targetID) {
        int startIndex = mapNodeIDToIndex(startID);
        int targetIndex = mapNodeIDToIndex(targetID);
        if(startIndex == -1 || targetIndex == -1) {
            return null;
        }
        double [] distance = new double [nodeIDs.size()]; // distance of shortest known path from start to all nodes
        for(int i = 0; i < nodeIDs.size(); i++) {
            distance[i] = Double.MAX_VALUE;
        }
        distance[startIndex] = 0;
        initialize();
        addNodeToRelax(startIndex, distance, targetIndex);

        // Search nodes and get the distances of the shortest path from start to each node.
        while(!finishedSearch()) {
            int current = getNodeToRelax(targetIndex);
            boolean currentIsStairs = nodeIsStairs.get(current);
            for (int i = 0; i < adj.get(current).size(); i++) {
                int nextNode = adj.get(current).get(i);
                if (!noStairsIsOn || !currentIsStairs || !nodeIsStairs.get(nextNode)) {
                    double currentDistance = distance[current] + adjWeights.get(current).get(i);
                    if (currentDistance < distance[nextNode]) {
                        distance[nextNode] = currentDistance;
                        addNodeToRelax(nextNode, distance, targetIndex);
                    }
                }
            }
        }
        return backtrack(distance, startIndex, targetIndex);
    }

    /**
     * Performs any operations needed before beginning the search.
     */
    protected  abstract void initialize();

    /**
     * Adds a node to a set of nodes that need to be relaxed.
     * @param node the node to be added
     * @param distanceFromStart the distances of the shortest known paths from start to each node in the graph
     * @param targetIndex the index in adj of the target node
     */
    protected abstract void addNodeToRelax(int node, double[] distanceFromStart, int targetIndex);

    /**
     * Removes a node from a set of nodes that need to be relaxed, and returns it.
     * @param targetIndex the index in adj of the target node
     * @return the node to be relaxed
     */
    protected abstract int getNodeToRelax(int targetIndex);

    /**
     * Checks if the algorithm has already found the shortest path from start to target.
     * @return true if the shortest path has been found, false otherwise
     */
    protected abstract boolean finishedSearch();

}
