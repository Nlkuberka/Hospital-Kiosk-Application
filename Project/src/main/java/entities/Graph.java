package entities;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import static java.lang.Math.*;


public class Graph {
    
    private LinkedList<LinkedList<Integer>> adj; //
    private LinkedList<LinkedList<Double>> adjWeights; //weights of the edges
    private LinkedList<Node> storedNodes; //nodes that have been stored


    /**
     * Constructor
     * Initalizes the graph with the given number of nodes
     */
    public Graph(LinkedList<Node> storedNodes) {
        this.storedNodes = storedNodes;
        if(storedNodes.size() < 0) {
            throw new IllegalArgumentException("Number of nodes must be greater than or equal to 0");
        }
        //this.storedNodes.size() = storedNodes.size();
        this.adj = new LinkedList<LinkedList<Integer>>();
        this.adjWeights = new LinkedList<>();
        for(int i = 0; i < storedNodes.size(); i++) {
            this.adj.add(new LinkedList<Integer>());
            this.adjWeights.add(new LinkedList<>());
        }
    }

    /**
     * Check the formatting and contecnt of adj and adjWeights
     */
    public void checkEdges() {
        for(int i = 0; i < adj.size(); i++) {
            System.out.println("Node " + (i ));
            System.out.println("adj " + adj.get(i));
            System.out.println("adjWeights" + adjWeights.get(i));
        }
    }

    /**
     * Maps a node to its index
     * @param desiredNode the node the user is looking to find the index of
     * @return the index of the desired node or -1 for failure
     */
    public int mapNodeToIndex(Node desiredNode){
        for(Node n: storedNodes){
            if(desiredNode.equals(storedNodes.indexOf(n))) {
                return storedNodes.indexOf(n);
            }
        }
        return -1;
    }

    /**
     * Maps the ID of a node to its index
     * @param desiredNodeID the ID of the node the user is looking to find
     * @return the index of the node in the LinkedList or -1 for failure
     */
    public int mapNodeIDToIndex(String desiredNodeID){
        for(Node n: storedNodes){
            if(n.getNodeID().equals(desiredNodeID)) {
                return storedNodes.indexOf(n);
            }
        }
        return -1;
    }

    /**
     * Maps the index of a node to the actual node
     * @param desiredIndex the location of the desired node
     * @return the node at the desired index
     */
    public Node mapIndexToNode(int desiredIndex) {
        return storedNodes.get(desiredIndex);
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
        Queue<Integer> queue = new LinkedList<Integer>(); //nodes that will be checked
        for(int i = 0; i < storedNodes.size(); i++) {
            distance[i] = Double.MAX_VALUE;
        }
        distance[startIndex] = 0;
        queue.add(current);

        // BFS of nodes and get the distances of each node
        while(queue.size() != 0) {
            current = queue.remove();
            for(int i = 0; i < adj.get(current).size(); i++) {
                int nextNode = adj.get(current).get(i);
                double currentDistance = distance[current] + adjWeights.get(current).get(i);
                if(currentDistance < distance[nextNode]) {
                    distance[nextNode] = currentDistance;
                    queue.add(nextNode);
                }
            }
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
     * Creates a deep copy of the given List
     * @param original The original list to copy
     * @return The deep copy of the list
     */
    private List<String> deepCopy(List<String> original) {
        List<String> copy = new LinkedList<String>();
        for(int i = 0 ; i < original.size(); i++) {
            copy.add(original.get(i));
        }
        return copy;
    }

    /**
     * Adds a single node to the graph
     */
    public void addNode(Node newNode) {
        storedNodes.add(newNode);
        adj.add(new LinkedList<Integer>());
        adjWeights.add(new LinkedList<>());
    }

    /**
     * Removes a node from the existing set of nodes
     * @param desiredNodeID is the ID of the desired ID to remove
     */
    public void removeNode(String desiredNodeID) {
        int storedNodesLength = storedNodes.size();
        for(int i = 0; i < storedNodesLength; i++) {
            removeBiEdge(desiredNodeID, mapIndexToNode(i).getNodeID());
        }
        int nodeIndex = mapNodeIDToIndex(desiredNodeID);
        adj.remove(nodeIndex);
        adjWeights.remove(nodeIndex);
        storedNodesLength--;

        for(int i = 0; i < storedNodesLength; i++) {
            List<Integer> adjList = adj.get(i);
            for(int j = 0; j < adjList.size(); j++) {
                if(adjList.get(j) > nodeIndex) {
                    adjList.set(j, adjList.get(j) - 1);
                }
            }
        }
    }


    /**
     * Adds an edge between two nodes and calculates wreight of edge
     * @param nodeID1 is the first node
     * @param nodeID2 is the second node to find the weight between
     */
    public void addEdge(String nodeID1, String nodeID2) {
        //check if nodes exist
        int node1Index = mapNodeIDToIndex(nodeID1);
        int node2Index = mapNodeIDToIndex(nodeID2);

        if(node1Index == -1 || node2Index == -1) {
            throw new IllegalArgumentException("Node does not exist");
        }

        Node node1 = storedNodes.get(node1Index);
        Node node2 = storedNodes.get(node2Index);
        //calculate weight
        double xWeight = abs(node1.getXcoord() - node2.getXcoord());
        double yWeight = abs(node1.getYcoord() - node2.getYcoord());

        double weight = sqrt((xWeight*xWeight) + (yWeight*yWeight));

        adj.get(node1Index).add(node2Index);
        adjWeights.get(node1Index).add(weight);
    }

    /**
     * Add a bidirectional edge between two nodes
     * @param ID1 Node ID 1
     * @param ID2 Node ID 2
     */
    public void addBiEdge(String ID1, String ID2) {
        addEdge(ID1, ID2);
        addEdge(ID2, ID1);
    }

    /**
     * Removed an edge between two nodes
     * @param nodeID1 is the first node to be checked
     * @param nodeID2 is the second node to be checked for an edge
     */
    public void removeEdge(String nodeID1, String nodeID2) {
        if(mapNodeIDToIndex(nodeID1) == mapNodeIDToIndex(nodeID2)) {
            return;
        }
        int edgeIndex = adj.get(mapNodeIDToIndex(nodeID1)).indexOf(mapNodeIDToIndex(nodeID2));
        if(edgeIndex >= 0) {
            //edgeNum--;  TODO: Fix error I do not know what this does or where it comes from
            adj.get(mapNodeIDToIndex(nodeID1)).remove(edgeIndex);
            adjWeights.get(mapNodeIDToIndex(nodeID1)).remove(edgeIndex);
        }
    }

    /**
     * Removes an edge that goes in two directions
     * @param nodeID1 is a node with a bidirectional edge
     * @param nodeID2 is the second node with that same bidirectional edge
     */
    public void removeBiEdge(String nodeID1, String nodeID2) {
        removeEdge(nodeID1, nodeID2);
        removeEdge(nodeID2, nodeID1);
    }

    /**
     * The getter for the edged
     * @param n a node index
     * @return the edged off that node
     */
    public LinkedList<Integer> getEdges(int n) {
        return adj.get(n);
    }

    /**
     * Finds the amount of edged off of a node
     * @param n a node index
     * @return the amount of edged off of a single node
     */
    public int degreeOf(int n) {
        return adj.get(n).size();
    }

    public String returnAngle(String ID1, String ID2) {

        String direction = "N";
        int node1Index = mapNodeIDToIndex(ID1);
        int node2Index = mapNodeIDToIndex(ID2);

        Node node1 = storedNodes.get(node1Index);
        Node node2 = storedNodes.get(node2Index);
        //calculate weight
        double xWeight = abs(node1.getXcoord() - node2.getXcoord());
        double yWeight = abs(node1.getYcoord() - node2.getYcoord());

        double angle = Math.atan2(yWeight, xWeight) * 180;
        System.out.println(angle);

        if (angle <= 15 || angle >= 345) {
            direction = "N";
        } else if (angle > 15 && angle <= 75) {
            direction = "NE";
        } else if (angle > 75 && angle <= 105) {
            direction = "E";
        } else if (angle > 105 && angle <= 165) {
            direction = "SE";
        } else if (angle > 165 && angle <= 195) {
            direction = "S";
        } else if (angle > 195 && angle <= 255) {
            direction = "SW";
        } else if (angle > 255 && angle <= 285) {
            direction = "W";
        } else if (angle > 285 && angle <= 345) {
            direction = "NW";
        }

        return direction;
    }

    public String textDirections(List<String> NodeIDS){
        String directions = "";
        String commaOrPeriod = ",";
        for(int i = 0; i < NodeIDS.size()-1; i++){
            if(i == NodeIDS.size()-2) {
                commaOrPeriod = ".";
            }
            else{
                commaOrPeriod = ", ";
            }
            int currentNodeIndex = mapNodeIDToIndex(NodeIDS.get(i));
            int nextNodeIndex = mapNodeIDToIndex(NodeIDS.get(i+1));
            //System.out.println(returnAngle(NodeIDS.get(i), NodeIDS.get(i+1)));
                    directions += returnAngle(NodeIDS.get(i), NodeIDS.get(i+1))
                    + " "
                    +  Math.round(adjWeights.get(currentNodeIndex).getFirst())
                    + " feet to "
                    + storedNodes.get(nextNodeIndex).getLongName()
                    + commaOrPeriod;
        }
        return directions;
    }

}
