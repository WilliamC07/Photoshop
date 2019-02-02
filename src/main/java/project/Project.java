package project;

import javafx.application.Platform;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import model.imageManipulation.edits.Edit;
import network.*;
import model.imageManipulation.edits.ImageBuilder;
import views.Head;
import views.MainDisplay;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * This is the head of the entire program. Think of this as the root node of a node tree.
 */
public class Project {
    /**
     * Name of the project
     */
    private String projectName;

    /**
     * This abstracts away the file system. Use this to gain access to all the files you could possibly need.
     */
    private FileInformation fileInformation;

    /**
     * Client used to connect to the server. If this is null, we are not connected.
     */
    private Client client;

    /**
     * Server (client connects to this).
     */
    private Server server;

    /**
     * Array of all the collaborators. The host will see their username as "<username he gave> (you)", but this array
     * only contains the username he gave without the "(you)"
     */
    private String[] collaborators;

    /**
     * The username given by the user
     */
    private String myUsername;

    /**
     * Builder of the most recent image with the most recent edits.
     */
    private ImageBuilder imageBuilder;

    private MainDisplay mainDisplay;

    private final Head head;

    //
    // Locks for accessing files
    //
    private Lock editsDoneLock = new ReentrantLock(true);

    /**
     * Constructs the program and the project.
     */
    public Project(Head head){
        this.head = head;
        fileInformation = new FileInformation();
    }

    /*
     * One of these three method should be called to initialize the project.
     */

    /**
     * Opens the existing project.
     * @param path Path to the existing project.
     */
    public void openExistingProject(Path path){
        fileInformation.openExistingProject(path);
    }

    /**
     * Creates a new project. The name of the project must not repeat those in the program directory.
     * @param name Name of the new project
     * @return True if the project could be created, false other wise.
     */
    public boolean createProject(String name){
        return fileInformation.createProject(name);
    }

    /**
     * Connects the the server through the given client. Asks the host for series of information.
     * @param client Client that is connected to the server
     */
    public void connectToServer(Client client){
        this.client = client;
        fileInformation.serverConnectionProject();
    }

    public Client getClient(){
        return client;
    }

    public ImageBuilder getImageBuilder() {
        if(imageBuilder == null){
            setImageBuilder();
        }
        return imageBuilder;
    }

    public void setImageBuilder(){
        imageBuilder = createImageBuilder();
        // refreshes the view to update what the user sees

    }

    private ImageBuilder createImageBuilder(){
        try{
            ImageBuilder builder = null;
            File recentImage = fileInformation.getRecentImage();

            // Try to get the original image, sometimes it gets corrupted so check for that
            try{
                if(recentImage != null) {
                    builder = new ImageBuilder(this, new Image(new FileInputStream(recentImage)));
                }
            }catch(IllegalArgumentException e){
                // Happens when the original image is corrupted
            }

            // Sets the builder to the original image because either there isn't a most recent image or the most recent
            // image has been corrupted while saving (no idea why that happens).
            if(builder == null) {
                builder = new ImageBuilder(this, new Image(new FileInputStream(fileInformation.getOriginalImage())));
            }

            Edit[] edits = fileInformation.getEditsDone();
            if(edits != null){
                builder.edit(fileInformation.getEditsDone());
            }

            return builder;
        }catch(IOException e){
            System.out.println("cannot get image builder");
            e.printStackTrace();
        }

        return null;
    }

    public boolean setOriginalImage(File file){
        return fileInformation.setOriginalImage(file);
    }

    public void setOriginalImage(byte[] file){
        imageBuilder = new ImageBuilder(this, fileInformation.setOriginalImage(file));
    }

    public File getOriginalImage(){
        return fileInformation.getOriginalImage();
    }

    private Image convertFileToImage(File file){
        try{
            return new Image(new FileInputStream(file));
        }catch(FileNotFoundException e){
            e.printStackTrace();
            // Do nothing
        }
        return null; // This means the original image does not exist
    }

    public void setCheckpointImage(byte[] file, String checkpointNumber){
        fileInformation.setCheckpointImage(Integer.valueOf(checkpointNumber), file);
    }

    /**
     * Creates the ImageBuilder given the edits.
     * @param file File given by the connection to the server (if one exists)
     */
    public void setEditsDone(byte[] file){
        editsDoneLock.lock();
        fileInformation.setEditsDone(file, this);

        editsDoneLock.unlock();
    }

    public void updateConnectors(String stringRepresentation){
        // Send the information
        if(server != null){
            server.send(new Sender(fileInformation.getEditsDoneFile(this), FileType.XML, ActionType.UPDATE_TO_LATEST_INSTRUCTION));
        }else if(client != null){
            client.sendFile(new Sender(stringRepresentation, ActionType.PUSH_INSTRUCTION));
        }
    }

    void setEditsDone(boolean needToRecreate, Edit[] edits){
        if(needToRecreate){
            imageBuilder = new ImageBuilder(this, convertFileToImage(getOriginalImage()));
        }
        imageBuilder.edit(edits);
        mainDisplay.regenerateEditingView();
    }

    /**
     * Gets all the file containing all the edits done on the project.
     * @return
     */
    public File getEditsDoneFile(){
        return fileInformation.getEditsDoneFile(this);
    }

    public void setServer(Server server) {
        // Save the files first so we can send them
        save();
        this.server = server;
    }

    public Server getServer() {
        return server;
    }

    public void setCollaborators(String[] collaborators){
        this.collaborators = collaborators;
        mainDisplay.getNetworkComponent().updateCollabList(collaborators);
    }

    public String[] getCollaborators() {
        return collaborators;
    }

    public String getProjectName(){
        return projectName;
    }

    public void save(){
        fileInformation.save(this);
    }

    public String getMyUsername() {
        return myUsername;
    }

    /**
     * This will only be called when there aren't any collaborator
     * @param myUsername Username provided by the user
     */
    public void setMyUsername(String myUsername) {
        this.myUsername = myUsername;
        if(server != null){
            // Need to update the server
            server.setMyUsername(myUsername);
            collaborators = new String[]{myUsername};
        }
    }

    public void setMainDisplay(MainDisplay mainDisplay) {
        this.mainDisplay = mainDisplay;
    }

    /**
     * Tells if the program is connected to a server
     * @return True if connected to a server, false otherwise
     */
    public boolean isConnectedToServer(){
        return client != null;
    }

    public void makeCheckpoint(int checkpointNumber, WritableImage image){
        fileInformation.setCheckpointImage(checkpointNumber, image);
    }

    /**
     * Gets the checkpoint image given the index.
     * @param index Index of the checkpoint image.
     * @return File of the checkpoint image, null if none are found
     */
    public File getCheckpointImage(int index){
        return fileInformation.getCheckpointImage(index);
    }

    public void showMainDisplay(){
        head.showMainDisplay();
    }

    public void copyRecentImage(Path savePath) throws IOException{
        // copy to the other directory
        fileInformation.copyRecentImage(savePath, getImageBuilder().getWritableImage());
    }
}
