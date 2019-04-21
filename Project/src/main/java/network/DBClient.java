package network;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.Socket;

/**
 * The class for the DBClient
 * @author Jonathan Chang
 * @version iteration4
 */
public class DBClient extends NetworkThread implements Runnable {
    private static final int attemptsNum = 10;
    private Socket socket;
    private String ip;
    private int socketNum;
    private boolean end;
    private BufferedReader input;
    private DataOutputStream output;

    private String outputString;

    /**
     * Constructor
     * @param ip The ip address to connect to
     * @param socketNum The socketNum to connect to
     */
    DBClient(String ip, int socketNum) {
        this.ip = ip;
        this.socketNum = socketNum;
        end = false;
    }

    /**
     * The running state of the client
     */
    public void run() {
        String result = "";
        int attempts = 0;
        while(!result.equals("CONFIRMED|:|" + outputString) && attempts < attemptsNum) {
            if(end) {
                break;
            }
            try{
                socket = new Socket(ip, socketNum);
                setup();

                output.writeBytes(outputString + "\n");
                log("C-SENT     : " + outputString);

                result = input.readLine();
                log("C-RECEIVED : " + result);
                closeSocket(socket);
            } catch(Exception e) {
                e.printStackTrace();
            }
            attempts++;
        }
        System.out.println("-");
    }

    /**
     * Setup for an individual socket
     */
    private void setup() {
        try{
            input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            output= new DataOutputStream(socket.getOutputStream());
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Outputs a string to the client
     * @param string The string to output
     */
    void outputString(String string) {
        this.outputString = string;
    }

    /**
     * Ends the client
     */
    void end() {
        end = true;
    }
}
