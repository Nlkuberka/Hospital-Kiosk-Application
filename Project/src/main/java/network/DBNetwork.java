package network;

import entities.Edge;
import entities.Node;

import com.fasterxml.jackson.databind.ObjectMapper;
import entities.User;

import java.net.ServerSocket;
import java.util.LinkedList;
import java.util.List;

/**
 * Class that handles the DBNetwork
 * @author Jonathan Chang
 */
public class DBNetwork {
    public static final List<String> ipAddresses = new LinkedList<String>(){{
        add("130.215.213.188");
    }};

    private DBServer dbServer;
    private List<DBClient> dbClients;
    private ObjectMapper mapper;
    private boolean mute;

    /**
     * Constructor
     * @param socketNum The socket num to use
     */
    public DBNetwork(int socketNum) {
        setupServer(socketNum);
        setupClient(socketNum);
        this.mapper = new ObjectMapper();
    }

    /**
     * Sets up all of the DBClients, one for each address
     * @param socketNum The socketNum to connect to
     * @return Whether the setup failed or succeeded
     */
    private boolean setupClient(int socketNum) {
        dbClients = new LinkedList<DBClient>();
        try {
            for(String ip : ipAddresses) {
                DBClient dbClient = new DBClient(ip, socketNum);
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
     * Shuts down the network
     */
    public void shutdown() {
        for(DBClient client : dbClients) {
            client.end();
        }
        dbServer.end();
    }

    public void mute() {
        this.mute = true;
    }

    public void unmute() {} {
        this.mute = false;
    }

    /**
     * Outputs a string to the from the client
     * @param string The string to output
     */
    public void outputString(String string) {
        if(mute) {
            return;
        }
        for(DBClient client : dbClients) {
            client.outputString(string);
        }
    }

    public void addNode(Node node) {
        String outputString = "ADD NODE|:|";
        try {
            outputString += mapper.writeValueAsString(node);
        } catch(Exception e) {
            e.printStackTrace();
        }
        outputString(outputString);
    }

    public void updateNode(Node node) {
        String outputString = "UPDATE NODE|:|";
        try {
            outputString += mapper.writeValueAsString(node);
        } catch(Exception e) {
            e.printStackTrace();
        }
        outputString(outputString);
    }

    public void deleteNode(Node node) {
        String outputString = "DELETE NODE|:|";
        try {
            outputString += mapper.writeValueAsString(node);
        } catch(Exception e) {
            e.printStackTrace();
        }
        outputString(outputString);
    }

    public void addEdge(Edge edge) {
        String outputString = "ADD EDGE|:|";
        try {
            outputString += mapper.writeValueAsString(edge);
        } catch(Exception e) {
            e.printStackTrace();
        }
        outputString(outputString);
    }

    public void updateEdge(Edge edge) {
        String outputString = "UPDATE EDGE|:|";
        try {
            outputString += mapper.writeValueAsString(edge);
        } catch(Exception e) {
            e.printStackTrace();
        }
        outputString(outputString);
    }

    public void deleteEdge(Edge edge) {
        String outputString = "DELETE EDGE|:|";
        try {
            outputString += mapper.writeValueAsString(edge);
        } catch(Exception e) {
            e.printStackTrace();
        }
        outputString(outputString);
    }

    public void addUser(User user) {
        String outputString = "ADD USER|:|";
        try {
            outputString += mapper.writeValueAsString(user);
        } catch(Exception e) {
            e.printStackTrace();
        }
        outputString(outputString);
    }

    public void updateUser(User user) {
        String outputString = "UPDATE USER|:|";
        try {
            outputString += mapper.writeValueAsString(user);
        } catch(Exception e) {
            e.printStackTrace();
        }
        outputString(outputString);
    }

    public void deleteUSER(User user) {
        String outputString = "DELETE USER|:|";
        try {
            outputString += mapper.writeValueAsString(user);
        } catch(Exception e) {
            e.printStackTrace();
        }
        outputString(outputString);
    }
}
