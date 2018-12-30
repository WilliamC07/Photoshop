package network;

/**
 * What action to perform on the receiving end of a socket.
 */
public enum ActionType{
    /**
     * Server to client
     * Gives the client a xml file that contains all the edits done on the project so far
     */
    UPDATE_TO_LATEST_INSTRUCTION,

    /**
     * Server to client
     * Tells the client to use the latest checkpoint image.
     */
    UPDATE_TO_LATEST_CHECKPOINT,

    /**
     * Server to client
     * Tells the client to update the list of collaborators to the one sent
     */
    UPDATE_TO_LATEST_COLLABORATOR,

    /**
     * Server to client
     * Tells the client to update to the new project name
     */
    UPDATE_PROJECT_NAME,

    /**
     * Server to client
     * Tells the client to use the original image defined by the host
     */
    UPDATE_ORIGINAL_IMAGE,

    /**
     * Server to client
     * Tells the client to use the given checkpoint image. This should be sent with a message to tell what the
     * checkpoint index is.
     */
    UPDATE_CHECKPOINT_IMAGE,
    /**
     * Server to client
     * Tells the client that they are out of sync. The client should respond by asking for instructions and all
     * related images (original and checkpoint).
     */
    OUT_OF_SYNC,

    /**
     * Client to server
     * Requests the server to send a list of the collaborators to the one sent. The server should respond with
     * {@link #UPDATE_TO_LATEST_COLLABORATOR}
     */
    REQUEST_COLLABORATOR_LIST,

    /**
     * Client to server
     * Request the name of the project named by the host. The server should respond with {@link #UPDATE_PROJECT_NAME}
     */
    REQUEST_PROJECT_NAME,

    /**
     * Client to server
     * Tells the server to add the client's username to the list of collaborator
     */
    ADD_COLLABORATOR_USERNAME,


    /**
     * Client to server or server to client
     * Tells the receiving end that the connection is closed and the sender will terminate the connection
     */
    QUIT_CONNECTION,

    /**
     * Special case where Connector tells the host to request data again (Connector to client or Connector to server)
     * @see Connector
     */
    RESOLVE_FAILED_FILE_TRANSFER,

    /**
     * Client to server
     * Tells the server to accept the changes done by the client.
     */
    PUSH_INSTRUCTION,

    /**
     * Client to server
     * Gets the original image (the very first image used in the project)
     */
    REQUEST_ORIGINAL_IMAGE,
    /**
     * Client to server
     * Tells the server to send all the checkpoint images to the client. If there are none, nothing will happen.
     */
    REQUEST_CHECKPOINT_IMAGES,

    /**
     * Client to server
     * Note: Each checkpoint image has a number corresponding to how close it is to the most up to date image (higher
     * being closer to the most up to date one).
     * Tells the server to send all the checkpoint images that are numbered the one defined by a message onwards
     * (inclusive). The client must give a number (will be represented as a string). If the checkpoint image does not
     * exist, that means the connection is out of sync and the server will respond with
     */
    REQUEST_CHECKPOINT_ONWARDS,
    /**
     * Client to server
     * Gets all the instruction done on the original image
     */
    REQUEST_ALL_INSTRUCTIONS,
    /**
     * Client to server
     * Gets the instruction done on the latest checkpoint image
     */
    REQUEST_LATEST_INSTRUCTION;

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
