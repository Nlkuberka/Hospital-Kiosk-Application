package entities;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class AStarGraph extends Graph {
    public AStarGraph(LinkedList<Node> storedNodes) {
        super(storedNodes);
    }

    @Override
    public List<String> shortestPath(String startID, String targetID) {
        List<Integer> priorityQueue = new LinkedList<>();
        // the distances of the shortest known path from start to each node
        List<Double> distanceFromStart = new ArrayList<>(storedNodes.size());
        // the estimated distances of the shortest path from start to target that includes each node.
        List<Double> pathDistance = new ArrayList<>(storedNodes.size());
        for(int i = 0; i < storedNodes.size(); i++) {
            distanceFromStart.add(Double.MAX_VALUE);
            pathDistance.add(Double.MAX_VALUE);
        }
        int startIndex = mapNodeIDToIndex(startID);
        int targetIndex = mapNodeIDToIndex(targetID);
        distanceFromStart.set(startIndex, 0.0);
        pathDistance.set(startIndex, getDistanceToTarget(startIndex, targetIndex));
        priorityQueue.add(startIndex);

        // Search nodes most likely to be in the shortest path from start to target.
        boolean done = false;
        while(!done) {
            if(priorityQueue.isEmpty()) {   // if there is no path from start to target
                return null;
            }
            int node = priorityQueue.remove(0);
            if(node == targetIndex) {   // Stop searching when a path to target is found.
                done = true;
            }
            // Search nodes adjacent to this node.
            for(int i = 0; i < adj.get(node).size(); i++) {
                int nextNode = adj.get(node).get(i);
                double newDistanceFromStart = distanceFromStart.get(node) + adjWeights.get(node).get(i);
                double newPathDistance = newDistanceFromStart + getDistanceToTarget(nextNode, targetIndex);
                if(newPathDistance < pathDistance.get(nextNode)) {
                    int index = 0;
                    distanceFromStart.set(nextNode, newDistanceFromStart);
                    pathDistance.set(nextNode, newPathDistance);
                    for (int queuedNode : priorityQueue) {
                        if (newPathDistance < pathDistance.get(queuedNode)) {
                            break;
                        }
                        index++;
                    }
                    priorityQueue.add(index, nextNode);
                }
            }
        }
        // Backtrack from target node to get the shortest path from start to target.
        List<String> path = new LinkedList<>();
        path.add(targetID);
        int current = targetIndex;
        while(current != startIndex) {
            for(int i = 0; i < adj.get(current).size(); i++) {
                int previousNode = adj.get(current).get(i);
                if(distanceFromStart.get(previousNode) + adjWeights.get(current).get(i) == distanceFromStart.get(current)) {
                    path.add(0, mapIndexToNode(previousNode).getNodeID());
                    current = previousNode;
                    break;
                }
            }
        }
        return path;
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
