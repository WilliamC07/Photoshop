package model.imageManipulation.edits;

public interface RequirePoints {
    void addPoint(Point p);
    boolean needMorePoints();
}
