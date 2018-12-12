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
    /**
     * Checks if 'this' point is between the start and end points that are located on the same line.
     * Returns True if this is in between start and end, false otherwise.
     * @param start start point
     * @param end end point
    */
    public boolean inBetween(Point start, Point end){
      return ((this.x > start.getX() && this.x < end.getX() && this.y > start.getY() && this.y < end.getY()) || // Checks if the point is within bounds of start and end.
      (this.x < start.getX() && this.x > end.getX() && this.y < start.getY() && this.y > end.getY())) &&        // These makes sure any points can be the start/end points.
      ((this.y - start.getY()) / (end.getY() - start.getY())) == ((this.x - start.getX()) / (end.getX() - start.getX())) // Checks if point lies on the line start-end.
    }
}
