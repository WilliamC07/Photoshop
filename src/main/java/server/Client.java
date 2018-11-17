//package server;

import java.io.*;
import java.net.Socket;

public class Client {
    public static void main(String[] args) {
        String address = "127.0.0.1";
        int port = 49153;
        FileHandle fileHandle = null;

        try(Socket socket = new Socket(address, port);
            // DataOutputStream to allow bytes and Java primitives to be sent
            DataOutputStream outStream = new DataOutputStream(socket.getOutputStream());
        ){
            // Get the file. Images are represented by File to make it flexible enough to send xml files
            File file = new File("8k.jpg");
            fileHandle = new FileHandle((int) file.length());
            fileHandle.send(file, outStream, FileType.IMAGE);
        }catch(IOException e){
            e.printStackTrace();
            System.exit(1);
        }
    }
}