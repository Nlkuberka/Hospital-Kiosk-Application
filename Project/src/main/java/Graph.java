import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import static java.lang.Math.abs;
import static java.lang.Math.sqrt;


public class Graph {
    
    private ArrayList<ArrayList<Integer>> adj;
    private ArrayList<ArrayList<Integer>> adjWeights;
    private ArrayList<Boolean> marked;
    private ArrayList<Node> storedNodes; //can produce the index
    //store map of short name to userid


    /**
     * Constructor
     * Initalizes the graph with the given number of nodes
     */
    public Graph() {
        if(storedNodes.size() < 0) {
            throw new IllegalArgumentException("Number of nodes must be greater than or equal to 0");
        }
        //this.storedNodes.size() = storedNodes.size();
        this.marked = new ArrayList<Boolean>(storedNodes.size());
        this.adj = new ArrayList<ArrayList<Integer>>();
        this.adjWeights = new ArrayList<ArrayList<Integer>>();
        for(int i = 0; i < storedNodes.size(); i++) {
            this.adj.add(new ArrayList<Integer>());
            this.adjWeights.add(new ArrayList<Integer>());
        }
    }

    /**
     *
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

    public int mapNodeIDToIndex(String desiredNodeID){
        for(Node n: storedNodes){
            if(n.getNodeID().equals(desiredNodeID)) {
                return storedNodes.indexOf(n);
            }
        }
        return -1;
    }

    /**
     *
     * @param desiredIndex the location of the desired node
     * @return the node at the desired index
     */
    public Node mapIndexToNode(int desiredIndex) {
        return storedNodes.get(desiredIndex);
    }


    public List<String> shortestPath(String startID, String targetID) {
        int startIndex = mapNodeIDToIndex(startID);
        int targetIndex = mapNodeIDToIndex(targetID);
        int current = startIndex;
        double[] distance = new double[storedNodes.size()]; //stored distance from start node to node at index
        boolean[] marked = new boolean[storedNodes.size()]; //lets know if have taken look at node/queued nodes it is connected to
        Queue<Integer> queue = new LinkedList<Integer>(); //nodes that will be checked
        ArrayList<List<String>> paths = new ArrayList<List<String>>(); //list of paths from the start node to the particular node
        for(int i = 0; i < storedNodes.size(); i++) {
            paths.add(new ArrayList<String>());
            distance[i] = Double.MAX_VALUE;
        }
        distance[startIndex] = 0;
        paths.get(startIndex).add(startID);
        queue.add(current);

        // BFS of nodes and get the distances of each node
        while(queue.size() != 0) {
            current = queue.remove();
            for(int i = 0; i < adj.get(current).size(); i++) {
                int nextNode = adj.get(current).get(i);
                double currentDistance = distance[current] + adjWeights.get(current).get(i);
                if(currentDistance < distance[nextNode]) {
                    distance[nextNode] = currentDistance;
                    List<String> newPath = deepCopy(paths.get(current));

                    newPath.add(mapIndexToNode(nextNode).getNodeID());
                    paths.set(nextNode, newPath);
                }
                if(!marked[nextNode]) {
                    queue.add(nextNode);
                    marked[nextNode] = true;
                }
            }
            //System.out.println("P - " + paths);
            //System.out.println("D - " + Arrays.toString(distance));
        }
        //paths.get(targetIndex).add(0, distance[targetIndex]);
        return paths.get(targetIndex);
    }

    /**
     * Creates a deep copy of the given List
     * @param original The original list to copy
     * @return The deep copy of the list
     */
    private List<String> deepCopy(List<String> original) {
        List<String> copy = new ArrayList<String>();
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
        adj.add(new ArrayList<Integer>());
        adjWeights.add(new ArrayList<Integer>());
    }

    public void removeNode(String desiredNodeID) {
        int storedNodesLength = storedNodes.size();
        for(int i = 0; i < storedNodes.size(); i++) {
            removeBiEdge(desiredNodeID, mapIndexToNode(i).getNodeID());
        }
        int nodeIndex = mapNodeIDToIndex(desiredNodeID);
        adj.remove(nodeIndex);
        adjWeights.remove(nodeIndex);
        storedNodesLength--;

        for(int i = 0; i < storedNodes.size(); i++) {
            List<Integer> adjList = adj.get(i);
            for(int j = 0; j < adjList.size(); j++) {
                if(adjList.get(j) > nodeIndex) {
                    adjList.set(j, adjList.get(j) - 1);
                }
            }
        }
    }


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

        int weight = (int) sqrt(Math.pow(xWeight, 2) + Math.pow(yWeight, 2));

        adj.get(node1Index).add(node2Index);
        adjWeights.get(node1Index).add(weight);
    }


   /* public void addBiEdge(int n1, int n2, int weight) {
        addEdge(n1, n2, weight);
        addEdge(n2, n1, weight);
    }*/

    public void removeEdge(String nodeID1, String nodeID2) {
        if(mapNodeIDToIndex(nodeID1) == mapNodeIDToIndex(nodeID2)) {
            return;
        }
        int edgeIndex = adj.get(mapNodeIDToIndex(nodeID1)).indexOf(mapNodeIDToIndex(nodeID2));
        if(edgeIndex >= 0) {
            //edgeNum--;  //TODO: Fix error I do not know what this does or where it comes from
            adj.get(mapNodeIDToIndex(nodeID1)).remove(edgeIndex);
            adjWeights.get(mapNodeIDToIndex(nodeID1)).remove(edgeIndex);
        }
    }

    public void removeBiEdge(String nodeID1, String nodeID2) {
        removeEdge(nodeID1, nodeID2);
        removeEdge(nodeID2, nodeID1);
    }

    public ArrayList<Integer> getEdges(int n) {
        return adj.get(n);
    }

    public int degreeOf(int n) {
        return adj.get(n).size();
    }


}
