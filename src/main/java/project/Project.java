package project;

import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import network.ActionType;
import network.Client;
import network.Sender;
import network.Server;
import model.imageManipulation.edits.ImageBuilder;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.HashMap;

/**
 * Relevant information about the project. Used to access the images used in the project, instructions done, server
 * information (who are the contributors, etc...).
 *
 * Construct an instance of this class before accessing using {@link #getInstance()} through ProjectFactory.
 *
 * @see ProjectFactory
 */
public class Project {
    /**
     * Singleton instance
     */
    static Project project;
    /**
     * Name of the project
     */
    private String name;
    /**
     * Path to the directory of the project. This may give the path to the server directory, created by
     * {@link #createServerDirectory()} if the user is connected to the server.
     */
    private Path projectRoot;
    /**
     * Path to the directory of the program. (Contains all the project directories and program information like
     * settings).
     */
    private Path programRoot;
    /**
     * Path to the directory that contains the original image, checkpoint images, and most recently saved image.
     */
    private Path checkpointImageDirectory;

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
     * Key: Value:
     * "original": path to original imaged used
     * "c#" (ex. "c0" gives the first checkpoint image): path to checkpoint image
     * "recent": path to most recently saved image
     */
    private HashMap<String, Path> imagePaths;

    /**
     * Builder of the most recent image with the most recent edits. The image inside this builder is the same as the
     * image inside {@link #imagePaths} with a key of "recent".
     */
    private ImageBuilder imageBuilder;

    /**
     * Constructor to create an instance of this singleton.
     * This should be called when you are creating a project for the first time with a unique name (the project name
     * cannot be the same as another project also located in the program directory).
     * @param name Name of the project
     * @throws FileAlreadyExistsException If the directory is created in the same directory as the project with the
     *                                    same name, it will throw this.
     */
    Project(String name) throws FileAlreadyExistsException{
        // Initializes program information
        createProgramDirectory();

        // Initialize project information
        this.name = name;
        createProjectDirectory();
        imagePaths = getCheckpointImages();
    }

    /**
     * Constructor to create an instance of this singleton.
     * FIXME: The project directory must contain all the files needed: If any pieces are missing, the program should inform the user that it is corrupt
     * @param projectRoot Path to the existing directory of the project. The directory can be empty and it will
     *                    create the files needed. If any files are empty, it will be placed in a sub directory
     *                    of old files and generate new ones.
     */
    Project(Path projectRoot) {
        // Initializes program information
        createProgramDirectory();

        // Initialize project information
        this.projectRoot = projectRoot;
        imagePaths = getCheckpointImages();
    }

    Project(Client client){
        // Initializes program information
        createProgramDirectory();
        createServerDirectory();

        // Initialize project information
        this.client = client;
    }

    /**
     * Singleton accessor.
     * Instance must be initialized using the ProjectFactory.
     * @return singleton instance of the class
     */
    public static Project getInstance(){
        return project;
    }

    /**
     * Create the directory for the program.
     */
    private void createProgramDirectory(){
        // Make program directory (information about the program (setting, etc...) and the projects)
        // Because the program directory also contains information about program itself, it must still exists even
        // if the project is accessed from elsewhere (downloaded from someone)
        this.programRoot = Path.of(System.getProperty("user.home"));
        // Depending on operating system, the data will be storied in different places
        String osName = System.getProperty("os.name");
        String programName = "PhotoshopJava";
        if(osName.startsWith("Mac")){
            this.programRoot = programRoot.resolve("Library").resolve(programName);
        }else if(osName.startsWith("Windows")){
            this.programRoot = programRoot.resolve("AppData").resolve(programName);
        }else if(osName.startsWith("Linux")){
            this.programRoot = programRoot.resolve(programName);
        }

        try{
            if(!(Files.exists(programRoot) && Files.isDirectory(programRoot))) {
                Files.createDirectory(programRoot);
            }
        }catch(IOException e){
            // Can't continue without a destination to store data
            e.printStackTrace();
            System.exit(1);
        }
    }

    /**
     * Create a directory for the project. If the user imported the project (from {@link #Project(Path)}) and there are
     * missing subdirectories, it will create them.
     * @throws FileAlreadyExistsException If the project name is already used, it will create a conflict in directory
     *                                    name.
     */
    private void createProjectDirectory() throws FileAlreadyExistsException{
        this.projectRoot = programRoot.resolve(name);

        try{
            // Make project directory (information about a project; One directory per project)
            Files.createDirectory(projectRoot);

            // Create all the needed files
            createProjectFiles();
        }catch(FileAlreadyExistsException e){
            throw e;
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    /**
     * When the user connects to a server, they will download the project into this directory. If one already exists,
     * it will delete all the contents inside without warning the user. If the user wants to save the project, they
     * can either clone the server directory manually and TODO: clone it through the program
     */
    private void createServerDirectory(){
        this.projectRoot = programRoot.resolve("server");
        try{
            if(Files.isDirectory(programRoot)){
                // Delete the files inside if it already exists
                deleteContentsOfDirectory(projectRoot);
            }else{
                // Create the directory because it doesn't exist
                Files.createDirectory(programRoot);
            }

            // Create all the needed files
            createProjectFiles();
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    /**
     * Creates all the necessary files needed in the project directory.
     * @throws IOException Exception from creating the files
     */
    private void createProjectFiles() throws IOException{
        // Create CheckPointImage directory if one doesn't exist
        this.checkpointImageDirectory = projectRoot.resolve("CheckPointImages");
        if (!(Files.exists(checkpointImageDirectory) && Files.isDirectory(checkpointImageDirectory))) {
            Files.createDirectory(checkpointImageDirectory);
        }
    }

    /**
     * Before the main view (the one where the user can see the image and edit it), this method should be called. This
     * will request the server to send all the information. This should not be called from the constructor because
     * an instance of this class must already exist before we can request information.
     */
    private void getSharedProjectInformation(){
        client.sendFile(new Sender(ActionType.REQUEST_COLLABORATOR_LIST));
        client.sendFile(new Sender(ActionType.REQUEST_PROJECT_NAME));
    }

    /**
     * Gets the paths for the original image, checkpoint images, and most recent image.
     * @return Paths to the original image, checkpoint image, and most recent image.
     */
    private HashMap<String, Path> getCheckpointImages(){
        HashMap<String, Path> paths = new HashMap<>();
        try{
            // The name of the file
            Files.walk(checkpointImageDirectory).
                    filter(p -> !Files.isDirectory(p)).
                    forEach((Path p) -> {
                        String fileName = p.getFileName().toString();
                        // Removes file extension (.png) to make it a key for the HashMap
                        // The files are named as original.png, c# (ex. c0 for first checkpoint image), and recent.png
                        paths.put(fileName.substring(0, fileName.indexOf(".")), p);
                    });
        }catch(IOException e){
            e.printStackTrace();
        }

        return paths;
    }

    /**
     * Clones the given file into the project directory {@link #checkpointImageDirectory} and sets that to be the
     * original picture to base the project off of.
     * If an original image already exists, it will delete it along with all checkpoint images and most recent image.
     * This cannot be undo (TODO: Make it undoable)
     * @param file File to clone and set as the original image
     * @return True if the image is valid (a .png file) and was successfully set as the original image. False otherwise.
     */
    public boolean setOriginalImage(File file){
        Path path = Paths.get(file.toURI());

        if(path.getFileName().toString().endsWith(".png")){
            try {
                // Remove any original image/checkpoint image/recent image
                deleteContentsOfDirectory(checkpointImageDirectory);

                // Clone it to the checkpoint directory to prevent editing directly on true original file
                Path pathToOriginal = checkpointImageDirectory.resolve("original.png");
                Files.copy(path, pathToOriginal);

                // Load the image onto the program
                Image image = new Image(Files.newInputStream(pathToOriginal));
                imageBuilder = new ImageBuilder(image);
            }catch(IOException e){
                // Do nothing, this will not occur unless the user decides to delete the project halfway through
            }

            imagePaths.put("original", path);
            return true;
        }
        return false; // Not a valid picture
    }

    /**
     * Name the project. This can be renamed multiple times.
     * You must check if the name won't conflict before passing it into this function.
     */
    public void setName(String name){
        this.name = name;
        try{
            // Rename the project
            Files.move(projectRoot, Files.createFile(projectRoot.getParent().resolve(name)),
                       StandardCopyOption.REPLACE_EXISTING);
        }catch(IOException e){
            e.printStackTrace();
        }

        // TODO: Update the name on the screen
        System.out.println(name);
    }
    /**
     * Get the name of the project
     * @return Name of the project.
     */
    public String getName(){
        return this.name;
    }
    public boolean hasOriginalImage(){
        return imagePaths.containsKey("original");
    }
    public Client getClient() {
        return client;
    }
    public void setClient(Client client) {
        this.client = client;
    }
    public Server getServer() {
        return server;
    }
    public void setServer(Server server) {
        this.server = server;
    }
    public String[] getCollaborators() {
        return collaborators;
    }
    public void setCollaborators(String[] collaborators){
        this.collaborators = collaborators;
        for(String s : collaborators){
            System.out.println(s);
        }
    }
    /**
     * The file will only exist if the user already chose an original image. If the user didn't choose the original
     * image yet, it will return null.
     * @return Instance of the current image being worked on.
     */
    public ImageBuilder getImageBuilder() {
        return imageBuilder;
    }

    /**
     * Deletes all the files in the directory. Depth of 10.
     * @param path Directory path whose content is to be deleted
     */
    private void deleteContentsOfDirectory(Path path){
        if(!Files.isDirectory(path)){
            throw new IllegalArgumentException("Not a directory");
        }
        try{
            Files.walk(path, 10).
                    filter(p -> !Files.isDirectory(p)).  // Do not delete a directory, only files
                    forEach(p -> {  // Delete the individual files
                try{
                    Files.delete(p);
                }catch(IOException e){
                    e.printStackTrace();
                }
            });
        }catch(IOException e){
            e.printStackTrace();
        }
    }
}
