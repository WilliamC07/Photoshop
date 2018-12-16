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
     * Used to build the data received. Data is sent by chunks so this is needed.
     */
    private Receiver receiver;

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
        try (DataOutputStream outStream = new DataOutputStream(socket.getOutputStream());
             DataInputStream inputStream = new DataInputStream(socket.getInputStream())) {
            // Prevent connection from breaking if the user doesn't make changes
            socket.setSoTimeout(0);
            while (true) {
                try{
                    // Send out data
                    sendData(outStream);

                    // Do not want an infinite loop, so we have to sleep if there is not data to read.
                    // We do not want a pause between reading data from the stream to speed up the program
                    if(inputStream.available() == 0){
                        Thread.sleep(200);
                        continue;
                    }

                    // Build the data up
                    buildData(inputStream);

                }catch(IOException e){
                    // Something went wrong with the streams, close the connection
                    actionHandler.handle(ActionType.QUIT_CONNECTION, this);
                }
                catch(NetworkException e){
                    // Check sum failed, request again
                    actionHandler.handle(ActionType.RESOLVE_FAILED_FILE_TRANSFER, this);
                }catch(InterruptedException e){
                    // Thread was stopped
                    // Do nothing
                }
            }
        }catch(IOException e){
            // Something went wrong with the streams, close the connection
            actionHandler.handle(ActionType.QUIT_CONNECTION, this);
        }
    }

    /**
     * Sends the first data in the queue. When the data sent is successfully received, the next data in queue will be
     * sent.
     * @param outputStream Stream to output the data into.
     * @throws IOException Exception from the stream
     */
    private void sendData(DataOutputStream outputStream) throws IOException{
        if (queue.size() > 0) {
            queue.poll().send(outputStream);
        }
    }

    /**
     * When data is sent through a stream, it is broken into different parts (called chunks). This will bring all the
     * different chunks together and create a File or a String
     * @param inputStream Stream to read the data from
     * @throws IOException Checksum failed.
     */
    private synchronized void buildData(DataInputStream inputStream) throws IOException, NetworkException{
        ChunkType chunkIdentifier = ChunkType.get(inputStream.readInt()); // First int is always identifier
        switch (chunkIdentifier) {
            case START: {
                int chunkSize = inputStream.readInt();
                int fileSize = inputStream.readInt();
                FileType fileType = FileType.get(inputStream.readInt());
                receiver = new Receiver(chunkSize, fileSize, fileType);
            }
            break;
            case DATA: {
                // receiver should never be null, since the first chunk always initializes an object
                receiver.build(inputStream);
            }
            break;
            case END: {
                ActionType actionType = ActionType.get(inputStream.readInt());

                String message = receiver.getMessage();
                byte[] file = receiver.getFile();

                if(file == null){
                    actionHandler.handle(message, actionType, this);
                }else if(file != null){
                    FileType fileType = receiver.getFileType();

                    if(message != null){
                        actionHandler.handle(file, fileType, message, actionType, this);
                    }else{
                        actionHandler.handle(file, fileType, actionType, this);
                    }
                }

                receiver = null;  // Garbage collect
            }
            break;
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
