import views.Head;

/**
 * True starting point of the program. This should only have one method, the main method. This class cannot extend 
 * javafx.application.Application because JavaFX is run with dependencies. Its only purpose is to call 
 * src.main.java.views.Head.
 * 
 * Related link: http://mail.openjdk.java.net/pipermail/openjfx-dev/2018-June/021977.html
 * 
 * @see Head
 * @author William Cao
 * @since 1.0
 */
public class Main{
	/**
	 * MainView cannot be the starting point of the program because it extends Applications.
	 */
	public static void main(String[] args){
		Head.main(args);
	}
}