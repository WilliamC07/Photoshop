package viewController;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.information.ScreenDimensions;


/**
 * This class is the real start to the program (it is called by another class (src.main.java.Main)). It should be
 * the starting point of the program. If any data needs to be read, any GUI content needs to be created, or anything 
 * else to begin the program, this is the class to do it in. 
 *
 * @author William Cao
 * @since 1.0
 */
public class ApplicationStart extends Application{

	/**
	 * Initialization before the screen is shown. This method is called right after {@link #main(String[])}.
	 */
	@Override public void init(){}

	/**
	 * Entry point of the application. Gets called after {@link #init()}.
	 */
	@Override public void start(Stage primaryStage){
		Scene scene = new Scene(new MainDisplay(), ScreenDimensions.width, ScreenDimensions.height);
		primaryStage.setScene(scene);
		primaryStage.setFullScreen(true); // This is meant to be a fullscreen application
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