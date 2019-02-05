package model.imageManipulation.edits;

import javafx.scene.image.PixelWriter;
import javafx.scene.image.PixelReader;
import javafx.scene.paint.Color;


class BlurredRectangle extends Edit{
    private final int x, y, width, height;
    private int red, green, blue;
    private Color[][] averages;

    /**
     * Constructs a rectangle (Vertical one only)
     * @param start Top left point of the rectangle
     * @param width Width of the rectangle
     * @param height Height of the rectangle
     */
    BlurredRectangle(Point start, int width, int height) {
        this.x = start.getX();
        this.y = start.getY();
        this.width = width;
        this.height = height;
        averages = new Color[width][height];
    }


    Color getAverageColor(int x, int y, PixelReader pixelReader){

      Color temp;
      Color average;
      double red = 0;
      double blue = 0;
      double green = 0;

      for(int i = y+3; i >= y-3; i--){
        for(int u = x-3; u <= x+3; u++){
          temp = pixelReader.getColor(u,i);
          red+= temp.getRed();
          green+= temp.getGreen();
          blue+= temp.getBlue();
        }
      }
      average = Color.color(red/49,green/49,blue/49,1.0);
      return average;
    }

    void fillArray(PixelReader pixelReader){
      for(int h = 0; h < height; h++){
          for(int w = 0; w < width; w++){
              averages[w][h] = getAverageColor(x+w,y+h, pixelReader);
          }
      }
    }

    void clearArray(){
      for(int q = 0; q < height; q++){
        for(int t = 0; t < width; t++){
          averages[t][q] = null;
        }
      }
    }

    @Override
    void change(PixelWriter pixelWriter, PixelReader pixelReader) {
      clearArray();
      fillArray(pixelReader);
        for(int h = 0; h < height; h++){
            for(int w = 0; w < width; w++){
                pixelWriter.setColor(x + w, y + h, averages[w][h]);
            }
        }
    }

    @Override
    public String getStringRepresentation() {
        return String.format("BLURREDRECTANGLE %d %d %d %d",
                x, y, width, height);
    }
}
