package src.main.java.ViewController;

import javafx.application.Application;
import javafx.stage.Stage;

/**
 * @author William Cao
 * @since 1.0
 */
public class MainView extends Application{

	/**
	 * Initialization before the screen is shown. This method is called right after {@link main()}. 
	 * 
	 */
	@Override public void init(){}

	/**
	 * Entry point of the application. Gets called after {@link #init()}.
	 */
	@Override public void start(Stage primaryStage){}

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