package views;

import javafx.scene.control.SplitPane;

/**
 * This is the view that is always shown to the user. The components are:
 * 1. TODO Editing information
 * 2. TODO Editing board
 * 3. TODO Chat
 * 4. TODO Switch to different pages
 *
 * Should only be initialized once in ApplicationStart. We are not having multiple windows, instead we have pages.
 * This is a SplitPane to allow the photo editor to choose how big he/she wants the sections to be.
 *
 * Use a SplitPane to allow the user to switch
 *
 *
 * @see ApplicationStart
 * @author William Cao
 * @since 1.0
 */
final class MainDisplay extends SplitPane{
  WHbox hbox;
}
