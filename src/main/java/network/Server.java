package network;

import javafx.application.Platform;
import project.Project;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;

/**
 * Only one user should have this instance running when sharing a file with collaborators.
 */
public class Server implements ActionHandler{
    private final Project project;
    private volatile ArrayList<Connector> connectors = new ArrayList<>();
    private volatile boolean isServerRunning = true;
    private volatile ServerSocket serverSocket = null;
    /**
     * LinkedList because we are always adding to the end of the list
     */
    private LinkedList<String> collaborators = new LinkedList<>();

    /**
     * Constructs a network to deal with the sharing of a file with multiple collaborators.
     */
    public Server(Project project){
        this.project = project;
        startServer.start();
    }

    public void setMyUsername(String name){
        collaborators.add(name);
    }

    /**
     * Sends information to all the connected users
     * @param sender Information to be sent
     */
    private void send(Sender sender){
        connectors.forEach(c -> c.sendFile(sender));
    }

    /**
     * Ends the server and informs the clients that the server is closed.
     */
    public void terminate(){
        for(Connector connector : connectors){
            connector.sendFile(new Sender(ActionType.QUIT_CONNECTION));
            connector.terminateConnection();
        }
        isServerRunning = false;
        try{
            serverSocket.close();
        }catch(IOException e){
            // Do nothing, program is closed, so we don't care
        }
    }

    /**
     * Allows for multiple connections to the server
     */
    private Thread startServer = new Thread(() -> {
        try{
            // server socket will be closed when the terminate function is called
            serverSocket = new ServerSocket(5000);

            while(isServerRunning){
                // IP address is the ip address of the current computer, so no need to access from ServerSocket
                Connector connector = new Connector(serverSocket.accept(), Server.this);
                connector.start();
                connectors.add(connector);
            }
        }catch(IOException e){
            e.printStackTrace();
            System.exit(1);
        }
    });

    @Override
    public synchronized void handle(String message, ActionType actionType, Connector connector) {
        Platform.runLater(() -> {
            switch (actionType) {
                case ADD_COLLABORATOR_USERNAME:
                    // Add the newly connected user to the list of contributors
                    collaborators.add(message);
                    // Update the host
                    project.setCollaborators(collaborators.toArray(new String[0]));
                    // Tell everyone to request for the update version of the list of collaborator
                    send(new Sender(collaboratorNames(), ActionType.UPDATE_TO_LATEST_COLLABORATOR));
                    break;
            }
        });
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
        Platform.runLater(() -> {

        });
    }

    @Override
    public synchronized void handle(ActionType actionType, Connector connector) {
        Platform.runLater(() -> {
            switch(actionType){
                case REQUEST_PROJECT_NAME:
                    // Tells the connector to use the project name
                    connector.sendFile(new Sender(project.getProjectName(), ActionType.UPDATE_PROJECT_NAME));
                    break;
                case REQUEST_COLLABORATOR_LIST:
                    // Tells all the connectors to update their list of contributors
                    send(new Sender(String.join(", ", collaborators.toArray(new String[0])),
                            ActionType.UPDATE_TO_LATEST_COLLABORATOR));
                    break;
                case REQUEST_ORIGINAL_IMAGE:
                    // Give the current connector the original image
                    try {
                        FileInputStream fileInputStream = new FileInputStream(project.getOriginalImage());
                        connector.sendFile(new Sender(fileInputStream, FileType.IMAGE, ActionType.UPDATE_ORIGINAL_IMAGE));
                    } catch (IOException e) {
                        // Do nothing
                    }
                    break;
                case REQUEST_CHECKPOINT_IMAGES: {
                    // Give all the checkpoint images
                    File fileToSend;
                    int checkpointIndex = 0;
                    while((fileToSend = project.getCheckpointImage(checkpointIndex)) != null){
                        connector.sendFile(new Sender(fileToSend, String.valueOf(checkpointIndex), FileType.IMAGE, ActionType.UPDATE_CHECKPOINT_IMAGE));
                        checkpointIndex++;
                    }
                }
                    break;
                case REQUEST_ALL_INSTRUCTIONS:{
                    connector.sendFile(new Sender(project.getEditsDoneFile(), FileType.XML, ActionType.UPDATE_TO_LATEST_INSTRUCTION));
                }
                    break;
                case REQUEST_PROJECT:
                {
                    // Sends all the needed files
                    handle(ActionType.REQUEST_ORIGINAL_IMAGE, connector);
                    handle(ActionType.REQUEST_CHECKPOINT_IMAGES, connector);
                    handle(ActionType.REQUEST_ALL_INSTRUCTIONS, connector);
                    System.out.println("\n----sent all requests stuff-----\n");
                    // Tells the connector to update its view
                }
                    break;
            }
        });
    }

    @Override
    public synchronized void handle(byte[] file, FileType fileType, String message, ActionType actionType, Connector connector) {
        Platform.runLater(() -> {

        });
    }
}
