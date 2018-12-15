package views;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.information.ScreenDimensions;
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
     * This is the head of the program.
     */
    private Project project;

	/**
	 * Initialization before the screen is shown. This method is called right after {@link #main(String[])}.
	 */
	@Override public void init(){
	    project = new Project();
    }

	/**
	 * Entry point of the application. Gets called after {@link #init()}.
	 */
	@Override public void start(Stage primaryStage){
		// First view is the welcome screen, so use those dimensions
		Scene scene = new Scene(new WelcomeScreen(primaryStage, project),
                                ScreenDimensions.welcomeWidth, ScreenDimensions.welcomeHeight);
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	/**
	 * Called when Platform.exit() is run.
	 */
	@Override public void stop(){}

	/**
	 * This is what starts the application. This should NOT be the starting point of the program. Another class should
	 * call this to start the application.
	 */
	public static void main(String[] args){
		launch(args);
	}
}