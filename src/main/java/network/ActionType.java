package network;

/**
 * What action to perform on the receiving end of a socket.
 */
public enum ActionType {
    // TODO: replace these placeholders with useful ones
    SEND_LATEST_CHANGES,
    UPDATE_TO_LATEST_CHANGES;

    /**
     * Give values to the enum values (index position)
     */
    private final static ActionType[] ENUMS = ActionType.values();

    /**
     * Get the ActionType object given a magic number
     * @param val The magic number (index number of {@link #ENUMS}) that represent each enum object
     * @return The ActionType Object
     */
    static ActionType get(int val){
        if(val >= ENUMS.length || val < 0){
            throw new IllegalArgumentException("ActionType number does not exist");
        }
        return ENUMS[val];
    }

    /**
     * Get the magic number of a ActionType object
     * @param actionType ActionType object to be converted into a magic number
     * @return Magic number of the given ActionType Object
     */
    static int get(ActionType actionType){
        for(int i = 0; i < ENUMS.length; i++){
            if(ENUMS[i] == actionType){
                return i;
            }
        }
        throw new IllegalArgumentException("Cannot file ActionType");
    }
}
