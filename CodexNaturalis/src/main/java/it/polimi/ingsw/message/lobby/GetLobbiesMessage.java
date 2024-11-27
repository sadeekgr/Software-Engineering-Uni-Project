package it.polimi.ingsw.message.lobby;

import it.polimi.ingsw.message.Message;
import it.polimi.ingsw.message.MessageType;

/**
 * Represents a message to request a list of available lobbies.
 * This message implements the {@link Message} interface and specifies the {@link MessageType#GET} type.
 */
public class GetLobbiesMessage implements Message {

    /**
     * Retrieves the type of this message, which is {@link MessageType#GET}.
     *
     * @return The message type, which is {@link MessageType#GET}.
     */
    @Override
    public MessageType getType() {
        return MessageType.GET;
    }
}

