package it.polimi.ingsw.network.server;

import it.polimi.ingsw.exception.MatchExceptions;
import it.polimi.ingsw.exception.NetworkExceptions;
import it.polimi.ingsw.lobby.Lobby;
import it.polimi.ingsw.message.*;
import it.polimi.ingsw.message.error.*;
import it.polimi.ingsw.message.action.GameMessage;
import it.polimi.ingsw.message.lobby.CreateLobbyMessage;
import it.polimi.ingsw.message.lobby.JoinLobbyMessage;
import it.polimi.ingsw.message.lobby.GetLobbiesResponseMessage;
import it.polimi.ingsw.lobby.LobbyInfo;
import it.polimi.ingsw.message.lobby.LobbyInfoMessage;
import it.polimi.ingsw.network.Server;
import it.polimi.ingsw.controller.Controller;

import java.util.List;
import java.util.logging.Logger;

/**
 * The ServerMessageHandler class handles incoming messages from clients
 * and processes them accordingly based on their type and the client's state.
 */
public class ServerMessageHandler {
    private static final Logger logger = Logger.getLogger(ServerMessageHandler.class.getName());

    /**
     * Handles an incoming message from a client.
     *
     * @param client the Connection representing the client
     * @param m the Message object received from the client
     */
    public static void handle(Connection client, Message m) {
        if (client.getUsername() == null){
            logger.info("Received message: " + m.getType());

            if(m.getType() != MessageType.LOGIN){
                client.send(new UnauthorizedError());
                return;
            }

            try {
                if(Server.login(((LoginMessage) m).username(), client)) // true for normal login, false for reconnection in game
                    client.send(new SuccessMessage(MessageType.LOGIN));
            } catch(NetworkExceptions e){
                client.send(new LoginError());
            }
        }
        else{
            logger.info("Received message: " + m.getType());
            switch (m.getType()){
                case CHAT:
                    Controller controller = client.getController();
                    if (controller == null){
                        client.send(new MatchDoesNotExistError());
                        return;
                    }
                    controller.chatMessage((ChatMessage) m);
                    break;
                case GAME:
                    Controller contr = client.getController();
                    if (contr == null){
                        client.send(new MatchDoesNotExistError());
                        return;
                    }
                    contr.action((GameMessage) m);
                    break;
                case JOIN:
                    try {
                        Lobby lobby = Server.joinLobby(client.getUsername(), ((JoinLobbyMessage) m).lobbyId(), client.getController());
                        client.send(new SuccessLobby(MessageType.JOIN, lobby.getId(), lobby.getPlayers(), lobby.getMaxPlayersNum()));
                    } catch (NetworkExceptions | MatchExceptions e){
                        client.send(new JoinMatchError());
                    }
                    break;
                case CREATE:
                    try {
                        Lobby lobby = Server.createLobby(client.getUsername(), ((CreateLobbyMessage) m).numPlayers(), client.getController());
                        client.send(new SuccessLobby(MessageType.JOIN, lobby.getId(), lobby.getPlayers(), lobby.getMaxPlayersNum()));
                    } catch (NetworkExceptions | MatchExceptions e){
                        client.send(new CreateMatchError());
                    }
                    break;
                case LEAVE:
                    try {
                        Server.leaveLobby(client.getUsername(), client.getController());
                        client.send(new SuccessMessage(MessageType.LEAVE));
                    } catch (NetworkExceptions | MatchExceptions e){
                        client.send(new LeaveMatchError());
                    }
                    break;
                case GET:
                    List<LobbyInfo> lobbies = Server.getNonStartedLobbies();
                    client.send(new GetLobbiesResponseMessage(lobbies));
                    break;
                case GET_LOBBY_INFO:
                    Lobby lobby = Server.getLobby(client.getUsername());
                    if(lobby != null){
                        client.send(new LobbyInfoMessage(lobby.getPlayers()));
                    } else {
                        client.send(new LeaveMatchError());
                    }
                    break;
                default:
                    client.send(new InvalidMessage());
            }
        }
    }

}
