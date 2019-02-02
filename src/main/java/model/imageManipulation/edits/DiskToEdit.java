package model.imageManipulation.edits;

public class DiskToEdit {
    public static Edit getEdit(String info){
        String[] parts = info.split(" ");

        switch(parts[0]){
            case "RECTANGLE":
                return new RectangleBuilder().convertDiskInfoToEdit(parts);
            case "LINE":
                return new LineBuilder().convertDiskInfoToEdit(parts);
            case "BLURREDRECTANGLE":
                return new BlurredRectangleBuilder().convertDiskInfoToEdit(parts);
            default:
                System.out.println("don't know what is " + info);
        }
        return null;
    }
}
