package network;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.PriorityBlockingQueue;

/**
 * This is used to deal with the information coming in through the stream from a socket. The class that uses this
 * needs implement ActionHandler. Information received through the socket will be passed onto the ActionHandler.
 *
 * TODO: MAJOR BUG: The connect should be the one dealing with queues, client and server does not need their own individual queue
 *
 * @see network.ActionHandler
 */
class Connector extends Thread{
    private final Socket socket;
    private final ActionHandler actionHandler;
    /**
     * Queue of items to be sent. PriorityBlockingQueue is for thread safety.
     */
    private PriorityBlockingQueue<Sender> queue = new PriorityBlockingQueue<>();

    /**
     * Creates a thread to listen and send information through a socket.
     * @param socket Socket to listen to and send information through
     * @param actionHandler Function to call to deal with incoming information
     */
    Connector(Socket socket, ActionHandler actionHandler){
        this.socket = socket;
        this.actionHandler = actionHandler;
    }

    /**
     * Send a file through the stream.
     * @param sender Information to be sent
     */
    void sendFile(Sender sender){
        queue.offer(sender);
    }

    /**
     * This sends information through the socket and listens for information from the socket as well.
     *
     * There is no reason to call this method. Call {@link #start()} instead.
     */
    @Override public void run() {
        Receiver receiver = null;
        try (DataOutputStream outStream = new DataOutputStream(socket.getOutputStream());
             DataInputStream inputStream = new DataInputStream(socket.getInputStream())) {

            socket.setSoTimeout(0); // The socket will NEVER timeout, connection is forever (TODO: good idea?)
            while (true) {
                try{
                    // Send out data
                    Sender sender;
                    if(queue.size() > 0 && !(sender = queue.peek()).wasSent()){
                        sender.send(outStream);
                        sender.wasSent(true); // One send data once and wait for a response
                    }

                    // Do not want an infinite loop, so we have to sleep if there is not data to read.
                    // We do not want a pause between reading data from the stream to speed up the program
                    if(inputStream.available() == 0){
                        Thread.sleep(200);
                        continue;
                    }

                    // This will be run when there is data to be read
                    // Build file
                    ChunkType chunkIdentifier = ChunkType.get(inputStream.readInt()); // First int is always identifier
                    switch (chunkIdentifier) {
                        case START:
                            int chunkSize = inputStream.readInt();
                            int fileSize = inputStream.readInt();
                            FileType fileType = FileType.get(inputStream.readInt());
                            receiver = new Receiver(chunkSize, fileSize, fileType);
                            break;
                        case DATA:
                            // receiver should never be null, since the first chunk always initializes an object
                            receiver.build(inputStream);
                            break;
                        case END:
                            ActionType actionType = ActionType.get(inputStream.readInt());
                            // getFileType() will not be null since the first chunk gives the ChunkType
                            if(receiver.getFileType() == FileType.STRING){
                                // No file to read
                                actionHandler.handle(null, receiver.getMessage(), FileType.STRING, actionType, this);
                            }else{
                                File fileReceived = new File("network/copy.png");
                                // This should never be null, since the first chunk always initializes an object
                                // Throws an error if there are any issues (bad checksum/file...)
                                receiver.setFile(fileReceived);

                                // Inform the sender that the message was successfully received
                                new Sender(ActionType.SUCCESSFUL_TRANSACTION);
                                // No message
                                actionHandler.handle(fileReceived, "", receiver.getFileType(), actionType, this);
                            }
                            receiver = null;  // Garbage collect
                            break;
                    }
                }catch(IOException e){
                    // Exception from reading data from the stream
                }catch(NetworkException e){
                    actionHandler.handle(null, null, null, ActionType.RESOLVE_FAILED_FILE_TRANSFER, this);
                    // Error in building the file
                }catch(InterruptedException e){
                    // Thread was stopped
                }
            }
        // TODO: Catching IOException is too vague
        }catch(IOException e){
            // Exception from reading data from stream or socket
        }
    }

    private void receiveData(DataInputStream inputStream){

    }

    /**
     * End the connection through the socket. This does not shut down the thread. To stop the thread, you must
     * call {@link #join()} from the class using this thread.
     */
    void terminateConnection(){
        try{
            socket.close();
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
