import java.util.Date;

public class Reservation {
    private String nodeID;
    private String userID;
    private Date startTime;
    private Date endTime;

    public Reservation() {
    }

    public Reservation(String nodeID, String userID, Date startTime, Date endTime) {
        this.nodeID = nodeID;
        this.userID = userID;
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

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }
}
