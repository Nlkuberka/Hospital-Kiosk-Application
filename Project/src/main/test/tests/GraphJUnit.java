
package tests;

import database.DBControllerNE;
import entities.AStarGraph;
import entities.Graph;
import entities.Node;
import junit.framework.TestCase;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import pathfinding.UIControllerPFM;

import entities.BFSGraph;

import java.sql.Connection;
import java.time.Clock;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class GraphJUnit extends TestCase {

    @Test
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
        Connection conn = DBControllerNE.dbConnect();
        DBControllerNE.addNode(n0, conn);
        DBControllerNE.addNode(n1, conn);
        DBControllerNE.addNode(n2, conn);
        DBControllerNE.addNode(n3, conn);
        DBControllerNE.addNode(n4, conn);
        DBControllerNE.addNode(n5, conn);
        DBControllerNE.addNode(n6, conn);
        DBControllerNE.addNode(n7, conn);
        DBControllerNE.addNode(n8, conn);
        DBControllerNE.addNode(n9, conn);
        DBControllerNE.closeConnection(conn);
        Graph.getGraph().addNode(n0);
        Graph.getGraph().addNode(n1);
        Graph.getGraph().addNode(n2);
        Graph.getGraph().addNode(n3);
        Graph.getGraph().addNode(n4);
        Graph.getGraph().addNode(n5);
        Graph.getGraph().addNode(n6);
        Graph.getGraph().addNode(n7);
        Graph.getGraph().addNode(n8);
        Graph.getGraph().addNode(n9);
        Graph.getGraph().addBiEdge("N0", "N1");
        Graph.getGraph().addBiEdge("N1", "N2");
        Graph.getGraph().addBiEdge("N1", "N4");
        Graph.getGraph().addBiEdge("N2", "N4");
        Graph.getGraph().addBiEdge("N2", "N5");
        Graph.getGraph().addBiEdge("N3", "N5");
        Graph.getGraph().addBiEdge("N0", "N6");
        Graph.getGraph().addBiEdge("N6", "N7");
        Graph.getGraph().addBiEdge("N7", "N8");
        Graph.getGraph().addBiEdge("N8", "N9");
        Graph.getGraph().addBiEdge("N9", "N3");

        //g.checkEdges();
        List<String> expected = new LinkedList<>();
        expected.add("N0");
        expected.add("N6");
        expected.add("N7");
        expected.add("N8");
        expected.add("N9");
        expected.add("N3");

        List<String> path = Graph.getGraph().shortestPath("N0", "N3");
        assertEquals(expected, path);

        Graph.toAStar();
        path = Graph.getGraph().shortestPath("N0", "N3");
        assertEquals(expected, path);

        Graph.toBFS();
        path = Graph.getGraph().shortestPath("N0", "N3");
        assertEquals(expected, path);

        Graph.toBellmanFord();
        path = Graph.getGraph().shortestPath("N0", "N3");
        assertEquals(expected, path);

        Graph.toDijkstra();
        path = Graph.getGraph().shortestPath("N0", "N3");
        assertEquals(expected, path);

        Graph.getGraph().removeNode("N0");
        Graph.getGraph().removeNode("N1");
        Graph.getGraph().removeNode("N2");
        Graph.getGraph().removeNode("N3");
        Graph.getGraph().removeNode("N4");
        Graph.getGraph().removeNode("N5");
        Graph.getGraph().removeNode("N6");
        Graph.getGraph().removeNode("N7");
        Graph.getGraph().removeNode("N8");
        Graph.getGraph().removeNode("N9");
        conn = DBControllerNE.dbConnect();
        DBControllerNE.deleteNode("N0", conn);
        DBControllerNE.deleteNode("N1", conn);
        DBControllerNE.deleteNode("N2", conn);
        DBControllerNE.deleteNode("N3", conn);
        DBControllerNE.deleteNode("N4", conn);
        DBControllerNE.deleteNode("N5", conn);
        DBControllerNE.deleteNode("N6", conn);
        DBControllerNE.deleteNode("N7", conn);
        DBControllerNE.deleteNode("N8", conn);
        DBControllerNE.deleteNode("N9", conn);
        DBControllerNE.closeConnection(conn);
    }

    @Test
    public void testRealNodes() {
        Node n0 = new Node("N0", 1580, 2538, "", "", "", "", "");
        Node n1 = new Node("N1", 1395, 2674, "", "", "", "", "");
        Node n2 = new Node("N2", 1532, 2777, "", "", "", "", "");
        Node n3 = new Node("N3", 1591, 2560, "", "", "", "", "");
        Node n4 = new Node("N4", 1590, 2604, "", "", "", "", "");
        Node n5 = new Node("N5", 1590, 2745, "", "", "", "", "");
        Connection conn = DBControllerNE.dbConnect();
        DBControllerNE.addNode(n0, conn);
        DBControllerNE.addNode(n1, conn);
        DBControllerNE.addNode(n2, conn);
        DBControllerNE.addNode(n3, conn);
        DBControllerNE.addNode(n4, conn);
        DBControllerNE.addNode(n5, conn);
        DBControllerNE.closeConnection(conn);
        Graph.getGraph().addNode(n0);
        Graph.getGraph().addNode(n1);
        Graph.getGraph().addNode(n2);
        Graph.getGraph().addNode(n3);
        Graph.getGraph().addNode(n4);
        Graph.getGraph().addNode(n5);
        Graph.getGraph().addBiEdge("N0", "N1");
        Graph.getGraph().addBiEdge("N1", "N5");
        Graph.getGraph().addBiEdge("N3", "N4");
        Graph.getGraph().addBiEdge("N2", "N3");
        Graph.getGraph().addBiEdge("N4", "N1");
        Graph.getGraph().addBiEdge("N3", "N0");
        List<String> expected = new LinkedList<>();
        expected.add("N5");
        expected.add("N1");
        expected.add("N0");

        List<String> path = Graph.getGraph().shortestPath("N5", "N0");
        assertEquals(expected, path);

        Graph.toAStar();
        path = Graph.getGraph().shortestPath("N5", "N0");
        assertEquals(expected, path);

        Graph.toBFS();
        path = Graph.getGraph().shortestPath("N5", "N0");
        assertEquals(expected, path);

        Graph.toBellmanFord();
        path = Graph.getGraph().shortestPath("N5", "N0");
        assertEquals(expected, path);

        Graph.toDijkstra();
        path = Graph.getGraph().shortestPath("N5", "N0");
        assertEquals(expected, path);

        Graph.getGraph().removeNode("N0");
        Graph.getGraph().removeNode("N1");
        Graph.getGraph().removeNode("N2");
        Graph.getGraph().removeNode("N3");
        Graph.getGraph().removeNode("N4");
        Graph.getGraph().removeNode("N5");

        conn = DBControllerNE.dbConnect();
        DBControllerNE.deleteNode("N0", conn);
        DBControllerNE.deleteNode("N1", conn);
        DBControllerNE.deleteNode("N2", conn);
        DBControllerNE.deleteNode("N3", conn);
        DBControllerNE.deleteNode("N4", conn);
        DBControllerNE.deleteNode("N5", conn);
        DBControllerNE.closeConnection(conn);
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
        Connection conn = DBControllerNE.dbConnect();
        DBControllerNE.addNode(n0, conn);
        DBControllerNE.addNode(n1, conn);
        DBControllerNE.addNode(n2, conn);
        DBControllerNE.addNode(n3, conn);
        DBControllerNE.addNode(n4, conn);
        DBControllerNE.addNode(n5, conn);
        DBControllerNE.addNode(n6, conn);
        DBControllerNE.addNode(n7, conn);
        DBControllerNE.closeConnection(conn);
        Graph.getGraph().addNode(n0);
        Graph.getGraph().addNode(n1);
        Graph.getGraph().addNode(n2);
        Graph.getGraph().addNode(n3);
        Graph.getGraph().addNode(n4);
        Graph.getGraph().addNode(n5);
        Graph.getGraph().addNode(n6);
        Graph.getGraph().addNode(n7);
        Graph.getGraph().addBiEdge("N0", "N1");
        Graph.getGraph().addBiEdge("N1", "N2");
        Graph.getGraph().addBiEdge("N2", "N3");
        Graph.getGraph().addBiEdge("N3", "N4");
        Graph.getGraph().addBiEdge("N4", "N5");
        Graph.getGraph().addBiEdge("N5", "N6");
        Graph.getGraph().addBiEdge("N1", "N7");
        Graph.getGraph().addBiEdge("N7", "N5");
        List<String> expected = new LinkedList<>();
        expected.add("N0");
        expected.add("N1");
        expected.add("N2");
        expected.add("N3");
        expected.add("N4");
        expected.add("N5");
        expected.add("N6");

        List<String> actual = Graph.getGraph().shortestPath("N0", "N6");
        assertEquals(expected, actual);

        Graph.toAStar();
        actual = Graph.getGraph().shortestPath("N0", "N6");
        assertEquals(expected, actual);

        Graph.toBFS();
        actual = Graph.getGraph().shortestPath("N0", "N6");
        assertEquals(expected, actual);

        Graph.toDijkstra();
        actual = Graph.getGraph().shortestPath("N0", "N6");
        assertEquals(expected, actual);

        Graph.toBellmanFord();
        actual = Graph.getGraph().shortestPath("N0", "N6");
        assertEquals(expected, actual);

        Graph.getGraph().removeNode("N0");
        Graph.getGraph().removeNode("N1");
        Graph.getGraph().removeNode("N2");
        Graph.getGraph().removeNode("N3");
        Graph.getGraph().removeNode("N4");
        Graph.getGraph().removeNode("N5");
        Graph.getGraph().removeNode("N6");
        Graph.getGraph().removeNode("N7");
        conn = DBControllerNE.dbConnect();
        DBControllerNE.deleteNode("N0", conn);
        DBControllerNE.deleteNode("N1", conn);
        DBControllerNE.deleteNode("N2", conn);
        DBControllerNE.deleteNode("N3", conn);
        DBControllerNE.deleteNode("N4", conn);
        DBControllerNE.deleteNode("N5", conn);
        DBControllerNE.deleteNode("N6", conn);
        DBControllerNE.deleteNode("N7", conn);
        DBControllerNE.closeConnection(conn);
    }

    @Test
    public void testDisconnectedGraph() {
        Node n0 = new Node("N0", 0, 0, "", "", "", "", "");
        Node n1 = new Node("N1", 0, 1, "", "", "", "", "");
        Node n2 = new Node("N2", 1, 0, "", "", "", "", "");
        Node n3 = new Node("N3", 1, 1, "", "", "", "", "");
        Node n4 = new Node("N4", 5, 0, "", "", "", "", "");
        Node n5 = new Node("N5", 0, 5, "", "", "", "", "");
        Node n6 = new Node("N6", 5, 5, "", "", "", "", "");
        Connection conn = DBControllerNE.dbConnect();
        DBControllerNE.addNode(n0, conn);
        DBControllerNE.addNode(n1, conn);
        DBControllerNE.addNode(n2, conn);
        DBControllerNE.addNode(n3, conn);
        DBControllerNE.addNode(n4, conn);
        DBControllerNE.addNode(n5, conn);
        DBControllerNE.addNode(n6, conn);
        DBControllerNE.closeConnection(conn);
        Graph.getGraph().addNode(n0);
        Graph.getGraph().addNode(n1);
        Graph.getGraph().addNode(n2);
        Graph.getGraph().addNode(n3);
        Graph.getGraph().addNode(n4);
        Graph.getGraph().addNode(n5);
        Graph.getGraph().addNode(n6);
        Graph.getGraph().addBiEdge("N0", "N1");
        Graph.getGraph().addBiEdge("N1", "N2");
        Graph.getGraph().addBiEdge("N2", "N3");
        Graph.getGraph().addBiEdge("N3", "N0");
        Graph.getGraph().addBiEdge("N4", "N5");
        Graph.getGraph().addBiEdge("N5", "N6");
        Graph.getGraph().addBiEdge("N6", "N4");
        List<String> expected = new LinkedList<>();
        expected.add("N0");
        expected.add("N1");

        List<String> actual = Graph.getGraph().shortestPath("N0", "N1");
        assertEquals(expected, actual);

        Graph.toAStar();
        actual = Graph.getGraph().shortestPath("N0", "N1");
        assertEquals(expected, actual);

        Graph.toBFS();
        actual = Graph.getGraph().shortestPath("N0", "N1");
        assertEquals(expected, actual);

        Graph.toBellmanFord();
        actual = Graph.getGraph().shortestPath("N0", "N1");
        assertEquals(expected, actual);

        Graph.toDijkstra();
        actual = Graph.getGraph().shortestPath("N0", "N1");
        assertEquals(expected, actual);

        actual = Graph.getGraph().shortestPath("N2", "N5");
        assertEquals(null, actual);

        Graph.toDFS();
        actual = Graph.getGraph().shortestPath("N2", "N5");
        assertEquals(null, actual);

        Graph.toAStar();
        actual = Graph.getGraph().shortestPath("N2", "N5");
        assertEquals(null, actual);

        Graph.toDijkstra();
        actual = Graph.getGraph().shortestPath("N2", "N5");
        assertEquals(null, actual);

        Graph.toBellmanFord();
        actual = Graph.getGraph().shortestPath("N2", "N5");
        assertEquals(null, actual);

        Graph.getGraph().removeNode("N0");
        Graph.getGraph().removeNode("N1");
        Graph.getGraph().removeNode("N2");
        Graph.getGraph().removeNode("N3");
        Graph.getGraph().removeNode("N4");
        Graph.getGraph().removeNode("N5");
        Graph.getGraph().removeNode("N6");
        conn = DBControllerNE.dbConnect();
        DBControllerNE.deleteNode("N0", conn);
        DBControllerNE.deleteNode("N1", conn);
        DBControllerNE.deleteNode("N2", conn);
        DBControllerNE.deleteNode("N3", conn);
        DBControllerNE.deleteNode("N4", conn);
        DBControllerNE.deleteNode("N5", conn);
        DBControllerNE.deleteNode("N6", conn);
        DBControllerNE.closeConnection(conn);
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
        Connection conn = DBControllerNE.dbConnect();
        DBControllerNE.addNode(n0, conn);
        DBControllerNE.addNode(n1, conn);
        DBControllerNE.addNode(n2, conn);
        DBControllerNE.addNode(n3, conn);
        DBControllerNE.addNode(n4, conn);
        DBControllerNE.addNode(n5, conn);
        DBControllerNE.addNode(n6, conn);
        DBControllerNE.addNode(n7, conn);
        DBControllerNE.addNode(n8, conn);
        DBControllerNE.addNode(n9, conn);
        DBControllerNE.closeConnection(conn);
        Graph.getGraph().addNode(n0);
        Graph.getGraph().addNode(n1);
        Graph.getGraph().addNode(n2);
        Graph.getGraph().addNode(n3);
        Graph.getGraph().addNode(n4);
        Graph.getGraph().addNode(n5);
        Graph.getGraph().addNode(n6);
        Graph.getGraph().addNode(n7);
        Graph.getGraph().addNode(n8);
        Graph.getGraph().addNode(n9);
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
        List<List<List<Node>>> actual = Graph.getGraph().separatePathByFloor(path);

        assertEquals(expected.size(), actual.size());
        for(int i = 0; i < actual.size(); i++) {
            assertEquals(expected.get(i).size(), actual.get(i).size());
            for(int j = 0; j < actual.get(i).size(); j++) {
                assertEquals(expected.get(i).get(j).size(), actual.get(i).get(j).size());
                for(int k = 0; k < actual.get(i).get(j).size(); k++) {
                    assertEquals(expected.get(i).get(j).get(k).getNodeID(), actual.get(i).get(j).get(k).getNodeID());
                }
            }
        }

        Graph.getGraph().removeNode("N0");
        Graph.getGraph().removeNode("N1");
        Graph.getGraph().removeNode("N2");
        Graph.getGraph().removeNode("N3");
        Graph.getGraph().removeNode("N4");
        Graph.getGraph().removeNode("N5");
        Graph.getGraph().removeNode("N6");
        Graph.getGraph().removeNode("N7");
        Graph.getGraph().removeNode("N8");
        Graph.getGraph().removeNode("N9");
        conn = DBControllerNE.dbConnect();
        DBControllerNE.deleteNode("N0", conn);
        DBControllerNE.deleteNode("N1", conn);
        DBControllerNE.deleteNode("N2", conn);
        DBControllerNE.deleteNode("N3", conn);
        DBControllerNE.deleteNode("N4", conn);
        DBControllerNE.deleteNode("N5", conn);
        DBControllerNE.deleteNode("N6", conn);
        DBControllerNE.deleteNode("N7", conn);
        DBControllerNE.deleteNode("N8", conn);
        DBControllerNE.deleteNode("N9", conn);
        DBControllerNE.closeConnection(conn);
    }
}
