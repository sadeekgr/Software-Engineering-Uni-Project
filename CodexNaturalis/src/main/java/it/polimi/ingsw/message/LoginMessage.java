package it.polimi.ingsw.message;

/**
 * Represents a login message containing the username of the player.
 *
 * @param username The username of the player attempting to log in.
 */
public record LoginMessage(String username) implements Message {

    /**
     * Retrieves the type of this message, which is {@link MessageType#LOGIN}.
     *
     * @return The message type, which is {@link MessageType#LOGIN}.
     */
    @Override
    public MessageType getType() {
        return MessageType.LOGIN;
    }
}
