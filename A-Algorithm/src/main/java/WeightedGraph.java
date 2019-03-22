import java.util.ArrayList;

public class WeightedGraph {
    private int nodeNum;
    private int edgeNum;
    private ArrayList<ArrayList<Integer>> adj;
    private ArrayList<ArrayList<Integer>> adjWeights;
    private ArrayList<Boolean> marked;

    public WeightedGraph() {
        this(0);
    }

    public WeightedGraph(int nodeNum) {
        if(nodeNum < 0) {
            throw new IllegalArgumentException("Number of nodes must be greater than or equal to 0");
        }
        this.nodeNum = nodeNum;
        this.edgeNum = 0;
        this.marked = new ArrayList<Boolean>(nodeNum);
        this.adj = new ArrayList<ArrayList<Integer>>();
        this.adjWeights = new ArrayList<ArrayList<Integer>>();
        for(int i = 0; i < nodeNum; i++) {
            this.adj.add(new ArrayList<Integer>());
            this.adjWeights.add(new ArrayList<Integer>());
        }
    }

    public ArrayList<Integer> shortestPath(int start, int target) {
        ArrayList<Integer> nodes = new ArrayList<Integer>();
        nodes.add(0);
        nodes.add(start);
        return shortestPathSub(start, target, nodes);
    }

    private ArrayList<Integer> shortestPathSub(int current, int target, ArrayList<Integer> nodes) {
        if(current == target) {
            return nodes;
        }

        boolean passedOn = false;
        int currentPath = 0;
        ArrayList<ArrayList<Integer>> possiblePaths = new ArrayList<ArrayList<Integer>>();
        ArrayList<Integer> nodeEdges = adj.get(current);
        for(int i = 0; i < nodeEdges.size(); i++) {
            if(nodes.indexOf((nodeEdges.get(i))) == -1) {
                passedOn = true;
                possiblePaths.add(deepCopy(nodes));
                possiblePaths.get(currentPath).set(0, possiblePaths.get(currentPath).get(0) + adjWeights.get(current).get(i));
                possiblePaths.get(currentPath).add(nodeEdges.get(i));
                possiblePaths.set(currentPath, shortestPathSub(nodeEdges.get(i), target, possiblePaths.get(currentPath)));
                currentPath++;
            }
        }

        System.out.println(current + " - " + possiblePaths);

        if(!passedOn) {
            nodes.set(0, Integer.MAX_VALUE);
            return nodes;
        }

        int shortestPath = 0;
        for(int i = 1; i < possiblePaths.size(); i++) {
            if(possiblePaths.get(i).get(0) < possiblePaths.get(shortestPath).get(0)) {
                shortestPath = i;
            }
        }

        return possiblePaths.get(shortestPath);
    }

    private ArrayList<Integer> deepCopy(ArrayList<Integer> original) {
        ArrayList<Integer> copy = new ArrayList<Integer>();
        for(int i = 0 ; i < original.size(); i++) {
            copy.add(original.get(i));
        }
        return copy;
    }

    public void addNode() {
        addNodes(1);
    }

    public void addNodes(int num) {
        for(int i = 0; i < num; i++) {
            nodeNum++;
            adj.add(new ArrayList<Integer>());
            adjWeights.add(new ArrayList<Integer>());
        }
    }

    public void addEdge(int n1, int n2, int weight) {
        edgeNum++;
        adj.get(n1).add(n2);
        adjWeights.get(n1).add(weight);
    }

    public void addBiEdge(int n1, int n2, int weight) {
        edgeNum++;
        edgeNum++;
        adj.get(n1).add(n2);
        adj.get(n2).add(n1);
        adjWeights.get(n1).add(weight);
        adjWeights.get(n2).add(weight);
    }


    public ArrayList<Integer> getEdges(int n) {
        return adj.get(n);
    }

    public int degreeOf(int n) {
        return adj.get(n).size();
    }

    public int getNodeNum() {
        return nodeNum;
    }

    public int getEdgeNum() {
        return edgeNum;
    }
}
