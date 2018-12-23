package model.imageManipulation.edits;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;

public class Resize extends Edit{
    private final int newWidth;
    private final int newHeight;

    public Resize(int newWidth, int newHeight) {
        this.newWidth = newWidth;
        this.newHeight = newHeight;
    }

    @Override
    void change(ImageBuilder imageBuilder) {
        ImageView imageView = new ImageView(imageBuilder.getWritableImage());
        imageView.setPreserveRatio(false);
        imageView.setFitHeight(newHeight);
        imageView.setFitWidth(newWidth);
        imageBuilder.setWritableImage(imageView.snapshot(null, null));
    }


    @Override
    String getStringRepresentation() {
        return null;
    }
}
