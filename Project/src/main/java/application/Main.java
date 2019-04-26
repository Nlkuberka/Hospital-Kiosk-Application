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
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.util.List;



public class Main extends Application {
    private static int serverSocketNum;
    private static int clientSocketNum;

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("/icon/bwh.png")));

        System.out.println("Collaborator is " + "X");

        try {
            InputStream inputStream = getClass().getResourceAsStream("/textfiles/ipAddresses.txt");
            BufferedReader br = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
            String line;
            while((line = br.readLine()) != null) {
                DBNetwork.ipAddresses.add(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        CurrentUser.network = new DBNetwork(serverSocketNum, clientSocketNum);
        CurrentUser.network.hold();

        CurrentUser.network.mute();

        System.out.println(DBNetwork.ipAddresses);

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
        DBController.closeConnection(conn);
        conn = DBController.dbConnect();
        CurrentUser.user = DBControllerU.getGuestUser(conn);
        DBController.closeConnection(conn);

        UIController controller = new UIController(primaryStage);
        controller.goToScene(UIController.ADMIN_TOOLS_MAP_VIEW);
        controller.goToScene(UIController.PATHFINDING_MAIN);
        controller.goToScene(UIController.LOGIN_MAIN);
        controller.goToScene(UIController.SPLASHSCREEN);

        UIController.SESSION_TIMEOUT_THREAD.start();

        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                try {
                    CurrentUser.network.shutdown();
                    System.exit(0);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

    }



    public static void main(String[] args) throws IOException {
        serverSocketNum = 5748;
        clientSocketNum = 5748;
        if(args.length > 0) {
            for(int i = 0; i < args.length - 2; i++) {
                DBNetwork.ipAddresses.add(args[i]);
            }
            serverSocketNum = Integer.parseInt(args[args.length - 2]);
            clientSocketNum = Integer.parseInt(args[args.length - 1]);
        }

        launch(args);
        System.exit(0);
    }

}
