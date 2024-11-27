package it.polimi.ingsw.lobby;

import java.io.Serializable;

/**
 * A class representing information about a lobby.
 */
public class LobbyInfo implements Serializable {

    private final int id;                             // The unique identifier of the lobby
    private final int numCurrentPlayers;              // The number of players currently in the lobby
    private final int maxPlayers;                     // The maximum number of players allowed in the lobby

    /**
     * Constructs a LobbyInfo object with the specified parameters.
     *
     * @param id                The unique identifier of the lobby.
     * @param numCurrentPlayers The number of players currently in the lobby.
     * @param maxPlayers        The maximum number of players allowed in the lobby.
     */
    public LobbyInfo(int id, int numCurrentPlayers, int maxPlayers){
        this.id = id;
        this.numCurrentPlayers = numCurrentPlayers;
        this.maxPlayers = maxPlayers;
    }

    /**
     * Retrieves the unique identifier of the lobby.
     *
     * @return The unique identifier of the lobby.
     */
    public int getId (){
        return id;
    }

    /**
     * Retrieves the number of players currently in the lobby.
     *
     * @return The number of players currently in the lobby.
     */
    public int getNumCurrentPlayers (){
        return numCurrentPlayers;
    }

    /**
     * Retrieves the maximum number of players allowed in the lobby.
     *
     * @return The maximum number of players allowed in the lobby.
     */
    public int getMaxPlayers (){
        return maxPlayers;
    }
}
