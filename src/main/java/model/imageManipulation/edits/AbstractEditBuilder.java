package model.imageManipulation.edits;

public abstract class AbstractEditBuilder {
    abstract public Edit convertDiskInfoToEdit(String data);
    abstract public void makeEdit();
}
