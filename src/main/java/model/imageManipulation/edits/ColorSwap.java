package model.imageManipulation.edits;

import javafx.scene.image.PixelWriter;
import javafx.scene.paint.Color;

class ColorSwap extends Edit{
    private final Color iColor;
    private final Color fColor;

    /**
     * Swaps a color with another in the image
     * @param iColor color that is being swapped
     * @param fColor color chosen to swap to
     */
    ColorSwap(Color oldColor, Color newColor) {
        this.iColor = oldColor;
        this.fColor = newColor;
    }

    @Override
    void change(PixelWriter pixelWriter, PixelReader pixelReader) {
        for(int x = 0; h < height; h++){
            for(int y = 0; w < width; w++){
              if (pixelReader.getColor(x, y).equals(iColor)) {
                pixelWriter.setColor(x, y, fColor);
              }
            }
        }
      }

    @Override
    public String getStringRepresentation() {
        return String.format("COLOR %s %s",
                ColorUtility.toHex(iColor), ColorUtility.toHex(fColor));
    }
}
