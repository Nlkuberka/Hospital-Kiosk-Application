package edu.wpi.cs3733.d19.teamD;

public class User {

    public static final int BASIC_PERMISSIONS = 1;
    public static final int ADMIN_PERMISSIONS = 2;

    private String userID;
    private String username;
    private String password;
    private int permissions;

    public User() {
    }

    public User(String userID, int permissions, String username, String password) {
        this.userID = userID;
        this.permissions = permissions;
        this.username = username;
        this.password = password;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public int getPermissions() { return permissions; }

    public void setPermissions(int permissions) { this.permissions = permissions; }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String toString() {
        String returnValue = "edu.wpi.cs3733.d19.teamD.User(";
        returnValue += userID + ", ";
        returnValue += username + ", ";
        returnValue += password + ", ";
        returnValue += permissions + "";
        returnValue += ")";
        return returnValue;
    }

}
