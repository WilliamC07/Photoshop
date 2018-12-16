package model.imageManipulation.edits;

import javafx.scene.paint.Color;

public class RectangleFactory implements RequirePoints{
    private final Point[] points = new Point[2];
    private final ImageBuilder imageBuilder;
    private Color color = Color.BLACK;

    RectangleFactory(ImageBuilder imageBuilder){
        this.imageBuilder = imageBuilder;
    }

    @Override
    public void addPoint(Point point){
        for(int i = 0; i < points.length; i++){
            Point p = points[i];
            if(p == null){
                points[i] = point;

                if(i == points.length - 1){
                    // Make the shape
                    System.out.println("made edit");
                    constructEdit();
                }

                return;
            }
        }
    }

    /**
     * Set the color of the rectangle. Default color of black.
     * @param color
     */
    public void setColor(Color color){
        this.color = color;
    }

    /**
     * Push the user input to the screen and image.
     */
    public void constructEdit(){
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

        imageBuilder.edit(new Rectangle(start, width, height, color));
    }
}
