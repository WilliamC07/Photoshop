package model.imageManipulation.edits;

import javafx.scene.paint.Color;

class ColorUtility {
    static String toHex(Color color){
        // must multiply by 255 because getCOLOR() is from 0 to 1 inclusive (percent of 255)
        return String.format("#%02X%02X%02X", (int) (color.getRed() * 255),
                (int) (color.getGreen() * 255), (int) (color.getBlue() * 255));
    }
}
