public class ServiceRequest {
    private String nodeID;
    private String serviceType;
    private String message;
    private String userID;
    private boolean resolved;
    private String resolverID;

    public ServiceRequest() {
    }

    public ServiceRequest(String nodeID, String serviceType, String message, String userID, boolean resolved, String resolverID) {
        this.nodeID = nodeID;
        this.serviceType = serviceType;
        this.message = message;
        this.userID = userID;
        this.resolved = resolved;
        this.resolverID = resolverID;
    }

    public String getNodeID() {
        return nodeID;
    }

    public void setNodeID(String nodeID) {
        this.nodeID = nodeID;
    }

    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public boolean isResolved() {
        return resolved;
    }

    public void setResolved(boolean resolved) {
        this.resolved = resolved;
    }

    public String getResolverID() {
        return resolverID;
    }

    public void setResolverID(String resolverID) {
        this.resolverID = resolverID;
    }

    public String toString() {
        String returnValue = "Node(";
        returnValue += getNodeID() + ", ";
        returnValue += getServiceType() + ", ";
        returnValue += getMessage() + ", ";
        returnValue += getUserID() + ", ";
        returnValue += isResolved()+ ", ";
        returnValue += getResolverID() + ")";
        return  returnValue;
    }
}
