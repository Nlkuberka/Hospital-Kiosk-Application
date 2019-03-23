import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;

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
        int current = start;
        int[] distance = new int[nodeNum];
        boolean[] marked = new boolean[nodeNum];
        Queue<Integer> queue = new LinkedList<Integer>();
        ArrayList<ArrayList<Integer>> paths = new ArrayList<ArrayList<Integer>>();
        for(int i = 0; i < nodeNum; i++) {
            paths.add(new ArrayList<Integer>());
            distance[i] = Integer.MAX_VALUE;
        }
        distance[start] = 0;
        paths.get(start).add(start);
        queue.add(current);

        while(queue.size() != 0) {
            current = queue.remove();
            for(int i = 0; i < adj.get(current).size(); i++) {
                int nextNode = adj.get(current).get(i);
                int currentDistance = distance[current] + adjWeights.get(current).get(i);
                if(currentDistance < distance[nextNode]) {
                    distance[nextNode] = currentDistance;
                    ArrayList<Integer> newPath = deepCopy(paths.get(current));
                    newPath.add(nextNode);
                    paths.set(nextNode, newPath);
                }
                if(!marked[nextNode]) {
                    queue.add(nextNode);
                    marked[nextNode] = true;
                }
            }
            System.out.println("P - " + paths);
            System.out.println("D - " + Arrays.toString(distance));
        }
        paths.get(target).add(0, distance[target]);
        return paths.get(target);
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
