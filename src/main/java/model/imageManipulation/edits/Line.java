package model.imageManipulation.edits;

import javafx.scene.image.PixelWriter;
import javafx.scene.paint.Color;

class Line extends Edit{
    private final Point point1;
    private final Point point2;
    private final Color color;
    private final int width;

    public Line(Point point1, Point point2, Color color, int width) {
        // get the left point as point1 to make it easier to code
        if(point1.getX() > point2.getX()){
            this.point1 = point2;
            this.point2 = point1;
        }else{
            this.point1 = point1;
            this.point2 = point2;
        }
        this.color = color;
        this.width = width;
    }

    @Override
    void change(PixelWriter pixelWriter) {
        int x1 = point1.getX();
        int y1 = point1.getY();
        int x2 = point2.getX();
        int y2 = point2.getY();

        // vertical line will cause division by 0
        if(y1 == y2){
            drawVertical(pixelWriter, x1, y1, y2);
            return;
        }

        // find equation of the line
        double slope = ((double) y2 - y1) / (x2 - x1);
        // find b value in y = mx + b
        double b = y1 - slope * x1;

        int xCurrent = x1;
        boolean xEnd = false;
        boolean yEnd = false;

        while(!(xEnd && yEnd)){
            // get y matching point (bottom of it due to multiple y values)
            int yMatch = equationToDraw(slope, b, xCurrent) - width / 2;
            for(int i = 0; i < width; i++){
                int yDraw = yMatch + i;
                // end drawing
                if(yDraw == y2){
                    yEnd = true;
                }
                pixelWriter.setColor(xCurrent, yDraw, color);
            }

            // end drawing
            if(xCurrent == x2){
                xEnd = true;
            }
            xCurrent++;
        }
    }

    /**
     * Find corresponding y value given the x
     * @param slope m in y = mx + b
     * @param b b in y = mx + b
     * @return y cord given the x value
     */
    private int equationToDraw(double slope, double b, int x){
        return (int) (slope * x + b);
    }


    /**
     * Slope of the line will be 0, so we need to deal with it
     * @param pixelWriter
     * @param x
     * @param y1
     * @param y2
     */
    private void drawVertical(PixelWriter pixelWriter, int x, int y1, int y2){
        // need x1 to be larger so that the for loop will work
        if(y1 < y2){
            drawVertical(pixelWriter, x, y2, y1);
            return;
        }

        // add one to be inclusive of the point
        for(int i = y2; i < y1 + 1; i++){
            pixelWriter.setColor(i, x, color);
        }
    }

    @Override
    public String getStringRepresentation() {
        return String.format("LINE %d %d %d %d %d %s",
                point1.getX(), point1.getY(), point2.getX(), point2.getY(), width, ColorUtility.toHex(color));
    }
}
