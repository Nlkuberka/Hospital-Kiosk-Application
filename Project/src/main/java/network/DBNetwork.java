package network;

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

    /**
     * Constructor
     * @param socketNum The socket num to use
     */
    public DBNetwork(int socketNum) {
        setupServer(socketNum);
        setupClient(socketNum);
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

    /**
     * Outputs a string to the from the client
     * @param string The string to output
     */
    public void outputString(String string) {
        for(DBClient client : dbClients) {
            client.outputString(string);
        }
    }
}
