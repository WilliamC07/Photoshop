package network;

import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.PriorityQueue;

/**
 * Everyone connected to a network should have an instance of this class. This is used to communicate with the network,
 * which is hosted by the same program or a different computer.
 *
 * @see network.Server
 */
public class Client{
    private Connector connector;
    private PriorityQueue<Sender> thingsToSend = new PriorityQueue<>();
    private Sender recentSend;
    private int resentAttempts;


    /**
     * Constructs a Client to communicate with a network
     * @param targetHost IP address of the network
     * @param targetPort Port of the network
     */
    public Client(String targetHost, int targetPort) throws UnknownHostException, IOException {
        connector = new Connector(new Socket(targetHost, targetPort), this::handle);
        connector.start();
        /*//TODO: Remove testing
        File file = new File("network/4k.png"); // Rename 4k.png to what file you are sending (must be a .png)
        sendFile(new Sender(file, FileType.IMAGE, ActionType.SEND_ORIGINAL_IMAGE)); // Ignore ActionType since it isn't completed yet*/
    }

    void sendFile(Sender sender){
        // No queue, so just send it
        if(thingsToSend.size() == 0){
            connector.sendFile(sender);
        }else{
            // A queue, wait for rest to send first
            thingsToSend.add(sender);
        }
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
            case RESEND:
                if(resentAttempts < 5){
                    // Skip the queue and send the message again, but don't remove it from the queue because we are
                    // not sure if it is successful this time
                    connector.sendFile(thingsToSend.peek());
                    resentAttempts++;
                }else{
                    connector.sendFile(new Sender(ActionType.FAILURE));
                }
                break;
            case SUCCESSFUL_TRANSACTION:
                // Can remove top priority from queue because it is successful
                thingsToSend.poll();
                // If there is something to send, send it, otherwise just leave
                if(thingsToSend.size() > 0){
                    // Send the next item. Don't poll because we are not sure if it has been successfully removed.
                    connector.sendFile(thingsToSend.peek());
                }
                break;
            case FAILURE:
                // Deal with failure
        }
    }
}
