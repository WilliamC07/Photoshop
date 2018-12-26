package project;

import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import model.imageManipulation.edits.Edit;
import network.ActionType;
import network.Client;
import network.Sender;
import network.Server;
import model.imageManipulation.edits.ImageBuilder;
import views.MainDisplay;

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
     * The username given by the user
     */
    private String myUsername;

    /**
     * Builder of the most recent image with the most recent edits.
     */
    private ImageBuilder imageBuilder;

    private MainDisplay mainDisplay;

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
                imageBuilder = new ImageBuilder(this, new Image(Files.newInputStream(getOriginalImage().toPath())));
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
        imageBuilder = new ImageBuilder(this, fileInformation.setOriginalImage(file));
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
}
