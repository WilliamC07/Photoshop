package model.imageManipulation.edits;

public class DiskToEdit {
    public static Edit getEdit(String info){
        String[] parts = info.split(" ");

        switch(parts[0]){
            case "RECTANGLE":
                return new RectangleBuilder().convertDiskInfoToEdit(parts);
            case "RESIZE":
                return new ResizeBuilder().convertDiskInfoToEdit(parts);
            default:
                System.out.println("don't know what is " + info);
        }
        return null;
    }
}
