/**
 * The object that holds data about a particular node
 * @version iteration1
 */
public class Node {
    private String nodeID;
    private int xcoord;
    private int ycoord;
    private String floor;
    private String building;
    private String nodeType;
    private String longName;
    private String shortName;

    public Node() {
    }

    public Node(String nodeID, int xcoord, int ycoord, String floor, String building, String nodeType, String longName, String shortName) {
        this.nodeID = nodeID;
        this.xcoord = xcoord;
        this.ycoord = ycoord;
        this.floor = floor;
        this.building = building;
        this.nodeType = nodeType;
        this.longName = longName;
        this.shortName = shortName;
    }

    public String getNodeID() {
        return nodeID;
    }

    public void setNodeID(String nodeID) {
        this.nodeID = nodeID;
    }

    public int getXcoord() {
        return xcoord;
    }

    public void setXcoord(int xcoord) {
        this.xcoord = xcoord;
    }

    public int getYcoord() {
        return ycoord;
    }

    public void setYcoord(int ycoord) {
        this.ycoord = ycoord;
    }

    public String getFloor() {
        return floor;
    }

    public void setFloor(String floor) {
        this.floor = floor;
    }

    public String getBuilding() {
        return building;
    }

    public void setBuilding(String building) {
        this.building = building;
    }

    public String getNodeType() {
        return nodeType;
    }

    public void setNodeType(String nodeType) {
        this.nodeType = nodeType;
    }

    public String getLongName() {
        return longName;
    }

    public void setLongName(String longName) {
        this.longName = longName;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public String toString() {
        String returnValue = "Node(";
        returnValue += getNodeID() + ", ";
        returnValue += getXcoord() + ", ";
        returnValue += getYcoord() + ", ";
        returnValue += getFloor() + ", ";
        returnValue += getBuilding() + ", ";
        returnValue += getNodeType() + ", ";
        returnValue += getLongName() + ", ";
        returnValue += getShortName() + ")";
        return  returnValue;
    }
}
