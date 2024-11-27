package it.polimi.ingsw.message;

/**
 * Represents a success message in the game.
 * This message indicates a successful operation or action.
 */
public class SuccessMessage implements Message {
    MessageType successType;

    /**
     * Constructs a success message with a specific type.
     *
     * @param successType The type of success message.
     */
    public SuccessMessage(MessageType successType) {
        this.successType = successType;
    }

    /**
     * Gets the type of success message.
     *
     * @return The MessageType representing the type of success.
     */
    public MessageType successType() {
        return successType;
    }

    /**
     * Retrieves the type of this message, which is {@link MessageType#SUCCESS}.
     *
     * @return The message type, which is {@link MessageType#SUCCESS}.
     */
    @Override
    public MessageType getType() {
        return MessageType.SUCCESS;
    }
}
