//package server;

public enum ChunkType {
    /**
     * Value of 0
     */
    START,

    /**
     * Value of 1
     */
    DATA,

    /**
     * Value of 2
     */
    END;

    private final static ChunkType[] ENUMS = ChunkType.values();

    public static ChunkType get(int val){
        if(val >= ENUMS.length || val < 0){
            throw new IllegalArgumentException("ChunkType number does not exist");
        }
        return ENUMS[val];
    }

    public static int get(ChunkType fileType){
        for(int i = 0; i < ENUMS.length; i++){
            if(ENUMS[i] == fileType){
                return i;
            }
        }
        throw new IllegalArgumentException("Cannot file ChunkType");
    }
}
