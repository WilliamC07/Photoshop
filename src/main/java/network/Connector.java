package network;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.Socket;

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
    private Sender sender;
    private final ActionHandler actionHandler;

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
        this.sender = sender;
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
                    // Listen for data first
                    if (inputStream.available() > 0) {
                        // Build file
                        int chunkNum = inputStream.readInt();
                        ChunkType chunkIdentifier = ChunkType.get(chunkNum); // First int is always identifier
                        switch (chunkIdentifier) {
                            case START:
                                int chunkSize = inputStream.readInt();
                                int fileSize = inputStream.readInt();
                                FileType fileType = FileType.get(inputStream.readInt());
                                receiver = new Receiver(chunkSize, fileSize, fileType);
                                break;
                            case DATA:
                                // receiver *should* never be null, since the first chunk always initializes an object
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
                                    // This *should* never be null, since the first chunk always initializes an object
                                    // Throws an error if there are any issues (bad checksum/file...)
                                    receiver.setFile(fileReceived);

                                    // Inform the sender that the message was successfully received
                                    new Sender(ActionType.SUCCESSFUL_TRANSACTION);
                                    // No message
                                    actionHandler.handle(fileReceived, "", receiver.getFileType(), actionType, this);
                                }
                                receiver = null;  // Garbage collect
                                break;
                            // Don't need default because if the byte is read wrong, error is thrown from ChunkType
                        }
                    } else {
                        Thread.sleep(1000); // Nothing to do, pause
                    }

                    // Send any information
                    if (sender != null) {
                        sender.send(outStream);
                        sender.wasSent(true);  // So we don't spam repeat the same thing over and over again
                        sender = null; // All data spent, nothing to do now
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
