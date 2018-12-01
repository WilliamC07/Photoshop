package network;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * This class is used to send instructions (ActionType), Files, or both.
 *
 * @see ActionType
 */
public final class Sender implements Comparable<Sender>{
    public static final int MAX_DATA_SIZE = 100_000;
    // Either send a file or a message
    private File file;
    private String message;

    private final FileType fileType;
    private final ActionType actionType;
    /**
     * The amount of data chunks to send
     */
    private final int chunksAmount;
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

    /**
     * Constructs an instance of the class.
     * @param file File to be sent through a stream.
     * @param fileType What type of file is being sent
     * @param actionType What action should the receiving end perform after receiving the data
     */
    public Sender(File file, FileType fileType, ActionType actionType) {
        this.file = file;
        this.fileType = fileType;
        this.actionType = actionType;

        int fileSize = (int) file.length();
        this.chunksAmount = (int) Math.ceil((double) fileSize / MAX_DATA_SIZE);
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
        this.file = null; // Not sending a file
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
        // Only send data if there is a file to send, otherwise just skip to closer
        if(file != null){
            sendFile(outputStream);
        }
        if(!(message == null || message.isEmpty())){
            sendMessage(outputStream);
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
        stream.writeInt(file != null ? (int) file.length() : 0); // File size
        stream.writeInt(FileType.get(fileType));
        stream.flush();
    }

    /**
     * Sends DATA chunks. These chunks will only send if there is a File to send (that is if {@link #file} is not null).
     * The chunks includes the following data:
     * 1. Chunk identifier (int)
     * 2. Chunk number (starting with 0) (int)
     * 3. Byte[] of a section of the file to be sent (byte[])
     * @param stream Stream to send the data through
     * @throws IOException Exception from the stream
     */
    private void sendFile(DataOutputStream stream) throws IOException{
        FileInputStream fileInputStream = new FileInputStream(file);
        int fileSize = (int) file.length();
        int lastChunkSize = fileSize - (chunksAmount - 1) * MAX_DATA_SIZE;

        for(int i = 0; i < chunksAmount; i++){
            // Last chunk has a different sizes for all files
            boolean isLastChunk = i == chunksAmount - 1;
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
     * 2. UTF representation of the string
     * @param stream Stream to send the data through
     * @throws IOException Exception from the stream
     */
    private void sendMessage(DataOutputStream stream) throws IOException{
        stream.writeInt(ChunkType.get(ChunkType.DATA)); // Chunk identifier
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
