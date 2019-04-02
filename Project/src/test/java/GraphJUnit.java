import junit.framework.TestCase;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class GraphJUnit extends TestCase {
    public void testShortestPath() {
        Node n1 = new Node("N1", 0, 0, "", "", "", "", "");
        Node n2 = new Node("N2", 1, 1, "", "", "", "", "");
        Node n3 = new Node("N3", 2, 2, "", "", "", "", "");
        Node n4 = new Node("N4", 4, 1, "", "", "", "", "");
        Node n5 = new Node("N5", 1, 4, "", "", "", "", "");
        Node n6 = new Node("N6", 3, 2, "", "", "", "", "");
        ArrayList<Node> nodes = new ArrayList<>();
        nodes.add(n1);
        nodes.add(n2);
        nodes.add(n3);
        nodes.add(n4);
        nodes.add(n5);
        nodes.add(n6);
        Graph g = new Graph(nodes);
        List<String> path = g.shortestPath("N1", "n2");
        List<String> expected = new LinkedList<>();
        expected.add("N1");
        expected.add("n2");
        assertTrue(path.equals(expected));
    }
}
