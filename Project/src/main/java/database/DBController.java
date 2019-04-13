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

        DBControllerNE.loadNodeData(new File("nodesv4.csv"),conn);
        DBControllerNE.loadEdgeData(new File("edgesv5.csv"),conn);
        loadWorkplaceData(new File( "workplaces.csv"),conn);

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
     * loadWorkplaceData
     *
     * reads and stores workplace data from given csv file
     *
     * @param file
     * @param connection
     */
    public static void loadWorkplaceData(File file, Connection connection) {
        BufferedReader br = null;
        String line = "";
        String[] arr;
        try {
            br = new BufferedReader(new FileReader(file));
            br.readLine();
            while((line = br.readLine()) != null) {
                arr = line.split(",");
                connection.createStatement().execute("insert into WORKPLACES (wkplaceid, roomname, capacity, outline) " +
                        "values ('"+ arr[0] + "','"+ arr[1]+ "',"+ arr[2]+ ",'"+ arr[3]+"')");
            }
        }catch(Exception e) {
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
     * fetchNode
     *
     * generates an node object from data under given ID
     *
     * @param reservation
     * @param connection
     */
    public static void updateReservation(Reservation reservation, Connection connection) {
        try{
            Statement s = connection.createStatement();
            s.execute("UPDATE RESERVATIONS SET WKPLACEID ='"+ reservation.getWkplaceID() +"'," +
                    "USERID = '"+ reservation.getUserID() + "'," +
                    "DAY = '" + reservation.getDate() + "'," +
                    "STARTTIME = '" + reservation.getStartTime() + "'," +
                    "ENDTIME = '" + reservation.getEndTime() + "'" +
                    " where RSVID = " + reservation.getRsvID());
        }catch(SQLException e) {
            e.printStackTrace();
        }
    }








    public static void deleteReservation(int reservationID,Connection connection){
        try {
            Statement s = connection.createStatement();
            s.execute("delete from RESERVATIONS where RSVID ="+ reservationID +"");
        }catch(SQLException e){
            e.printStackTrace();
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
            Time startTime = Time.valueOf(reservation.getStartTime());
            Time endTime = Time.valueOf(reservation.getEndTime());
            Date date = new SimpleDateFormat("yyyy-MM-dd").parse(reservation.getDate());

            if(DBController.isRoomAvailable(reservation.getWkplaceID(), date, startTime, endTime, connection)) {
                //connection = DriverManager.getConnection("jdbc:derby:myDB");
                PreparedStatement s = connection.prepareStatement("INSERT into RESERVATIONS (WKPLACEID, USERID, DAY, STARTTIME, ENDTIME) values ('" + reservation.getWkplaceID() +"','" + reservation.getUserID() +
                        "','"+ reservation.getDate() +"','"+ reservation.getStartTime() + "','" + reservation.getEndTime() + "')",Statement.RETURN_GENERATED_KEYS);
                s.execute();
                ResultSet rs = s.getGeneratedKeys();
                rs.next();
                return rs.getInt(1);
            }
            else {
                return -1; // Room is already reserved during the requested tie
            }
        }catch(SQLException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
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
     * generateListOfUserReservations
     *
     * generates list of reservations made by the given user
     * @param userID - ID of user whose reservations are being accessed
     * @return - LinkedList of all reservations made by a user
     */
    public static LinkedList<Reservation> generateListofUserReservations(String userID ,Connection connection){
        try{
            Statement s = connection.createStatement();
            ResultSet rs = s.executeQuery("select * from RESERVATIONS where USERID = '" + userID + "'");
            LinkedList<Reservation> listOfReservations = new LinkedList<Reservation>();
            while(rs.next()){
                Reservation r = new Reservation(rs.getString(1),rs.getString(2),rs.getString(3),
                                                rs.getString(4),rs.getString(5));
                listOfReservations.add(r);
            }
            return listOfReservations;
        }catch(SQLException e){
            e.printStackTrace();
        }
        return null;
    }



    /**
     * isRoomAvailable
     *
     * Determines whether a room is available on a certain day within the given time parameters
     * @param wkplaceID - ID of room which is being checked for availability
     * @param day - The day where the room's availability is being checked
     * @param startTime - Check to see if the room is available after this time
     * @param endTime - Check to see if the room is available before this time
     * @return - Whether or not the selected room will be available on the day and times given
     */
    public static boolean isRoomAvailable(String wkplaceID, Date day, Time startTime, Time endTime, Connection connection){
        try{
            //Check if room has any reservations overlapping with the given times
            //Four cases to check:
            //Reservation within the given times, starts before and ends during, starts during and ends after, or room is booked for the whole duration or more
            Statement s = connection.createStatement();
            ResultSet rs = s.executeQuery("select * from RESERVATIONS where WKPLACEID = '" + wkplaceID + "' and DAY = '" + day + "' and " +
                    "((STARTTIME >= '" + startTime + "' and ENDTIME <= '" + endTime + "') " +
                    "OR (STARTTIME < '" + startTime + "' and ENDTIME > '" + startTime + "') " +
                    "OR (STARTTIME < '" + endTime + "' and ENDTIME > '" + endTime + "'))");
            return !rs.next();
        }catch(SQLException e){
            e.printStackTrace();
        }
        return false;
    }

    public static boolean isRoomAvailableString(String wkplaceID, String dateSTR, String startTimeSTR, String endTimeSTR, Connection connection){
        try{
            //Check if room has any reservations overlapping with the given times
            //Four cases to check:
            //Reservation within the given times, starts before and ends during, starts during and ends after, or room is booked for the whole duration or more

            Time startTime = Time.valueOf(startTimeSTR);
            Time endTime = Time.valueOf(endTimeSTR);
            Date date = new SimpleDateFormat("yyyy-MM-dd").parse(dateSTR);

            Statement s = connection.createStatement();
            ResultSet rs = s.executeQuery("select * from RESERVATIONS where WKPLACEID = '" + wkplaceID + "' and DAY = '" + date + "' and " +
                    "((STARTTIME >= '" + startTime + "' and ENDTIME <= '" + endTime + "') " +
                    "OR (STARTTIME < '" + startTime + "' and ENDTIME > '" + startTime + "') " +
                    "OR (STARTTIME < '" + endTime + "' and ENDTIME > '" + endTime + "'))");
            return !rs.next();
        }catch(SQLException e){
            e.printStackTrace();
        }catch(ParseException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * unavailableRooms
     *
     * Determines which rooms are available on the given date, and within the given times.
     * @param day - The day where the availability of all rooms is being checked
     * @param startTime - Check to see if each room is available after this time
     * @param endTime - Check to see if each room is available before this time
     * @return - A list of all the available rooms within the given parameters
     */
    public static LinkedList<Workplace> unavailableRooms(Date day, Time startTime, Time endTime, Connection connection){
        try {
            Statement s = connection.createStatement();
            ResultSet rs = s.executeQuery("SELECT * from WORKPLACES");
            LinkedList<Workplace> unavailableRooms = new LinkedList<>();
            while(rs.next()){
                Workplace room = new Workplace(rs.getString(1),rs.getString(2),rs.getInt(3),
                        rs.getString(4));
                if(!isRoomAvailable(room.getWkplaceID(),day,startTime,endTime,connection)){
                    unavailableRooms.add(room);
                }
            }
            return unavailableRooms;
        }catch(SQLException e){
            e.printStackTrace();
        }
        return null;
    }



    /**
     * exportData
     *
     * selects all content held in Nodes table and prints it to a file

     */

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

            if(!(ID == null  || ID == "")){
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


}










