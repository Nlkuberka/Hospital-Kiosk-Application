package entities;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import pathfinding.UIControllerPFM;

import java.util.*;

import static java.lang.Math.*;


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
     * Divides a path into several lists of nodes based on which floor they are located.
     * There is a list for each floor, containing the nodes in the path on that floor.
     * The nodes on a particular floor are further divided into lists based on where they appear in the path.
     * A set of nodes that appear consecutively in the path are stored in a bottom-level list together.
     * If the user has to leave a floor and come back to it later in the path,
     * then the nodes in this path would be stored in two bottom-level lists.
     * If the path never touches a certain floor, then the mid-level list for that floor would be empty,
     * but there is a mid-level list instantiated for every floor.
     * @param path a list of nodes
     * @return a list of lists of lists of node IDs
     */
    public List<List<List<Node>>> separatePathByFloor(List<String> path) {
        // a collection of lists for each floor
        List<List<List<Node>>> separatedPath = new ArrayList<>(UIControllerPFM.Floors.values().length);
        for(int i = 0; i < UIControllerPFM.Floors.values().length; i++) {
            separatedPath.add(new LinkedList<>());  // Create the mid-level list for each floor.
        }
        // Analyze the nodes in the order that they appear in the path, looking for when the path jumps between floors.
        UIControllerPFM.Floors previousFloor = null; // the floor of the node previously analyzed
        for(String nodeID : path) {
            Node node = storedNodes.get(mapNodeIDToIndex(nodeID));
            UIControllerPFM.Floors currentFloor = UIControllerPFM.Floors.getByID(node.getFloor());
            // If the current node is on a different floor than the previous node,
            // then put the current node in a different list.
            if(previousFloor != currentFloor) {
                separatedPath.get(currentFloor.ordinal()).add(new LinkedList<>());
            }
            ((LinkedList<List<Node>>) separatedPath.get(currentFloor.ordinal())).getLast().add(node);
            previousFloor = currentFloor;
        }
        return separatedPath;
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

    /**
     * Creates a graph with the same nodes and edges as the object this method is called on.
     * The new graph computes shortest paths using a breadth-first-search algorithm.
     */
    public BFSGraph toBFS() {
        BFSGraph returnValue = new BFSGraph(storedNodes);
        returnValue.adj = adj;
        returnValue.adjWeights = adjWeights;
        return returnValue;
    }
    /**
     * Creates a graph with the same nodes and edges as the object this method is called on.
     * The new graph computes shortest paths using a depth-first-search algorithm.
     */
    public DFSGraph toDFS() {
        DFSGraph returnValue = new DFSGraph(storedNodes);
        returnValue.adj = adj;
        returnValue.adjWeights = adjWeights;
        return returnValue;
    }
    /**
     * Deterines the angle of any edge and how to proceed through the path
     * @param ID1: the nodeID of the first node
     * @param ID2: the nodeID of the second node
     * @return a command for how to move in the path
     */
    public String returnAngle(String ID1, String ID2) {

        String cardinalDirection = "N";
        String direction = "";
        int node1Index = mapNodeIDToIndex(ID1);
        int node2Index = mapNodeIDToIndex(ID2);

        Node node1 = storedNodes.get(node1Index);
        Node node2 = storedNodes.get(node2Index);
        //calculate weight
        double xWeight = abs(node1.getXcoord() - node2.getXcoord());
        double yWeight = abs(node1.getYcoord() - node2.getYcoord());

        //converts angle to degrees
        double angle = Math.atan2(yWeight, xWeight) * 180;
        //System.out.println(angle);

        //splits cartesian plane into 8 sections
        if (angle <= 15 || angle >= 345) {
            cardinalDirection = "N";
        } else if (angle > 15 && angle <= 75) {
            cardinalDirection = "NE";
        } else if (angle > 75 && angle <= 105) {
            cardinalDirection = "E";
        } else if (angle > 105 && angle <= 165) {
            cardinalDirection = "SE";
        } else if (angle > 165 && angle <= 195) {
            cardinalDirection = "S";
        } else if (angle > 195 && angle <= 255) {
            cardinalDirection = "SW";
        } else if (angle > 255 && angle <= 285) {
            cardinalDirection = "W";
        } else if (angle > 285 && angle <= 345) {
            cardinalDirection = "NW";
        }
        return cardinalDirection;
    }

    /**
     * Returns the actual movement a user should take to move towards their destination
     * @param cardinalDirection the current cardinal direction the user takes
     * @param pastDirection the past cardinal direction the user took
     * @return a descriptive way to move from one point to another
     */
    public String returnDirection(String cardinalDirection, String pastDirection){

        String direction = "BLAHHHHHH";

        if(cardinalDirection.equals("")){
            cardinalDirection = "N";
        }

        switch (cardinalDirection) {
            case ("N"):
                if (pastDirection.equals("N")) {
                    direction = "Go straight for";
                } else if (pastDirection.equals("S")) {
                    direction = "Turn around and go straight for";
                } else if (pastDirection.equals("E")) {
                    direction = "Turn right and go";
                } else if (pastDirection.equals("W")) {
                    direction = "Turn left and go";
                } else if (pastDirection.equals("NW")) {
                    direction = "Veer right and go";
                } else if (pastDirection.equals("NE")) {
                    direction = "Veer left and go";
                } else if (pastDirection.equals("SE")) {
                    direction = "Turn around, veer left and go";
                } else if (pastDirection.equals("SW")) {
                    direction = "Turn around, veer right and go";
                }
                return direction;
            //break;
            case ("E"):
                if (pastDirection.equals("E")) {
                    direction = "Go straight for";
                } else if (pastDirection.equals("W")) {
                    direction = "Turn around and go straight for";
                } else if (pastDirection.equals("S")) {
                    direction = "Turn right and go";
                } else if (pastDirection.equals("N")) {
                    direction = "Turn left and go";
                } else if (pastDirection.equals("SE")) {
                    direction = "Veer right and go";
                } else if (pastDirection.equals("NE")) {
                    direction = "Veer left and go";
                } else if (pastDirection.equals("SW")) {
                    direction = "Turn around, veer left and go";
                } else if (pastDirection.equals("NW")) {
                    direction = "Turn around, veer right and go";
                }
                return direction;
            //break;
            case ("S"):
                if (pastDirection.equals("S")) {
                    direction = "Go straight for";
                } else if (pastDirection.equals("N")) {
                    direction = "Turn around and go straight for";
                } else if (pastDirection.equals("W")) {
                    direction = "Turn right and go";
                } else if (pastDirection.equals("E")) {
                    direction = "Turn left and go";
                } else if (pastDirection.equals("SW")) {
                    direction = "Veer right and go";
                } else if (pastDirection.equals("SE")) {
                    direction = "Veer left and go";
                } else if (pastDirection.equals("NW")) {
                    direction = "Turn around, veer left and go";
                } else if (pastDirection.equals("NE")) {
                    direction = "Turn around, veer right and go";
                }
                return direction;
            //break;
            case ("W"):
                if (pastDirection.equals("W")) {
                    direction = "Go straight for";
                } else if (pastDirection.equals("E")) {
                    direction = "Turn around and go straight for";
                } else if (pastDirection.equals("N")) {
                    direction = "Turn right and go";
                } else if (pastDirection.equals("S")) {
                    direction = "Turn left and go";
                } else if (pastDirection.equals("NW")) {
                    direction = "Veer right and go";
                } else if (pastDirection.equals("SW")) {
                    direction = "Veer left and go";
                } else if (pastDirection.equals("NE")) {
                    direction = "Turn around, veer left and go";
                } else if (pastDirection.equals("SE")) {
                    direction = "Turn around, veer right and go";
                }
                return direction;
            //break;
            case ("NE"):
                if (pastDirection.equals("NE")) {
                    direction = "Go straight for";
                } else if (pastDirection.equals("SW")) {
                    direction = "Turn around and go straight for";
                } else if (pastDirection.equals("SE")) {
                    direction = "Turn right and go";
                } else if (pastDirection.equals("NW")) {
                    direction = "Turn left and go";
                } else if (pastDirection.equals("E")) {
                    direction = "Veer right and go";
                } else if (pastDirection.equals("N")) {
                    direction = "Veer left and go";
                } else if (pastDirection.equals("S")) {
                    direction = "Turn around, veer left and go";
                } else if (pastDirection.equals("W")) {
                    direction = "Turn around, veer right and go";
                }
                return direction;
            //break;
            case ("NW"):
                if (pastDirection.equals("NW")) {
                    direction = "Go straight for";
                } else if (pastDirection.equals("SE")) {
                    direction = "Turn around and go straight for";
                } else if (pastDirection.equals("SW")) {
                    direction = "Turn right and go";
                } else if (pastDirection.equals("NE")) {
                    direction = "Turn left and go";
                } else if (pastDirection.equals("N")) {
                    direction = "Veer right and go";
                } else if (pastDirection.equals("W")) {
                    direction = "Veer left and go";
                } else if (pastDirection.equals("E")) {
                    direction = "Turn around, veer left and go";
                } else if (pastDirection.equals("S")) {
                    direction = "Turn around, veer right and go";
                }
                return direction;
            //break;
            case ("SE"):
                if (pastDirection.equals("SE")) {
                    direction = "Go straight for";
                } else if (pastDirection.equals("NW")) {
                    direction = "Turn around and go straight for";
                } else if (pastDirection.equals("SW")) {
                    direction = "Turn right and go";
                } else if (pastDirection.equals("NE")) {
                    direction = "Turn left and go";
                } else if (pastDirection.equals("S")) {
                    direction = "Veer right and go";
                } else if (pastDirection.equals("E")) {
                    direction = "Veer left and go";
                } else if (pastDirection.equals("W")) {
                    direction = "Turn around, veer left and go";
                } else if (pastDirection.equals("N")) {
                    direction = "Turn around, veer right and go";
                }
                return direction;
            //break;
            case ("SW"):
                if (pastDirection.equals("SW")) {
                    direction = "Go straight for";
                } else if (pastDirection.equals("NE")) {
                    direction = "Turn around and go straight for";
                } else if (pastDirection.equals("NW")) {
                    direction = "Turn right and go";
                } else if (pastDirection.equals("SE")) {
                    direction = "Turn left and go";
                } else if (pastDirection.equals("W")) {
                    direction = "Veer right and go";
                } else if (pastDirection.equals("S")) {
                    direction = "Veer left and go";
                } else if (pastDirection.equals("N")) {
                    direction = "Turn around, veer left and go";
                } else if (pastDirection.equals("E")) {
                    direction = "Turn around, veer right and go";
                }
                return direction;
        }
        return direction;
    }

    /**
     * Prints directions to every node in a path
     * @param NodeIDS the path generated from shortestPath
     * @return text based directions directing a reader from one point to another
     */
    public String textDirections(List<String> NodeIDS){
        String directions = "";
        String commaOrPeriod;
        String currentDirection;

        for(int i = 0; i < NodeIDS.size()-1; i++){
            String pastDirection = returnAngle(NodeIDS.get(i), NodeIDS.get(i+1));
            if(i <= NodeIDS.size() - 3){
                currentDirection = returnAngle(NodeIDS.get(i+1), NodeIDS.get(i+2));
            } else {
                currentDirection = pastDirection;
            }

            if(i == NodeIDS.size()-2) {
                commaOrPeriod = ".";
            }
            else{
                commaOrPeriod = ", ";
            }
            int currentNodeIndex = mapNodeIDToIndex(NodeIDS.get(i));
            int nextNodeIndex = mapNodeIDToIndex(NodeIDS.get(i+1));
            //System.out.println(returnAngle(NodeIDS.get(i), NodeIDS.get(i+1), directions));
                    directions += returnDirection(currentDirection, pastDirection)
                    + " "
                    +  Math.round(adjWeights.get(currentNodeIndex).getFirst())
                    + " pixels to "
                    + storedNodes.get(nextNodeIndex).getLongName()
                    + commaOrPeriod;
        }
        return directions;
    }

    /**
     * Creates a graph with the same nodes and edges as the object this method is called on.
     * The new graph computes shortest paths using an A* algorithm.
     */
    public AStarGraph toAStar() {
        AStarGraph returnValue = new AStarGraph(storedNodes);
        returnValue.adj = adj;
        returnValue.adjWeights = adjWeights;
        return returnValue;
    }
}
