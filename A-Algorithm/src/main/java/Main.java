import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        System.out.println("Collaborator is " + "Jonathan");
        WeightedGraph wg = new WeightedGraph();
        wg.addNodes(10);
        wg.addBiEdge(0,1,3);
        wg.addBiEdge(1,2,5);
        wg.addBiEdge(1,3,7);
        wg.addBiEdge(2,5,3);
        wg.addBiEdge(2,6,8);
        wg.addBiEdge(3,4,2);
        wg.addBiEdge(3,5,2);
        wg.addBiEdge(3,7,5);
        wg.addBiEdge(3,8,3);
        wg.addBiEdge(4,8,1);
        wg.addBiEdge(5,9,3);
        wg.addBiEdge(6,7,9);
        wg.addBiEdge(7,8,5);
        wg.addBiEdge(8,9,2);
        ArrayList<Integer> path = wg.shortestPath(9, 6);
        System.out.println(path);
    }
}