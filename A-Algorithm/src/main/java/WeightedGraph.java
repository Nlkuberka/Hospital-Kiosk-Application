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

    private ArrayList<Integer> shortestPathSub(int current, int target, int previous, ArrayList<Integer> nodes) {
        if(current == target) {
            return nodes;
        }
        boolean passedOn = false;
        ArrayList<ArrayList<Integer>> possiblePaths = new ArrayList<ArrayList<Integer>>();
        ArrayList<Integer> nodeEdges = adj.get(current);
        for(int i = 0; i < nodeEdges.size(); i++) {
            if(nodeEdges.get(i) != previous) {
                passedOn = true;
                possiblePaths.add(deepCopy(nodes));
                possiblePaths.get(i).get(0) += adjWeights.get(current).get(i);
                possiblePaths.get(i).add(nodeEdges.get(i));
                shortestPathSub(nodeEdges.get(i), target, current, possiblePaths.get(i));
            }
        }

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
    public void addEdge(int n1, int n2, int weight) {
        edgeNum++;
        adj.get(n1).add(n2);
        adjWeights.get(n1).add(weight);
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
