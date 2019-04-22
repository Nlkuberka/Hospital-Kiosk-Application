package entities;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class User {
    public static final int GUEST_PERMISSIONS = 1;
    public static final int BASIC_PERMISSIONS = 2;
    public static final int ADMIN_PERMISSIONS = 3;
    public static final String[] serviceRequests = {"Audio Visual", "Religious Services", "Prescription Services", "Flower Delivery",
            "IT Services", "Sanitation", "External Transportation", "Interpreter", "Babysitter", "Security"};

    private String userID;
    private String username;
    private String password;
    private int permissions;
    private String wpiid;

    public User() {
    }

    public User(String userID, String username, String password, int permissions) {
        this.userID = userID;
        this.username = username;
        this.password = password;
        this.permissions = permissions;
    }

    public User(String userID, String username, int permissions) {
        this.userID = userID;
        this.username = username;
        this.permissions = permissions;
    }

    public User(String userID, String username, int permissions,String wpiid) {
        this.userID = userID;
        this.username = username;
        this.permissions = permissions;
        this.wpiid = wpiid;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String toString() {
        String returnValue = "User(";
        returnValue += userID + ", ";
        returnValue += username + ", ";
        returnValue += password + ", ";
        returnValue += permissions + "";
        returnValue += ")";
        return returnValue;
    }

    /**
     * Gets the overall permissions level of the user
     * @return
     */
    public int getPermissions() {
        boolean[] bits = getBitArray(permissions);
        boolean[] permissionsBits = {bits[0], bits[1]};
        return getIntFromBitArray(permissionsBits);
    }


    /**
     * Sets the overall permissions level of the user
     * @param permissions
     */
    public void setPermissions(int permissions) {
        boolean[] bits = getBitArray(this.permissions);
        boolean[] permissionsBits = getBitArray(permissions);
        bits[0] = permissionsBits[10];
        bits[1] = permissionsBits[11];
        this.permissions = getIntFromBitArray(bits);
    }

    /**
     * Gets the int representation of the overall permissions and service request
     */
    public int getPermissionsNumber() {
        return permissions;
    }

    /**
     * Gets the int representation of the overall permissions and service request
     */
    public void setPermissionsNumber(int permissions) {
        this.permissions = permissions;
    }

    /**
     * Gets the entire list of service requests that this user can fulfill
     * @return The list of service requests that this user can fulfill
     */
    public List<String> getServiceRequestFullfillment() {
        boolean[] bits = getBitArray(permissions);
        List<String> result = new LinkedList<String>();
        for(int i = 2; i < bits.length; i++) {
            if(bits[i]) {
                result.add(serviceRequests[i - 2]);
            }
        }
        return result;
    }

    /**
     * Sets the entire list of service requests that this user can fulfill
     * @param serviceRequestsNew The list of service Requests
     */
    public void setServiceRequestsFullfillment(List<String> serviceRequestsNew) {
        boolean[] bits = getBitArray(permissions);
        boolean[] newBits = new boolean[bits.length];
        newBits[0] = bits[0];
        newBits[1] = bits[1];
        this.permissions = getIntFromBitArray(newBits);

        for(int i = 0; i < serviceRequestsNew.size(); i++) {
            addServiceRequest(serviceRequestsNew.get(i));
        }
    }

    /**
     * Adds a service request fulfillment from this user
     * @param serviceRequest The string type of service Request
     */
    public void addServiceRequest(String serviceRequest) {
        int index = getServiceRequestIndex(serviceRequest);
        if(index == -1) {
            return;
        }

        boolean[] bits = getBitArray(permissions);
        bits[index + 2] = true;
        this.permissions = getIntFromBitArray(bits);
    }

    /**
     * Removes a service request fulfillment from this user
     * @param serviceRequest The string type of service Request
     */
    public void removeServiceRequest(String serviceRequest) {
        int index = getServiceRequestIndex(serviceRequest);
        if(index == -1) {
            return;
        }

        boolean[] bits = getBitArray(permissions);
        bits[index + 2] = false;
        this.permissions = getIntFromBitArray(bits);
    }

    /**
     * Gets the bit array of a int as a boolean array
     * @param permissions The number to get the array of
     * @return The bit array
     */
    public static boolean[] getBitArray(int permissions) {
        boolean[] bits = new boolean[12];
        for (int i = 0 ; i < bits.length; i++) {
            bits[bits.length - 1 - i] = (permissions & (1 << i)) != 0;
        }

        return bits;
    }

    /**
     * Gets an int from a given boolean array as bits
     * @param bits The array of bits
     * @return The int representation
     */
    public static int getIntFromBitArray(boolean[] bits) {
        int sum = 0;
        for(int i = 0; i < bits.length; i++) {
            if(bits[i]) {
                sum += Math.pow(2, bits.length - 1 - i);
            }
        }
        return sum;
    }

    /**
     * Gets the index of the service Request string
     */
    private int getServiceRequestIndex(String serviceRequest) {
        List<String> serviceRequestList = new LinkedList<String>(Arrays.asList(serviceRequests));
        int index = serviceRequestList.indexOf(serviceRequest);
        if(index < 0 || index >= 10) {
            return -1;
        }
        return index;
    }
}
