package project;

import javafx.scene.image.Image;
import model.imageManipulation.edits.Edit;
import network.ActionType;
import network.Client;
import network.Sender;
import network.Server;
import model.imageManipulation.edits.ImageBuilder;

import java.io.File;
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

        client.sendFile(new Sender(ActionType.REQUEST_ORIGINAL_IMAGE));
    }

    public Client getClient(){
        return client;
    }

    public ImageBuilder getImageBuilder() {
        if(imageBuilder == null){
            try{
                imageBuilder = new ImageBuilder(new Image(Files.newInputStream(getOriginalImage().toPath())));
                Edit[] edits = fileInformation.getEditsDone();
                if(edits != null){
                    imageBuilder.edit(fileInformation.getEditsDone());
                }
            }catch(IOException e){
                System.out.println("cannot get image builder");
                e.printStackTrace();
            }
        }
        return imageBuilder;
    }

    public boolean setOriginalImage(File file){
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

    public Server getServer() {
        return server;
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

    public void save(){
        fileInformation.save(this);
    }
}
