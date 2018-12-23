package model.imageManipulation.edits;

public class ResizeFactory {
    public ResizeFactory(int width, int height, ImageBuilder imageBuilder){
        Edit edit = new Resize(width, height);
        imageBuilder.edit(edit);
    }
}
