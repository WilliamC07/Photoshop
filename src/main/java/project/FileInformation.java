package project;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;

/**
 * Deals with the file system on the computer. Creates all needed files and provides paths.
 */
public class FileInformation {
    /**
     * Name of the program.
     * TODO: Come up with a better name
     */
    public final String PROGRAM_NAME = "PhotoshopJava";
    /**
     * Name of the checkpoint directory. Each project has this directory.
     */
    private final String CHECKPOINT_IMAGE_DIRECTORY_NAME = "checkpoints";
    /**
     * Name of the server directory. Only one of this exists.
     */
    private final String SERVER_DIRECTORY_NAME = "server";

    /**
     * The program will have its own directory.
     * The name of the program directory is "photoshopJava"
     */
    private Path programPath;
    /**
     * List of all the projects in the program directory.
     * Note: Projects can exist outside the program directory.
     */
    private ArrayList<Path> projectPaths;
    /**
     * Each project will have a unique project name. The project directories are located in the program directory.
     * This project path should be used only if {@link #createProject(String)} returns true or if the user
     * is connect to a server. The information received from the server will be located in this path.
     */
    private Path projectPath;
    /**
     * Keys:
     * "original" The first image the user chose for the project
     * "checkpoint#" The checkpoint images created by the program (checkpoint0, checkpoint1, ect.)
     * "recent" The most recent image created (the one with the most recent edits
     */
    private HashMap<String, Path> images = new HashMap<>();

    /**
     * Constructs an instance of this class. It will create the program directory if it doesn't already exist.
     */
    public FileInformation() {
        // Create the program directory
        createProgramDirectory();
        // Get all the existing projects created in the program directory
        projectPaths = getProjectsPath();
        // Creates any other required directories/files for the program to run
        createServerDirectory();
    }

    /**
     * Creates a path pointing to the directory of the program and creates the directory based on the path if one does
     * not exists.
     */
    private void createProgramDirectory() {
        // All files are created based on the home directory. No system wide files are created.
        programPath = Path.of(System.getProperty("user.home"));

        // Depending on the operating system, the directories will be placed in different places
        String operatingSystem = System.getProperty("os.name");
        if (operatingSystem.startsWith("Mac")) {
            programPath = programPath.resolve("Library").resolve(PROGRAM_NAME);
        } else if (operatingSystem.startsWith("Windows")) {
            programPath = programPath.resolve("AppData").resolve(PROGRAM_NAME);
        } else if (operatingSystem.startsWith("Linux")) {
            programPath = programPath.resolve(PROGRAM_NAME);
        }

        try {
            // Create the program directory if it doesn't exists
            if (!(Files.isDirectory(programPath))) {
                Files.createDirectory(programPath);
            }
        } catch (IOException e) {
            System.out.println("Failed to create program directory");
            System.exit(1); // Cannot continue
        }
    }

    /**
     * Looks through the program directory and gets the path of all the projects located in the directory.
     */
    private ArrayList<Path> getProjectsPath() {
        ArrayList<Path> paths = new ArrayList<>();
        try {
            Files.walk(programPath).
                    filter(p -> Files.isDirectory(p) && !p.equals(programPath)). // walk gives root directory too
                    forEach(System.out::println);
        } catch (IOException e) {
            System.out.println("Error in getting getting paths of existing projects");
            System.exit(1);
        }
        return paths;
    }

    /**
     * Attempts to create a project given the name in the program directory. The name not be blank and no other
     * project can share the same name as it.
     *
     * @param name Name of the project
     * @return True if the project was successfully created, false otherwise.
     */
    public boolean createProject(String name) {
        projectPath = programPath.resolve(name);

        // Make sure the name is valid and unique
        if (name.isBlank() || projectPaths.contains(projectPath))
            return false;

        try {
            // Create the directory
            Files.createDirectory(projectPath);

            // Add all necessary files/directories in the project
            Files.createDirectory(projectPath.resolve(CHECKPOINT_IMAGE_DIRECTORY_NAME));
        } catch (IOException e) {
            System.out.println("Cannot create project");
            e.printStackTrace();
            System.exit(1);
        }

        return true;
    }

    /**
     * Opens the existing project and read its information.
     *
     * @param pathToProject Path to the existing project
     */
    public void openExistingProject(Path pathToProject) {
        // Set variables
        projectPath = programPath;

        try {
            // Get all the checkpoint images
            Files.walk(projectPath.resolve(CHECKPOINT_IMAGE_DIRECTORY_NAME)).
                    filter(p -> !p.equals(projectPath)).
                    forEach(p -> {
                        String fileName = p.getFileName().toString();
                        // Removes file extension (.png) to make it a key for the HashMap
                        // The files are named as original.png, c# (ex. c0 for first checkpoint image), and recent.png
                        images.put(fileName.substring(0, fileName.indexOf(".")), p);
                    });

        } catch (IOException e) {
            System.out.println("Bad project, missing information");
            System.exit(1);
        }
    }

    /**
     * Creates the server directory. This directory is used when the user is connected to another computer. All
     * the files the server sends will be located in this directory. It is cleared when the user connects to a
     * server (even if it is the same server).
     */
    private void createServerDirectory() {
        // Delete the old connection data
        Path serverPath = programPath.resolve(SERVER_DIRECTORY_NAME);
        if (projectPaths.contains(serverPath)) {
            deleteContentsOfDirectory(serverPath);
        }

        // Remake files/directories
        createProjectFiles(serverPath);
    }

    /**
     * Get the original image of the project
     *
     * @return Null if none exists, the file if one does
     */
    public File getOriginalImage() {
        if (images.keySet().contains("original")) {
            return images.get("original").toFile();
        }
        // None found
        return null;
    }

    /**
     * Clones the given file and uses the cloned one as the original image. This assumes that no original image exists.
     *
     * @param file File to clone and use the clone as the original image of the project
     * @return True if the image is valid (a .png file) and was successfully cloned and set ast he original image,
     * false other wise.
     */
    public boolean setOriginalImage(File file) {
        Path path = Paths.get(file.toURI());

        if(path.getFileName().toString().endsWith(".png")){
            try {
                // Clone it to the checkpoint directory to prevent editing directly on true original file
                Path pathToOriginal = projectPath.resolve(CHECKPOINT_IMAGE_DIRECTORY_NAME).resolve("original.png");
                Files.copy(path, pathToOriginal);
            }catch(IOException e){
                // Do nothing, this will not occur unless the user decides to delete the project halfway through
            }

            return true;
        }
        return false; // Not a valid picture
    }

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

    /**
     * Creates all the necessary files needed in the project directory.
     */
    private void createProjectFiles(Path projectRoot){
        try{
            // Create CheckPointImage directory if one doesn't exist
            Path checkpointImageDirectory = projectRoot.resolve("CheckPointImages");
            if (!(Files.exists(checkpointImageDirectory) && Files.isDirectory(checkpointImageDirectory))) {
                Files.createDirectory(checkpointImageDirectory);
            }
        }catch(IOException e){
            // Do nothing
        }
    }
}
