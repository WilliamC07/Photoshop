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
public class Client{
    private Connector connector;
    /**
     * Allows multiple threads to access
     */
    private PriorityQueue<Sender> queue = new PriorityQueue<>();
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
        sendQueuedItems.start();
        /*//TODO: Remove testing
        File file = new File("network/4k.png"); // Rename 4k.png to what file you are sending (must be a .png)
        sendFile(new Sender(file, FileType.IMAGE, ActionType.SEND_ORIGINAL_IMAGE)); // Ignore ActionType since it isn't completed yet*/
    }

    public void sendFile(Sender sender){
        queue.add(sender);
    }

    private Thread sendQueuedItems = new Thread(() -> {
        while(true){
            try{
                // So the thread doesn't eat up the cpu
                Thread.sleep(500);

                // Only send files once, if the receiving ends needs it to be resent, it is handled elsewhere
                Sender sender = queue.peek();
                if(sender != null && !sender.wasSent()){
                    connector.sendFile(sender); // This will set wasSent to true
                }
            }catch(InterruptedException e){
                e.printStackTrace();
            }
        }
    });

    /**
     *
     * @param file File received (Can be null)
     * @param message Can be null if and only if file is null.
     * @param fileType File type received
     * @param actionType Action type received
     * @param connector Connector that delivered the information
     * @throws IllegalArgumentException Wrong instruction sent
     */
    private void handle(File file, String message, FileType fileType, ActionType actionType, Connector connector) throws IllegalArgumentException{
        switch(actionType){
            case RESEND:
                if(resentAttempts < 5){
                    // Skip the queue and send the message again, but don't remove it from the queue because we are
                    // not sure if it is successful this time
                    connector.sendFile(queue.peek());
                    resentAttempts++;
                }else{
                    connector.sendFile(new Sender(ActionType.FAILURE));
                }
                break;
            case SUCCESSFUL_TRANSACTION:
                // Can remove top priority from queue because it is successful
                queue.poll();
                // If there is something to send, send it, otherwise just leave
                if(queue.size() > 0){
                    // Send the next item. Don't poll because we are not sure if it has been successfully removed.
                    connector.sendFile(queue.peek());
                }
                break;
            case FAILURE:
                // Deal with failure
                break;
            case UPDATE_TO_LATEST_COLLABORATOR:
                // Must update the list of all connectors to this
                ArrayList<String> collaborators = new ArrayList<>(Arrays.asList(message.split(", ")));
                for(String collaborator : collaborators){
                    Project.getInstance().addCollaborator(collaborator);
                }
                break;
        }
    }
}
