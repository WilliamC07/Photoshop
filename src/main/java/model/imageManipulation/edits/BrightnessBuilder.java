package model.imageManipulation.edits;

public class BrightnessBuilder extends AbstractEditBuilder{

    private ImageBuilder imageBuilder;

    BrightnessBuilder() { }

    public BrightnessBuilder(ImageBuilder imageBuilder) {
      this.imageBuilder = imageBuilder;
    }

    @Override
    Edit convertDiskInfoToEdit(String[] parts) {
      boolean makeBrighter = false;
      if (parts[1].equals("true")){
        makeBrighter = true;
      }
      int width = Integer.parseInt(parts[2]);
      int height = Integer.parseInt(parts[3]);
      return new Brightness(makeBrighter, width, height);
    }

    @Override
    public void makeEdit() {
      
    }
}
