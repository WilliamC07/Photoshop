package model.imageManipulation.edits;

/**
 * Point to represent the coordinate of a pixel on an image.
 */
public class Point {
    private final int x;
    private final int y;

    /**
     * Constructs a point given the x and y coordinates
     * @param x X coordinate of the image
     * @param y Y coordinate of the image
     */
    public Point(int x, int y){
        this.x = x;
        this.y = y;
    }

    /**
     * Constructs a point given the raw x and y coordinates (coordinates of pixel clicked in an ImageView) and
     * scale factor (how zoomed out or zoomed in the ImageView is)
     * @param x raw x coordinate of the mouse
     * @param y raw y coordinate of the mouse
     * @param scaleFactor
     */
    public Point(double x, double y, double scaleFactor){
        this.x = (int) (x / scaleFactor);
        this.y = (int) (y / scaleFactor);
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    @Override
    public String toString() {
        return String.format("(%d, %d)", x, y);
    }

    /**
     * Determines if two points are equal. That is if they share the same x and y values.
     * @param p Point to check against.
     * @return True if the points share the same x and y values, false otherwise
     */
    public boolean equals(Point p){
        return p.getY() == getY() && p.getX() == getX();
    }
}
