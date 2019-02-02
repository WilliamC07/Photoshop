package model.imageManipulation.edits;

public class BrightnessBuilder extends AbstractEditBuilder{

    private ImageBuilder imageBuilder;
    private boolean makeBrighter;

    BrightnessBuilder() { }

    public BrightnessBuilder(ImageBuilder imageBuilder, boolean makeBrighter) {
      this.imageBuilder = imageBuilder;
      this.makeBrighter = makeBrighter;
    }

    @Override
    Edit convertDiskInfoToEdit(String[] parts) {
        boolean makeBright = parts[1].equalsIgnoreCase("true");
        return new Brightness(makeBright, Integer.valueOf(parts[2]), Integer.valueOf(parts[3]));
    }

    @Override
    public void makeEdit() {
        int width = (int) imageBuilder.getWritableImage().getWidth();
        int height = (int) imageBuilder.getWritableImage().getHeight();
        imageBuilder.edit(new Brightness(makeBrighter, width, height), true);
    }
}
