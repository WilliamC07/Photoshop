package project;

import javafx.scene.image.Image;
import network.ActionType;
import network.Client;
import network.Sender;
import network.Server;
import model.imageManipulation.edits.ImageBuilder;
import views.ApplicationStart;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

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
     * Builder of the most recent image with the most recent edits.
     */
    private ImageBuilder imageBuilder;

    /**
     * Constructs the program and the project.
     */
    public Project(){
        fileInformation = new FileInformation();
        // Show the view
    }

    /**
     * Opens the existing project.
     * @param path Path to the existing project.
     */
    public void openExistingProject(Path path){
        fileInformation.openExistingProject(path);
    }

    public boolean createProject(String name){
        return fileInformation.createProject(name);
    }

    public void connectToServer(Client client){
        this.client = client;
    }

    public Client getClient(){
        return client;
    }

    public ImageBuilder getImageBuilder() {
        return imageBuilder;
    }

    public boolean setOriginalImage(File file){
        try{
            imageBuilder = new ImageBuilder(new Image(Files.newInputStream(file.toPath())));
        }catch(IOException e){
            return false;
        }

        return fileInformation.setOriginalImage(file);
    }

    public void setOriginalImage(byte[] file){
        imageBuilder = new ImageBuilder(fileInformation.setOriginalImage(file));
    }

    public File getOriginalImage(){
        return fileInformation.getOriginalImage();
    }

    public void setServer(Server server) {
        this.server = server;
    }

    public void setCollaborators(String[] collaborators){
        this.collaborators = collaborators;
    }

    public void setProjectName(String projectName){
        this.projectName = projectName;
    }

    public String getProjectName(){
        return projectName;
    }

    /**
     * Requests the server for a series of information
     */
    public void initializeSharedProject(){
        client.sendFile(new Sender(ActionType.REQUEST_ORIGINAL_IMAGE));
    }
}
