package it.polimi.ingsw.message.lobby;

import it.polimi.ingsw.message.Message;
import it.polimi.ingsw.message.MessageType;

/**
 * Represents a message to create a new lobby with a specified number of players.
 * This message implements the {@link Message} interface and specifies the {@link MessageType#CREATE} type.
 * @param numPlayers max number of players
 */
public record CreateLobbyMessage(int numPlayers) implements Message {

    /**
     * Retrieves the type of this message, which is {@link MessageType#CREATE}.
     *
     * @return The message type, which is {@link MessageType#CREATE}.
     */
    @Override
    public MessageType getType() {
        return MessageType.CREATE;
    }
}
