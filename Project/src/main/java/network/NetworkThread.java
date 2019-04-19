package network;

import java.net.ServerSocket;
import java.net.Socket;

/**
 * Shared Networking Functions
 * @author Jonathan Chang
 * @version iteration4
 */
public class NetworkThread {
    /**
     * Closes the given serverSocket
     * @param serverSocket The serverSocket to close
     */
    void closeServerSocket(ServerSocket serverSocket) {
        try {
            serverSocket.close();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Closes the given socket
     * @param socket The socket to close
     */
    void closeSocket(Socket socket) {
        try {
            socket.close();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}
