package it.polimi.ingsw.message;

/**
 * This class represents a message indicating a failed attempt to reconnect to a game.
 */
public class FailedGameReconnectionMessage implements Message {

    /**
     * Retrieves the type of this message, which is {@link MessageType#RECONNECTION_FAILED}.
     *
     * @return The message type, which is {@link MessageType#RECONNECTION_FAILED}.
     */
    @Override
    public MessageType getType() {
        return MessageType.RECONNECTION_FAILED;
    }
}
