package model.imageManipulation.edits;

import javafx.scene.control.ColorPicker;

public class RectangleBuilder extends AbstractEditBuilder implements RequirePoints {
    /**
     * Points given to construct this rectangle.
     * Only two points are required to make a rectangle.
     */
    private final Point[] points = new Point[2];
    private final ColorPicker colorPicker;
    private final ImageBuilder imageBuilder;

    public RectangleBuilder(ImageBuilder imageBuilder, ColorPicker colorPicker){
        this.imageBuilder = imageBuilder;
        this.colorPicker = colorPicker;
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

        imageBuilder.edit(new Rectangle(start, width, height, colorPicker.getValue()));
    }

    @Override
    public Edit convertDiskInfoToEdit(String data) {
        return null;
    }


}
