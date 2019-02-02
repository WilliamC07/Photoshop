package model.imageManipulation.edits;

import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.paint.Color;

public class Brightness extends Edit{
    private final boolean makeBrighter;
    private int width;
    private int height;

    public Brightness(boolean makeBrighter, int width, int height) {
        this.makeBrighter = makeBrighter;
        this.width = width;
        this.height = height;
    }

    @Override
    void change(PixelWriter pixelWriter, PixelReader pixelReader) {
      for (int w = 0; w < width; w++) {
        for (int h = 0; h < height; h ++) {
          Color toChange = pixelReader.getColor(w,h);
          if (makeBrighter) {
            pixelWriter.setColor(w,h,toChange.brighter());
          }else{
            pixelWriter.setColor(w,h,toChange.darker());
          }
        }
      }
    }

    @Override
    public String getStringRepresentation() {
        return String.format("BRIGHTNESS %s %d %d", Boolean.valueOf(makeBrighter).toString(), width, height);
    }
}
