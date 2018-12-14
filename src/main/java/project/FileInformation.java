package project;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Deals with the file system on the computer. Creates all needed files and provides paths.
 */
public class FileInformation {
    public final String PROGRAM_NAME = "PhotoshopJava";
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
     */
    private Path projectPath;
    /**
     * Keys:
     * "original" The first image the user chose for the project
     * "checkpoint#" The checkpoint images created by the program (checkpoint0, checkpoint1, ect.)
     * "recent" The most recent image created (the one with the most recent edits
     */
    private HashMap<String, Path> images;

    /**
     * Constructs an instance of this class. It will create the program directory if it doesn't already exist.
     */
    public FileInformation(){
        // Creates necessary directories and paths pointing to those directories
        createProgramDirectory();
        // Get all the existing projects created in the program directory
        projectPaths = getProjectsPath();
    }

    /**
     * Creates a path pointing to the directory of the program and creates the directory based on the path if one does
     * not exists.
     */
    private void createProgramDirectory(){
        // All files are created based on the home directory. No system wide files are created.
        programPath = Path.of(System.getProperty("user.home"));

        // Depending on the operating system, the directories will be placed in different places
        String operatingSystem = System.getProperty("os.name");
        if(operatingSystem.startsWith("Mac")){
            programPath = programPath.resolve("Library").resolve(PROGRAM_NAME);
        }else if(operatingSystem.startsWith("Windows")){
            programPath = programPath.resolve("AppData").resolve(PROGRAM_NAME);
        }else if(operatingSystem.startsWith("Linux")){
            programPath = programPath.resolve(PROGRAM_NAME);
        }

        try{
            // Create the program directory if it doesn't exists
            if(!(Files.isDirectory(programPath))){
                Files.createDirectory(programPath);
            }
        }catch(IOException e){
            System.out.println("Failed to create program directory");
            System.exit(1); // Cannot continue
        }
    }

    /**
     * Looks through the program directory and gets the path of all the projects located in the directory.
     */
    private ArrayList<Path> getProjectsPath(){
        ArrayList<Path> paths = new ArrayList<>();
        try{
            Files.walk(programPath).
                    filter(p -> Files.isDirectory(p) && !p.equals(programPath)). // walk gives root directory too
                    forEach(System.out::println);
        }catch(IOException e){
            System.out.println("Error in getting getting paths of existing projects");
            System.exit(1);
        }
        return paths;
    }


}
