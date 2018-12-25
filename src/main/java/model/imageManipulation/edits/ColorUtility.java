package model.imageManipulation.edits;

import javafx.scene.paint.Color;

class ColorUtility {
    static String toHex(Color color){
        return String.format("#%02X%02X%02X", (int) color.getRed(), (int) color.getGreen(), (int) color.getBlue());
    }
}
