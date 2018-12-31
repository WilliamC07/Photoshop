package project;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import model.XMLHandle.EditsDoneXML;
import model.imageManipulation.edits.Edit;
import network.Client;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
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

    private final String EDITS_DONE_FILE_NAME = "edits.xml";
    /**
     * Name of the server directory. Only one of this exists.
     */
    private final String SERVER_DIRECTORY_NAME = "server";

    /**
     * The program will have its own directory.
     * The name of the program directory is "PhotoshopJava"
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
     * "#" The checkpoint images created by the program (checkpoint0, checkpoint1, ect.)
     * "recent" The most recent image created (the one with the most recent edits
     */
    private HashMap<String, Path> images = new HashMap<>();

    private EditsDoneXML editsDoneXML;

    /**
     * Constructs an instance of this class. It will create the program directory if it doesn't already exist.
     */
    FileInformation() {
        // Create the program directory
        createProgramDirectory();
        // Get all the existing projects created in the program directory
        projectPaths = getProjectsPath();
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
                    forEach(paths::add);
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
    boolean createProject(String name) {
        projectPath = programPath.resolve(name);

        // Make sure the name is valid and unique
        if (name.isBlank() || projectPaths.contains(projectPath))
            return false;

        try {
            // Create the directory
            Files.createDirectory(projectPath);

            // Add all necessary files/directories in the project
            Files.createDirectory(projectPath.resolve(CHECKPOINT_IMAGE_DIRECTORY_NAME));
            editsDoneXML = new EditsDoneXML(projectPath.resolve(EDITS_DONE_FILE_NAME));
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
    void openExistingProject(Path pathToProject) {
        // Set variables
        projectPath = pathToProject;

        try {
            // Get edits done on the images
            editsDoneXML = new EditsDoneXML(projectPath.resolve(EDITS_DONE_FILE_NAME));

            // Get all the checkpoint images
            Files.walk(projectPath.resolve(CHECKPOINT_IMAGE_DIRECTORY_NAME)).
                    filter(p -> !Files.isDirectory(p)).
                    forEach(p -> {
                        String fileName = p.getFileName().toString();
                        // Removes file extension (.png) to make it a key for the HashMap
                        // The files are named as original.png, c# (ex. 0 for first checkpoint image), and recent.png
                        images.put(fileName.substring(0, fileName.indexOf(".")), p);
                    });

        } catch (IOException e) {
            System.out.println("Bad project, missing information");
            System.exit(1);
        }
    }

    Edit[] getEditsDone(){
        return editsDoneXML.getData();
    }

    /**
     * Creates the server directory. This directory is used when the user is connected to another computer. All
     * the files the server sends will be located in this directory. It is cleared when the user connects to a
     * server (even if it is the same server). If this is called, it is assumed that the user is connected to the
     * server.
     */
    void serverConnectionProject() {
        // Delete the old connection data
        projectPath = programPath.resolve(SERVER_DIRECTORY_NAME);

        if (projectPaths.contains(projectPath)) {
            deleteContentsOfDirectory(projectPath);
        }else{
            // Make the directory since it doesn't exist
            try{
                Files.createDirectory(projectPath);
            }catch(IOException e){
                System.out.println("error in server connection project creation");
                e.printStackTrace();
            }
        }

        // Remake files/directories
        createProjectFiles(projectPath);
    }

    /**
     * Get the original image of the project
     *
     * @return Null if none exists, the file if one does
     */
    File getOriginalImage() {
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
    boolean setOriginalImage(File file) {
        Path path = Paths.get(file.toURI());

        if(path.getFileName().toString().endsWith(".png")){
            try {
                // Clone it to the checkpoint directory to prevent editing directly on true original file
                Path pathToOriginal = projectPath.resolve(CHECKPOINT_IMAGE_DIRECTORY_NAME).resolve("original.png");
                Files.copy(path, pathToOriginal);
            }catch(IOException e){
                // Do nothing, this will not occur unless the user decides to delete the project halfway through
            }

            images.put("original", path);

            return true;
        }
        return false; // Not a valid picture
    }

    Image setOriginalImage(byte[] fileBytes){
        try{
            Path originalImagePath = projectPath.resolve(CHECKPOINT_IMAGE_DIRECTORY_NAME).resolve("original.png");
            FileOutputStream outputStream = new FileOutputStream(originalImagePath.toFile());
            outputStream.write(fileBytes);
            outputStream.close();

            // Update the program
            images.put("original", originalImagePath);
            System.out.println("made original image");
            return new Image(Files.newInputStream(originalImagePath));
        }catch(IOException e){
            e.printStackTrace();
            System.out.println("error in making original data from received data");
        }

        return null; // error in making the file with the received data
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
            Path checkpointImageDirectory = projectRoot.resolve(CHECKPOINT_IMAGE_DIRECTORY_NAME);
            if (!(Files.exists(checkpointImageDirectory) && Files.isDirectory(checkpointImageDirectory))) {
                Files.createDirectory(checkpointImageDirectory);
            }
        }catch(IOException e){
            // Do nothing
        }
    }

    void save(Project project){
        // If no project has been opened, just return because there is nothing to save
        if(editsDoneXML == null)
            return;

        editsDoneXML.writeData(project.getImageBuilder().getEdits());
        editsDoneXML.save();

        // Save to most recent image
        setRecentImage(project.getImageBuilder().getWritableImage());
    }

    /**
     * Makes a checkpoint given the image and checkpoint index.
     * @param checkpointNumber Index of the checkpoint (zero based)
     * @param writableImage Image to be saved
     */
    void setCheckpointImage(int checkpointNumber, WritableImage writableImage){
        // Need to remove all checkpoint images from checkpointNumber onwards (inclusive)
        // since all future images will no longer match up
        removeCheckpointImages(checkpointNumber);

        // Regenerate the checkpoint image
        BufferedImage bufferedImage = SwingFXUtils.fromFXImage(writableImage, null);
        String format = "png"; // only supporting png
        Path location = projectPath.resolve(CHECKPOINT_IMAGE_DIRECTORY_NAME).resolve(checkpointNumber+".png");
        try{
            ImageIO.write(bufferedImage, format, location.toFile());
        }catch(IOException e){
            e.printStackTrace();
            // do nothing
        }

        // add back to hash map
        images.put(String.valueOf(checkpointNumber), location);
    }

    /**
     * Saves the given byte representation of the image to disk as a checkpoint image. This will remove all checkpoint
     * images that shares the same index onwards (inclusive).
     * @param checkpointNumber Index of the checkpoint.
     * @param file File to be saved
     */
    void setCheckpointImage(int checkpointNumber, byte[] file){
        removeCheckpointImages(checkpointNumber);
        // Create the file
        Path location = projectPath.resolve(CHECKPOINT_IMAGE_DIRECTORY_NAME).resolve(checkpointNumber+".png");
        try(FileOutputStream makeFile = new FileOutputStream(location.toFile())){
            makeFile.write(file);
        }catch(IOException e){
            e.printStackTrace();
            // Do nothing
        }

        // Keep track of the added checkpoint
        images.put(String.valueOf(checkpointNumber), location);
    }

    /**
     * Sets the edits done and reads all the edits onto the program.
     * @param file
     */
    Edit[] setEditsDone(byte[] file){
        // Make the file
        Path location = projectPath.resolve(EDITS_DONE_FILE_NAME);
        try(FileOutputStream write = new FileOutputStream(location.toFile())){
            write.write(file);
        }catch(IOException e){
            e.printStackTrace();
            // Do nothing
        }

        // Read the file and uses it to recreate the image
        editsDoneXML = new EditsDoneXML(location);
        System.out.println("made edits done file");

        // Returns all the edits done
        return editsDoneXML.getData();
    }

    /**
     * Get the xml file that contains all the edits done on the image.
     * @return File containing all the edits done.
     */
    File getEditsDoneFile(Project project){
        // Need to write all the information to disk before continuing.
        save(project);
        return projectPath.resolve(EDITS_DONE_FILE_NAME).toFile();
    }

    /**
     * Gets the checkpoint image given the index.
     * @param index Index of the checkpoint image.
     * @return File of the checkpoint image, null if none are found
     */
    File getCheckpointImage(int index){
        Path pathToCheckpointImage = images.get(String.valueOf(index));
        return pathToCheckpointImage == null ? null : pathToCheckpointImage.toFile();
    }

    /**
     * Removes all the checkpoint images from the starting index onwards (inclusive)
     * @param start Checkpoint image number to start removal from
     */
    private void removeCheckpointImages(int start){
        for(String key : images.keySet()){
            if(!key.equals("original") && !key.equals("recent") && Integer.valueOf(key) >= start){
                deleteFile(images.get(key));
            }
        }
    }

    void setRecentImage(WritableImage writableImage){
        if(images.containsKey("recent")){
            deleteFile(images.get("recent"));
        }
        Path location = projectPath.resolve(CHECKPOINT_IMAGE_DIRECTORY_NAME).resolve("recent.png");
        BufferedImage bufferedImage = SwingFXUtils.fromFXImage(writableImage, null);
        try{
            ImageIO.write(bufferedImage, "png", location.toFile());
        }catch(IOException e){
            e.printStackTrace();
            // do nothing
        }
    }

    File getRecentImage(){
        if(images.containsKey("recent")){
            return images.get("recent").toFile();
        }
        return null;
    }

    /**
     * Deletes the given path.
     * @param path Path to delete off the file system.
     */
    private void deleteFile(Path path){
        try{
            Files.delete(path);
        }catch(IOException e){
            e.printStackTrace();
            // Do nothing
        }
    }
}
