package model.imageManipulation.edits;

import javafx.scene.image.PixelWriter;
import javafx.scene.image.PixelReader;
import javafx.scene.paint.Color;


class BlurredRectangle extends Edit{
    private final int x, y, width, height;
    private int red, green, blue;

    /**
     * Constructs a rectangle (Vertical one only)
     * @param start Top left point of the rectangle
     * @param width Width of the rectangle
     * @param height Height of the rectangle
     * @param color Color to fill the rectangle
     */
    BlurredRectangle(Point start, int width, int height) {
        this.x = start.getX();
        this.y = start.getY();
        this.width = width;
        this.height = height;
    }

    Color[][] averageColors = new Color[width][height];

    void fillArray(PixelReader pixelReader){
      for(int h = 0; h < height; h++){
          for(int w = 0; w < width; w++){
            averageColors[w][h] = pixelReader.getColor(x + w, y + h);
          }
      }
    }

    Color getAverageColor(int x, int y){

    }

    @Override
    void change(PixelWriter pixelWriter) {
        for(int h = 0; h < height; h++){
            for(int w = 0; w < width; w++){
                pixelWriter.setColor(x + w, y + h, color);
            }
        }
    }

    @Override
    public String getStringRepresentation() {
        return String.format("RECTANGLE %d %d %d %d %s %f",
                x, y, width, height, ColorUtility.toHex(color), color.getOpacity());
    }
}
