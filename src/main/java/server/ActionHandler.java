package server;

import java.io.File;

/**
 * All classes that uses Connector must implement this class.
 * This class forces you to deal with the received file and instruction (ActionType)
 *
 * @see server.Connector
 * @see server.ActionType
 */
interface ActionHandler {
    void handle(File file, ActionType actionType);
}
