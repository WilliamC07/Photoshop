package model.imageManipulation.edits;

import javafx.scene.image.PixelWriter;
import javafx.scene.paint.Color;

class Rectangle extends Edit{
    private final int x, y, width, height;
    private final Color color;

    /**
     * Constructs a rectangle (Vertical one only)
     * @param start Top left point of the rectangle
     * @param width Width of the rectangle
     * @param height Height of the rectangle
     * @param color Color to fill the rectangle
     */
    Rectangle(Point start, int width, int height, Color color) {
        this.x = start.getX();
        this.y = start.getY();
        this.width = width;
        this.height = height;
        this.color = color;
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
    String getStringRepresentation() {
        return String.format("RECTANGLE %d %d %d %d", x, y, width, height);
    }
}
