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
     * @param color Color to fill the rectangle
     */
    BlurredRectangle(Point start, int width, int height) {
        this.x = start.getX();
        this.y = start.getY();
        this.width = width;
        this.height = height;
        averages = new Color[width][height];
    }


    Color getAverageColor(int x, int y){
      PixelReader pixelReader;
      Color temp;
      Color average;
      double red = 0;
      double blue = 0;
      double green = 0;

      for(int i = y+1; i >= y-1; i--){
        for(int u = x-1; u <= x+1; u++){
          temp = pixelReader.getColor(i,u);
          red+= temp.getRed();
          green+= temp.getGreen();
          blue+= temp.getBlue();
        }
      }
      average = Color.color(red/9,green/9,blue/9,1.0);
      return average;
    }

    void fillArray(){
      for(int h = 0; h < height; h++){
          for(int w = 0; w < width; w++){
              averages[w][h] = getAverageColor(x+w,y+h);
          }
      }
    }

    @Override
    void change(PixelWriter pixelWriter) {
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
