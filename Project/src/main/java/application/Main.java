package application;

import database.DBController;
import database.DBControllerNE;
import database.DBControllerU;
import entities.Edge;
import entities.Graph;
import entities.Node;
import javafx.application.Application;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.util.List;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        UIController controller = new UIController(primaryStage);

        controller.goToScene(UIController.ADMIN_TOOLS_MAP_VIEW);


        System.out.println("Collaborator is " + "X");

        Connection conn = DBController.dbConnect();
        assert conn != null;
        DatabaseMetaData dbmd = conn.getMetaData();
        ResultSet rs = dbmd.getTables(null, null, "RESERVATIONS",null);
        if(!rs.next()){
            DBController.initializeAppDB();
        }

        // Initialize the graph.
        List<Node> allNodes = DBControllerNE.generateListOfNodes(conn,DBControllerNE.ALL_NODES);
        assert allNodes != null;
        for (Node node : allNodes) {
            try {
                Graph.getGraph().addNode(node);
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            }
        }
        List<Edge> allEdges = DBControllerNE.generateListofEdges(conn);
        assert allEdges != null;
        for (Edge edge : allEdges) {
            try {
                Graph.getGraph().addBiEdge(edge.getNode1ID(), edge.getNode2ID());
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            }
        }

        CurrentUser.user = DBControllerU.getGuestUser(conn);
        DBController.closeConnection(conn);
        //DBController.initializeAppDB();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
