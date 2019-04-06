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

    public Reservation() {
    }

    public Reservation(String nodeID, String userID, String date, String startTime, String endTime) {
        this.nodeID = nodeID;
        this.userID = userID;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
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
            if(!oneSided(startTimeLocal, otherStartTimeLocal, otherEndTimeLocal) || !oneSided(endTimeLocal, otherStartTimeLocal, otherEndTimeLocal)
                || !oneSided(otherStartTimeLocal, startTimeLocal, endTimeLocal) || !oneSided(otherEndTimeLocal, startTimeLocal, endTimeLocal)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Whether the second and third LocalTimes are on the same side compared to the first LocalTime
     * @param lt1 The first LocalTime to compare to
     * @param lt2 The second LocalTime
     * @param lt3 The third LocalTime
     * @return Whether the third and second LocalTimes are on one side of the first
     */
    public boolean oneSided(LocalTime lt1, LocalTime lt2, LocalTime lt3) {
        return (lt1.compareTo(lt2) == lt1.compareTo(lt3));
    }
}
