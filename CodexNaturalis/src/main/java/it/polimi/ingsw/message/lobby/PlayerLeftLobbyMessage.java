package it.polimi.ingsw.message.lobby;

import it.polimi.ingsw.message.Message;
import it.polimi.ingsw.message.MessageType;

/**
 * Represents a message indicating that a player has left a lobby.
 * This message implements the {@link Message} interface and specifies the {@link MessageType#PLAYER_LEFT_LOBBY} type.
 */
public class PlayerLeftLobbyMessage implements Message {
    String username;

    /**
     * Constructs a new PlayerLeftLobbyMessage for the specified player.
     *
     * @param username The username of the player who left the lobby.
     */
    public PlayerLeftLobbyMessage(String username) {
        this.username = username;
    }

    /**
     * Retrieves the username of the player who left the lobby.
     *
     * @return The username of the player.
     */
    public String getUsername() {
        return username;
    }

    /**
     * Retrieves the type of this message, which is {@link MessageType#PLAYER_LEFT_LOBBY}.
     *
     * @return The message type, which is {@link MessageType#PLAYER_LEFT_LOBBY}.
     */
    @Override
    public MessageType getType() {
        return MessageType.PLAYER_LEFT_LOBBY;
    }
}
