package it.polimi.ingsw.lobby;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.exception.*;
import it.polimi.ingsw.message.FailedToStartMatchMessage;
import it.polimi.ingsw.message.lobby.PlayerLeftLobbyMessage;
import it.polimi.ingsw.model.game.Match;
import it.polimi.ingsw.network.Server;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * Represents the lobby where players join before starting a match.
 */
public class Lobby {
    private static final Logger logger = Logger.getLogger(Lobby.class.getName());
    private final Match match;                      // The match associated with the lobby
    private final List<String> players;             // List of players in the lobby
    private final Chat chat;                        // Chat room for the lobby
    private final int numPlayers;                   // The number of players required for the match
    private final int id;

    /**
     * Constructs a lobby with a specified number of players.
     * @param id The ID used to identify the lobby.
     * @param numPlayers The number of players for the match.
     */
    public Lobby(int id, int numPlayers) {
        this.id = id;
        match = new Match(id);
        chat = new Chat();
        players = new ArrayList<>();
        this.numPlayers = numPlayers;
    }

    /**
     * Constructs a new Lobby instance with the specified ID and list of controllers.
     *
     * @param id          The ID used to identify the lobby.
     * @param controllers The list of controllers representing players in the lobby.
     * @throws IOException If an IO error occurs during initialization.
     */
    public Lobby(int id, List<Controller> controllers) throws IOException {
        this.id = id;
        match = new Match(id);
        chat = new Chat();
        numPlayers = controllers.size();
        players = controllers.stream().map(Controller::getUsername).toList();

        for(Controller controller : controllers){
            chat.addUser(controller);
        }

        match.load(controllers);

        new Thread(()->{
            try{
                match.resumeGame();
            } catch (Exception e){
                logger.warning("Lobby " + id + " failed to start match : " + e.getMessage());
                match.broadcast(new FailedToStartMatchMessage());
                Server.matchEnded(this);
            }
            Server.matchEnded(this);
        }).start();
    }

    /**
     * Allows a player to join the lobby.
     *
     * @param username   The username of the player joining.
     * @param controller The controller associated with the player.
     * @throws MatchExceptions If there is an error while joining the match.
     */
    synchronized public void join(String username, Controller controller) throws MatchExceptions {
        match.takeSeat(controller);
        players.add(username);
        chat.addUser(controller);
        if(players.size() == numPlayers){
            new Thread(()->{
                try{
                    match.startMatch();
                } catch (Exception e){
                    logger.warning("Lobby " + id + " failed to start match : " + e.getMessage());
                    match.broadcast(new FailedToStartMatchMessage());
                    Server.matchEnded(this);
                }
                Server.matchEnded(this);
            }).start();
        }
    }

    /**
     * Allows a player to leave the lobby.
     *
     * @param username   The username of the player leaving.
     * @param controller The controller associated with the player.
     * @throws MatchExceptions If there is an error while leaving the match.
     */
    synchronized public void leave(String username, Controller controller) throws MatchExceptions {
        match.broadcast(new PlayerLeftLobbyMessage(username), controller.getPlayer());
        match.leaveSeat(controller);
        players.remove(username);
        chat.removeUser(controller);
    }

    /**
     * Checks if the match has already started.
     *
     * @return True if the match has started, otherwise False.
     */
    public boolean isStarted(){
        return match.isStarted();
    }

    /**
     * Checks if the lobby is empty.
     *
     * @return True if the lobby is empty, otherwise False.
     */
    public boolean isEmpty(){
        return players.isEmpty();
    }

    /**
     * Retrieves the maximum number of players allowed in the lobby.
     *
     * @return The maximum number of players allowed in the lobby.
     */
    public int getMaxPlayersNum(){
        return numPlayers;
    }

    /**
     * Retrieves the current number of players in the lobby.
     *
     * @return The current number of players in the lobby.
     */
    public int getCurrentPlayersNum(){
        return players.size();
    }

    /**
     * Retrieves the chat room associated with the lobby.
     *
     * @return The chat room associated with the lobby.
     */
    public Chat getChat(){return chat;}

    /**
     * Gets a list of player usernames.
     *
     * @return A list of player usernames.
     */
    public List<String> getPlayers(){return new ArrayList<>(players);}

    /**
     * Gets the ID of the lobby.
     *
     * @return The ID of the lobby.
     */
    public int getId(){
        return id;
    }

    /**
     * Terminates the match, ending it due to a disconnection.
     */
    public void terminateMatch(){
        match.endForDisconnection();
    }
}
