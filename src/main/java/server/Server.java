//package server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;

// TODO: Fix up whatever mess this is
public class Server {
    /**
     * Temporary
     * Use this method for testing features.
     * @param args Command line argument
     */
    public static void main(String[] args) {
        int port = 49153; // This should later be changed to 0 the port is guaranteed to be free

        boolean isReadingImage = false;
        DownloadImage downloadImage = null;


        try(ServerSocket serverSocket = new ServerSocket(port);
            Socket socket = serverSocket.accept();
            DataInputStream dataInputStream = new DataInputStream(socket.getInputStream())
        ){
            while(true){
                if(dataInputStream.readInt() == 0){  // Starting or ending the reading of an image
                    isReadingImage = !isReadingImage;
                    if(downloadImage == null){
                        downloadImage = new DownloadImage(dataInputStream.readInt(), dataInputStream.readInt(),
                                                          dataInputStream.readInt(), dataInputStream.readInt());
                    }
                    // Finish reading
                    if(!isReadingImage){
                        downloadImage.generateAndDownloadImage(new File("copy.jpg"));
                        System.exit(0);
                    }
                }else if(isReadingImage){
                    downloadImage.addChunk(dataInputStream);
                }
            }
        }catch(IOException e){
            e.printStackTrace();
            System.exit(0);
        }
    }

    private static class DownloadImage{
        private final int fileSize;
        private final int totalChunks;
        private final int MAX_CHUNK_SIZE;
        private final int lastChunkSize;
        private final byte[] byteRepresentation;

        DownloadImage(int fileSize, int totalChunks, int MAX_CHUNK_SIZE, int lastChunkSize) {
            this.fileSize = fileSize;
            this.totalChunks = totalChunks;
            this.MAX_CHUNK_SIZE = MAX_CHUNK_SIZE;
            this.lastChunkSize = lastChunkSize;

            /*System.out.println("server: "+fileSize);
            System.out.println("server: "+totalChunks);
            System.out.println("server: "+MAX_CHUNK_SIZE);
            System.out.println("server: "+lastChunkSize);*/
            byteRepresentation = new byte[fileSize];
        }

        void addChunk(DataInputStream dataInputStream) throws IOException{
            int chunkNumber = dataInputStream.readInt();
            int chunkSize = dataInputStream.readInt();

            int start = 0;
            if(chunkNumber != 0){
                start = (chunkNumber - 1) * MAX_CHUNK_SIZE;
            }
            int end = start + chunkSize;

            dataInputStream.read(byteRepresentation, start, end); // TODO: Ensure the read is same as expected chunkSize
        }

        void generateAndDownloadImage(File file) throws IOException{
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            fileOutputStream.write(byteRepresentation);
        }
    }
}
