package it.polimi.ingsw.message;

import java.io.Serializable;

/**
 * Represents a message in the game.
 * All message types must implement this interface.
 */
public interface Message extends Serializable {

    /**
     * Gets the type of the message.
     *
     * @return The MessageType of the message.
     */
    MessageType getType();
}
