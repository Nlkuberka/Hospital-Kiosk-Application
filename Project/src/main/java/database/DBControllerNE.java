package database;

import application.UIController;
import entities.Edge;
import entities.Node;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.sql.*;
import java.util.LinkedList;
import java.util.List;

public class DBControllerNE extends DBController{


    public static final String ALL_NODES = "SELECT * FROM NODES";
    public static final String ALL_EDGES = "SELECT * FROM EDGES";

    public static final String ALL_NODES_FLOOR_L2 = "SELECT * FROM NODES WHERE FLOOR = 'L2'";
    public static final String ALL_NODES_FLOOR_L1 = "SELECT * FROM NODES WHERE FLOOR = 'L1'";
    public static final String ALL_NODES_FLOOR_G = "SELECT * FROM NODES WHERE FLOOR = 'G'";
    public static final String ALL_NODES_FLOOR_1 = "SELECT * FROM NODES WHERE FLOOR = '1'";
    public static final String ALL_NODES_FLOOR_2 = "SELECT * FROM NODES WHERE FLOOR = '2'";
    public static final String ALL_NODES_FLOOR_3 = "SELECT * FROM NODES WHERE FLOOR = '3'";

    public static final String ALL_ROOMS = "SELECT * FROM NODES WHERE NODETYPE != 'HALL' and NODETYPE != 'STAI' and NODETYPE != 'ELEV'";
    public static final String ALL_ROOMS_FLOOR_L2 = "SELECT * FROM NODES WHERE FLOOR = 'L2' AND NODETYPE != 'HALL' and NODETYPE != 'STAI' and NODETYPE != 'ELEV'";
    public static final String ALL_ROOMS_FLOOR_L1 = "SELECT * FROM NODES WHERE FLOOR = 'L1' AND NODETYPE != 'HALL' and NODETYPE != 'STAI' and NODETYPE != 'ELEV'";
    public static final String ALL_ROOMS_FLOOR_G = "SELECT * FROM NODES WHERE FLOOR = 'G' AND NODETYPE != 'HALL' and NODETYPE != 'STAI' and NODETYPE != 'ELEV'";
    public static final String ALL_ROOMS_FLOOR_1 = "SELECT * FROM NODES WHERE FLOOR = '1' AND NODETYPE != 'HALL' and NODETYPE != 'STAI' and NODETYPE != 'ELEV'";
    public static final String ALL_ROOMS_FLOOR_2 = "SELECT * FROM NODES WHERE FLOOR = '2' AND NODETYPE != 'HALL' and NODETYPE != 'STAI' and NODETYPE != 'ELEV'";
    public static final String ALL_ROOMS_FLOOR_3 = "SELECT * FROM NODES WHERE FLOOR = '3' AND NODETYPE != 'HALL' and NODETYPE != 'STAI' and NODETYPE != 'ELEV'";

    public static final String ALL_BUT_ROOMS_L2 = "SELECT * FROM NODES WHERE FLOOR = 'L2' AND (NODETYPE = 'STAI' OR NODETYPE = 'ELEV')";
    public static final String ALL_BUT_ROOMS_L1 = "SELECT * FROM NODES WHERE FLOOR = 'L1' AND (NODETYPE = 'STAI' OR NODETYPE = 'ELEV')";
    public static final String ALL_BUT_ROOMS_G = "SELECT * FROM NODES WHERE FLOOR = 'G' AND (NODETYPE = 'STAI' OR NODETYPE = 'ELEV')";
    public static final String ALL_BUT_ROOMS_1 = "SELECT * FROM NODES WHERE FLOOR = '1' AND (NODETYPE = 'STAI' OR NODETYPE = 'ELEV')";
    public static final String ALL_BUT_ROOMS_2 = "SELECT * FROM NODES WHERE FLOOR = '2' AND (NODETYPE = 'STAI' OR NODETYPE = 'ELEV')";
    public static final String ALL_BUT_ROOMS_3 = "SELECT * FROM NODES WHERE FLOOR = '3' AND (NODETYPE = 'STAI' OR NODETYPE = 'ELEV')";


    public static final String ALL_BATHROOMS = "SELECT * FROM NODES WHERE NODETYPE = 'BATH'";
    public static final String ALL_EXITS = "SELECT * FROM NODES WHERE NODETYPE = 'EXIT";
    public static final String ALL_CONFERENCE_ROOMS = "SELECT * FROM NODES WHERE NODETYPE = 'CONF'";
    public static final String ALL_DEPARTMENT_ROOMS = "SELECT * FROM NODES WHERE NODETYPE = 'DEPT'";
    public static final String ALL_ELEVATOR_ROOMS = "SELECT * FROM NODES WHERE NODETYPE = 'ELEV'";
    public static final String ALL_INFOROMATION_DESKS = "SELECT * FROM NODES WHERE NODETYPE = 'INFO'";
    public static final String ALL_LABS = "SELECT * FROM NODES WHERE NODETYPE = 'LABS'";

    public static final String ALL_HALLS_FLOOR_L2 = "SELECT * FROM NODES WHERE FLOOR = 'L2' AND NODETYPE = 'HALL'";
    public static final String ALL_HALLS_FLOOR_L1 = "SELECT * FROM NODES WHERE FLOOR = 'L1' AND NODETYPE = 'HALL'";
    public static final String ALL_HALLS_FLOOR_G = "SELECT * FROM NODES WHERE FLOOR = 'G' AND NODETYPE = 'HALL'";
    public static final String ALL_HALLS_FLOOR_1 = "SELECT * FROM NODES WHERE FLOOR = '1' AND NODETYPE = 'HALL'";
    public static final String ALL_HALLS_FLOOR_2 = "SELECT * FROM NODES WHERE FLOOR = '2' AND NODETYPE = 'HALL'";
    public static final String ALL_HALLS_FLOOR_3 = "SELECT * FROM NODES WHERE FLOOR = '3' AND NODETYPE = 'HALL'";

    public static final String ALL_DEPARTMENT_ROOMS_FLOOR_L2 = "SELECT * FROM NODES WHERE FLOOR = 'L2' AND NODETYPE = 'DEPT'";
    public static final String ALL_DEPARTMENT_ROOMS_FLOOR_L1 = "SELECT * FROM NODES WHERE FLOOR = 'L1' AND NODETYPE = 'DEPT'";
    public static final String ALL_DEPARTMENT_ROOMS_FLOOR_G = "SELECT * FROM NODES WHERE FLOOR = 'G' AND NODETYPE = 'DEPT'";
    public static final String ALL_DEPARTMENT_ROOMS_FLOOR_1 = "SELECT * FROM NODES WHERE FLOOR = '1' AND NODETYPE = 'DEPT'";
    public static final String ALL_DEPARTMENT_ROOMS_FLOOR_2 = "SELECT * FROM NODES WHERE FLOOR = '2' AND NODETYPE = 'DEPT'";
    public static final String ALL_DEPARTMENT_ROOMS_FLOOR_3 = "SELECT * FROM NODES WHERE FLOOR = '3' AND NODETYPE = 'DEPT'";

    public static final String ALL_ELEVATORS_FLOOR_L2 = "SELECT * FROM NODES WHERE FLOOR = 'L2' AND NODETYPE = 'ELEV'";
    public static final String ALL_ELEVATORS_FLOOR_L1 = "SELECT * FROM NODES WHERE FLOOR = 'L1' AND NODETYPE = 'ELEV'";
    public static final String ALL_ELEVATORS_FLOOR_G = "SELECT * FROM NODES WHERE FLOOR = 'G' AND NODETYPE = 'ELEV'";
    public static final String ALL_ELEVATORS_FLOOR_1 = "SELECT * FROM NODES WHERE FLOOR = '1' AND NODETYPE = 'ELEV'";
    public static final String ALL_ELEVATORS_FLOOR_2 = "SELECT * FROM NODES WHERE FLOOR = '2' AND NODETYPE = 'ELEV'";
    public static final String ALL_ELEVATORS_FLOOR_3 = "SELECT * FROM NODES WHERE FLOOR = '3' AND NODETYPE = 'ELEV'";

    /**
     * loadNodeData
     *
     * reads and stores node data from given csv file
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


    private static Node buildNode(ResultSet rs){
    try {
        Node node = new Node(rs.getString("NODEID"), rs.getInt("XCOORD"), rs.getInt("YCOORD"),
                    rs.getString("FLOOR"), rs.getString("BUILDING"), rs.getString("NODETYPE"),
                    rs.getString("LONGNAME"), rs.getString("SHORTNAME"));
        return node;
    }catch(SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static Edge buildEdge(ResultSet rs){
        try {
            Edge edge = new Edge(rs.getString(1),rs.getString(2),rs.getString(3));
            return edge;
        }catch(SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * updateNode
     *
     * updates node of given id, overriding all fields
     * @param node desired node content -- Must have an existing ID --
     */
    public static void updateNode(Node node, Connection connection){
        try{
            PreparedStatement ps = connection.prepareStatement(
                    "UPDATE NODES SET XCOORD = ?, YCOORD = ? ," +
                    " FLOOR = ? ,BUILDING = ?, NODETYPE = ?," +
                    "LONGNAME = ?,SHORTNAME = ? where NODEID = ?"
            );
            ps.setInt(1,node.getXcoord());
            ps.setInt(2,node.getYcoord());
            ps.setString(3,node.getFloor());
            ps.setString(4,node.getBuilding());
            ps.setString(5,node.getNodeType());
            ps.setString(6,node.getLongName());
            ps.setString(7,node.getShortName());
            ps.setString(8,node.getNodeID());
            ps.execute();
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
            PreparedStatement ps = connection.prepareStatement("Select * from NODES where NODEID = ?");
            ps.setString(1,ID);
            ResultSet rs = ps.executeQuery();
            rs.next();
            node = buildNode(rs);
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
            PreparedStatement ps = connection.prepareStatement("Select * from EDGES where EDGEID = ?");
            ps.setString(1,ID);
            ResultSet rs = ps.executeQuery();
            rs.next();
            edge = buildEdge(rs);
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
            PreparedStatement s1 = connection.prepareStatement("DELETE from EDGES where ENDNODE = ? OR STARTNODE = ?");
            PreparedStatement s2 = connection.prepareStatement("Delete from NODES where NODEID = ?");
            s1.setString(1,ID);
            s1.setString(2,ID);
            s2.setString(1,ID);
            s1.execute();
            s2.execute();
        }catch(SQLException e){
            e.printStackTrace();
        }
    }

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
            PreparedStatement ps = connection.prepareStatement("DELETE from EDGES where EDGEID = ?");
            ps.setString(1,ID);
            ps.execute();
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
    public static boolean addNode(Node node, Connection connection){
        try{
            Statement s = connection.createStatement();
            if(nodeInsert(s,node)){
                return true;
            }else{
                return false;
            }
        }catch(SQLException e){
            e.printStackTrace();
            return false;
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
     * generateListofNodes
     *
     * creates and returns a list of node objects
     * @return LinkedList<Node>
     */
    public static LinkedList<Node> generateListOfNodes(Connection connection,String query){
        try{
            Statement s = connection.createStatement();
            ResultSet rs = s.executeQuery(query);
            LinkedList<Node> listOfNodes = new LinkedList<>();
            while(rs.next()){
                listOfNodes.add(buildNode(rs));
            }
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
                listofEdges.add(buildEdge(rs));
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
    public static boolean nodeInsert(Statement s, Node node){
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
            UIController ui = new UIController();
            ui.popupMessage("Duplicate NodeID", true);
            return false;
            //e.printStackTrace();
        }
        return true;
    }



}

