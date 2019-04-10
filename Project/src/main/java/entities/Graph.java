package entities;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import static java.lang.Math.abs;
import static java.lang.Math.sqrt;


public abstract class Graph {
    
    protected LinkedList<LinkedList<Integer>> adj; //
    protected LinkedList<LinkedList<Double>> adjWeights; //weights of the edges
    protected LinkedList<Node> storedNodes; //nodes that have been stored


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
    public abstract List<String> shortestPath(String startID, String targetID);

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

    public BFSGraph toBFS() {
        BFSGraph returnValue = new BFSGraph(storedNodes);
        returnValue.adj = adj;
        returnValue.adjWeights = adjWeights;
        return returnValue;
    }

    public DFSGraph toDFS() {
        DFSGraph returnValue = new DFSGraph(storedNodes);
        returnValue.adj = adj;
        returnValue.adjWeights = adjWeights;
        return returnValue;
    }

    public AStarGraph toAStar() {
        AStarGraph returnValue = new AStarGraph(storedNodes);
        returnValue.adj = adj;
        returnValue.adjWeights = adjWeights;
        return returnValue;
    }
}
