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
    private final FileType fileType;
    private final int MAX_CHUNK_SIZE = Sender.MAX_DATA_SIZE;
    private int expectedReadBytes;
    private int readBytes;
    private int chunksAmount;
    private byte[] byteRepresentation;
    private String message;

    /**
     * Constructs an instance to build a file
     * @param chunksAmount Amount of chunks expected to be sent
     * @param fileSize Final size of File expected to be created
     */
    Receiver(int chunksAmount, int fileSize, FileType fileType){
        this.fileType = fileType;

        // We are receiving instructions only
        if(fileSize == 0){
            byteRepresentation = null;
        }else{
            expectedReadBytes = fileSize;

            // Allocated space in the array to store the content of future incoming DATA chunks
            byteRepresentation = new byte[fileSize];
            this.chunksAmount = chunksAmount;
        }
    }

    /**
     * Build the file up given a stream that contains only part of the File to be made.
     * If no file is sent, this function should never be called (this is guaranteed by the Sender class)
     * If a string was sent (FileType.STRING), it will just read that string.
     *
     * TODO: Checksum for receving all the chunk in order
     * @param stream Stream with the data chunk.
     * @throws IOException Exception from the stream
     */
    void build(DataInputStream stream) throws IOException {
        if(fileType == FileType.STRING){
            // Only a single UTF string is sent, so read that
            this.message = stream.readUTF();
        }else{
            int chunkNumber = stream.readInt(); // First data is always which chunk number
            int start = MAX_CHUNK_SIZE * chunkNumber; // data chunks starts at 0
            int lastChunkSize = expectedReadBytes - MAX_CHUNK_SIZE * (chunksAmount - 1);
            int size = chunkNumber == chunksAmount - 1 ? lastChunkSize : MAX_CHUNK_SIZE; // length of chunk

            readBytes += stream.read(byteRepresentation, start, size);
        }
    }

    /**
     * Creates the file given all the data received from {@link #build(DataInputStream)}.
     *
     * @return byte representation of the file received
     * @throws IOException Exception from the stream
     */
    byte[] getFile() throws NetworkException{
        // No file received, don't do anything
        if(byteRepresentation == null){
            return null;
        }

        // TODO: Temporary checksum, will improve on later
        if(readBytes != expectedReadBytes){
            throw new NetworkException(
                    String.format("Read bytes(%d) doesn't match expected read(%d)", readBytes, expectedReadBytes));
        }

        return byteRepresentation;
    }

    String getMessage(){
        return message;
    }

    public FileType getFileType() {
        return fileType;
    }
}
