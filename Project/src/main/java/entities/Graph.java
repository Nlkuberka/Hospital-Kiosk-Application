package entities;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import database.DBControllerNE;
import pathfinding.UIControllerPFM;
import static java.lang.Math.*;

public abstract class Graph {
    
    protected LinkedList<LinkedList<Integer>> adj; // adjacency list
    protected LinkedList<LinkedList<Double>> adjWeights; //weights of the edges
    protected LinkedList<String> nodeIDs; //nodes that have been stored


    /**
     * Constructor
     * Initalizes the graph with the given number of nodes
     */
    protected Graph() {
        this.adj = new LinkedList<>();
        this.adjWeights = new LinkedList<>();
        this.nodeIDs = new LinkedList<>();
    }

    private static class GraphGetter {
        private static Graph graph = new DFSGraph();
    }

    /**
     * Gets the graph from the singleton design pattern.
     * @return the graph for the hospital
     */
    public static Graph getGraph() {
        return GraphGetter.graph;
    }

    /**
     * Replaces the graph with a new graph that computes shortest path by A* algorithm.
     */
    public static void toAStar() {
        Graph newGraph = new AStarGraph();
        newGraph.adj = GraphGetter.graph.adj;
        newGraph.adjWeights = GraphGetter.graph.adjWeights;
        newGraph.nodeIDs = GraphGetter.graph.nodeIDs;
        GraphGetter.graph = newGraph;
    }

    /**
     * Replaces the graph with a new graph that computes shortest path by breadth first search algorithm.
     */
    public static void toBFS() {
        Graph newGraph = new BFSGraph();
        newGraph.adj = GraphGetter.graph.adj;
        newGraph.adjWeights = GraphGetter.graph.adjWeights;
        newGraph.nodeIDs = GraphGetter.graph.nodeIDs;
        GraphGetter.graph = newGraph;
    }

    /**
     * Replaces the graph with a new graph that computes shortest path by depth first search algorithm.
     */
    public static void toDFS() {
        Graph newGraph = new DFSGraph();
        newGraph.adj = GraphGetter.graph.adj;
        newGraph.adjWeights = GraphGetter.graph.adjWeights;
        newGraph.nodeIDs = GraphGetter.graph.nodeIDs;
        GraphGetter.graph = newGraph;
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
     * fetchNode
     *
     * generates an node object from data under given ID
     *
     * @param nodeID the ID of the desired node
     * @return the node object that contains nodeID
     */
    protected Node fetchNode(String nodeID) {
        Connection conn = DBControllerNE.dbConnect();
        Node n = DBControllerNE.fetchNode(nodeID, conn);
        DBControllerNE.closeConnection(conn);
        return n;
    }

    /**
     * Maps a node to its index
     * @param desiredNode the node the user is looking to find the index of
     * @return the index of the desired node or -1 for failure
     */
    public int mapNodeToIndex(Node desiredNode){
        int index = 0;
        for(String id: nodeIDs){
            if(desiredNode.getNodeID().equals(id)) {
                return index;
            }
            index++;
        }
        return -1;
    }

    /**
     * Maps the ID of a node to its index
     * @param desiredNodeID the ID of the node the user is looking to find
     * @return the index of the node in the LinkedList or -1 for failure
     */
    public int mapNodeIDToIndex(String desiredNodeID){
        int index = 0;
        for(String id: nodeIDs){
            if(id.equals(desiredNodeID)) {
                return index;
            }
            index++;
        }
        return -1;
    }

    /**
     * Maps the index of a node to the actual node
     * @param desiredIndex the location of the desired node
     * @return the node at the desired index
     */
    public Node mapIndexToNode(int desiredIndex) {
        return fetchNode(nodeIDs.get(desiredIndex));
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
        double [] distance = new double [nodeIDs.size()]; // distance of shortest known path from start to all nodes
        for(int i = 0; i < nodeIDs.size(); i++) {
            distance[i] = Double.MAX_VALUE;
        }
        distance[startIndex] = 0;
        initialize();
        addNodeToRelax(startIndex, distance[startIndex], targetIndex);

        // Search nodes and get the distances of the shortest path from start to each node.
        while(!finishedSearch()) {
            int current = getNodeToRelax();
            for(int i = 0; i < adj.get(current).size(); i++) {
                int nextNode = adj.get(current).get(i);
                double currentDistance = distance[current] + adjWeights.get(current).get(i);
                if(currentDistance < distance[nextNode]) {
                    distance[nextNode] = currentDistance;
                    addNodeToRelax(nextNode, distance[nextNode], targetIndex);
                }
            }
        }
        if(distance[targetIndex] == Double.MAX_VALUE) { // if there is no path from start to target
            return null;
        }
        // Backtrack from target to start to find the shortest path from start to target.
        List<String> path = new LinkedList<>();
        path.add(targetID);
        int current = targetIndex;
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
     * Performs any operations needed before beginning the search.
     */
    protected  abstract void initialize();

    /**
     * Adds a node to a set of nodes that need to be relaxed.
     * @param node the node to be added
     * @param distanceFromStart the distance of the shortest known path from start to the node to be added
     * @param targetIndex the index in adj of the target node
     */
    protected abstract void addNodeToRelax(int node, double distanceFromStart, int targetIndex);

    /**
     * Removes a node from a set of nodes that need to be relaxed, and returns it.
     * @return the node to be relaxed
     */
    protected abstract int getNodeToRelax();

    /**
     * Checks if the algorithm has already found the shortest path from start to target.
     * @return true if the shortest path has been found, false otherwise
     */
    protected abstract boolean finishedSearch();

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
            Node node = fetchNode(nodeID);
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
     * Adds a single node to the graph
     */
    public void addNode(Node newNode) {
        if(mapNodeIDToIndex(newNode.getNodeID()) == -1) {
            nodeIDs.add(newNode.getNodeID());
            adj.add(new LinkedList<Integer>());
            adjWeights.add(new LinkedList<>());
        }
    }

    /**
     * Removes a node from the existing set of nodes
     * @param desiredNodeID is the ID of the desired ID to remove
     */
    public void removeNode(String desiredNodeID) {
        int nodeIndex = mapNodeIDToIndex(desiredNodeID);
        if(nodeIndex == -1) {
            return;
        }
        for(String nodeID : nodeIDs) {
            removeBiEdge(desiredNodeID, nodeID);
        }
        adj.remove(nodeIndex);
        adjWeights.remove(nodeIndex);
        nodeIDs.remove(nodeIndex);

        for(List<Integer> adjList : adj) {
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

        Node node1 = mapIndexToNode(node1Index);
        Node node2 = mapIndexToNode(node2Index);
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
        int nodeIndex1 = mapNodeIDToIndex(nodeID1);
        int nodeIndex2 = mapNodeIDToIndex(nodeID2);
        if(nodeIndex1 == nodeIndex2 || nodeIndex1 == -1 || nodeIndex2 == -1) {
            return;
        }
        int edgeIndex = adj.get(nodeIndex1).indexOf(nodeIndex2);
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
     * Deterines the angle of any edge
     * @param ID1: the nodeID of the first node
     * @param ID2: the nodeID of the second node
     * @return the cardinal direction of the edge
     */
    public String returnAngle(String ID1, String ID2, String pastDirection) {

        String direction = "N";
        int node1Index = mapNodeIDToIndex(ID1);
        int node2Index = mapNodeIDToIndex(ID2);

        Node node1 = mapIndexToNode(node1Index);
        Node node2 = mapIndexToNode(node2Index);
        //calculate weight
        double xWeight = abs(node1.getXcoord() - node2.getXcoord());
        double yWeight = abs(node1.getYcoord() - node2.getYcoord());

        double angle = Math.atan2(yWeight, xWeight) * 180;
        //System.out.println(angle);

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

        switch (direction) {
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
                } else if(pastDirection.equals("SE")) {
                    direction = "Turn around, veer left and go";
                } else if(pastDirection.equals("SW")) {
                    direction = "Turn around, veer right and go";
                }
                break;
            case ("E"):
            case ("S"):
            case ("W"):
            case ("NE"):
            case ("NW"):
            case ("SE"):
            case ("SW"):



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
                    directions += returnAngle(NodeIDS.get(i), NodeIDS.get(i+1), directions)
                    + " "
                    +  Math.round(adjWeights.get(currentNodeIndex).getFirst())
                    + " pixels to "
                    + mapIndexToNode(nextNodeIndex).getLongName()
                    + commaOrPeriod;
        }
        return directions;
    }
}
