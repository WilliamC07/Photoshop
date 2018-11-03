package src.main.java;

import src.main.java.ViewController.MainView;

/**
 * Starting point of the program. This should only have one method, the main method.
 * 
 * @author William Cao
 * @since 1.0
 */
public class Main{
	/**
	 * MainView cannot be the starting point of the program because it extends Applications.
	 */
	public static void main(String[] args){
		MainView.main(args);
	}
}