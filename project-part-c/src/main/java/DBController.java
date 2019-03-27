
import org.apache.derby.client.am.SqlException;

import javax.swing.plaf.nimbus.State;
import java.io.*;
import java.sql.*;
import java.util.LinkedList;
import java.util.List;


public class DBController {


    Connection connection;

    public DBController() {
        try {
            connection = DriverManager.getConnection("jdbc:derby:myDB;create=true");

        }catch(SQLException e){
            e.printStackTrace();
        }
    }

    /**
     * enterData
     *
     * enters node values to existing node table
     * @param nodes list of nodes read from a CSV file
     */
    public void enterData(List<Node> nodes){
        try {
            //connection = DriverManager.getConnection("jdbc:derby:myDB");
            Statement s = connection.createStatement();
            for (Node node : nodes) {
                nodeInsert(s,node);
            }
            //connection.close();
        }catch(SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * updateNode
     *
     * updates node of given id, overriding all fields
     * @param node desired node content -- Must have an existing ID --
     */
    public void updateNode(Node node){
        try{
            //connection = DriverManager.getConnection("jdbc:derby:myDB");
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

            //connection.close();
        }catch(SQLException e){
            e.printStackTrace();
        }
    }

    /**
     * delete node
     *
     * deletes node of given ID
     * @param ID of node to be deleted
     */
    public void deleteNode(String ID){
        try {
            //connection = DriverManager.getConnection("jdbc:derby:myDB");
            Statement s = connection.createStatement();
            s.execute("Delete from NODES where NODEID ="+ ID);
            //connection.close();


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
    public void addNode(Node node){
       try{
           //connection = DriverManager.getConnection("jdbc:derby:myDB");
           Statement s = connection.createStatement();
           nodeInsert(s,node);
           //connection.close();
       }catch(SQLException e){
           e.printStackTrace();
       }
    }

    /**
     * generateListofNodes
     *
     * creates and returns a list of node objects
     * @return
     */
    public LinkedList<Node> generateListofNodes(){
        try{
            //connection = DriverManager.getConnection("jdbc:derby:myDB");
            Statement s = connection.createStatement();
            ResultSet rs = s.executeQuery("SELECT * from NODES");
            LinkedList<Node> listOfNodes = new LinkedList<Node>();
            while(rs.next()){
                Node node = new Node(rs.getString(1),rs.getInt(2),rs.getInt(3),
                                        rs.getInt(4),rs.getString(5),rs.getString(6),
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
    public void nodeInsert(Statement s, Node node){
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

    /**
     * exportData
     *
     * selects all content held in Nodes table and prints it to a file
     * @param filename name of output file
     */
    public void exportData(String filename) {
        Connection connection = null;
        Statement stmt;
        String query = "Select * from nodes";
        try {
            connection = DriverManager.getConnection("jdbc:derby:myDB");
            stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            File file = new File(filename);
            FileWriter fw = new FileWriter(filename);
            fw.write("nodeID, xcoord, ycoord, floor, building, nodeType, longName, shortName \r\n");
            while(rs.next()) {
                fw.append(rs.getString(1));
                fw.append(',');
                fw.append(rs.getString(2));
                fw.append(',');
                fw.append(rs.getString(3));
                fw.append(',');
                fw.append(rs.getString(4));
                fw.append(',');
                fw.append(rs.getString(5));
                fw.append(',');
                fw.append(rs.getString(6));
                fw.append(',');
                fw.append(rs.getString(7));
                fw.append(',');
                fw.append(rs.getString(8));
                fw.write("\r\n");
            }
            fw.flush();
            fw.close();
            connection.close();
        } catch(Exception e) {
            e.printStackTrace();
            stmt = null;

        }
    }
}
