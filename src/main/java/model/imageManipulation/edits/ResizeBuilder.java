package model.imageManipulation.edits;

public class ResizeBuilder extends AbstractEditBuilder{
    private ImageBuilder imageBuilder;
    private int newWidth;
    private int newHeight;

    /**
     * Should only be called when reading from disk.
     */
    ResizeBuilder(){ }

    public ResizeBuilder(ImageBuilder imageBuilder, int newWidth, int newHeight){
        this.newWidth = newWidth;
        this.newHeight = newHeight;
        this.imageBuilder = imageBuilder;
    }

    @Override
    Edit convertDiskInfoToEdit(String[] parts) {
        // First element (index 0) is reserved for identifier. In this case, it reads "RESIZE"
        return new Resize(Integer.valueOf(parts[1]), Integer.valueOf(parts[2]));
    }

    @Override
    public void makeEdit() {
        imageBuilder.edit(new Resize(newWidth, newHeight), true);
    }
}
