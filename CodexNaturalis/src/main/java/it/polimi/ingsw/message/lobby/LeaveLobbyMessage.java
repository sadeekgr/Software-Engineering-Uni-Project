package it.polimi.ingsw.message.lobby;

import it.polimi.ingsw.message.Message;
import it.polimi.ingsw.message.MessageType;

/**
 * Represents a message to leave the current lobby.
 * This message implements the {@link Message} interface and specifies the {@link MessageType#LEAVE} type.
 */
public class LeaveLobbyMessage implements Message {

    /**
     * Retrieves the type of this message, which is {@link MessageType#LEAVE}.
     *
     * @return The message type, which is {@link MessageType#LEAVE}.
     */
    @Override
    public MessageType getType() {
        return MessageType.LEAVE;
    }
}
