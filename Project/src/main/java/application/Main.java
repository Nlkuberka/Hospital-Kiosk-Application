package application;

import database.DBController;
import database.DBControllerNE;
import database.DBControllerU;
import entities.Edge;
import entities.Graph;
import entities.Node;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import network.DBNetwork;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.util.List;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("/icon/bwh.png")));

        System.out.println("Collaborator is " + "X");

        Connection conn = DBController.dbConnect();
        DatabaseMetaData dbmd = conn.getMetaData();
        ResultSet rs = dbmd.getTables(null, null, "RESERVATIONS",null);
        if(!rs.next()){
            DBController.initializeAppDB();
        }

        UIController controller = new UIController(primaryStage);

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

        CurrentUser.user = DBControllerU.getGuestUser(conn);
        DBController.closeConnection(conn);

        CurrentUser.network = new DBNetwork(4590);
        CurrentUser.network.hold();
        CurrentUser.network.mute();
        //CurrentUser.network.sendNodePacket(DBNetwork.ADD_NODE, new Node("TEST", 123, 456, "TEST", "TEST", "TEST", "TEST", "TEST"));

        controller.goToScene(UIController.ADMIN_TOOLS_MAP_VIEW);
        controller.goToScene(UIController.PATHFINDING_MAIN);
        controller.goToScene(UIController.WELCOME_MAIN);

        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                try {
                    CurrentUser.network.shutdown();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }



    public static void main(String[] args) throws IOException {

        launch(args);
    }

}
