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
}
