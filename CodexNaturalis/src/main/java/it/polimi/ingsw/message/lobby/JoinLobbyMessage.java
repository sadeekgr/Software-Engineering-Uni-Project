package it.polimi.ingsw.message.lobby;

import it.polimi.ingsw.message.Message;
import it.polimi.ingsw.message.MessageType;

/**
 * Represents a message to join a specific lobby identified by its ID.
 * This message is a record that implements the {@link Message} interface
 * and specifies the {@link MessageType#JOIN} type.
 * @param lobbyId of the lobby to join
 */
public record JoinLobbyMessage(int lobbyId) implements Message {

    /**
     * Retrieves the type of this message, which is {@link MessageType#JOIN}.
     *
     * @return The message type, which is {@link MessageType#JOIN}.
     */
    @Override
    public MessageType getType() {
        return MessageType.JOIN;
    }
}
