package network;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * Sending files follows the format:
 * 1. Start
 */
final class FileSender {
    public static final int MAX_DATA_SIZE = 100_000;
    private final File file;
    private final FileType fileType;
    private final ActionType actionType;
    private final int chunksAmount;

    /**
     * Constructs an instance of the class.
     * @param file File to be sent through a stream
     * @param fileType What type of file is being sent
     * @param actionType What action should the receiving end perform after receiving the data
     */
    public FileSender(File file, FileType fileType, ActionType actionType) {
        this.file = file;
        this.fileType = fileType;
        this.actionType = actionType;

        int fileSize = (int) file.length();
        this.chunksAmount = (int) Math.ceil((double) fileSize / 100_000);
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
        sendData(outputStream, file);
        sendCloser(outputStream, fileType, actionType);
    }

    /**
     * Sends the START chunk. This chunks includes the following data:
     * 1. Chunk identifier (int)
     * 2. Amount of DATA chunks that will be sent through the stream (int)
     * 3. Final expected file size to act as a checksum (int)
     *
     * @param stream Stream to send the data through
     * @throws IOException Exception from the stream
     */
    private void sendIdentifier(DataOutputStream stream) throws IOException {
        stream.writeInt(ChunkType.get(ChunkType.START)); // Chunk identifier
        stream.writeInt(chunksAmount);
        int fileSize = (int) file.length();
        stream.writeInt(fileSize);
        stream.flush();
    }

    /**
     * Sends DATA chunks. The chunks includes the following data:
     * 1. Chunk identifier (int)
     * 2. Chunk number (starting with 0) (int)
     * 3. Byte[] of a section of the file to be sent (byte[])
     * @param stream Stream to send the data through
     * @param file File to be sent through the stream
     * @throws IOException Exception from the stream
     */
    private void sendData(DataOutputStream stream, File file) throws IOException{
        FileInputStream fileInputStream = new FileInputStream(file);
        int fileSize = (int) file.length();
        int amountOfChunks = (int) Math.ceil((double) fileSize / MAX_DATA_SIZE);
        int lastChunkSize = fileSize - (amountOfChunks - 1) * MAX_DATA_SIZE;

        for(int i = 0; i < amountOfChunks; i++){
            // Last chunk has a different sizes for all files
            boolean isLastChunk = i == amountOfChunks - 1;
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
     * Send the END chunk. This chunks includes the following data:
     * 1. Chunk identifier (int)
     * 2. The type of file sent
     * 3. What action to be performed by the receiver
     * @param stream Stream to send the data through
     * @param fileType Type of file sent
     * @param actionType Action to be performed by the receiver
     * @throws IOException Exception from the stream
     */
    private void sendCloser(DataOutputStream stream, FileType fileType, ActionType actionType) throws IOException {
        stream.writeInt(ChunkType.get(ChunkType.END)); // Chunk identifier
        stream.writeInt(FileType.get(fileType));
        stream.writeInt(ActionType.get(actionType));
        stream.flush();
    }
}
