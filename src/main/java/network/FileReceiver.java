package network;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Builds a file given a stream that sends data in chunks. The is formatted through FileSender.
 *
 * @see network.FileSender
 */
public class FileReceiver {
    private final int MAX_CHUNK_SIZE = FileSender.MAX_DATA_SIZE;
    private int readBytes = 0;
    private byte[][] byteRepresentation;

    /**
     * Constructs an instance to build a file
     * @param chunksAmount Amount of chunks expected to be sent
     * @param fileSize Final size of File expected to be created
     */
    FileReceiver(int chunksAmount, int fileSize){
        // Allocated space in the array to store the content of future incoming DATA chunks
        byteRepresentation = new byte[chunksAmount][];
        int remainder = fileSize - (chunksAmount - 1) * 100_000;
        for(int i = 0; i < chunksAmount; i++){
            byteRepresentation[i] = new byte[i == chunksAmount - 1 ? remainder : 100_000];
        }
    }

    /**
     * Build the file up given a stream that contains only part of the File to be made
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
     * @throws IOException
     */
    void setFile(File file) throws IOException{
        FileOutputStream fileOutputStream = new FileOutputStream(file);
        for(int i = 0; i < byteRepresentation.length; i++){
            fileOutputStream.write(byteRepresentation[i]);
        }

        // Don't need it anymore, can be garbage collected
        byteRepresentation = null;
    }
}
