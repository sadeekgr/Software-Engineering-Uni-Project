package it.polimi.ingsw.message.lobby;

import it.polimi.ingsw.message.Message;
import it.polimi.ingsw.message.MessageType;

import java.util.List;

/**
 * Represents a message containing information about players in a lobby.
 * This message implements the {@link Message} interface and specifies the {@link MessageType#LOBBY_INFO} type.
 */
public class LobbyInfoMessage implements Message {
    private final List<String> players;

    /**
     * Constructs a new LobbyInfoMessage with the specified list of players.
     *
     * @param players The list of players in the lobby to include in the message.
     */
    public LobbyInfoMessage(List<String> players){
        this.players = players;
    }

    /**
     * Retrieves the list of players contained in this lobby information message.
     *
     * @return The list of players in the lobby.
     */
    public List<String> getPlayers(){ return players; }

    /**
     * Retrieves the type of this message, which is {@link MessageType#LOBBY_INFO}.
     *
     * @return The message type, which is {@link MessageType#LOBBY_INFO}.
     */
    @Override
    public MessageType getType() {
        return MessageType.LOBBY_INFO;
    }
}
