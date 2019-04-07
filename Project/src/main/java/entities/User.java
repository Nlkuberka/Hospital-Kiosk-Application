package entities;

public class User {
    public static final int GUEST_PERMISSIONS = 1;
    public static final int BASIC_PERMISSIONS = 2;
    public static final int ADMIN_PERMISSIONS = 3;

    private String userID;
    private String username;
    private String password;
    private int permissions;

    public User() {
    }

    public User(String userID, String username, String password, int permissions) {
        this.userID = userID;
        this.username = username;
        this.password = password;
        this.permissions = permissions;
    }

    public User(String userID, String username, int permissions) {
        this.userID = userID;
        this.username = username;
        this.permissions = permissions;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

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

    public int getPermissions() {
        return permissions;
    }

    public void setPermissions(int permissions) {
        this.permissions = permissions;
    }
}
