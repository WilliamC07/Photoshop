package model.imageManipulation.edits;

public class Rectangle implements Shape{
    private Point[] points;

    public Rectangle(Point p1, Point p2, Point p3, Point p4){
        points = new Point[]{p1, p2, p3, p4};
    }

    @Override
    public boolean pointExists(Point point) {

        return false;
    }

    @Override
    public boolean onBorder(Point point) {

        return false;
    }

    @Override
    public Point[] getPoints() {
        return new Point[0];
    }
}
