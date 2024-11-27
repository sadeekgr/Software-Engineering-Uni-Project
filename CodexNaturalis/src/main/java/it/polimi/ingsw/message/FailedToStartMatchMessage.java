package it.polimi.ingsw.message;

/**
 * This class represents a message indicating a failed attempt to start the match.
 */
public class FailedToStartMatchMessage implements Message {
    /**
     * Retrieves the type of this message, which is {@link MessageType#FAILED_TO_START_MATCH}.
     *
     * @return The message type, which is {@link MessageType#FAILED_TO_START_MATCH}.
     */
    @Override
    public MessageType getType() {
        return MessageType.FAILED_TO_START_MATCH;
    }
}
