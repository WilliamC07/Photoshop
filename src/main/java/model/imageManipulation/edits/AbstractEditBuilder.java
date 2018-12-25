package model.imageManipulation.edits;

public abstract class AbstractEditBuilder {
    abstract Edit convertDiskInfoToEdit(String[] parts);
    abstract public void makeEdit();
}
