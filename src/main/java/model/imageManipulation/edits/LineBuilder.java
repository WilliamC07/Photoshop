package model.imageManipulation.edits;

import javafx.scene.control.ColorPicker;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;

public class LineBuilder extends AbstractEditBuilder implements RequirePoints{
    private ColorPicker colorPicker;
    private TextField widthSize;
    private Point[] points = new Point[2];
    private ImageBuilder imageBuilder;

    public LineBuilder(ImageBuilder imageBuilder, ColorPicker colorPicker, TextField widthSize) {
        this.imageBuilder = imageBuilder;
        this.colorPicker = colorPicker;
        this.widthSize = widthSize;
    }

    /**
     * Only call when reading from disk
     */
    LineBuilder(){}

    @Override
    Edit convertDiskInfoToEdit(String[] parts) {
        Point p1 = new Point(Integer.parseInt(parts[1]), Integer.parseInt(parts[2]));
        Point p2 = new Point(Integer.parseInt(parts[3]), Integer.parseInt(parts[4]));
        Color color = Color.web(parts[6]);
        int width = Integer.parseInt(parts[5]);
        return new Line(p1, p2, color, width);
    }

    @Override
    public void makeEdit() {
        int width;
        try{
            width = Integer.parseInt(widthSize.getText());
        }catch(NumberFormatException e){
            width = 5;
            System.out.println("bad number");
        }
        imageBuilder.edit(new Line(points[0], points[1], colorPicker.getValue(), width), true);
    }

    @Override
    public void addPoint(Point p) {
        if(points[0] == null){
            points[0] = p;
        }else{
            points[1] = p;

            makeEdit();
        }
    }

    @Override
    public boolean needMorePoints() {
        return points[1] == null;
    }
}
