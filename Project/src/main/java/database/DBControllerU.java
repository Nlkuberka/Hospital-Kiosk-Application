package database;

import application.Encryptor;
import entities.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;

public class DBControllerU extends DBController {
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
