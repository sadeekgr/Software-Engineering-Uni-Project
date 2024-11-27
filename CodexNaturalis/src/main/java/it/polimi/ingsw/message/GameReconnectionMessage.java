package it.polimi.ingsw.message;

/**
 * This class represents a message indicating a successful reconnection to a game.
 */
public class GameReconnectionMessage implements Message {

    /**
     * Retrieves the type of this message, which is {@link MessageType#RECONNECTION}.
     *
     * @return The message type, which is {@link MessageType#RECONNECTION}.
     */
    @Override
    public MessageType getType() {
        return MessageType.RECONNECTION;
    }
}
