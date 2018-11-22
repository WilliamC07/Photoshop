package project;

import java.io.File;

/**
 * Relevant information about the project.
 * Before using anything in this class, you must initialize the project ({@link #initializeProject(String)}
 */
public class Project {
    /**
     * Singleton instance
     */
    public static Project project;

    /**
     * Singleton accessor
     * @return singleton instance of the class
     */
    public static Project getInstance(){
        if(project == null){
            project = new Project();
        }
        return project;
    }
}
