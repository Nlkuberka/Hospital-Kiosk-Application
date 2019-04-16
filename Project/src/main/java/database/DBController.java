package database;

import entities.Reservation;
import entities.User;

import java.io.File;
import java.sql.*;
import java.util.LinkedList;

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



        createTable(nodes,conn);
        createTable(edges,conn);
        createTable(user,conn);
        createTable(servicerequest,conn);
        createTable(workplaces, conn);
        createTable(reservations,conn);

        DBControllerNE.loadNodeData(new File("nodesv5.csv"),conn);
        DBControllerNE.loadEdgeData(new File("edgesv5.csv"),conn);
        DBControllerRW.loadWorkplaceData(new File( "workplaces.csv"),conn);

        DBControllerU.addUser(new User("USER0001","user","user",3071),conn);
        DBControllerU.addUser(new User("GUEST0001","guest","guest",1024),conn);
        DBControllerU.addUser(new User("ADMIN00001","admin","admin",4095),conn);
        DBControllerU.addUser(new User("WWONG2","staff","staff",4095),conn);

        DBControllerU.loadTeam(conn);
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











