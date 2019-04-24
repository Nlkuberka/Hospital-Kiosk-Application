package application;

import entities.User;
import network.DBNetwork;

/**
 * An class with static variables to store information about the current user
 * @version iteration1
 */
public class CurrentUser {
    public static final int AALOGRITHM = 1;
    public static final int DFSEARCH = 2;
    public static final int BFSEARCH = 3;
    public static final int BELLMAN_FORD = 4;
    public static final int DIJKSTRA = 5;

    public static User user;
    public static String startingLocation = "Shattuck Street Lobby ATM";
    public static String startingLocationID = "FSERV00101";
    public static DBNetwork network;
    public static boolean testing = false;

    public static String getStartingLocation() {
        return startingLocation;
    }

    public static void setStartingLocation(String startingLocation) {
        CurrentUser.startingLocation = startingLocation;
    }

    public static String getStartingLocationID() {
        return startingLocationID;
    }

    public static void setStartingLocationID(String startingLocationID) {
        CurrentUser.startingLocationID = startingLocationID;
    }
}
