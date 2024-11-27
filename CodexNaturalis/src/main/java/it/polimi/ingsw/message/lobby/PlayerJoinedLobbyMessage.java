package it.polimi.ingsw.message.lobby;

import it.polimi.ingsw.message.Message;
import it.polimi.ingsw.message.MessageType;

/**
 * Represents a message indicating that a player has joined a lobby.
 * This message implements the {@link Message} interface and specifies the {@link MessageType#PLAYER_JOINED_LOBBY} type.
 */
public class PlayerJoinedLobbyMessage implements Message {
    String username;

    /**
     * Constructs a new PlayerJoinedLobbyMessage for the specified player.
     *
     * @param username The username of the player who joined the lobby.
     */
    public PlayerJoinedLobbyMessage(String username) {
        this.username = username;
    }

    /**
     * Retrieves the username of the player who joined the lobby.
     *
     * @return The username of the player.
     */
    public String getUsername() {
        return username;
    }

    /**
     * Retrieves the type of this message, which is {@link MessageType#PLAYER_JOINED_LOBBY}.
     *
     * @return The message type, which is {@link MessageType#PLAYER_JOINED_LOBBY}.
     */
    @Override
    public MessageType getType() {
        return MessageType.PLAYER_JOINED_LOBBY;
    }
}
