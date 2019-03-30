public class ServiceRequest {
    private String nodeID;
    private String serviceType;
    private String message;
    private String requesterID;
    private boolean resolved;

    public ServiceRequest() {
    }

    public ServiceRequest(String nodeID, String serviceType, String message, String requesterID, boolean resolved) {
        this.nodeID = nodeID;
        this.serviceType = serviceType;
        this.message = message;
        this.requesterID = requesterID;
        this.resolved = resolved;
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

    public String getRequesterID() {
        return requesterID;
    }

    public void setRequesterID(String requesterID) {
        this.requesterID = requesterID;
    }

    public boolean isResolved() {
        return resolved;
    }

    public void setResolved(boolean resolved) {
        this.resolved = resolved;
    }
}
