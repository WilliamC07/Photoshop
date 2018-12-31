package network;

import java.io.*;

/**
 * This class is used to send instructions (ActionType), Files, or both.
 *
 * @see ActionType
 */
public final class Sender implements Comparable<Sender>{
    public static final int MAX_DATA_SIZE = 100_000;
    // Either send a file or a message
    private FileInputStream fileInputStream;
    /**
     * We are always sending a string to make it easier to code and understand
     */
    private String message = "";

    private final FileType fileType;
    private final ActionType actionType;
    /**
     * The amount of data chunks to send
     */
    private int chunksAmount;
    private boolean wasSent = false;
    /**
     * Keep order of instances of this class
     */
    private static int sendersSent = 0;
    /**
     * The first sender is 0, the next is 1, etc.
     * Used for comparable
     */
    private int number = sendersSent++;
    private int fileSize;

    /**
     * Constructs an instance of the class.
     * @param file File to be sent through a stream.
     * @param fileType What type of file is being sent
     * @param actionType What action should the receiving end perform after receiving the data
     */
    public Sender(File file, FileType fileType, ActionType actionType) {
        try{
            this.fileInputStream = new FileInputStream(file);
        }catch(IOException e){
            // Do nothing
        }
        this.fileType = fileType;
        this.actionType = actionType;

        this.fileSize = (int) file.length();
        // first chunk is for message chunk. In this case, there isn't a message chunk, so an empty one is sent
        this.chunksAmount = (int) Math.ceil((double) fileSize / MAX_DATA_SIZE) + 1;
    }

    public Sender(FileInputStream fileInputStream, FileType fileType, ActionType actionType){
        this.fileInputStream = fileInputStream;
        this.fileType = fileType;
        this.actionType = actionType;
        try{
            this.fileSize = fileInputStream.available();
            // first chunk is for message chunk. In this case, there isn't a message chunk, so an empty one is sent
            this.chunksAmount = (int) Math.ceil((double) fileSize / MAX_DATA_SIZE) + 1;
        }catch(IOException e){
            // Do nothing
        }
    }

    public Sender(File file, String message, FileType fileType, ActionType actionType){
        try{
            this.fileInputStream = new FileInputStream(file);
        }catch(FileNotFoundException e){
            // Do nothing
        }

        this.message = message;
        this.fileType = fileType;
        this.actionType = actionType;
        this.fileSize = (int) file.length();
        this.chunksAmount = (int) Math.ceil((double) fileSize / MAX_DATA_SIZE) + 1; // add one because a message chunk is 1 chunk
    }

    public Sender(String message, ActionType actionType){
        this.message = message;
        this.fileType = FileType.STRING;
        this.actionType = actionType;
        this.chunksAmount = 1; // Only one chunk is needed for sending strings
    }

    public Sender(ActionType actionType){
        this.actionType = actionType;
        this.chunksAmount = 0;
        this.fileInputStream = null; // Not sending a file
        this.fileType = FileType.NONE;
    }

    /**
     * This method is only used for the Connector class. If you are not calling if from the Connector class, there
     * is not reason to use this.
     *
     * @see network.Connector
     * @param outputStream
     * @throws IOException
     */
    void send(DataOutputStream outputStream) throws IOException {
        sendIdentifier(outputStream);
        sendMessage(outputStream);

        // Only send data if there is a file to send, otherwise just skip to closer
        if(fileInputStream != null){
            sendFile(outputStream);
        }
        sendCloser(outputStream);
    }

    /**
     * Sends the START chunk. This chunks includes the following data:
     * 1. chunkIdentifier: Chunk identifier (int)
     * 2. chunkSize: Amount of DATA chunks that will be sent through the stream (int)
     * 3. fileSize: Final expected file size to act as a checksum (int)
     * 4. fileType: What FileType is being sent
     *
     * @param stream Stream to send the data through
     * @throws IOException Exception from the stream
     */
    private void sendIdentifier(DataOutputStream stream) throws IOException {
        stream.writeInt(ChunkType.get(ChunkType.START)); // Chunk identifier
        stream.writeInt(chunksAmount);
        stream.writeInt(fileInputStream != null ? fileSize : 0); // File size
        stream.writeInt(FileType.get(fileType));
        stream.flush();
    }

    /**
     * Sends DATA chunks. These chunks will only send if there is a File to send (that is if {@link #fileInputStream}
     * is not null). The chunks includes the following data:
     * 1. Chunk identifier (int)
     * 2. Chunk number (starting with 0) (int)
     * 3. Byte[] of a section of the file to be sent (byte[])
     * @param stream Stream to send the data through
     * @throws IOException Exception from the stream
     */
    private void sendFile(DataOutputStream stream) throws IOException{
        int lastChunkSize = fileSize - (chunksAmount - 2) * MAX_DATA_SIZE;

        // Start at i = 1 because the first chunk is the message chunk (message chunks are data chunks)
        for(int i = 1; i < chunksAmount; i++){
            // Last chunk has a different sizes for all files
            boolean isLastChunk = i == (chunksAmount - 1);
            // File must be converted into a byte array to be sent by a stream
            byte[] byteRepresentation = new byte[isLastChunk ? lastChunkSize : MAX_DATA_SIZE];
            fileInputStream.read(byteRepresentation);

            stream.writeInt(ChunkType.get(ChunkType.DATA)); // Chunk identifier
            stream.writeInt(i); // What chunk is being sent
            stream.write(byteRepresentation);
            stream.flush();
        }
    }

    /**
     * Sends the {@link #message} through the stream.
     * The chunk includes the following data:
     * 1. Chunk identifier
     * 2. Chunk number (always 0)
     * 3. UTF representation of the string
     * @param stream Stream to send the data through
     * @throws IOException Exception from the stream
     */
    private void sendMessage(DataOutputStream stream) throws IOException{
        stream.writeInt(ChunkType.get(ChunkType.DATA)); // Chunk identifier
        stream.writeInt(0); // Message chunks are always the first data chunk to be sent
        stream.writeUTF(message);
        stream.flush();
    }

    /**
     * Send the END chunk. This chunks includes the following data:
     * 1. Chunk identifier (int)
     * 2. What action to be performed by the receiver
     * @param stream Stream to send the data through
     * @throws IOException Exception from the stream
     */
    private void sendCloser(DataOutputStream stream) throws IOException {
        stream.writeInt(ChunkType.get(ChunkType.END));
        stream.writeInt(ActionType.get(actionType));
        stream.flush();
    }

    @Override
    public int compareTo(Sender o) {
        return Integer.compare(number, o.number);
    }

    boolean wasSent() {
        return wasSent;
    }

    void wasSent(boolean wasSent) {
        this.wasSent = wasSent;
    }
}
