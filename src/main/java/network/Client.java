package network;

import project.Project;

import java.io.File;
import java.io.IOException;
import java.lang.management.ThreadInfo;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.PriorityQueue;
import java.util.concurrent.PriorityBlockingQueue;

/**
 * Everyone connected to a network should have an instance of this class. This is used to communicate with the network,
 * which is hosted by the same program or a different computer.
 *
 * @see network.Server
 */
public class Client implements ActionHandler{
    private Connector connector;
    private Project project = Project.getInstance();

    /**
     * Constructs a Client to communicate with a network
     * @param targetHost IP address of the network
     * @param targetPort Port of the network
     */
    public Client(String targetHost, int targetPort) throws IOException {
        connector = new Connector(new Socket(targetHost, targetPort), this);
        connector.start();
    }

    public void sendFile(Sender sender){
        connector.sendFile(sender);
    }

    @Override
    public void handle(String message, ActionType actionType, Connector connector) {
        switch(actionType){
            case UPDATE_TO_LATEST_COLLABORATOR:
                project.setCollaborators(message.split(", "));
                break;
            case UPDATE_PROJECT_NAME:
                project.setName(message);
                break;
        }

    }

    @Override
    public void handle(byte[] file, FileType fileType, ActionType actionType, Connector connector) {
        switch(actionType){
            case UPDATE_ORIGINAL_IMAGE:

                break;
        }
    }

    @Override
    public void handle(ActionType actionType, Connector connector) {

    }

    @Override
    public void handle(byte[] file, FileType fileType, String message, ActionType actionType, Connector connector) {

    }
}
