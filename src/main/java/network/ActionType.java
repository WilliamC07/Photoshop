package network;

/**
 * What action to perform on the receiving end of a socket.
 */
public enum ActionType {
    /**
     * Server to client
     * Tells the client to update their instructions to the one received.
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
     * Client to server
     * Requests the server to send a list of the collaborators to the one sent. The server should respond with
     * {@link #UPDATE_TO_LATEST_COLLABORATOR}
     */
    REQUEST_COLLABORATOR_LIST,

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
     * Client to server or server to client
     * Tells the receiving end to send the information it most recently sent to the server.
     * Works with {@link #SUCCESSFUL_TRANSACTION} and {@link #FAILURE}. One of these will be sent.
     */
    RESEND,

    /**
     * Client to server or server to client
     * Tells the sender that the file transfer was successful and the receiver is ready to take in another file.
     * Works with {@link #RESEND} and {@link #FAILURE}. One of these will be sent.
     */
    SUCCESSFUL_TRANSACTION,

    /**
     * Client to server or server to client
     * Tells the receiving end that it took too many attempts to send the file again and it will not be sending it
     * again. The response from the receiving should deal with it and send {@link #SUCCESSFUL_TRANSACTION}.
     * Works with {@link #RESEND} and {@link #SUCCESSFUL_TRANSACTION}.
     */
    FAILURE,

    /**
     * Special case where Connector tells the host to request data again (Connector to client or Connector to server)
     * @see Connector
     */
    RESOLVE_FAILED_FILE_TRANSFER,


    /**
     * Host client to server.
     * This should be called when a user wants to share the image with others. The user should send the original image.
     * This should be sent before {@link #SHARE_INITIALIZE_INSTRUCTIONS}.
     */
    SHARE_INITIALIZE_IMAGE,

    /**
     * Host client to server.
     * This should be called when a user wants to share the image with others. The user should send the XML file with
     * all the instructions done already.
     * This should be sent after {@link #SHARE_INITIALIZE_IMAGE}
     */
    SHARE_INITIALIZE_INSTRUCTIONS,

    /**
     * Client to server
     * Tells the server to accept the changes done by the client.
     */
    PUSH_INSTRUCTION,

    /**
     * Client to server
     * Gets the original image (the very first image used in the project)
     */
    SEND_ORIGINAL_IMAGE,
    /**
     * Client to server
     * Gets the most recent checkpoint image.
     */
    SEND_LATEST_CHECKPOINT_IMAGE,
    /**
     * Client to server
     * Gets all the instruction done on the original image
     */
    SEND_ALL_INSTRUCTIONS,
    /**
     * Client to server
     * Gets the instruction done on the latest checkpoint image
     */
    SEND_LATEST_INSTRUCTION;

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
