package network;

/**
 * Enum to identify what type of data is being sent.
 */
enum ChunkType {
    /**
     * Value of 0
     * First chunk sent by a socket connection
     */
    START,

    /**
     * Value of 1
     * Intermediate chunk sent by a socket connection
     */
    DATA,

    /**
     * Value of 2
     * Last chunk sent by a socket connection
     */
    END;

    /**
     * Give values to the enum values (index position)
     */
    private final static ChunkType[] ENUMS = ChunkType.values();

    /**
     * Get the ChunkType object given a magic number
     * @param val The magic number (index number of {@link #ENUMS}) that represent each enum object
     * @return The ChunkType Object
     */
    static ChunkType get(int val){
        if(val >= ENUMS.length || val < 0){
            throw new IllegalArgumentException("ChunkType number does not exist " + val);
        }
        return ENUMS[val];
    }

    /**
     * Get the magic number of a ChunkType object
     * @param chunkType ChunkType object to be converted into a magic number
     * @return Magic number of the given ChunkType Object
     */
    static int get(ChunkType chunkType){
        for(int i = 0; i < ENUMS.length; i++){
            if(ENUMS[i] == chunkType){
                return i;
            }
        }
        throw new IllegalArgumentException("Cannot file ChunkType");
    }
}
