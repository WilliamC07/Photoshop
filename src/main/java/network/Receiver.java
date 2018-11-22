package network;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Receives information send by Sender. Gets the File sent (if any) and tells the receiving end to perform the action
 * described by ActionType.
 *
 * @see Sender
 * @see ActionType
 */
public class Receiver {
    private final int MAX_CHUNK_SIZE = Sender.MAX_DATA_SIZE;
    private int expectedReadBytes;
    private int readBytes;
    private byte[][] byteRepresentation;

    /**
     * Constructs an instance to build a file
     * @param chunksAmount Amount of chunks expected to be sent
     * @param fileSize Final size of File expected to be created
     */
    Receiver(int chunksAmount, int fileSize){
        // We are receiving instructions only
        if(chunksAmount == 0){
            byteRepresentation = null;
        }else{
            expectedReadBytes = fileSize;

            // Allocated space in the array to store the content of future incoming DATA chunks
            byteRepresentation = new byte[chunksAmount][];
            int remainder = fileSize - (chunksAmount - 1) * MAX_CHUNK_SIZE;
            for(int i = 0; i < chunksAmount; i++){
                byteRepresentation[i] = new byte[i == chunksAmount - 1 ? remainder : MAX_CHUNK_SIZE];
            }
        }
    }

    /**
     * Build the file up given a stream that contains only part of the File to be made.
     * If no file is sent, this function should never be called (this is guaranteed by the Sender class)
     * @param stream Stream with the data chunk.
     * @throws IOException Exception from the stream
     */
    void build(DataInputStream stream) throws IOException {
        int chunkNumber = stream.readInt(); // First data is always which chunk number
        readBytes += stream.read(byteRepresentation[chunkNumber]);
    }

    /**
     * Creates the file given all the data received from {@link #build(DataInputStream)}.
     *
     * @param file File to create (does not create a new instance, it modifies the existing one)
     * @throws IOException Exception from the stream
     */
    void setFile(File file) throws IOException, NetworkException{
        // No file received, don't do anything
        if(byteRepresentation == null){
            return;
        }

        // TODO: Temporary checksum, will improve on later
        if(readBytes != expectedReadBytes){
            throw new NetworkException(
                    String.format("Read bytes(%d) doesn't match expected read(%d)", readBytes, expectedReadBytes));
        }

        FileOutputStream fileOutputStream = new FileOutputStream(file);
        for(int i = 0; i < byteRepresentation.length; i++){
            fileOutputStream.write(byteRepresentation[i]);
        }
    }
}
