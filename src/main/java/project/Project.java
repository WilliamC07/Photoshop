package project;

import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;

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
     * @param name
     * @throws FileAlreadyExistsException
     */
    Project(String name) throws FileAlreadyExistsException{
        this.name = name;
        createProgramDirectory();
        createProjectDirectory();
    }

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

    private void createProgramDirectory(){
        // Make program directory (information about the program (setting, etc...) and the projects)
        // Because the program directory also contains information about program itself, it must still exists even
        // if the project is accessed from elsewhere (downloaded from someone)
        this.programRoot = Path.of(System.getProperty("user.home"));
        // Depending on operating system, the data will be storied in different places
        String osName = System.getProperty("os.name");
        String programName = "photoshopJava";
        if(osName.startsWith("Mac")){
            this.programRoot = programRoot.resolve("Library").resolve(programName);

        }else if(osName.startsWith("Windows")){
            this.programRoot = programRoot.resolve("AppData").resolve(programName);
        }else if(osName.startsWith("Linux")){
            // TODO: finish if condition and project root
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

    private void createProjectDirectory() throws FileAlreadyExistsException{
        try{
            // Make project directory (information about a project; One directory per project)
            this.projectRoot = programRoot.resolve(name);
            Files.createDirectory(projectRoot);
        }catch(FileAlreadyExistsException e){
            throw e;
        }catch(IOException e){
            // Can't continue without a destination to store data
            e.printStackTrace();
            System.exit(1);
        }
    }

    /**
     * Name the project. This can be renamed multiple times.
     */
    public void setProjectName(String name){
        this.name = name;
    }
    /**
     * Get the name of the project
     * @return Name of the project.
     */
    public String getName(){
        return this.name;
    }

}
