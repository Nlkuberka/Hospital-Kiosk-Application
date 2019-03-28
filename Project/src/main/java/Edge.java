public class Edge {
    private String edgeID;
    private String node1ID;
    private String node2ID;

    public Edge() {

    }

    public Edge(String edgeID, String node1ID, String node2ID) {
        this.edgeID = edgeID;
        this.node1ID = node1ID;
        this.node2ID = node2ID;
    }

    public String getEdgeID() {
        return edgeID;
    }

    public void setEdgeID(String edgeID) {
        this.edgeID = edgeID;
    }

    public String getNode1ID() {
        return node1ID;
    }

    public void setNode1ID(String node1ID) {
        this.node1ID = node1ID;
    }

    public String getNode2ID() {
        return node2ID;
    }

    public void setNode2ID(String node2ID) {
        this.node2ID = node2ID;
    }

    public String toString() {
        String returnValue = "Edge(";
        returnValue += edgeID + ", ";
        returnValue += node1ID + ", ";
        returnValue += node2ID + ", ";
        returnValue += ")";
        return returnValue;
    }
}
