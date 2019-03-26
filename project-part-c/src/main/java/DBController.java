
import org.apache.derby.client.am.SqlException;

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
//    public void updateNode(Node node){
//        try{
//            Connection connection = DriverManager.getConnection("jdbc:derby:myDB");
//            Statement s = connection.createStatement();
//            s.executeQuery("UPDATE NODES WHERE NODEID =" + node.getNodeID()" +
//                    " ");
//        }catch(SQLException e){
//
//        }
//    }

    public void exportData(String filename) {
        Connection connection = null;
        Statement stmt;
        String query;
        try {
            stmt = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,
                    ResultSet.CONCUR_UPDATABLE);

            //For comma separated file
            query = "SELECT id,text,price into OUTFILE  '"+filename+
                    "' FIELDS TERMINATED BY ',' FROM testtable t";
            stmt.executeQuery(query);
            connection.close();
        } catch(Exception e) {
            e.printStackTrace();
            stmt = null;
        }
    }
}
