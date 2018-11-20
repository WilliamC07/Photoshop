package project;

/**
 * Relevant information about the project.
 * Before using anything in this class, you must initialize the project ({@link #initializeProject(String)}
 */
public class Project {
    public static String projectName;
    public static ProjectFolder projectFolder;

    public static void initializeProject(String projectName){
        Project.projectName = projectName;
        Project.projectFolder = new ProjectFolder(projectName);
    }
}
