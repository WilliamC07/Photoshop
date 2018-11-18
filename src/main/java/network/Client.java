package network;

import java.io.File;
import java.io.IOException;
import java.net.Socket;

/**
 * Everyone connected to a network should have an instance of this class. This is used to communicate with the network,
 * which is hosted by the same program or a different computer.
 *
 * @see network.Server
 */
public class Client{
    private Connector connector;

    /**
     * Constructs a Client to communicate with a network
     * @param targetHost IP address of the network
     * @param targetPort Port of the network
     */
    public Client(String targetHost, int targetPort){
        targetHost = "127.0.0.1"; // For testing purposes. TODO: Remove
        targetPort = 49153; // For testing purpises TODO: Remove
        try{
            connector = new Connector(new Socket(targetHost, targetPort), this::handle);
            connector.start();
        }catch (IOException e){
            e.printStackTrace();
        }
        /*
        TODO: Remove testing
        File file = new File("network/4k.png");
        System.out.println("go" + file.length());
        sendFile(new Sender(file, FileType.IMAGE, ActionType.SEND_LATEST_CHANGES));
        */
        sendFile(new Sender(ActionType.SEND_ORIGINAL_IMAGE));
    }

    void sendFile(Sender sender){
        connector.sendFile(sender);
    }

    /**
     *
     * @param file File received
     * @param fileType File type received
     * @param actionType Action type received
     * @param connector Connector that delivered the information
     * @throws IllegalArgumentException Wrong instruction sent
     */
    private void handle(File file, FileType fileType, ActionType actionType, Connector connector) throws IllegalArgumentException{
        switch(actionType){

        }
    }
}