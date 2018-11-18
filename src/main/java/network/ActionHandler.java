package network;

import java.io.File;

/**
 * All classes that uses Connector must implement this class.
 * This class forces you to deal with the received file and instruction (ActionType)
 *
 * @see network.Connector
 * @see network.ActionType
 */
interface ActionHandler {
    /**
     * Handles the instruction given by the sender
     *
     * @param file File received (can be null)
     * @param actionType Action to be perform
     * @throws Exception If actionType is not for the receiving end
     */
    void handle(File file, ActionType actionType) throws Exception;
}
