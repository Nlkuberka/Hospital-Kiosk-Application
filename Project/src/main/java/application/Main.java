package application;

import database.DBController;

import database.DBControllerNE;
import entities.Edge;
import entities.Graph;
import entities.Node;
import javafx.application.Application;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.time.Clock;
import java.util.List;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        UIController controller = new UIController(primaryStage);

        controller.goToScene(UIController.WELCOME_MAIN);


        System.out.println("Collaborator is " + "X");

        Connection conn = DBController.dbConnect();
        DatabaseMetaData dbmd = conn.getMetaData();
        ResultSet rs = dbmd.getTables(null, null, "RESERVATIONS",null);
        if(!rs.next()){
            DBController.initializeAppDB();
        }

        // Initialize the graph.
        List<Node> allNodes = DBControllerNE.generateListOfNodes(conn,DBControllerNE.ALL_NODES);
        for (Node node : allNodes) {
            try {
                Graph.getGraph().addNode(node);
            } catch (IllegalArgumentException e) {
            }
        }
        List<Edge> allEdges = DBControllerNE.generateListofEdges(conn);
        for (Edge edge : allEdges) {
            try {
                Graph.getGraph().addBiEdge(edge.getNode1ID(), edge.getNode2ID());
            } catch (IllegalArgumentException e) {
            }
        }

        testShortestPathTimes();

        //DBController.initializeAppDB();
    }

    private static void testShortestPathTimes() {
        final String nodeID1 = "BSTAI00602";
        final String nodeID2 = "DSTAI00302";
        for(int algo = 0; algo < 5; algo++) {
            long totalTime = 0;
            switch (algo) {
                case 0:
                    Graph.toAStar();
                    System.out.println("Algorithm: A*");
                    break;
                case 1:
                    Graph.toBellmanFord();
                    System.out.println("Algorithm: Bellman-Ford");
                    break;
                case 2:
                    Graph.toDijkstra();
                    System.out.println("Algorithm: Dijkstra");
                    break;
                case 3:
                    Graph.toBFS();
                    System.out.println("Algorithm: BFS");
                    break;
                case 4:
                    Graph.toDFS();
                    System.out.println("Algorithm: DFS");
                    break;
            }
            List<String> path = null;
            for(int i = 0; i < 100; i++) {

                Clock clock = Clock.systemDefaultZone();
                final long startTime = clock.millis();
                path = Graph.getGraph().shortestPath(nodeID1, nodeID2);
                final long endTime = clock.millis();
                totalTime += endTime - startTime;
            }
            System.out.println("Path: " + path);
            System.out.println("Average Time: " + (totalTime / 100));
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
