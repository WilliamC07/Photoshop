package views;

import javafx.scene.control.Button;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

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
final class MainDisplay extends SplitPane {
    MainDisplay(){
        // Screen is divided into 3 different parts
        getItems().addAll(new Button("App"), new Button("Bpp"), new Button("ccc"));
        setDividerPositions(0.2f, 0.6f);
    }

    /**
     * Goes on the left of the screen.
     * This provides all the tools to use to edit the image.
     */
    private class Tools extends VBox{

    }

    /**
     * Goes on the center of the screen
     * The image being edited goes here. If no image is selected, this should prompt the user to choose a file
     * or use a blank canvas.
     */
    private class EditPane extends Pane{

    }

    /**
     * Goes on the right of the screen.
     * Allows the user to host a server and view the collaborators and information on how to join (give ip address and
     * port). Below this, the user can see the history of all the edits done on the image.
     */
    private class RightSide extends VBox{

    }
}
