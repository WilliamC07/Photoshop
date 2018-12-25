package model;

/**
 * The classes that implements this contains data that must be written onto disk.
 */
public interface Savable {
    /**
     * This method should save the class information onto disk.
     */
    void save();
}
