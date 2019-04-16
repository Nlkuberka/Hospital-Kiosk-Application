package application;

import entities.User;
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
}
