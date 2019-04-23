package network;

import entities.*;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;

/**
 * Class that handles the DBNetwork
 * @author Jonathan Chang
 */
public class DBNetwork {
    public static List<String> ipAddresses = new LinkedList<String>(){};

    private DBServer dbServer;
    private List<DBClient> dbClients;
    private ObjectMapper mapper;
    private boolean mute;
    private int socketNum;

    /**
     * Constructor
     * @param socketNum The socket num to use
     */
    public DBNetwork(int socketNum) {
        setupServer(socketNum);
        //DBNetwork.ipAddresses.remove(getOwnIP());
        this.dbClients = new LinkedList<DBClient>();
        this.mapper = new ObjectMapper();
        this.socketNum = socketNum;
    }

    /**
     * Sets up the DB Server
     * @param socketNum The socketNum to listen on
     * @return Whether the setup failed or succeeded
     */
    public boolean setupServer(int socketNum) {
        try {
            ServerSocket serverSocket = new ServerSocket(socketNum);
            dbServer = new DBServer(serverSocket);
            Thread serverThread = new Thread(dbServer);
            serverThread.start();
        } catch(Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * Sets up all of the DBClients, one for each address
     * @param socketNum The socketNum to connect to
     * @return Whether the setup failed or succeeded
     */
    private boolean setupClients(int socketNum, String outputString) {
        dbClients = new LinkedList<DBClient>();
        try {
            for(String ip : ipAddresses) {
                DBClient dbClient = new DBClient(ip, socketNum);
                dbClient.outputString(outputString);
                dbClients.add(dbClient);
                Thread clientThread = new Thread(dbClient);
                clientThread.start();
            }
        } catch(Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * Outputs a string to the from the client
     * @param string The string to output
     */
    public void outputString(String string) {
        if(mute) {
            return;
        }
        setupClients(this.socketNum, string);
    }
    
    /*===== Node Functions ======*/
    public static final String ADD_NODE = "ADD NODE|:|";
    public static final String UPDATE_NODE = "UPDATE NODE|:|";
    public static final String DELETE_NODE = "DELETE NODE|:|";

    public void sendNodePacket(String command, Node node) {
        try {
            command += mapper.writeValueAsString(node);
        } catch(Exception e) {
            e.printStackTrace();
        }
        outputString(command);
    }
    
    /*===== Edge Functions ======*/
    public static final String ADD_EDGE = "ADD EDGE|:|";
    public static final String UPDATE_EDGE = "UPDATE EDGE|:|";
    public static final String DELETE_EDGE = "DELETE EDGE|:|";
    
    public void sendEdgePacket(String command, Edge edge) {
        try {
            command += mapper.writeValueAsString(edge);
        } catch(Exception e) {
            e.printStackTrace();
        }
        outputString(command);
    }

    /*===== User Functions ======*/
    public static final String ADD_USER = "ADD USER|:|";
    public static final String UPDATE_USER = "UPDATE USER|:|";
    public static final String DELETE_USER = "DELETE USER|:|";
    
    public void sendUserPacket(String command, User user) {
        try {
            command += mapper.writeValueAsString(user);
        } catch(Exception e) {
            e.printStackTrace();
        }
        return;
        //outputString(command);
    }

    /*===== Service Request Functions ======*/
    public static final String ADD_SERVICE_REQUEST = "ADD SERVICE REQUEST|:|";
    public static final String UPDATE_SERVICE_REQUEST = "UPDATE SERVICE REQUEST|:|";
    public static final String DELETE_SERVICE_REQUEST = "DELETE SERVICE REQUEST|:|";
    
    public void sendServiceRequestPacket(String command, ServiceRequest serviceRequest) {
        try {
            command += mapper.writeValueAsString(serviceRequest);
        } catch(Exception e) {
            e.printStackTrace();
        }
        outputString(command);
    }

    /*===== Reservations Functions ======*/
    public static final String ADD_RESERVATION = "ADD RESERVATION|:|";
    public static final String UPDATE_RESERVATION = "UPDATE RESERVATION|:|";
    public static final String DELETE_RESERVATION = "DELETE RESERVATION|:|";

    public void sendReservationPacket(String command, Reservation reservation) {
        try {
            command += mapper.writeValueAsString(reservation);
        } catch(Exception e) {
            e.printStackTrace();
        }
        outputString(command);
    }

    /**
     * Shuts down the network
     */
    public void shutdown() {
        for(DBClient client : dbClients) {
            client.end();
        }
        dbServer.end();
    }

    /**
     * Quires a bot to get the public IP address of the self
     */
    private String getOwnIP() {
        String ownIP = "";
        try {
            InetAddress localhost = InetAddress.getLocalHost();
            URL url_name = new URL("http://bot.whatismyipaddress.com");

            BufferedReader sc = new BufferedReader(new InputStreamReader(url_name.openStream()));

            ownIP = sc.readLine().trim();
        } catch (Exception e) {
            e.printStackTrace();
            ownIP = "Cannot Execute Properly";
        }
        System.out.println(ownIP);
        return ownIP;
    }

    public void mute() {
        this.mute = true;
    }

    public void unmute() {} {
        this.mute = false;
    }

    public void hold() {
        this.dbServer.hold();
    }

    public void unhold() {
        this.dbServer.unhold();
    }

}
