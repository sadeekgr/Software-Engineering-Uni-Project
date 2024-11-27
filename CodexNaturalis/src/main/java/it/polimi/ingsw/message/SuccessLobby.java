package it.polimi.ingsw.message;

import java.util.List;

/**
 * Represents a success message related to a lobby in the game.
 * This message provides information about a specific lobby, including its ID, players, and maximum number of players.
 */
public class SuccessLobby extends SuccessMessage {
    int id;
    List<String> players;
    int maxNumPlayers;

    /**
     * Constructs a success message related to a lobby.
     *
     * @param successType   The type of success message.
     * @param id            The ID of the lobby.
     * @param players       The list of players in the lobby.
     * @param maxNumPlayers The maximum number of players allowed in the lobby.
     */
    public SuccessLobby(MessageType successType, int id, List<String> players, int maxNumPlayers) {
        super(successType);
        this.id = id;
        this.players = players;
        this.maxNumPlayers = maxNumPlayers;
    }

    /**
     * Gets the ID of the lobby.
     *
     * @return The ID of the lobby.
     */
    public int getId() {
        return id;
    }

    /**
     * Gets the list of players in the lobby.
     *
     * @return The list of players in the lobby.
     */
    public List<String> getPlayers() {
        return players;
    }

    /**
     * Gets the maximum number of players allowed in the lobby.
     *
     * @return The maximum number of players allowed in the lobby.
     */
    public int getMaxNumPlayers() {
        return maxNumPlayers;
    }
}