package it.polimi.ingsw.message.lobby;

import it.polimi.ingsw.message.Message;
import it.polimi.ingsw.message.MessageType;

/**
 * Represents a message to request information about a specific lobby.
 * This message implements the {@link Message} interface and specifies the {@link MessageType#GET_LOBBY_INFO} type.
 */
public class GetLobbyInfoMessage implements Message {

    /**
     * Retrieves the type of this message, which is {@link MessageType#GET_LOBBY_INFO}.
     *
     * @return The message type, which is {@link MessageType#GET_LOBBY_INFO}.
     */
    @Override
    public MessageType getType() {
        return MessageType.GET_LOBBY_INFO;
    }
}
