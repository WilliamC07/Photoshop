package model.imageManipulation.edits;

public interface Shape {
    /**
     * Determines if the point is located in the shape. Also returns true if the point is on the border of the shape.
     * @param point Point to check if it is located within the shape.
     * @return True if the point is located in the image (can be on the border), false otherwise.
     */
    boolean pointExists(Point point);
    /**
     * Returns boolean value for if the point is located on the border of the object
     * @param point Point to check if it is located on the border.
     * @return True if the point is located on the border, false otherwise
     */
    boolean onBorder(Point point);
    /**
     * Gives the important information of a shape. For example, a circle will give the center and 4 quadrant points and
     * a square will give 4 points for each vertex.
     * @return Array of all the points in the shape
     */
    Point[] getPoints();

}
