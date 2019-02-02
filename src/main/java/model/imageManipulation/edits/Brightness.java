package model.imageManipulation.edits;

import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.paint.Color;

public class Brightness extends Edit{
    private final boolean makeBrighter;
    private final int width;
    private final int height;

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
          if (makeBrighter == true) {
            pixelWriter.setColor(w,h,toChange.brighter());
          }
          if (makeBrighter == false) {
            pixelWriter.setColor(w,h,toChange.darker());
          }
        }
      }
    }

    @Override
    public String getStringRepresentation() {
      String makeChange = "";
      if (makeBrighter == true) {
        makeChange = "true";
      }
      if (makeBrighter == false) {
        makeChange = "false";
      }
        return String.format("BRIGHTNESS", makeChange, width, height);
    }
}
