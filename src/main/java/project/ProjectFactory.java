package project;

import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Path;

/**
 * Used to create an instance of Project. Project is a singleton and this is the only method to create an instance of it
 * so it can then be accessed using getInstance().
 *
 * @see Project
 */
public class ProjectFactory {
    /**
     * Creates a new project using the name given.
     *
     * @param name Name of the project
     * @return Instance of Project
     */
    public static Project createProject(String name) throws FileAlreadyExistsException {
        Project.project = new Project(name);
        return Project.getInstance();
    }

    /**
     * Opens the already existing project.
     *
     * @param projectPath Path to the existing project.
     * @return Instance of Project
     */
    public static Project createProject(Path projectPath) {
        Project.project = new Project(projectPath);
        return Project.getInstance();
    }
}
