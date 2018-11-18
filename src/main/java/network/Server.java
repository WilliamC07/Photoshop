package network;

import java.io.IOException;
import java.io.File;
import java.net.ServerSocket;
import java.util.ArrayList;

/**
 * Only one user should have this instance running when sharing a file with collaborators.
 */
public class Server{
    private ArrayList<Connector> connectors = new ArrayList<>();

    /**
     * Constructs a network to deal with the sharing of a file with multiple collaborators.
     */
    public Server(){
        int targetPort = 49153; // TODO: replace with 0, leave it for testing purposes
        // TODO: Make into another thread to allow multiple connections through the same port
        try(ServerSocket serverSocket = new ServerSocket(targetPort)){
            Connector connector = new Connector(serverSocket.accept(), this::handle);
            connector.start();
            connectors.add(connector);
        }catch(IOException e){
            e.printStackTrace();
            System.exit(1);
        }
    }

    /**
     * Handles the file sent through given the file, file type, action type, and connector
     * Must be synchronized since multiple threads are accessing this
     * @param file File received
     * @param fileType File type received
     * @param actionType Action received
     * @param connector Connector that delivered the information
     * @throws IllegalArgumentException Wrong instruction sent
     */
    private synchronized void handle(File file, FileType fileType, ActionType actionType, Connector connector)
            throws IllegalArgumentException{
        switch (actionType) {
            case SEND_ORIGINAL_IMAGE:
                // TODO: remove. Testing purposes.
                File filed = new File("network/4k.png");
                System.out.println(filed.length());

                connector.sendFile(new Sender(filed, FileType.IMAGE, ActionType.PUSH_INSTRUCTION));
                System.out.println("sent file from server");
                break;
            default:
                throw new IllegalArgumentException("This is not a server instruction: " + actionType.name());
        }
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
