package network;

import java.io.File;

/**
 * Deal with the information sent.
 *
 * @see network.Connector
 * @see network.ActionType
 */
interface ActionHandler {
    /**
     * If only a message, ActionType are received will this be called.
     * @param message String value of data received
     * @param actionType Action to be done by the receiving end
     * @param connector Reference to the socket connection
     */
    void handle(String message, ActionType actionType, Connector connector);

    /**
     * If only a file, ActionType are received will this be called.
     * @param file File received
     * @param fileType FileType received.
     * @param actionType Action to be done by the receiving end
     * @param connector Reference to the socket connection
     */
    void handle(byte[] file, FileType fileType, ActionType actionType, Connector connector);

    /**
     * If only a action is received
     * @param actionType Action to be done by the receiving end
     * @param connector Reference to the socket connection
     */
    void handle(ActionType actionType, Connector connector);

    /**
     * If a file, message, and ActionType is received.
     * @param file File received
     * @param fileType FileType received.
     * @param message String value of data received
     * @param actionType Action to be done by the receiving end
     * @param connector Reference to the socket connection
     */
    void handle(byte[] file, FileType fileType, String message, ActionType actionType, Connector connector);
}
