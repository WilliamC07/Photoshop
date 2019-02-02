package model.imageManipulation.edits;

import javafx.scene.control.ColorPicker;
import javafx.scene.paint.Color;
import javafx.scene.image.PixelReader;

public class BlurredRectangleBuilder extends AbstractEditBuilder implements RequirePoints {
    /**
     * Points given to construct this rectangle.
     * Only two points are required to make a rectangle.
     */
    private final Point[] points = new Point[2];
    private ImageBuilder imageBuilder;

    // Empty constructor for when reading from disk
    public BlurredRectangleBuilder(){ }

    public BlurredRectangleBuilder(ImageBuilder imageBuilder){
        this.imageBuilder = imageBuilder;
    }

    @Override
    public void addPoint(Point p) {
        if(points[0] == null){
            points[0] = p;
        }else{
            points[1] = p;

            // Once the second point is given, we can make the edit
            makeEdit();
        }
    }

    @Override
    public boolean needMorePoints() {
        return points[0] == null || points[1] == null;
    }

    @Override
    public void makeEdit() {
        // Calculate the other points
        // Get the point with higher x and y value
        Point firstSelection = points[0];
        Point secondSelection = points[1];
        Point start;

        int width = Math.abs(firstSelection.getX() - secondSelection.getX());
        int height = Math.abs(firstSelection.getY() - secondSelection.getY());

        if(firstSelection.getX() < secondSelection.getX() && firstSelection.getY() < secondSelection.getY()){
            start = firstSelection;
        }else if(firstSelection.getX() > secondSelection.getX() && firstSelection.getY() > secondSelection.getY()){
            start = secondSelection;
        }else if(firstSelection.getX() > secondSelection.getX() && firstSelection.getY() < secondSelection.getY()){
            start = new Point(secondSelection.getX(), firstSelection.getY());
        }else if(firstSelection.getX() < secondSelection.getX() && firstSelection.getY() > secondSelection.getY()){
            start = new Point(firstSelection.getX(), secondSelection.getY());
        }else{
            // Making a line
            // Vertical line
            if(width == 0){
                start = firstSelection.getY() > secondSelection.getY() ? secondSelection : firstSelection;
                width = 1;
            }else{
                // Only other case is a horizontal line
                start = firstSelection.getX() > secondSelection.getX() ? secondSelection : firstSelection;
                height = 1;
            }

        }

        imageBuilder.edit(new BlurredRectangle(start, width, height), true);
    }

    @Override
    Edit convertDiskInfoToEdit(String[] parts) {
        // Start at 1 because first section is the name of the edit, so in this case it is "BLURREDRECTANGLE"
        Point point = new Point(Integer.valueOf(parts[1]), Integer.valueOf(parts[2]));
        int width = Integer.parseInt(parts[3]);
        int height = Integer.parseInt(parts[4]);
        return new BlurredRectangle(point, width, height);
    }


}
