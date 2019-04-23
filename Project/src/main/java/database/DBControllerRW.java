package database;


import application.CurrentUser;
import com.calendarfx.model.Entry;
import com.calendarfx.model.Interval;
import entities.Reservation;
import entities.Workplace;
import network.DBNetwork;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.LinkedList;

/**
 * Database controller for Reservations/Workspace
 *
 * @author imoralessirgo
 */

public class DBControllerRW extends DBController {

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
                    " where RSVID = '" + reservation.getRsvID()+"'");
            CurrentUser.network.sendReservationPacket(DBNetwork.UPDATE_RESERVATION, reservation);
        }catch(SQLException e) {
            e.printStackTrace();
        }
    }

    public static void deleteReservation(String reservationID,Connection connection){
        try {
            Statement s = connection.createStatement();
            s.execute("delete from RESERVATIONS where RSVID = '"+ reservationID +"'");
            Reservation reservation = new Reservation();
            reservation.setRsvID(reservationID);
            CurrentUser.network.sendReservationPacket(DBNetwork.DELETE_RESERVATION, reservation);
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

            if(isRoomAvailableString(reservation.getWkplaceID(), reservation.getDate(), reservation.getStartTime(), reservation.getEndTime(), connection)) {
                //connection = DriverManager.getConnection("jdbc:derby:myDB");
                PreparedStatement s = connection.prepareStatement("INSERT into RESERVATIONS (WKPLACEID, USERID, DAY, STARTTIME, ENDTIME) values ('" + reservation.getWkplaceID() +"','" + reservation.getUserID() +
                        "','"+ reservation.getDate() +"','"+ reservation.getStartTime() + "','" + reservation.getEndTime() + "')",Statement.RETURN_GENERATED_KEYS);
                s.execute();
                ResultSet rs = s.getGeneratedKeys();
                rs.next();
                CurrentUser.network.sendReservationPacket(DBNetwork.ADD_RESERVATION, reservation);
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

    /**
     * generateListOfUserReservations
     *
     * generates list of reservations made by the given user
     * @param userID - ID of user whose reservations are being accessed
     * @return - LinkedList of all reservations made by a user
     */
    public static LinkedList<Reservation> generateListofUserReservations(String userID , Connection connection){
        try{
            Statement s = connection.createStatement();
            ResultSet rs = s.executeQuery("select * from RESERVATIONS where USERID = '" + userID + "'");
            LinkedList<Reservation> listOfReservations = new LinkedList<Reservation>();
            while(rs.next()){
                Reservation r = new Reservation(rs.getString(2),rs.getString(3), rs.getString(4),
                        rs.getString(5),rs.getString(6),rs.getString(1));
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

//            Time startTime = Time.valueOf(startTimeSTR);
//            Time endTime = Time.valueOf(endTimeSTR);
//            Date date = new SimpleDateFormat("yyyy-MM-dd").parse(dateSTR);

            Statement s = connection.createStatement();
            ResultSet rs = s.executeQuery("select * from RESERVATIONS where WKPLACEID = '" + wkplaceID + "' and DAY = '" + dateSTR + "' and " +
                    "((STARTTIME >= '" + startTimeSTR + "' and ENDTIME <= '" + endTimeSTR + "') " +
                    "OR (STARTTIME < '" + startTimeSTR + "' and ENDTIME > '" + startTimeSTR + "') " +
                    "OR (STARTTIME < '" + endTimeSTR + "' and ENDTIME > '" + endTimeSTR + "'))");
            return !rs.next();
        }catch(SQLException e){
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



    public static LinkedList<Entry> getEntriesforRoom(String WKPID, Connection conn){
        LinkedList<Entry> list = new LinkedList<Entry>();
        try {

            PreparedStatement ps2 = conn.prepareStatement("SELECT ROOMNAME from WORKPLACES where WKPLACEID = ?");
            ps2.setString(1,WKPID);
            ResultSet rs2 = ps2.executeQuery();
            rs2.next();
            String title = rs2.getString(1);
            PreparedStatement ps = conn.prepareStatement("SELECT * from RESERVATIONS where WKPLACEID = ?");
            ps.setString(1,WKPID);
            ResultSet rs = ps.executeQuery();

            while(rs.next()){
                Entry e = new Entry(rs.getString("RSVID"), new Interval(rs.getDate("DAY").toLocalDate(),rs.getTime("STARTTIME").toLocalTime(),rs.getDate("DAY").toLocalDate(),rs.getTime("ENDTIME").toLocalTime()));
                e.setTitle(title);
                list.add(e);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
        return list;
    }

}
