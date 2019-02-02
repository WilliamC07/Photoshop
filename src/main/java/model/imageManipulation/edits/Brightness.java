package model.imageManipulation.edits;

import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;

public class Brightness extends Edit{
    private final double factor;
    private final int width;
    private final int height;

    public Brightness(double factor, int width, int height) {
        this.factor = factor;
        this.width = width;
        this.height = height;
    }

    @Override
    void change(PixelWriter pixelWriter, PixelReader pixelReader) {

    }

    @Override
    public String getStringRepresentation() {
        return null;
    }
}
