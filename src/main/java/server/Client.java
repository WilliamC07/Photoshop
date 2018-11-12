package server;

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
            File file = new File("image.jpg");
            int fileSize = (int) file.length();  // The image size should be less than the max size of an int

            // Turn the file into a byte array to be sent through a stream
            FileInputStream fileInputStream = new FileInputStream(file);
            byte[] imageRepresentation = new byte[fileSize];
            fileInputStream.read(imageRepresentation, 0, fileSize);

            // First data is the file size
            outStream.writeInt(fileSize);
            // Second data is the actual byte array representation of the image
            outStream.write(imageRepresentation);
            // Send the data out
            outStream.flush();
        }catch(IOException e){
            e.printStackTrace();
            System.exit(1);
        }
    }
}