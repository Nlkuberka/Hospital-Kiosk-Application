package entities;

import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.Date;
import java.util.List;

/**
 * The Object that holds data about a particular reservation
 * @version iteration1
 */
public class Reservation {
    private String wkplaceID;
    private String userID;
    private String date;
    private String startTime;
    private String endTime;
    private String rsvID;

    public Reservation() {
    }

    public Reservation(String wkplaceID, String userID, String date, String startTime, String endTime) {
        this.wkplaceID = wkplaceID;
        this.userID = userID;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
    }
    public Reservation(String wkplaceID, String userID, String date, String startTime, String endTime, String rsvID) {
        this.wkplaceID = wkplaceID;
        this.userID = userID;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.rsvID = rsvID;
    }

    public String getWkplaceID() {
        return wkplaceID;
    }

    public void setWkplaceID(String wkplaceID) {
        this.wkplaceID = wkplaceID;
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

    public String getRsvID() { return rsvID; }

    public void setRsvID(String rsvID) { this.rsvID = rsvID; }

    public String toString() {
        String returnValue = "Wkplace(";
        returnValue += getWkplaceID() + ", ";
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

    /**
     * Gets the timestamp of the current reservation
     * @return The String representation of the timstamp
     */
    public String getTimeStamp() {
        Date date = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        return format.format(date);
    }
}
