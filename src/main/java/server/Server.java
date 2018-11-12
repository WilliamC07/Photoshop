package server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    /**
     * Temporary
     * Use this method for testing features.
     * @param args Command line argument
     */
    public static void main(String[] args) {
        int port = 49153; // This should later be changed to 0 the port is guaranteed to be free

        try(ServerSocket serverSocket = new ServerSocket(port);
            Socket socket = serverSocket.accept();
            DataInputStream dataInputStream = new DataInputStream(socket.getInputStream())
        ){
            // Get the byte representation
            byte[] fileRepresentation = new byte[dataInputStream.readInt()];
            dataInputStream.read(fileRepresentation);

            // Turn the byte representation into a File
            FileOutputStream fileOutputStream = new FileOutputStream("imageCopy.jpg");
            fileOutputStream.write(fileRepresentation, 0, fileRepresentation.length);
        }catch(IOException e){
            e.printStackTrace();
            System.exit(0);
        }
    }
}
