package model.information;

import javafx.geometry.Rectangle2D;
import javafx.stage.Screen;

/**
 * Information about the screen width and height.
 *
 * @author William Cao
 * @since 1.0
 */
public class ScreenDimensions {
    /**
     * Width of the screen.
     */
    public static final int width;

    /**
     * Height of the screen
     */
    public static final int height;

    static{
        Rectangle2D primaryScreen = Screen.getPrimary().getBounds();

        width = (int) primaryScreen.getWidth();
        height = (int) primaryScreen.getHeight();
    }
}
