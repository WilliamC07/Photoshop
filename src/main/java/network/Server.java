package network;

import project.Project;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.HashSet;

/**
 * Only one user should have this instance running when sharing a file with collaborators.
 */
public class Server implements ActionHandler{
    private Project project = Project.getInstance();
    private ArrayList<Connector> connectors = new ArrayList<>();
    /**
     * HashSet to prevent multiple users of the same name
     */
    private HashSet<String> collaborators = new HashSet<>();

    /**
     * Constructs a network to deal with the sharing of a file with multiple collaborators.
     */
    public Server(){
        startServer.start();
    }

    /**
     * Sends information to all the connected users
     * @param sender Information to be sent
     */
    private void send(Sender sender){
        connectors.forEach(c -> c.sendFile(sender));
    }

    /**
     * ServerSocket.accept() waits until there is a connection before moving on with the code. Make another thread
     * so it doesn't halt the main thread.
     */
    private Thread startServer = new Thread(() -> {
        try(ServerSocket serverSocket = new ServerSocket(5000)){
            // IP address is the ip address of the current computer, so no need to access from ServerSocket
            Connector connector = new Connector(serverSocket.accept(), this);
            connector.start();
            connectors.add(connector);
        }catch(IOException e){
            e.printStackTrace();
            System.exit(1);
        }
    });

    @Override
    public synchronized void handle(String message, ActionType actionType, Connector connector) {
        switch(actionType){
            case REQUEST_PROJECT_NAME:
                // Tells the connector to use the project name
                connector.sendFile(new Sender(project.getName(), ActionType.UPDATE_PROJECT_NAME));
            case ADD_COLLABORATOR_USERNAME:
                // Add the newly connected user to the list of contributors
                collaborators.add(message);
                // Tell everyone to request for the update version of the list of collaborator
                send(new Sender(collaboratorNames(), ActionType.UPDATE_TO_LATEST_COLLABORATOR));
                break;
            case REQUEST_COLLABORATOR_LIST:
                // Tells all the connectors to update their list of contributors
                send(new Sender(String.join(", ", collaborators.toArray(new String[0])),
                                ActionType.UPDATE_TO_LATEST_COLLABORATOR));

        }
    }

    /**
     * Returns a formatted string with the names of all the current contributors
     * @return A formatted string with the names of all the current contributors
     */
    private String collaboratorNames(){
        return String.join(", ", collaborators.toArray(new String[0]));
    }

    @Override
    public synchronized void handle(byte[] file, FileType fileType, ActionType actionType, Connector connector) {

    }

    @Override
    public synchronized void handle(ActionType actionType, Connector connector) {

    }

    @Override
    public synchronized void handle(byte[] file, FileType fileType, String message, ActionType actionType, Connector connector) {

    }
}
