package project;

import java.io.IOException;
import java.nio.file.*;

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

    private String name;
    private Path projectRoot;
    private Path programRoot;

    /**
     * Constructor to create an instance of this singleton.
     * @param name Name of the project
     * @throws FileAlreadyExistsException If the directory is created in the same directory as the project with the
     *                                    same name, it will throw this.
     */
    Project(String name) throws FileAlreadyExistsException{
        this.name = name;
        createProgramDirectory();
        createProjectDirectory();
    }

    /**
     * Constructor to create an instance of this singleton
     * @param projectRoot Path to the existing directory of the project. The directory can be empty and it will
     *                    create the files needed. If any files are empty, it will be placed in a sub directory
     *                    of old files and generate new ones.
     */
    Project(Path projectRoot) {
        this.projectRoot = projectRoot;
        createProgramDirectory();
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
            this.programRoot = projectRoot.resolve(programName);
        }

        try{
            if(!(Files.exists(programRoot) && Files.isDirectory(programRoot))) {
                Files.createDirectory(programRoot);
                System.out.println(programRoot);
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
        try{
            // Make project directory (information about a project; One directory per project)
            this.projectRoot = programRoot.resolve(name);
            Files.createDirectory(projectRoot);

            // Create CheckPointImage directory if one doesn't exist
            Path checkpointImageDirectory = projectRoot.resolve("CheckPointImages");
            if(!(Files.exists(checkpointImageDirectory) && Files.isDirectory(checkpointImageDirectory))){
                Files.createDirectory(checkpointImageDirectory);
            }
        }catch(FileAlreadyExistsException e){
            // Will only be thrown if the project directory exists.
            // If the CheckpointImage directory exists, this will not be thrown
            throw e;
        }catch(IOException e){
            // Can't continue without a destination to store data
            e.printStackTrace();
            System.exit(1);
        }
    }

    /**
     * Name the project. This can be renamed multiple times.
     * You must check if the name won't conflict before passing it into this function.
     */
    public void setProjectName(String name){
        this.name = name;
        try{
            Files.move(projectRoot, Files.createFile(projectRoot.getParent().resolve(name)),
                       StandardCopyOption.REPLACE_EXISTING);
        }catch(IOException e){
            e.printStackTrace();
        }
    }
    /**
     * Get the name of the project
     * @return Name of the project.
     */
    public String getName(){
        return this.name;
    }

}
