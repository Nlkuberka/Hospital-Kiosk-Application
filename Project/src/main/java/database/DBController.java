package database;

import application.Encryptor;
import entities.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.io.*;
import java.sql.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
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
                "  WPIID VARCHAR(11),\n" +
                "  CONSTRAINT USER_PK PRIMARY KEY(USERID),\n" +
                "  CONSTRAINT UN_UN UNIQUE (USERNAME),\n" +
                "  CONSTRAINT WPI_UN UNIQUE (WPIID)\n" +
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
        String workplaces = "CREATE TABLE WORKPLACES(\n" +
                " WKPLACEID VARCHAR(10),\n" +
                " ROOMNAME VARCHAR(50),\n" +
                " CAPACITY INT,\n" +
                " OUTLINE VARCHAR(150),\n" +
                " CONSTRAINT WK_PK PRIMARY KEY(WKPLACEID) " +
                ")\n";
        String reservations = "CREATE TABLE RESERVATIONS(\n" +
                "  RSVID INTEGER GENERATED ALWAYS AS IDENTITY,\n" +
                "  WKPLACEID VARCHAR(10) REFERENCES WORKPLACES(WKPLACEID),\n" +
                "  USERID VARCHAR(10) REFERENCES USERS(USERID),\n" +
                "  DAY DATE,\n" +
                "  STARTTIME TIME,\n" +
                "  ENDTIME TIME,\n" +
                "  CONSTRAINT RSV_PK PRIMARY KEY(RSVID)\n" +
                ")\n";


        System.out.println(user);
        createTable(nodes,conn);
        createTable(edges,conn);
        createTable(user,conn);
        createTable(servicerequest,conn);
        createTable(workplaces, conn);
        createTable(reservations,conn);

        DBControllerNE.loadNodeData(new File("nodesv5.csv"),conn);
        DBControllerNE.loadEdgeData(new File("edgesv5.csv"),conn);
        DBControllerRW.loadWorkplaceData(new File( "workplaces.csv"),conn);

        DBControllerU.addUser(new User("USER0001","user","user",2816),conn);
        DBControllerU.addUser(new User("GUEST0001","guest","guest",1024),conn);
        DBControllerU.addUser(new User("ADMIN00001","admin","admin",4032),conn);
        DBControllerU.addUser(new User("WWONG2","staff","staff",4032),conn);

        DBControllerRW.addReservation(new Reservation("CL001","WWONG2","2019-04-18","10:00:00","12:00:00"),conn);
        DBControllerRW.addReservation(new Reservation("CL002","WWONG2","2019-04-18","11:00:00","13:00:00"),conn);
        DBControllerRW.addReservation(new Reservation("CL003","WWONG2","2019-04-19","08:00:00","13:00:00"),conn);
        DBControllerRW.addReservation(new Reservation("CL001","WWONG2","2019-04-18","14:00:00","17:00:00"),conn);
        DBControllerRW.addReservation(new Reservation("CL005","WWONG2","2019-04-19","11:00:00","13:00:00"),conn);


        DBControllerU.loadTeam(conn);

        DBControllerU.teamID("TM0001","11349566301",conn);//jon
        DBControllerU.teamID("TM0002","78200284901",conn);//joe
        DBControllerU.teamID("TM0003","11595001701",conn);//ryan
//        DBControllerU.teamID("TM0004","67296274501",conn);//shiyi
//        DBControllerU.teamID("TM0005","67296274501",conn);//nicole
//        DBControllerU.teamID("TM0006","67296274501",conn);//dimitri
//        DBControllerU.teamID("TM0007","67296274501",conn);//Rakesh
        DBControllerU.teamID("TM0008","40179988401",conn);//henry
//        DBControllerU.teamID("TM0009","67296274501",conn);//panos
        DBControllerU.teamID("TM0010","67296274501",conn);//isabel
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

    public static LinkedList<Reservation> getResForRoom(String ID, String Date, Connection conn){
        LinkedList<Reservation> list = new LinkedList<Reservation>();
        try {
            PreparedStatement ps = conn.prepareStatement("Select * from RESERVATIONS where WKPLACEID ='"+ID+"' and DAY ='"+Date+"'");
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                Reservation r = new Reservation(rs.getString(2),rs.getString(3),rs.getString(4),
                        rs.getString(5),rs.getString(6));
                list.add(r);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
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


}











