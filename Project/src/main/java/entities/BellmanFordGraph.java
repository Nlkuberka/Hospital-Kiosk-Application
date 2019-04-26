package entities;

import java.util.List;

public class BellmanFordGraph extends Graph {

    /**
     * Finds the shortest path between two nodes
     * @param startID the String ID of the starting node
     * @param targetID the String ID of the desired finish node
     * @return returns an LinkedList<List<String>> of the shortest path between those two points
     */
    @Override
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
        boolean done = false;
        for(int i = 1; !done && i < nodeIDs.size(); i++) {
            done = true;
            // Iterate over all edges. Relax the endpoints if necessary.
            int nodeIndex1 = 0; // the index of edges in adj
            for(List<Integer> edges : adj) {
                boolean node1IsStairs = nodeIsStairs.get(nodeIndex1);
                int edgesIndex = 0; // the index of nodeIndex2 in edges
                for (int nodeIndex2 : edges) {
                    if(!noStairsIsOn || !node1IsStairs || !nodeIsStairs.get(nodeIndex2)) {
                        double newDistance = distance[nodeIndex1] + adjWeights.get(nodeIndex1).get(edgesIndex);
                        if (newDistance < distance[nodeIndex2]) {
                            distance[nodeIndex2] = newDistance;
                            done = false;
                        }
                    }
                    edgesIndex++;
                }
                nodeIndex1++;
            }
        }
        return backtrack(distance, startIndex, targetIndex);
    }
}
