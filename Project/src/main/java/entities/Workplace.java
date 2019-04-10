package entities;

/**
 * Object that holds data for reservable rooms
 */
public class Workplace {
    private String wkplaceID;
    private String roomName;
    private int capacity;
    private String outline;

    public Workplace(){
    }

    public Workplace(String wkplaceID, String roomName, int capacity, String outline) {
        this.wkplaceID = wkplaceID;
        this.roomName = roomName;
        this.capacity = capacity;
        this.outline = outline;
    }

    public String getWkplaceID() {
        return wkplaceID;
    }

    public void setWkplaceID(String wkplaceID) {
        this.wkplaceID = wkplaceID;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public String getOutline() {
        return outline;
    }

    public void setOutline(String outline) {
        this.outline = outline;
    }

    @Override
    public String toString() {
        return "Workplace{" +
                "wkplaceID='" + wkplaceID + '\'' +
                ", roomName='" + roomName + '\'' +
                ", capacity=" + capacity +
                ", outline='" + outline + '\'' +
                '}';
    }
}
