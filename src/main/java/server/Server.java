package server;

import java.io.IOException;
import java.io.File;
import java.net.ServerSocket;
import java.util.ArrayList;

/**
 * Only one user should have this instance running when sharing a file with collaborators.
 */
public class Server implements ActionHandler{
    private ArrayList<Connector> connectors = new ArrayList<>();

    /**
     * Constructs a server to deal with the sharing of a file with multiple collaborators.
     */
    public Server(){
        int targetPort = 49153; // TODO: replace with 0, leave it for testing purposes
        // TODO: Make into another thread to allow multiple connections through the same port
        try(ServerSocket serverSocket = new ServerSocket(targetPort)){
            Connector connector = new Connector(serverSocket.accept(), this);
            connector.start();
            connectors.add(connector);
        }catch(IOException e){
            e.printStackTrace();
            System.exit(1);
        }
    }

    /**
     * Handles the file sent through given the file and action type
     * Must be synchronized since multiple threads are accessing this
     *
     * @see server.ActionType
     * @param file File to be received
     * @param actionType What action to be performed
     */
    @Override public synchronized void handle(File file, ActionType actionType) {

    }

    /**
     * Temporary TODO: Remove
     * Use this method for testing features.
     * @param args Command line argument
     */
    public static void main(String[] args) {
        new Server();
    }
}
