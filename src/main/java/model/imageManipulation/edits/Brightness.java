package model.imageManipulation.edits;

import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;

public class Brightness extends Edit{
    private final double factor;

    public Brightness(double factor) {
        this.factor = factor;
    }

    @Override
    void change(PixelWriter pixelWriter, PixelReader pixelReader) {

    }

    @Override
    public String getStringRepresentation() {
        return null;
    }
}
