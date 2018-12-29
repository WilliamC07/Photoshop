package network;

/**
 * This tells what type of File is being sent.
 * You are safe to add more objects to this enum without side effects.
 */
public enum FileType {
    /**
     * Value of 0
     */
    IMAGE,
    /**
     * Value of 1
     */
    XML,

    /**
     * Value of 2
     * Only send a string value
     */
    STRING,

    /**
     * Value of 3
     * If you are not sending any files or a {@link #STRING}, send this
     */
    NONE,

    /**
     * Misc. stuff
     */
    STRING_AND_FILE;

    /**
     * Give values to the enum objects (index position)
     */
    private final static FileType[] ENUMS = FileType.values();

    /**
     * Get the FileType object given a magic number
     * @param val The magic number (index number of {@link #ENUMS}) that represent each enum object
     * @return The FileType Object
     */
    public static FileType get(int val){
        if(val > ENUMS.length || val < 0){
            throw new IllegalArgumentException("FileType number does not exist");
        }
        return ENUMS[val];
    }

    /**
     * Get the magic number of a FileType object
     * @param fileType FileType object to be converted into a magic number
     * @return Magic number of the given FileType Object
     */
    public static int get(FileType fileType){
        for(int i = 0; i < ENUMS.length; i++){
            if(ENUMS[i] == fileType){
                return i;
            }
        }
        throw new IllegalArgumentException("Cannot file FileType");
    }
}
