package entities;

import java.time.LocalTime;
import java.util.List;

/**
 * The Object that holds data about a particular reservation
 * @version iteration1
 */
public class Reservation {
    private String nodeID;
    private String userID;
    private String date;
    private String startTime;
    private String endTime;
    private int rsvID;

    public Reservation() {
    }

    public Reservation(String nodeID, String userID, String date, String startTime, String endTime) {
        this.nodeID = nodeID;
        this.userID = userID;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
    }
    public Reservation(String nodeID, String userID, String date, String startTime, String endTime, int rsvID) {
        this.nodeID = nodeID;
        this.userID = userID;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.rsvID = rsvID;
    }

    public String getNodeID() {
        return nodeID;
    }

    public void setNodeID(String nodeID) {
        this.nodeID = nodeID;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public int getRsvID() { return rsvID; }

    public void setRsvID(int rsvID) { this.rsvID = rsvID; }

    public String toString() {
        String returnValue = "Node(";
        returnValue += getNodeID() + ", ";
        returnValue += getUserID() + ", ";
        returnValue += getDate() + ", ";
        returnValue += getStartTime() + ", ";
        returnValue += getEndTime() + ")";
        return  returnValue;
    }

    /**
     * Returns whether this current reservations would be valid in the given list of reservations
     * @param reservations The list of reservations to compare to
     * @return Whether this reservation can actually fit into that list
     */
    public boolean isValid(List<Reservation> reservations) {
        LocalTime startTimeLocal = LocalTime.parse(startTime);
        LocalTime endTimeLocal = LocalTime.parse(endTime);
        for(int i = 0 ; i < reservations.size(); i++) {
            LocalTime otherStartTimeLocal = LocalTime.parse(reservations.get(i).getStartTime());
            LocalTime otherEndTimeLocal = LocalTime.parse(reservations.get(i).getEndTime());
            // Tests to ensure that the current reservation is either completely before or after the reservation in the list
            if((endTimeLocal.compareTo(otherStartTimeLocal) >= 0) && (startTimeLocal.compareTo(otherEndTimeLocal) <= 0)) {
                return false;
            }
        }
        return true;
    }
}
