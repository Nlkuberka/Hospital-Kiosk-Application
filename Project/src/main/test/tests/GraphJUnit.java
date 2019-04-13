
package tests;


import entities.*;
import junit.framework.TestCase;
import org.junit.Test;
import pathfinding.UIControllerPFM;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static javax.swing.text.html.HTML.Tag.HEAD;

public class GraphJUnit extends TestCase {
    public void testShortestPath() {
        Node n0 = new Node("N0", 0, 0, "", "", "", "", "");
        Node n1 = new Node("N1", 1, 1, "", "", "", "", "");
        Node n2 = new Node("N2", 2, 2, "", "", "", "", "");
        Node n3 = new Node("N3", 4, 1, "", "", "", "", "");
        Node n4 = new Node("N4", 1, 4, "", "", "", "", "");
        Node n5 = new Node("N5", 3, 2, "", "", "", "", "");
        Node n6 = new Node("N6", 1, 0, "", "", "", "", "");
        Node n7 = new Node("N7", 2, 0, "", "", "", "", "");
        Node n8 = new Node("N8", 3, 0, "", "", "", "", "");
        Node n9 = new Node("N9", 4, 0, "", "", "", "", "");
        LinkedList<Node> nodes = new LinkedList<>();
        nodes.add(n0);
        nodes.add(n1);
        nodes.add(n2);
        nodes.add(n3);
        nodes.add(n4);
        nodes.add(n5);
        nodes.add(n6);
        nodes.add(n7);
        nodes.add(n8);
        nodes.add(n9);
        Graph g = new BFSGraph(nodes);
        g.addBiEdge("N0", "N1");
        g.addBiEdge("N1", "N2");
        g.addBiEdge("N1", "N4");
        g.addBiEdge("N2", "N4");
        g.addBiEdge("N2", "N5");
        g.addBiEdge("N3", "N5");
        g.addBiEdge("N0", "N6");
        g.addBiEdge("N6", "N7");
        g.addBiEdge("N7", "N8");
        g.addBiEdge("N8", "N9");
        g.addBiEdge("N9", "N3");
        List<String> path = g.shortestPath("N0", "N3");
        //g.checkEdges();
        List<String> expected = new LinkedList<>();
        expected.add("N0");
        expected.add("N6");
        expected.add("N7");
        expected.add("N8");
        expected.add("N9");
        expected.add("N3");
        assertEquals(expected, path);
        g = g.toDFS();
        path = g.shortestPath("N0", "N3");
        assertEquals(expected, path);
        g = g.toAStar();
        path = g.shortestPath("N0", "N3");
        assertEquals(expected, path);
    }

    public void testRealNodes() {
        Node n0 = new Node("N0", 1580, 2538, "", "", "", "", "");
        Node n1 = new Node("N1", 1395, 2674, "", "", "", "", "");
        Node n2 = new Node("N2", 1532, 2777, "", "", "", "", "");
        Node n3 = new Node("N3", 1591, 2560, "", "", "", "", "");
        Node n4 = new Node("N4", 1590, 2604, "", "", "", "", "");
        Node n5 = new Node("N5", 1590, 2745, "", "", "", "", "");
        LinkedList<Node> nodes = new LinkedList<>();
        nodes.add(n0);
        nodes.add(n1);
        nodes.add(n2);
        nodes.add(n3);
        nodes.add(n4);
        nodes.add(n5);
        Graph g = new BFSGraph(nodes);
        g.addBiEdge("N0", "N1");
        g.addBiEdge("N1", "N5");
        g.addBiEdge("N3", "N4");
        g.addBiEdge("N2", "N3");
        g.addBiEdge("N4", "N1");
        g.addBiEdge("N3", "N0");
        List<String> path = g.shortestPath("N5", "N0");
        List<String> expected = new LinkedList<>();
        expected.add("N5");
        expected.add("N1");
        expected.add("N0");
        assertEquals(expected, path);
        g = g.toDFS();
        path = g.shortestPath("N5", "N0");
        assertEquals(expected, path);
        g = g.toAStar();
        path = g.shortestPath("N5", "N0");
        assertEquals(expected, path);
    }

    @Test
    public void testDistanceReplacement() {
        Node n0 = new Node("N0", 0, 0, "", "", "", "", "");
        Node n1 = new Node("N1", 1, 0, "", "", "", "", "");
        Node n2 = new Node("N2", 2, 0, "", "", "", "", "");
        Node n3 = new Node("N3", 3, 0, "", "", "", "", "");
        Node n4 = new Node("N4", 4, 0, "", "", "", "", "");
        Node n5 = new Node("N5", 5, 0, "", "", "", "", "");
        Node n6 = new Node("N6", 6, 0, "", "", "", "", "");
        Node n7 = new Node("N7", 3, 1, "", "", "", "", "");
        LinkedList<Node> nodes = new LinkedList<>();
        nodes.add(n0);
        nodes.add(n1);
        nodes.add(n2);
        nodes.add(n3);
        nodes.add(n4);
        nodes.add(n5);
        nodes.add(n6);
        nodes.add(n7);
        Graph g = new AStarGraph(nodes);
        g.addBiEdge("N0", "N1");
        g.addBiEdge("N1", "N2");
        g.addBiEdge("N2", "N3");
        g.addBiEdge("N3", "N4");
        g.addBiEdge("N4", "N5");
        g.addBiEdge("N5", "N6");
        g.addBiEdge("N1", "N7");
        g.addBiEdge("N7", "N5");
        List<String> expected = new LinkedList<>();
        expected.add("N0");
        expected.add("N1");
        expected.add("N2");
        expected.add("N3");
        expected.add("N4");
        expected.add("N5");
        expected.add("N6");
        List<String> actual = g.shortestPath("N0", "N6");
        assertEquals(expected, actual);
        g = g.toBFS();
        actual = g.shortestPath("N0", "N6");
        assertEquals(expected, actual);
        g = g.toDFS();
        actual = g.shortestPath("N0", "N6");
        assertEquals(expected, actual);
    }

    public void testDisconnectedGraph() {
        LinkedList<Node> nodes = new LinkedList<>();
        Node n0 = new Node("N0", 0, 0, "", "", "", "", "");
        Node n1 = new Node("N1", 0, 1, "", "", "", "", "");
        Node n2 = new Node("N2", 1, 0, "", "", "", "", "");
        Node n3 = new Node("N3", 1, 1, "", "", "", "", "");
        Node n4 = new Node("N4", 5, 0, "", "", "", "", "");
        Node n5 = new Node("N5", 0, 5, "", "", "", "", "");
        Node n6 = new Node("N6", 5, 5, "", "", "", "", "");
        nodes.add(n0);
        nodes.add(n1);
        nodes.add(n2);
        nodes.add(n3);
        nodes.add(n4);
        nodes.add(n5);
        nodes.add(n6);
        Graph g = new DFSGraph(nodes);
        g.addBiEdge("N0", "N1");
        g.addBiEdge("N1", "N2");
        g.addBiEdge("N2", "N3");
        g.addBiEdge("N3", "N0");
        g.addBiEdge("N4", "N5");
        g.addBiEdge("N5", "N6");
        g.addBiEdge("N6", "N4");
        List<String> expected = new LinkedList<>();
        expected.add("N0");
        expected.add("N1");
        List<String> actual = g.shortestPath("N0", "N1");
        assertEquals(expected, actual);
        g = g.toBFS();
        actual = g.shortestPath("N0", "N1");
        assertEquals(expected, actual);
        g = g.toAStar();
        actual = g.shortestPath("N0", "N1");
        assertEquals(expected, actual);
        g = g.toBFS();
        actual = g.shortestPath("N2", "N5");
        assertEquals(null, actual);
        g = g.toDFS();
        actual = g.shortestPath("N2", "N5");
        assertEquals(null, actual);
        g = g.toAStar();
        actual = g.shortestPath("N2", "N5");
        assertEquals(null, actual);
    }

    @Test
    public void testSeparatePathByFloor() {
        Node n0 = new Node("N0", 0, 0, "1", "", "", "", "");
        Node n1 = new Node("N1", 1, 1, "1", "", "", "", "");
        Node n2 = new Node("N2", 2, 2, "G", "", "", "", "");
        Node n3 = new Node("N3", 4, 1, "1", "", "", "", "");
        Node n4 = new Node("N4", 1, 4, "2", "", "", "", "");
        Node n5 = new Node("N5", 3, 2, "2", "", "", "", "");
        Node n6 = new Node("N6", 1, 0, "2", "", "", "", "");
        Node n7 = new Node("N7", 2, 0, "G", "", "", "", "");
        Node n8 = new Node("N8", 3, 0, "1", "", "", "", "");
        Node n9 = new Node("N9", 4, 0, "1", "", "", "", "");
        LinkedList<Node> nodes = new LinkedList<>();
        nodes.add(n0);
        nodes.add(n1);
        nodes.add(n2);
        nodes.add(n3);
        nodes.add(n4);
        nodes.add(n5);
        nodes.add(n6);
        nodes.add(n7);
        nodes.add(n8);
        nodes.add(n9);
        Graph g = new AStarGraph(nodes);
        List<String> path = new LinkedList<>();
        path.add("N0");
        path.add("N1");
        path.add("N2");
        path.add("N3");
        path.add("N4");
        path.add("N5");
        path.add("N6");
        path.add("N7");
        path.add("N8");
        path.add("N9");
        List<List<List<Node>>> expected = new ArrayList<>();
        for(int i = 0; i < UIControllerPFM.Floors.values().length; i++) {
            expected.add(new LinkedList<>());
        }
        expected.get(UIControllerPFM.Floors.FIRST.ordinal()).add(new LinkedList<>());
        expected.get(UIControllerPFM.Floors.FIRST.ordinal()).get(0).add(n0);
        expected.get(UIControllerPFM.Floors.FIRST.ordinal()).get(0).add(n1);
        expected.get(UIControllerPFM.Floors.GROUND.ordinal()).add(new LinkedList<>());
        expected.get(UIControllerPFM.Floors.GROUND.ordinal()).get(0).add(n2);
        expected.get(UIControllerPFM.Floors.FIRST.ordinal()).add(new LinkedList<>());
        expected.get(UIControllerPFM.Floors.FIRST.ordinal()).get(1).add(n3);
        expected.get(UIControllerPFM.Floors.SECOND.ordinal()).add(new LinkedList<>());
        expected.get(UIControllerPFM.Floors.SECOND.ordinal()).get(0).add(n4);
        expected.get(UIControllerPFM.Floors.SECOND.ordinal()).get(0).add(n5);
        expected.get(UIControllerPFM.Floors.SECOND.ordinal()).get(0).add(n6);
        expected.get(UIControllerPFM.Floors.GROUND.ordinal()).add(new LinkedList<>());
        expected.get(UIControllerPFM.Floors.GROUND.ordinal()).get(1).add(n7);
        expected.get(UIControllerPFM.Floors.FIRST.ordinal()).add(new LinkedList<>());
        expected.get(UIControllerPFM.Floors.FIRST.ordinal()).get(2).add(n8);
        expected.get(UIControllerPFM.Floors.FIRST.ordinal()).get(2).add(n9);
        List<List<List<Node>>> actual = g.separatePathByFloor(path);
        assertEquals(expected, actual);
    }
}
