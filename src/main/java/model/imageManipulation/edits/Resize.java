package model.imageManipulation.edits;

import javafx.scene.image.ImageView;

class Resize extends Edit{
    private final int newWidth;
    private final int newHeight;

    Resize(int newWidth, int newHeight) {
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
    public String getStringRepresentation() {
        return String.format("RESIZE %d %d", newWidth, newHeight);
    }
}
