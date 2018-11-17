//package server;

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
     */
    MISC;

    private final static FileType[] ENUMS = FileType.values();

    public static FileType get(int val){
        if(val > ENUMS.length || val < 0){
            throw new IllegalArgumentException("FileType number does not exist");
        }
        return ENUMS[val];
    }

    public static int get(FileType fileType){
        for(int i = 0; i < ENUMS.length; i++){
            if(ENUMS[i] == fileType){
                return i;
            }
        }
        throw new IllegalArgumentException("Cannot file FileType");
    }
}
