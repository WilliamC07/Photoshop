//package server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

// TODO: Fix up whatever mess this is
public class Server {
    /**
     * Temporary
     * Use this method for testing features.
     * @param args Command line argument
     */
    public static void main(String[] args) {
        int port = 49153; // This should later be changed to 0 the port is guaranteed to be free

        FileHandle fileHandle = null;
        FileType fileType = null;

        try(ServerSocket serverSocket = new ServerSocket(port);
            Socket socket = serverSocket.accept();
            DataInputStream dataInputStream = new DataInputStream(socket.getInputStream())
        ){
            while(true){
                // Make sure there is something to read
                if(dataInputStream.available() <= 0){
                    continue;
                }
                int chunkID = dataInputStream.readInt();
                System.out.println("CHUNKID: " + chunkID);
                ChunkType chunkType = ChunkType.get(chunkID);
                switch(chunkType){
                    case START:
                        fileType = FileType.get(dataInputStream.readInt());
                        // TODO: Checksum, for now just read off the data
                        dataInputStream.readInt();
                        fileHandle = new FileHandle(dataInputStream.readInt());
                        break;
                    case DATA:
                        fileHandle.build(dataInputStream);
                        break;
                    case END:
                        File file = fileHandle.get("copy.jpg");
                        fileHandle = null;
                        fileType = null;
                        break;
                }
                try{
                    Thread.sleep(100);
                }catch(InterruptedException e){}
            }
        }catch(IOException e){
            e.printStackTrace();
            System.exit(0);
        }
    }
}
