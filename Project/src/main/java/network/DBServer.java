package network;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

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

    /**
     * Constructor
     * @param socket The serverSocket to use
     */
    DBServer(ServerSocket socket) {
        this.serverSocket = socket;
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
                closeSocket(socket);
            } catch(Exception e) {
                e.printStackTrace();
            }
        }
        closeServerSocket(serverSocket);
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