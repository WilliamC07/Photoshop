package network;

import project.Project;

import java.io.IOException;
import java.io.File;
import java.net.Inet4Address;
import java.net.ServerSocket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.PriorityQueue;
import java.util.concurrent.PriorityBlockingQueue;

/**
 * Only one user should have this instance running when sharing a file with collaborators.
 */
public class Server{
    private ArrayList<Connector> connectors = new ArrayList<>();
    private int port;
    private PriorityQueue<Sender> queue = new PriorityQueue<>();

    /**
     * Constructs a network to deal with the sharing of a file with multiple collaborators.
     */
    public Server(){
        startServer.start();
        try{
            // Pause so the ServerSocket and start up
            Thread.sleep(500);
        }catch(InterruptedException e){
            // Do nothing
        }
    }

    private Thread sendQueuedItems = new Thread(() -> {
        while(true){
            try{
                // So the thread doesn't eat up the cpu
                Thread.sleep(500);

                // Only send files once, if the receiving ends needs it to be resent, it is handled elsewhere
                Sender sender = queue.peek();
                if(!sender.wasSent()){
                    for(Connector connector : connectors){
                        connector.sendFile(sender); // This will set wasSent to true;
                    }
                }
            }catch(InterruptedException e){
                e.printStackTrace();
            }
        }
    });

    /**
     * ServerSocket.accept() waits until there is a connection before moving on with the code. Make another thread
     * so it doesn't halt the main thread.
     */
    private Thread startServer = new Thread(() -> {
        try(ServerSocket serverSocket = new ServerSocket(0)){
            // IP address is the ip address of the current computer, so no need to access from ServerSocket
            port = serverSocket.getLocalPort();
            Connector connector = new Connector(serverSocket.accept(), this::handle);
            connector.start();
            connectors.add(connector);
        }catch(IOException e){
            e.printStackTrace();
            System.exit(1);
        }
    });

    /**
     * Handles the file sent through given the file, file type, action type, and connector
     * Must be synchronized since multiple threads are accessing this
     * @param file File received
     * @param message Can be null if and only if file is null.
     * @param fileType File type received
     * @param actionType Action received
     * @param connector Connector that delivered the information
     * @throws IllegalArgumentException Wrong instruction sent
     */
    private synchronized void handle(File file, String message, FileType fileType, ActionType actionType, Connector connector)
            throws IllegalArgumentException{
        switch (actionType) {
            case SEND_ORIGINAL_IMAGE:
                // TODO: remove. Testing purposes.
                File filed = new File("network/4k.png");
                System.out.println(filed.length());
                queue.add(new Sender(filed, FileType.IMAGE, ActionType.PUSH_INSTRUCTION));
                System.out.println("sent file from server");
                break;
            case ADD_COLLABORATOR_USERNAME:
                // Updates the view to add the latest collaborator
                System.out.println("Server received message: " + message);
                Project.getInstance().addCollaborator(message);
                // User connected, so everyone has to update
                handle(null, null, null, ActionType.REQUEST_COLLABORATOR_LIST, connector);
                break;
            case REQUEST_COLLABORATOR_LIST:
                String collaboratorString = String.join(", ", Project.getInstance().getCollaborators());
                System.out.println("requested : " + collaboratorString);

                // There isn't a good way to send data individually, so just send it to everyone
                queue.add(new Sender(collaboratorString, ActionType.UPDATE_TO_LATEST_COLLABORATOR));
                break;
            default:
                throw new IllegalArgumentException("This is not a server instruction: " + actionType.name());
        }
    }

    public int getPort() {
        return port;
    }
}
