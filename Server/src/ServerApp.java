import com.discordapp.controller.ServerController;

import java.io.*;
import java.net.*;

/**
 * The type Server app.
 */
public class ServerApp {
    private final ServerSocket serverSocket;

    /**
     * Instantiates a new Server app.
     *
     * @param serverSocket the server socket
     */
    public ServerApp(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;
    }

    /**
     * Start server.
     */
    public void startServer() {
        try {

            while (!serverSocket.isClosed()) {
                Socket socket = serverSocket.accept();
                System.out.println("A new Application has connected!");
                ServerController serverController = new ServerController(socket);
                Thread thread = new Thread(serverController);
                thread.start();
            }
        } catch (IOException e) {
            closeServerSocket();
        }
    }


    /**
     * Close server socket.
     */
    public void closeServerSocket() {
        try {
            if (serverSocket != null) {
                serverSocket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Run the program.
    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(7777);
        ServerApp server = new ServerApp(serverSocket);
        server.startServer();
    }
}
