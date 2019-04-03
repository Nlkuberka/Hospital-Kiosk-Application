import com.sun.org.apache.bcel.internal.generic.RETURN;

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
 * @author imoralessirgo
 * @version iteration1
 */

public class DBController {
    // Connection connection;


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

    public static void closeConnection(Connection connection){
        try{
            connection.close();
        }catch(SQLException e){
            e.printStackTrace();
        }
    }

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

    public static void createTable(String createStatement, Connection conn){
        try {
            conn.createStatement().execute(createStatement);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

//    public void enterData(List<Node> nodes, Connection connection){
//        try {
//            //connection = DriverManager.getConnection("jdbc:derby:myDB");
//            Statement s = connection.createStatement();
//            for (Node node : nodes) {
//                nodeInsert(s,node);
//            }
//            //connection.close();
//        }catch(SQLException e) {
//            e.printStackTrace();
//        }
//    }

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
                    "FLOOR ="+ node.getFloor() + ","+
                    "BUILDING ='"+ node.getBuilding() + "',"+
                    "NODETYPE = '"+ node.getNodeType() + "',"+
                    "LONGNAME = '"+ node.getLongName() + "',"+
                    "SHORTNAME = '"+ node.getShortName() +"'"+
                    "where NODEID = '" + node.getNodeID() +"'");
        }catch(SQLException e){
            e.printStackTrace();
        }
    }

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

    public static void updateServiceRequest(ServiceRequest serviceRequest, Connection connection){
        try{
            Statement s = connection.createStatement();
            s.execute("UPDATE SERVICEREQUEST SET  SERVICETYPE ='"+ serviceRequest.getServiceType() +"',"+
                    "MESSAGE = '"+ serviceRequest.getMessage() + "'," +
                    "RESOLVED = '" + serviceRequest.isResolved() + "'," +
                    "RESOLVERID = '"+serviceRequest.getResolverID()+"' " +
                    "where  NODEID = '" + serviceRequest.getNodeID() + "' and " +
                    "USERID = '" + serviceRequest.getUserID() + "'");

        }catch(SQLException e){
            e.printStackTrace();
        }
    }

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

    public static void deleteEdge(String ID, Connection connection){
        try {
            Statement s = connection.createStatement();
            s.execute("DELETE from EDGES where EDGEID = '"+ ID +"'");

        }catch(SQLException e){
            e.printStackTrace();
        }
    }

    public static void deleteServiceRequest(String NODEID,String USERID, Connection connection){
        try {
            Statement s = connection.createStatement();
            s.execute("delete  from SERVICEREQUEST where NODEID ='"+ NODEID +"' and USERID ='" + USERID + "'");
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
            //connection = DriverManager.getConnection("jdbc:derby:myDB");
            Statement s = connection.createStatement();
            nodeInsert(s,node);
            //connection.close();
        }catch(SQLException e){
            e.printStackTrace();
        }
    }

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

    public static void addServiceRequest(ServiceRequest serviceRequest, Connection connection){
        try{
            Statement s = connection.createStatement();
            s.execute("INSERT into SERVICEREQUEST  values ('" + serviceRequest.getNodeID() +
                    "','"+ serviceRequest.getServiceType() +"','"+ serviceRequest.getMessage() + "','"+
                    serviceRequest.getUserID()+"',"+serviceRequest.isResolved()+","+ serviceRequest.getResolverID() +")");
        }catch(SQLException e){
            e.printStackTrace();
        }
    }


    /**
     * generateListofNodes
     *
     * creates and returns a list of node objects
     * @return LinkedList<Node>
     */
    public LinkedList<Node> generateListofNodes(Connection connection){
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
                    +node.getYcoord()+","+
                    node.getFloor() + "," +
                    " '" + node.getBuilding() + "'," +
                    " '" + node.getNodeType() + "'," +
                    " '" + node.getLongName() + "'," +
                    " '" + node.getShortName() + "')");
        }catch(SQLException e){
            e.printStackTrace();
        }
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
