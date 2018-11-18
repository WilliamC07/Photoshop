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
    void handle(File file, ActionType actionType);
}
