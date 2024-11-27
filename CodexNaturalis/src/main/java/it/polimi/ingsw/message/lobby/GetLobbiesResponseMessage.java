package it.polimi.ingsw.message.lobby;

import it.polimi.ingsw.message.Message;
import it.polimi.ingsw.message.MessageType;
import it.polimi.ingsw.lobby.LobbyInfo;

import java.util.List;

/**
 * Represents a message containing a response with a list of available lobbies.
 * This message implements the {@link Message} interface and specifies the {@link MessageType#LOBBIES} type.
 */
public class GetLobbiesResponseMessage implements Message {
    private final List<LobbyInfo> lobbies;

    /**
     * Constructs a new GetLobbiesResponseMessage with the specified list of lobbies.
     *
     * @param lobbies The list of lobbies to include in the response message.
     */
    public GetLobbiesResponseMessage(List<LobbyInfo> lobbies){
        this.lobbies = lobbies;
    }

    /**
     * Retrieves the list of lobbies contained in this response message.
     *
     * @return The list of lobbies.
     */
    public List<LobbyInfo> getLobbies(){
        return lobbies;
    }

    /**
     * Retrieves the type of this message, which is {@link MessageType#LOBBIES}.
     *
     * @return The message type, which is {@link MessageType#LOBBIES}.
     */
    @Override
    public MessageType getType() {
        return MessageType.LOBBIES;
    }

}
