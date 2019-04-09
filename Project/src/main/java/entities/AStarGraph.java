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
        List<Double> distanceFromStart = new ArrayList<>(storedNodes.size());
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
        boolean done = false;
        while(!done) {
            if(priorityQueue.isEmpty()) {
                return null;
            }
            int node = priorityQueue.remove(0);
            if(node == targetIndex) {
                done = true;
            }
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

    private double getDistanceToTarget(int nodeIndex, int targetIndex) {
        Node startNode = storedNodes.get(nodeIndex);
        Node targetNode = storedNodes.get(targetIndex);
        double xDifference = startNode.getXcoord() - targetNode.getXcoord();
        double yDifference = startNode.getYcoord() - targetNode.getYcoord();
        return Math.sqrt(xDifference * xDifference + yDifference * yDifference);
    }
}
