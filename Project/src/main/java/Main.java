import javafx.application.Application;
import javafx.stage.Stage;
import java.io.File;
import java.sql.Connection;
import java.util.LinkedList;
import java.util.List;


public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        CurrentUser.userID = "Guest";
        CurrentUser.permissions = User.GUEST_PERMISSIONS;
        UIController userIntController = new UIController(primaryStage);
        userIntController.goToScene(UIController.PATHFINDING_MAIN);

        System.out.println("Collaborator is " + "X");

        DBController dbController = new DBController();
        Connection conn = DBController.dbConnect();

//        // Only needed if DB have not been populated yet
//        File nodeCSV = new File("nodesv3.csv");
//        File edgeCSV = new File("edgesv3.csv");
//        DBController.loadNodeData(nodeCSV, conn);
//        DBController.loadEdgeData(edgeCSV, conn);

//        List<Node> list = CSVHandler.readFile("PrototypeNodes.csv");
//        dbController.enterData(list, connection);

        String ID = "AELEV00S01";
        Node singleNode= DBController.fetchNode(ID, conn);
        String strNode; // = singleNode.toString();
//        System.out.print("Node:" + strNode);

        LinkedList<Node> allNodes = dbController.generateListofNodes(conn);
        strNode = allNodes.getFirst().toString();
        System.out.print("Node:" + strNode);

        // IF YOU DO NOT HAVE THE TABLES SET UP RUN THIS CODE TO GENERATE
        // LEAVE COMMENTED OUT OTHERWISE
//         DBController.createTable("CREATE TABLE NODES(" +
//                "NODEID VARCHAR(10),"+
//                "XCOORD INTEGER," +
//                "YCOORD INTEGER," +
//                "FLOOR VARCHAR(3)," +
//                "BUILDING VARCHAR(15)," +
//                "NODETYPE VARCHAR(4)," +
//                "LONGNAME VARCHAR(50)," +
//                "SHORTNAME VARCHAR(50)," +
//                "CONSTRAINT NODE_PK PRIMARY KEY(NODEID)" +
//                ")",conn);
//        DBController.createTable("CREATE TABLE USERS(" +
//                "  USERID VARCHAR(10)," +
//                "  PERMISSION SMALLINT," +
//                "  USENAME VARCHAR(15)," +
//                "  PASSWORD VARCHAR(15)," +
//                "  CONSTRAINT USER_PK PRIMARY KEY(USERID)" +
//                ")",conn);
//        DBController.createTable("CREATE TABLE EDGES (" +
//                "  EDGEID VARCHAR(21)," +
//                "  STARTNODE VARCHAR(10) REFERENCES NODES(NODEID)," +
//                "  ENDNODE varchar(10) REFERENCES NODES(NODEID)," +
//                "  CONSTRAINT EDGE_PK PRIMARY KEY(EDGEID)" +
//                ")",conn);
//        DBController.createTable("CREATE TABLE SERVICEREQUEST(" +
//                "  NODEID VARCHAR(10) REFERENCES NODES(NODEID)," +
//                "  SERVICETYPE VARCHAR(20)," +
//                "  MESSAGE VARCHAR(100)," +
//                "  USERID VARCHAR(10) REFERENCES USERS(USERID)," +
//                "  RESOLVED BOOLEAN" +
//                ")",conn);
//        DBController.createTable("CREATE TABLE RESERVATIONS(" +
//                "  NODEID VARCHAR(10) REFERENCES NODES(NODEID)," +
//                "  USERID VARCHAR(10) REFERENCES USERS(USERID)," +
//                "  DAY DATE," +
//                "  STARTTIME TIME," +
//                "  ENDTIME TIME" +
//                ")",conn);
    }


    public static void main(String[] args) {
        launch(args);
    }
}