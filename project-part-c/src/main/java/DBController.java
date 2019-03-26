
import org.apache.derby.client.am.SqlException;

import java.io.FileWriter;
import java.sql.*;
import java.util.LinkedList;


public class DBController {

    public DBController(){

    }


    public void DBConnect(){
        Connection connection = null;
        try {
            // substitute your database name for myDB
            connection = DriverManager.getConnection("jdbc:derby:myDB;create=true");
            connection.setSchema("APP");
            connection.close();
        } catch (SQLException e) {
            System.out.println("Connection failed. Check output console.");
            e.printStackTrace();
            return;
        }
    }


    public void enterData(LinkedList<Node> nodes){
        try {
            Connection connection = DriverManager.getConnection("jdbc:derby:myDB");
            Statement s = connection.createStatement();
            for (Node node : nodes) {
                s.executeQuery("insert into NODES " +
                        "values (" + node.getNodeID() + "," +
                        " "+ node.getXcoord() +"," +
                        " "+ node.getYcoord() +"," +
                        " "+ node.getFloor() +"," +
                        " "+ node.getBuilding() +"," +
                        " "+ node.getNodeType() +"," +
                        " "+ node.getLongName() +"," +
                        " "+ node.getShortName() +")");
            }
            connection.close();
        }catch(SQLException e) {
            e.printStackTrace();
        }
    }
    public void updateNode(Node node){
        try{
            Connection connection = DriverManager.getConnection("jdbc:derby:myDB");
            Statement s = connection.createStatement();
            s.executeQuery("UPDATE NODES" +
                    "Set xcoord ="+ node.getXcoord() +","+
                    "ycoord ="+ node.getYcoord() + ","+
                    "floor ="+ node.getFloor() + ","+
                    "building ="+ node.getBuilding() + ","+
                    "nodetype ="+ node.getNodeType() + ","+
                    "longname ="+ node.getLongName() + ","+
                    "shortname ="+ node.getShortName() +
                    "where nodeid=" + node.getNodeID()
            );
        }catch(SQLException e){

        }
    }

    public void exportData(String filename) {
        Connection connection = null;
        Statement stmt;
        String query = "Select * from nodes";
        try {
            connection = DriverManager.getConnection("jdbc:derby:myDB");
            stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            FileWriter fw = new FileWriter(filename);
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
