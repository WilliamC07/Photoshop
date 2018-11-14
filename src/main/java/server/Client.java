//package server;

import java.awt.*;
import java.io.*;
import java.net.Socket;

public class Client {
    public static void main(String[] args) {
        String address = "127.0.0.1";
        int port = 49153;

        try(Socket socket = new Socket(address, port);
            // DataOutputStream to allow bytes and Java primitives to be sent
            DataOutputStream outStream = new DataOutputStream(socket.getOutputStream());
        ){
            // Get the file. Images are represented by File to make it flexible enough to send xml files
            File file = new File("1080p.jpg");
            sendFile(file, outStream);
        }catch(IOException e){
            e.printStackTrace();
            System.exit(1);
        }
    }

    public static void sendFile(File file, DataOutputStream dataOutputStream) throws IOException{
        // File size is needed to create a byte[] which will then be used to create a file
        int fileSize = (int) file.length();

        // Get byte representation of the image to send through a stream
        byte[] byteRepresentation = new byte[fileSize];
        FileInputStream fileStream = new FileInputStream(file);
        fileStream.read(byteRepresentation, 0, fileSize);  // TODO: Make sure we do real all the bytes

        // We are sending the image by chunks of maximum of 100 000 bytes
        final int MAX_CHUNK_SIZE = 100_000;
        int totalChunks = (int) Math.ceil(fileSize / (double) MAX_CHUNK_SIZE);
        int lastChunkSize = fileSize % MAX_CHUNK_SIZE;

        // Information to generate the image and make sure all the information is received
        dataOutputStream.writeInt(0); // 0 means we are sending an image
        dataOutputStream.writeInt(fileSize);
        dataOutputStream.writeInt(totalChunks);
        dataOutputStream.writeInt(MAX_CHUNK_SIZE);
        dataOutputStream.writeInt(lastChunkSize);

        /*System.out.println("Client: "+fileSize);
        System.out.println("Client: "+totalChunks);
        System.out.println("Client: "+MAX_CHUNK_SIZE);
        System.out.println("Client: "+lastChunkSize);*/
        dataOutputStream.flush();

        // We are then sending the actual image
        for(int i = 0; i < totalChunks; i++){
            boolean isLastChunk = i == totalChunks - 1;

            // First part of the stream is the chunk number
            dataOutputStream.writeInt(i);
            // Second part is the size of the chunk
            dataOutputStream.writeInt(isLastChunk ? lastChunkSize : MAX_CHUNK_SIZE);
            // Send the chunks
            int start = i * MAX_CHUNK_SIZE;
            if(isLastChunk){
                start = (i - 1) * MAX_CHUNK_SIZE;
            }
            int end = start + (isLastChunk ? lastChunkSize : MAX_CHUNK_SIZE);
            System.out.println("start: "+start);
            System.out.println("end: " +end);
            dataOutputStream.write(byteRepresentation, start, end);

            dataOutputStream.flush();
        }

        // Close off sending image
        dataOutputStream.writeInt(0);
    }
}