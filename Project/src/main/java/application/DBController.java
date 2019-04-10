package application;

import entities.*;

import javax.jws.soap.SOAPBinding;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.io.*;
import java.sql.*;

import java.util.LinkedList;
import java.util.List;

/**
 * Database Controller
 * manages connection as well as add/updates to
 * all application tables
 *
 * handles SQLExceptions thrown by statement execution
 *
 * @author imoralessirgo, ryano647
 * @version iteration2
 */
public class DBController {
    // Connection connection;


    /**
     * initializeAppDB
     *
     * generates application tables and loads them with data if not already set up
     * for JAR file use
     *
     * LAST UPDATE 04/05/2019
     */
    public static void initializeAppDB(){
        Connection conn = dbConnect();
        String nodes = "CREATE TABLE NODES(\n" +
                "NODEID VARCHAR(10),\n" +
                "XCOORD INTEGER,\n" +
                "YCOORD INTEGER,\n" +
                "FLOOR VARCHAR(3),\n" +
                "BUILDING VARCHAR(15),\n" +
                "NODETYPE VARCHAR(4),\n" +
                "LONGNAME VARCHAR(50),\n" +
                "SHORTNAME VARCHAR(50),\n" +
                "CONSTRAINT NODE_PK PRIMARY KEY(NODEID)\n" +
                ")\n";
        String edges = "CREATE TABLE EDGES (\n" +
                "  EDGEID VARCHAR(21),\n" +
                "  STARTNODE VARCHAR(10) REFERENCES NODES(NODEID),\n" +
                "  ENDNODE varchar(10) REFERENCES NODES(NODEID),\n" +
                "  CONSTRAINT EDGE_PK PRIMARY KEY(EDGEID)\n" +
                ")\n";
        String user = "CREATE TABLE USERS(\n" +
                "  USERID VARCHAR(10),\n" +
                "  PERMISSION SMALLINT,\n" +
                "  USERNAME VARCHAR(15),\n" +
                "  PASSWORD VARCHAR(15),\n" +
                "  CONSTRAINT USER_PK PRIMARY KEY(USERID),\n" +
                "  CONSTRAINT UN_UN UNIQUE (USERNAME)" +
                ")\n";
        String servicerequest = "CREATE TABLE SERVICEREQUEST(\n" +
                "  SERVICEID INTEGER GENERATED ALWAYS AS IDENTITY, \n" +
                "  NODEID VARCHAR(10) REFERENCES NODES(NODEID),\n" +
                "  SERVICETYPE VARCHAR(20),\n" +
                "  MESSAGE VARCHAR(100),\n" +
                "  USERID VARCHAR(10) REFERENCES USERS(USERID),\n" +
                "  RESOLVED BOOLEAN,\n" +
                "  RESOLVERID VARCHAR(10) REFERENCES USERS(USERID), \n" +
                "  CONSTRAINT SERVICE_PK PRIMARY KEY(SERVICEID)\n" +
                ")\n";
        String reservations = "CREATE TABLE RESERVATIONS(\n" +
                "  RSVID INTEGER GENERATED ALWAYS AS IDENTITY,\n" +
                "  NODEID VARCHAR(10) REFERENCES NODES(NODEID),\n" +
                "  USERID VARCHAR(10) REFERENCES USERS(USERID),\n" +
                "  DAY DATE,\n" +
                "  STARTTIME TIME,\n" +
                "  ENDTIME TIME,\n" +
                "  CONSTRAINT RSV_PK PRIMARY KEY(RSVID)\n" +
                ")\n";



        createTable(nodes,conn);
        createTable(edges,conn);
        createTable(user,conn);
        createTable(reservations,conn);
        createTable(servicerequest,conn);

        loadNodeData(new File("nodesv4.csv"),conn);
        loadEdgeData(new File("edgesv5.csv"),conn);

        try {
            Statement s = conn.createStatement();
            addUser(new User("USER0001","user","user",3071),conn);
            addUser(new User("GUEST0001","guest","guest",1024),conn);
            addUser(new User("ADMIN00001","admin","admin",4095),conn);
            addUser(new User("WWONG2","staff","staff",4095),conn);

        }catch(SQLException e){
            e.printStackTrace();
        }
    }


    /**
     * dbConnect
     *
     * connects to the application's database for query execution
     * handles SQLExceptions
     *
     * @return
     */
    public static Connection dbConnect() {
        try {
            DriverManager.registerDriver(new org.apache.derby.jdbc.EmbeddedDriver());
            Connection connection = DriverManager.getConnection("jdbc:derby:myDB;create=true");
            return connection;
        }catch(SQLException e){
            e.printStackTrace();
        }
        return null;
    }

    /**
     * closeConnection
     *
     * Terminates connection to database after use
     * ensures proper functionality during query execution
     *
     * @param connection
     */
    public static void closeConnection(Connection connection){
        try{
            connection.close();
        }catch(SQLException e){
            e.printStackTrace();
        }
    }


    /**
     * loadNodeData
     *
     * reads and stores node data fro given csv file
     *
     * @param file
     * @param connection
     */
    public static void loadNodeData(File file, Connection connection){
        BufferedReader br = null;
        String line = "";
        String[] arr;
        try{
            br = new BufferedReader(new FileReader(file));
            br.readLine(); // skip header
            while((line = br.readLine()) != null){
                arr = line.split(",");
                connection.createStatement().execute("insert into NODES " +
                        "values ('"+ arr[0] +"',"+ arr[1]+","+ arr[2]+",'"+ arr[3]+"','"+ arr[4]+"','"+ arr[5]+"','"+ arr[6]+"','"+ arr[7]+"')");
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }


    /**
     * loadEdgeData
     *
     * reads and stores edge data from given csv file
     *
     * @param file
     * @param connection
     */
    public static void loadEdgeData(File file, Connection connection){
        BufferedReader br = null;
        String line = "";
        String[] arr;
        try{
            br = new BufferedReader(new FileReader(file));
            br.readLine(); // skip header
            while((line = br.readLine()) != null){
                arr = line.split(",");
                connection.createStatement().execute("insert into EDGES " +
                        "values ('"+ arr[0] + "','"+ arr[1]+"','"+ arr[2]+"')");
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    /**
     * CreateTable
     *
     * executes the given query
     *
     * @param createStatement
     * @param conn
     */
    public static void createTable(String createStatement, Connection conn){
        try {
            conn.createStatement().execute(createStatement);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }



    /**
     * updateNode
     *
     * updates node of given id, overriding all fields
     * @param node desired node content -- Must have an existing ID --
     */
    public static void updateNode(Node node, Connection connection){
        try{
            Statement s = connection.createStatement();
            s.execute("UPDATE NODES" +
                    " SET XCOORD ="+ node.getXcoord() +","+
                    "YCOORD ="+ node.getYcoord() + ","+
                    "FLOOR = '"+ node.getFloor() + "',"+
                    "BUILDING ='"+ node.getBuilding() + "',"+
                    "NODETYPE = '"+ node.getNodeType() + "',"+
                    "LONGNAME = '"+ node.getLongName() + "',"+
                    "SHORTNAME = '"+ node.getShortName() +"'"+
                    " where NODEID = '" + node.getNodeID() +"'");
        }catch(SQLException e){
            e.printStackTrace();
        }
    }

    /**
     * updateNode
     *
     * updates edge of given id, overriding all fields
     * @param edge desired node content -- Must have an existing ID --
     */
    public static void updateEdge(Edge edge, Connection connection){
        try{
            Statement s = connection.createStatement();
            s.execute("UPDATE EDGES" +
                    " SET  STARTNODE ='"+ edge.getNode1ID() +"',"+
                    "ENDNODE = '"+ edge.getNode2ID() + "'" +
                    "where EDGEID = '" + edge.getEdgeID()+"'");

        }catch(SQLException e){
            e.printStackTrace();
        }
    }

    /**
     * updateServiceRequest
     *
     * saves changes msde to a ServiceRequest object
     *
     * UP TO DATE
     * @param serviceRequest
     * @param connection
     */
    public static void updateServiceRequest(ServiceRequest serviceRequest, Connection connection){
        try{
            Statement s = connection.createStatement();
            s.execute("UPDATE SERVICEREQUEST SET  SERVICETYPE ='"+ serviceRequest.getServiceType() +"',"+
                    "MESSAGE = '"+ serviceRequest.getMessage() + "'," +
                    "RESOLVED = '" + serviceRequest.isResolved() + "'," +
                    "RESOLVERID = '"+serviceRequest.getResolverID()+"' " +
                    "where  SERVICEID = " + serviceRequest.getServiceID());

        }catch(SQLException e){
            e.printStackTrace();
        }
    }

    /**
     * fetchNode
     *
     * generates an node object from data under given ID
     *
     * @param ID
     * @param connection
     * @return
     */
    public static Node fetchNode(String ID,Connection connection ){
        Node node = null;
        try{
            Statement s = connection.createStatement();
            ResultSet rs = s.executeQuery("Select from NODES where NODEID = '" + ID + "'");
            rs.next();
            node = new Node(rs.getString("NODEID"),rs.getInt("XCOORD"),
                    rs.getInt("YCOORD"),rs.getString("FLOOR"),
                    rs.getString("BUILDING"),rs.getString("NODETYPE"),
                    rs.getString("SHORTNAME"),rs.getString("LONGNAME"));
        }catch(SQLException e){
            e.printStackTrace();
        }
        return node;

    }

    /**
     * fetchEdge
     *
     * generates an edge object from data under given ID
     *
     * @param ID
     * @param connection
     * @return
     */
    public static Edge fetchEdge(String ID,Connection connection ){
        Edge edge = null;
        try{
            Statement s = connection.createStatement();
            ResultSet rs = s.executeQuery("Select from EDGES where EDGEID= '" + ID + "'");
            rs.next();
            edge = new Edge(rs.getString(1),rs.getString(2),rs.getString(3));
        }catch(SQLException e){
            e.printStackTrace();
        }
        return edge;
    }

    /**
     * delete node
     *
     * deletes node of given ID
     * @param ID of node to be deleted
     */
    public static void deleteNode(String ID, Connection connection){
        try {
            Statement s = connection.createStatement();
            s.execute("DELETE from EDGES where ENDNODE = '"+ ID +"' OR STARTNODE ='"+ ID +"'");
            s.execute("Delete from NODES where NODEID = '"+ ID +"'");

        }catch(SQLException e){
            e.printStackTrace();
        } }

    /**
     * deleteEdge
     *
     * deletes edge of given id from the database
     *
     * @param ID
     * @param connection
     */
    public static void deleteEdge(String ID, Connection connection){
        try {
            Statement s = connection.createStatement();
            s.execute("DELETE from EDGES where EDGEID = '"+ ID +"'");

        }catch(SQLException e){
            e.printStackTrace();
        }
    }

    /**
     * deleteServiceRequest
     *
     * Not in use
     *
     *
//     * @param NODEID
//     * @param USERID
     * @param connection
     */
//    public static void deleteServiceRequest(String NODEID,String USERID, Connection connection){
//        try {
//            Statement s = connection.createStatement();
//            s.execute("delete  from SERVICEREQUEST where NODEID ='"+ NODEID +"' and USERID ='" + USERID + "'");
//        }catch(SQLException e){
//            e.printStackTrace();
//        }
//    }

    public static void deleteReservation(int reservationID,Connection connection){
        try {
            Statement s = connection.createStatement();
            s.execute("delete  from RESERVATIONS where RSVID ='"+ reservationID +"'");
        }catch(SQLException e){
            e.printStackTrace();
        }
    }

    /**
     * addNode
     *
     * lets user introduce a single node to the DB
     * @param node new node object
     */
    public static void addNode(Node node, Connection connection){
        try{
            Statement s = connection.createStatement();
            nodeInsert(s,node);
        }catch(SQLException e){
            e.printStackTrace();
        }
    }

    /**
     * addEdge
     *
     * lets user introduce a new edge to the DB
     * @param edge
     */
    public static void addEdge(Edge edge, Connection connection){
        try{
            //connection = DriverManager.getConnection("jdbc:derby:myDB");
            Statement s = connection.createStatement();
            s.execute("INSERT into EDGES values ('" + edge.getNode2ID() +"_" + edge.getNode1ID() +
                    "','"+ edge.getNode1ID() +"','"+ edge.getNode2ID() + "')");
            //connection.close();
        }catch(SQLException e){
            e.printStackTrace();
        }
    }



    /**
     * addServiceRequest
     *
     * Enters ServiceRequest object to database
     *
     * @param serviceRequest
     * @param connection
     */
    public static int addServiceRequest(ServiceRequest serviceRequest, Connection connection){
        try{
            PreparedStatement s = connection.prepareStatement("INSERT into SERVICEREQUEST (NODEID, SERVICETYPE, MESSAGE, USERID, RESOLVED, RESOLVERID)" +
                    " values ('" + serviceRequest.getNodeID() +
                    "','"+ serviceRequest.getServiceType() +"','"+ serviceRequest.getMessage() + "','"+
                    serviceRequest.getUserID()+"',"+serviceRequest.isResolved()+","+ serviceRequest.getResolverID()+")");
            ResultSet rs = s.getGeneratedKeys();
            rs.next();
            return rs.getInt("SERVICEID");
        }catch(SQLException e){
            e.printStackTrace();
            return 0;
        }
    }




    /**
     * addReservation
     *
     * records new reservation object to database
     *
     * @param reservation new reservation object
     */
    public static int addReservation(Reservation reservation, Connection connection){
        try{
            //connection = DriverManager.getConnection("jdbc:derby:myDB");
            PreparedStatement s = connection.prepareStatement("INSERT into RESERVATIONS (NODEID, USERID, DAY, STARTTIME, ENDTIME) values ('" + reservation.getNodeID() +"','" + reservation.getUserID() +
                    "','"+ reservation.getDate() +"','"+ reservation.getStartTime() + "','" + reservation.getEndTime() + "')");
            s.execute();
            ResultSet rs = s.getGeneratedKeys();
            rs.next();
            return rs.getInt("RSVID");
        }catch(SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }


    public static String IDfromLongName(String longName, Connection connection) {
        try{
            Statement s = connection.createStatement();
            ResultSet rs = s.executeQuery("SELECT * FROM NODES where LONGNAME = '" + longName + "'");
            rs.next();
            String ID = rs.getString(1);
            return ID;
        }catch(SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * multiEdgeFetch
     *
     * gets multiple nodes from database using the edge object IDs
     *
     * @param IDList list of Edge IDs
     * @param connection
     * @return list of edges from the database
     */
    public static void multiFetchEdge(List<String> IDList, Connection connection) {
        try{
            Statement s = connection.createStatement();
            LinkedList<Edge> listOfEdges = new LinkedList<>();
            for(int x = 0; x < IDList.size(); x++) {
                ResultSet rs = s.executeQuery("Select * from EDGES where EDGEID = '" + IDList.get(x) + "'");
                rs.next();
                Edge edge = new Edge(rs.getString(1), rs.getString(2), rs.getString(3));
                listOfEdges.add(edge);
            }
        }catch(SQLException e) {
            e.printStackTrace();
        }
    }



    /**
     * multiNodeFetch
     *
     * gets multiple nodes from database using the node object IDs
     *
     * @param IDList list of Node IDs
     * @param connection
     * @return list of nodes from the database
     */
    public static LinkedList<Node> multiNodeFetch(List<String> IDList, Connection connection) {
        try{
            Statement s = connection.createStatement();
            LinkedList<Node> listOfNodes = new LinkedList<>();
            for(int x = 0; x < IDList.size(); x++) {
                ResultSet rs = s.executeQuery("SELECT * FROM NODES WHERE NODEID='" + IDList.get(x) + "'");
                rs.next();
                Node node = new Node(rs.getString(1), rs.getInt(2), rs.getInt(3),
                        rs.getString(4), rs.getString(5), rs.getString(6),
                        rs.getString(7), rs.getString(8));
                listOfNodes.add(node);
            }
            return listOfNodes;
        }catch(SQLException e) {
            e.printStackTrace();
        }
        return null;
    }



    /**
     * generateListofNodes
     *
     * creates and returns a list of node objects
     * @return LinkedList<Node>
     */
    public static LinkedList<Node> generateListofNodes(Connection connection){
        try{
            //connection = DriverManager.getConnection("jdbc:derby:myDB");
            Statement s = connection.createStatement();
            ResultSet rs = s.executeQuery("SELECT * from NODES");
            LinkedList<Node> listOfNodes = new LinkedList<>();
            while(rs.next()){
                Node node = new Node(rs.getString(1),rs.getInt(2),rs.getInt(3),
                        rs.getString(4),rs.getString(5),rs.getString(6),
                        rs.getString(7),rs.getString(8));
                listOfNodes.add(node);
            }
            //connection.close();
            return listOfNodes;
        }catch(SQLException e){
            e.printStackTrace();
        }
        return null;
    }

    /**
     * generateListofEdges
     *
     * generates a list of edge objects fetched from the database
     * @param connection
     * @return
     */
    public static LinkedList<Edge> generateListofEdges(Connection connection) {
        try{
            Statement s = connection.createStatement();
            ResultSet rs = s.executeQuery("SELECT * from EDGES");
            LinkedList<Edge> listofEdges = new LinkedList<>();
            while(rs.next()){
                Edge edge = new Edge(rs.getString(1), rs.getString(2), rs.getString(3));
                listofEdges.add(edge);
            }
            return listofEdges;
        }catch(SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * nodeInsert
     *
     * helper method, inserts nodes into existing table
     * @param s existing statement
     * @param node new node object
     */
    public static void nodeInsert(Statement s, Node node){
        try {
            s.execute("insert into NODES values ('"+node.getNodeID()+"',"+
                    node.getXcoord()+","
                    +node.getYcoord()+", '"+
                    node.getFloor() + "' ," +
                    " '" + node.getBuilding() + "'," +
                    " '" + node.getNodeType() + "'," +
                    " '" + node.getLongName() + "'," +
                    " '" + node.getShortName() + "')");
        }catch(SQLException e){
            e.printStackTrace();
        }
    }


    public static User loginCheck(String username, String password, Connection conn, int permission){
            try{
                PreparedStatement ps = conn.prepareStatement("SELECT * FROM USERS WHERE USERNAME = '"+ username + "'" +
                        " AND PASSWORD = '"+ Encryptor.encrypt(password) +"'");
                if(ps.execute()) {
                    ResultSet rs = ps.getResultSet();
                    rs.next();
                    User curr = new User(rs.getString("USERID"),rs.getString("USERNAME"),rs.getInt("PERMISSION"));
                    if(curr.getPermissions() == permission){
                        return curr;
                    }else {
                        return null;
                    }
                }else{
                    return null;
                }
            }catch(SQLException e){
                e.printStackTrace();
                return null;
            }
    }




     public static User getGuestUser(Connection conn){
         User guestUser;
        try {
             PreparedStatement ps = conn.prepareStatement("SELECT * from USERS where PERMISSION = 1024");
             ResultSet rs = ps.executeQuery();
             rs.next();
             guestUser = new User(rs.getString("USERID"),rs.getString("USERNAME"),rs.getInt("PERMISSiON"));
         } catch (SQLException e) {
             e.printStackTrace();
             guestUser = null;
         }


        return guestUser;
     }

     public static LinkedList<User> getUser(Connection conn){
        LinkedList<User> listOfUsers = new LinkedList<User>();
         PreparedStatement ps = null;
         try {
             ps = conn.prepareStatement("SELECT * from USERS");
             ResultSet rs = ps.executeQuery();
             while(rs.next()){
                 listOfUsers.add(new User(rs.getString("USERID"),rs.getString("USERNAME"),rs.getString("PASSWORD"),rs.getInt("PERMISSION")));
             }
         } catch (SQLException e) {
             e.printStackTrace();
         }
         return listOfUsers;
     }



//    /**
//     * exportData
//     *
//     * selects all content held in Nodes table and prints it to a file
//     * @param filename name of output file
//     */

//    public void exportData(String filename) {
//        Connection connection = null;
//        Statement stmt;
//        String query = "Select * from nodes";
//        try {
//            connection = DriverManager.getConnection("jdbc:derby:myDB");
//            stmt = connection.createStatement();
//            ResultSet rs = stmt.executeQuery(query);
//            File file = new File(filename);
//            FileWriter fw = new FileWriter(filename);
//            fw.write("nodeID, xcoord, ycoord, floor, building, nodeType, longName, shortName \r\n");
//            while(rs.next()) {
//                fw.append(rs.getString(1));
//                fw.append(',');
//                fw.append(rs.getString(2));
//                fw.append(',');
//                fw.append(rs.getString(3));
//                fw.append(',');
//                fw.append(rs.getString(4));
//                fw.append(',');
//                fw.append(rs.getString(5));
//                fw.append(',');
//                fw.append(rs.getString(6));
//                fw.append(',');
//                fw.append(rs.getString(7));
//                fw.append(',');
//                fw.append(rs.getString(8));
//                fw.write("\r\n");
//            }
//            fw.flush();
//            fw.close();
//            connection.close();
//        } catch(Exception e) {
//            e.printStackTrace();
//            stmt = null;
//
//        }
//    }


    /**
     * UpdateUser
     *
     *
     */
    public static void updateUser(String ID, User user, Connection conn){
        try {

            if(ID == null  || ID == ""){
            PreparedStatement ps = conn.prepareStatement("UPDATE USERS " +
                    "SET USERID ='"+user.getUserID()+"'," +
                    " PERMISSION = "+ user.getPermissionsNumber() +"," +
                    " USERNAME = '"+ user.getUsername() +"' where USERID = '"+ID +"'");
            ps.execute();}else{
                addUser(user,conn);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * AddUser
     *
     *
     */
    public static void addUser(User user,Connection conn){
        try {
            PreparedStatement s = conn.prepareStatement("insert into USERS (userid, permission, username, password) \n" +
                    "values ('"+ user.getUserID() +"',"+ user.getPermissionsNumber()+",'"+user.getUsername()+"','"+Encryptor.encrypt(user.getPassword())+"')");
            s.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    /**
     * ClearData
     * Drops all data stored in Nodes and Edges
     */
    public void clearData(Connection connection) {
        try {
            Statement s = connection.createStatement();
            s.execute("DELETE from NODES where 1=1");
            s.execute("DELETE from EDGES where 1=1");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }




}










