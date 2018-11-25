package network;

import java.io.File;

/**
 * Deal with the information sent.
 *
 * @see network.Connector
 * @see network.ActionType
 */
@FunctionalInterface
interface ActionHandler {
    /**
     * Handles the file sent through given the file, file type, action type, and connector
     * 
     * @param file file received
     * @param message Can be null if and only if file is null.
     * @param fileType file type received
     * @param actionType action type received.
     * @param connector Connector that delivered the information
     * @throws IllegalArgumentException Wrong instruction sent
     */
    void handle(File file, String message, FileType fileType, ActionType actionType, Connector connector) throws IllegalArgumentException;
}
