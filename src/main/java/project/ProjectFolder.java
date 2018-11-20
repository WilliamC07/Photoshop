package project;

import java.io.File;

/**
 * Access all the files in the user's project.
 */
class ProjectFolder {
    /**
     * The parent directory of the project.
     */
    private File headDirectory;
    private static ProjectFolder instance;

    ProjectFolder(String projectName){
        createHeadDirectory(projectName);
    }

    /**
     * If the project already exists, open that one instead of creating a new project.
     * @param headDirectory
     */
    ProjectFolder(File headDirectory){
        this.headDirectory = headDirectory;
    }

    private void createHeadDirectory(String projectName){
        // Get reference to the directory, may or may not exist
        String homeDirectory = System.getProperty("user.home");
        //TODO: Test on linux and Windows
        // Create program folder if it doesn't exist
        File programDirectory = new File(homeDirectory + File.separator + "PhotoshopJava");
        System.out.println(programDirectory.mkdir());

        // Creates the project level folder
        this.headDirectory = new File(programDirectory.toString() + File.separator + projectName);
        System.out.println(headDirectory);

        // The file can be turned into a directory so ignore the returned value
        System.out.println(headDirectory.mkdir());
    }
}
