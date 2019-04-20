package network;

import com.fasterxml.jackson.databind.ObjectMapper;
import database.DBController;
import database.DBControllerNE;
import database.DBControllerSR;
import database.DBControllerU;
import entities.Edge;
import entities.Node;
import entities.ServiceRequest;
import entities.User;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.util.Arrays;

/**
 * The class for a DBServer
 * @author Jonathan Chang
 * @vserion iteration4
 */
public class DBServer extends NetworkThread implements Runnable {
    private ServerSocket serverSocket;
    private boolean end;
    private BufferedReader input;
    private DataOutputStream output;
    private ObjectMapper mapper;

    /**
     * Constructor
     * @param socket The serverSocket to use
     */
    DBServer(ServerSocket socket) {
        this.serverSocket = socket;
        mapper = new ObjectMapper();
        end = false;
    }

    /**
     * The running state of the server
     */
    public void run() {
        while(true) {
            // If end, then exit
            if(end) {
                break;
            }
            try{
                // Wait for connection
                Socket socket = serverSocket.accept();
                setup(socket);

                // Output input
                String string = input.readLine();
                System.out.println("Received:" + string);
                handleString(string);
                closeSocket(socket);
            } catch(Exception e) {
                e.printStackTrace();
            }
        }
        closeServerSocket(serverSocket);
    }

    private void handleString(String string) {
        String[] parts = string.split("\\|:\\|");
        String command = parts[0];
        String object = parts[1];
        if(command.contains("NODE")) {
            Node node;
            try{
                node = mapper.readValue(object, Node.class);
            } catch(Exception e) {
                e.printStackTrace();
                return;
            }
            handleNode(command, node);
        } else if(command.contains("EDGE")) {
            Edge edge;
            try{
                edge = mapper.readValue(object, Edge.class);
            } catch(Exception e) {
                e.printStackTrace();
                return;
            }
            handleEdge(command, edge);
        } else if(command.contains("USER")) {
            User user;
            try{
                user = mapper.readValue(object, User.class);
            } catch(Exception e) {
                e.printStackTrace();
                return;
            }
            handleUser(command, user);
        }
    }

    private void handleNode(String command, Node node) {
        Connection conn = DBController.dbConnect();
        if(command.contains("ADD")) {
            DBControllerNE.addNode(node, conn);
        } else if(command.contains("UPDATE")) {
            DBControllerNE.updateNode(node, conn);
        } else if(command.contains("DELETE")) {
            DBControllerNE.deleteNode(node.getNodeID(), conn);
        }
        DBController.closeConnection(conn);
    }

    private void handleEdge(String command, Edge edge) {
        Connection conn = DBController.dbConnect();
        if(command.contains("ADD")) {
            DBControllerNE.addEdge(edge, conn);
        } else if(command.contains("UPDATE")) {
            DBControllerNE.updateEdge(edge, conn);
        } else if(command.contains("DELETE")) {
            DBControllerNE.deleteEdge(edge.getNode1ID(), edge.getNode2ID(), conn);
        }
        DBController.closeConnection(conn);
    }

    private void handleUser(String command, User user) {
        Connection conn = DBController.dbConnect();
        if(command.contains("ADD")) {
            DBControllerU.addUser(user, conn);
        } else if(command.contains("UPDATE")) {
            DBControllerU.updateUser(user.getUserID(), user, conn);
        } else if(command.contains("DELETE")) {
            DBController.createTable("Delete From USERS WHERE USERID = '" + user.getUserID() + "'", conn);
        }
        DBController.closeConnection(conn);
    }
    /**
     * Any setup for a new socket
     */
    private void setup(Socket socket) {
        try{
            input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            output= new DataOutputStream(socket.getOutputStream());
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Ends the server
     */
    void end() {
        end = true;
    }

}