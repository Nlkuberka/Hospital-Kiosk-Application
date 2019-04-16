package application;

import com.twilio.exception.RestException;
import database.DBController;
import database.DBControllerNE;
import database.DBControllerU;
import entities.Edge;
import entities.Graph;
import entities.Node;
import javafx.application.Application;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.time.Clock;
import java.util.Arrays;
import java.util.List;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

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

        CurrentUser.user = DBControllerU.getGuestUser(conn);
        DBController.closeConnection(conn);
        //DBController.initializeAppDB();


    }

    public static final String ACCOUNT_SID = "AC176f9cd821ffa8dcad559ceecad9ecf1";
    public static final String AUTH_TOKEN = "ab7f1a58335f47c98bac61b471920dfe";

    public static void main(String[] args) throws IOException {

        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
        Message message = Message.creator(
                new com.twilio.type.PhoneNumber("+15089834963"),
                new com.twilio.type.PhoneNumber("+17472290044"),
                "body")
                .create();

        System.out.println(message.getSid());

        //launch(args);
    }

}
