package network;

import application.CurrentUser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.twilio.twiml.voice.Echo;
import database.*;
import entities.*;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.sql.Connection;

/**
 * The class for a DBServer
 * @author Jonathan Chang
 * @vserion iteration4
 */
public class DBServer extends NetworkThread implements Runnable {
    public ServerSocket serverSocket;
    private boolean end;
    private BufferedReader input;
    private DataOutputStream output;
    private ObjectMapper mapper;

    private DBNetwork network;
    private String inputString;
    private boolean hold;

    /**
     * Constructor
     * @param socket The serverSocket to use
     */
    DBServer(ServerSocket socket, DBNetwork network) {
        this.network = network;
        this.serverSocket = socket;
        mapper = new ObjectMapper();
        end = false;
        hold = false;
    }

    /**
     * The running state of the server
     */
    public void run() {
        log("Server Started");
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
                inputString = input.readLine();
                log("S-RECEIVED : " + inputString);

                if(!hold) {
                    CurrentUser.network.mute();
                    handleString(inputString);
                    CurrentUser.network.unmute();
                } else {
                    outputSuccess();
                }

                closeSocket(socket);
            } catch(SocketException e) {
                //
            } catch(Exception e) {
                e.printStackTrace();
            }
            System.out.println("-");
            //network.restart();
        }
        closeServerSocket(serverSocket);
    }

    /**
     * Handles the given string from a client and parse as the given object class
     * @param string The string to handle
     */
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
        } else if(command.contains("SERVICE REQUEST")) {
            ServiceRequest serviceRequest;
            try{
                serviceRequest = mapper.readValue(object, ServiceRequest.class);
            } catch(Exception e) {
                e.printStackTrace();
                return;
            }
            handleServiceRequest(command, serviceRequest);
        } else if(command.contains("RESERVATION")) {
            Reservation reservation;
            try{
                reservation = mapper.readValue(object, Reservation.class);
            } catch(Exception e) {
                e.printStackTrace();
                return;
            }
            handleReservation(command, reservation);
        }
    }

    /**
     * Calls the DB function based on the command for the given node
     * @param command The command
     * @param node The node for that command
     */
    private void handleNode(String command, Node node) {
        Connection conn = DBController.dbConnect();
        if(command.contains("ADD")) {
            DBControllerNE.addNode(node, conn);
            Graph.getGraph().addNode(node);
        } else if(command.contains("UPDATE")) {
            DBControllerNE.updateNode(node, conn);
        } else if(command.contains("DELETE")) {
            DBControllerNE.deleteNode(node.getNodeID(), conn);
            Graph.getGraph().removeNode(node.getNodeID());
        }
        DBController.closeConnection(conn);
        outputSuccess();
    }

    /**
     * Calls the DB function based on the command for the given edge
     * @param command The command
     * @param edge The edge for that command
     */
    private void handleEdge(String command, Edge edge) {
        Connection conn = DBController.dbConnect();
        if(command.contains("ADD")) {
            DBControllerNE.addEdge(edge, conn);
            Graph.getGraph().addBiEdge(edge.getNode1ID(), edge.getNode2ID());
        } else if(command.contains("UPDATE")) {
            DBControllerNE.updateEdge(edge, conn);
        } else if(command.contains("DELETE")) {
            DBControllerNE.deleteEdge(edge.getNode1ID(), edge.getNode2ID(), conn);
            Graph.getGraph().removeBiEdge(edge.getNode1ID(), edge.getNode2ID());
        }
        DBController.closeConnection(conn);
        outputSuccess();
    }

    /**
     * Calls the DB function based on the command for the given user
     * @param command The command
     * @param user The user for that command
     */
    private void handleUser(String command, User user) {
        Connection conn = DBController.dbConnect();
        if(command.contains("ADD")) {
            DBControllerU.addUser(user, conn);
        } else if(command.contains("UPDATE")) {
            DBControllerU.updateUser(user.getUserID(), user, conn);
        } else if(command.contains("DELETE")) {
            DBControllerU.deleteUser(user, conn);
        }
        DBController.closeConnection(conn);
        outputSuccess();
    }

    /**
     * Calls the DB function based on the command for the given service request
     * @param command The command
     * @param serviceRequest The service request for that command
     */
    private void handleServiceRequest(String command, ServiceRequest serviceRequest) {
        Connection conn = DBController.dbConnect();
        if(command.contains("ADD")) {
            DBControllerSR.addServiceRequest(serviceRequest, conn);
        } else if(command.contains("UPDATE")) {
            DBControllerSR.updateServiceRequest(serviceRequest, conn);
        } else if(command.contains("DELETE")) {
            DBControllerSR.deleteServiceRequest(serviceRequest.getServiceID(), conn);
        }
        DBController.closeConnection(conn);
        outputSuccess();
    }

    /**
     * Calls the DB function based on the command for the given reservation
     * @param command The command
     * @param reservation The reservation for that command
     */
    private void handleReservation(String command, Reservation reservation) {
        Connection conn = DBController.dbConnect();
        if(command.contains("ADD")) {
            DBControllerRW.addReservation(reservation, conn);
        } else if(command.contains("UPDATE")) {
            DBControllerRW.updateReservation(reservation, conn);
        } else if(command.contains("DELETE")) {
            DBControllerRW.deleteReservation(reservation.getRsvID(), conn);
        }
        DBController.closeConnection(conn);
        outputSuccess();
    }

    /**
     * Returns a success to the client
     */
    private void outputSuccess() {
        try {
            output.writeBytes("CONFIRMED|:|" + inputString);
            log("S-SENT     : " + "CONFIRMED|:|" + inputString);
        } catch(Exception e) {
            e.printStackTrace();
        }
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
        try{
            serverSocket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        end = true;
    }

    void hold() {
        this.hold = true;
    }

    void unhold() {
        this.hold = false;
    }
}