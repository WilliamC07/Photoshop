//package server;


import java.io.*;

public final class FileHandle {
    /**
     * Files are broken down into multiple chunks. This holds all the chunks. Not all inner arrays are the same length.
     */
    private final byte[][] byteRepresentation;
    /**
     * The amount of chunks expected to receive and write into {@link #byteRepresentation}.
     */
    private final int CHUNKS_AMOUNT;

    /**
     * A file will be broken down into multiple pieces. Each piece holds a maximum amount of data of the file.
     * The actual size of the chunk can be greater than 100_000 because it it also holds the chunk identifier,
     * chunk number as an int. Not all chunks hold data (some are identifier chunks)
     */
    private final int CHUNK_DEFAULT_SIZE = 100_000;

    /**
     * The last chunk may or may not contain the maximum amount of bytes defined by {@link #CHUNK_DEFAULT_SIZE}.
     * This is ths size for the last chunk.
     */
    private final int CHUNK_REMAINDER_SIZE;

    /**
     * Constructs an FileHandle to send or build and create a file.
     *
     * @see java.io.File
     * @param fileSize The length of the file.
     */
    public FileHandle(int fileSize) {
        this.CHUNKS_AMOUNT = (int) Math.ceil(((double) fileSize) / CHUNK_DEFAULT_SIZE);
        this.CHUNK_REMAINDER_SIZE = fileSize - (CHUNKS_AMOUNT - 1) * CHUNK_DEFAULT_SIZE;

        this.byteRepresentation = new byte[CHUNKS_AMOUNT][];
        for(int i = 0; i < CHUNKS_AMOUNT; i++)
            byteRepresentation[i] = new byte[i == CHUNKS_AMOUNT - 1 ? CHUNK_REMAINDER_SIZE : CHUNK_DEFAULT_SIZE];
    }

    /**
     * Sends the File through the stream.
     * @param file File to send through the stream
     * @param stream Stream medium to send the file through
     */
    public void send(File file, DataOutputStream stream, FileType type){
        try{
            setByteRepresentation(file);
            sendIdentifier(type, stream, (int) file.length());
            sendData(stream);
            sentEnder(stream);
        }catch(IOException e){
            System.out.println("Error in sending files");
            e.printStackTrace();
            System.exit(1);
        }
    }

    /**
     * Reads the file int {@link #byteRepresentation}.
     * @param file File to be converted into bytes.
     */
    private void setByteRepresentation(File file) throws IOException{
        try(FileInputStream fileInputStream = new FileInputStream(file)){
            for(int i = 0; i < CHUNKS_AMOUNT; i++){
                fileInputStream.read(byteRepresentation[i]);
            }
        }
    }

    /**
     * Sends a identifier chunk with (in the order):
     * 1. What type of chunk is being sent
     * 2. What type of data is being sent
     * 3. Number of chunks being sent
     * 4. Final size of the file expected
     * @param fileType What type of file is being sent
     * @param stream Stream to write the data to
     * @throws IOException Exception from writing to stream
     */
    private void sendIdentifier(FileType fileType, DataOutputStream stream, int size) throws IOException{
        stream.writeInt(ChunkType.get(ChunkType.START));
        stream.writeInt(FileType.get(fileType));
        stream.writeInt(CHUNKS_AMOUNT);
        stream.writeInt(size);
        stream.flush();
        System.out.println("send id");
    }

    /**
     * Sends parts of the File with (in the order):
     * 1. What type of chunk is being sent
     * 2. What chunk number
     * 3. The actual part of the file
     * @param stream Stream to write the data to
     */
    private void sendData(DataOutputStream stream) throws IOException{
        for(int i = 0; i < CHUNKS_AMOUNT; i++){
            stream.writeInt(ChunkType.get(ChunkType.DATA));
            stream.writeInt(i);
            stream.write(byteRepresentation[i]);
            stream.flush(); // Send the data away
            System.out.println("sent data " + i);
        }
    }

    /**
     * Sends the end identifier with (in the order):
     * 1. What type of chunk is being sent
     * @param stream
     * @throws IOException
     */
    private void sentEnder(DataOutputStream stream) throws IOException{
        stream.writeInt(ChunkType.get(ChunkType.END));
        stream.flush();
        System.out.println("sent end");
    }

    public void build(DataInputStream stream){
        try{
            int chunkNumber = stream.readInt(); // First data is always which chunk number
            stream.read(byteRepresentation[chunkNumber]);
        }catch(IOException e){
            System.out.println("error in making the file");
            e.printStackTrace();
            System.exit(1);
        }
    }

    public File get(String fileName) {
        File file = new File(fileName);
        try(FileOutputStream fileOutputStream = new FileOutputStream(file)){
            for(int i = 0; i < CHUNKS_AMOUNT; i++){
                fileOutputStream.write(byteRepresentation[i]);
            }
        }catch(IOException e){
            e.printStackTrace();
        }

        return null;
    }
}
