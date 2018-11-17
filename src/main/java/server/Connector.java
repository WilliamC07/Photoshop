package server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * This is used to deal with the information coming in through the stream from a socket. The class that uses this
 * needs implement ActionHandler. Information received through the socket will be passed onto the ActionHandler.
 *
 * @see server.ActionHandler
 */
class Connector extends Thread{
    private File fileReceived;
    private final Socket socket;
    private FileSender fileSender;
    private FileReceiver fileReceiver;
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
     * @param fileSender Information to be sent
     */
    void sendFile(FileSender fileSender){
        this.fileSender = fileSender;
    }

    /**
     * This sends information through the socket and listens for information from the socket as well.
     *
     * There is no reason to call this method. Call {@link #start()} instead.
     */
    @Override public void run() {
        try(DataOutputStream outStream = new DataOutputStream(socket.getOutputStream());
            DataInputStream inputStream = new DataInputStream(socket.getInputStream())) {

            socket.setSoTimeout(0); // The socket will NEVER timeout, connection is forever (TODO: good idea?)
            while(true){
                // Listen for data first
                if(inputStream.available() > 0){
                    // Build file
                    int chunkNum = inputStream.readInt();
                    System.out.println(chunkNum);
                    ChunkType chunkIdentifier = ChunkType.get(chunkNum); // First int is always identifier
                    switch(chunkIdentifier){
                        case START:
                            int chunkSize = inputStream.readInt();
                            int fileSize = inputStream.readInt();
                            fileReceiver = new FileReceiver(chunkSize, fileSize);
                            break;
                        case DATA:
                            fileReceiver.build(inputStream);
                            break;
                        case END:
                            fileReceived = new File("copy.png");
                            fileReceiver.setFile(inputStream, fileReceived);
                            ActionType actionType = ActionType.get(inputStream.readInt());
                            actionHandler.handle(fileReceived, actionType);
                            break;
                    }
                }else{
                    Thread.sleep(1000); // Nothing to do, pause
                }

                // Send any information
                if(fileSender != null){
                    fileSender.send(outStream);
                    fileSender = null; // All data spent, nothing to do now
                }
            }
        }catch(UnknownHostException e){
            // Deal with if the connect cannot be made (ip address doesn't exist)
        }catch(IOException e){
            // Deal with if the reading/writing of data cannot be made
        }catch(InterruptedException e){
            // Cannot send information
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