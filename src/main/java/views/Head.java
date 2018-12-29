package views;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.information.ScreenDimensions;
import network.Client;
import network.Server;
import project.Project;


/**
 * This class is the real start to the program (it is called by another class (src.main.java.Main)). It should be
 * the starting point of the program. If any data needs to be read, any GUI content needs to be created, or anything 
 * else to begin the program, this is the class to do it in. 
 *
 * @author William Cao
 * @since 1.0
 */
public class Head extends Application{
    /**
     * Information about the project being worked on.
     */
    private final Project project = new Project();
    /**
     * Stage of the program.
     */
    private Stage primaryStage;

	/**
	 * Initialization before the screen is shown. This method is called right after {@link #main(String[])}.
	 */
	@Override
    public void init(){

    }

	/**
	 * Entry point of the application. Gets called after {@link #init()}.
	 */
	@Override
    public void start(Stage primaryStage){
	    this.primaryStage = primaryStage;

		// First view is the welcome screen, so use those dimensions
		Scene scene = new Scene(new WelcomeScreen(project, this),
                                ScreenDimensions.welcomeWidth, ScreenDimensions.welcomeHeight);
		primaryStage.setTitle("Welcome");
		primaryStage.setScene(scene);
		primaryStage.show();
	}

    /**
     * Shows the main screen of the application. This is where the user can see the image being worked on, edit the
     * image, host the server.
     *
     * @see MainDisplay
     */
	public void showMainDisplay(){
		MainDisplay mainDisplay = new MainDisplay(project);
		project.setMainDisplay(mainDisplay);
	    Scene scene = new Scene(mainDisplay);
        primaryStage.setScene(scene);
        primaryStage.setFullScreen(true);
        primaryStage.setTitle("PhotoshopEdit");
    }

	/**
	 * Called when Platform.exit() is run.
	 */
	@Override
    public void stop(){
	    // Closes any connections
	    Server server = project.getServer();
	    if(server != null){
	        server.terminate();
        }

	    // Closes connection to the server if one exists
	    Client client = project.getClient();
	    if(client != null){
	    	//client.
		}

	    // TODO: Saves the most recently worked on image to disk
		project.save();

        System.out.println("System quit successfully");
    }

	/**
	 * This is what starts the application. This should NOT be the starting point of the program. Another class should
	 * call this to start the application.
	 */
	public static void main(String[] args){
		launch(args);
	}
}