package it.polimi.ingsw.view;

import it.polimi.ingsw.gamestate.GameState;
import it.polimi.ingsw.message.*;
import it.polimi.ingsw.message.error.ErrorMessage;
import it.polimi.ingsw.message.lobby.GetLobbiesResponseMessage;
import it.polimi.ingsw.message.lobby.LobbyInfoMessage;
import it.polimi.ingsw.message.lobby.PlayerJoinedLobbyMessage;
import it.polimi.ingsw.message.lobby.PlayerLeftLobbyMessage;
import it.polimi.ingsw.message.notify.*;
import it.polimi.ingsw.network.client.Connection;
import it.polimi.ingsw.network.client.Skeleton;
import it.polimi.ingsw.network.client.SocketConnection;
import it.polimi.ingsw.network.server.ServerRemote;

import java.io.IOException;
import java.net.Socket;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

/**
 * The View class serves as an abstract base for handling the connection between the client and the server.
 * It provides methods for setting up connections, sending messages, and handling various types of messages
 * received from the server.
 */
public abstract class View {
    //should load server address, socket port etc. from a configuration file
    private String serverAddress = "127.0.0.1";
    private int socketPort = 13337;
    private int rmiPort = 33445;

    private Connection connection = null;
    private GameState gameState;

    /**
     * Constructs a View and loads the server address, socket port, and RMI port from a configuration file.
     */
    protected View(){
        // Here load serverAddress, socketPort, rmiPort from a configuration file
    }

    /**
     * Sends a message to the server.
     *
     * @param m The message to send.
     */
    public void sendMessage(Message m){
        if(connection != null && m != null){
            connection.send(m);
        }
    }

    /**
     * Closes the current connection to the server.
     */
    public void closeConnection(){ if(connection != null) connection.close(); }

    /**
     * Gets the current connection.
     *
     * @return The current connection.
     */
    public Connection getConnection(){ return connection; }

    /**
     * Gets the server address.
     *
     * @return The server address.
     */
    public String getServerAddress() {
        return serverAddress;
    }

    /**
     * Sets the server address if it is valid.
     *
     * @param address The new server address.
     * @return True if the address is valid and set, otherwise false.
     */
    public boolean setServerAddress(String address){
        if(!isIPValid(address)){
            return false;
        }

        serverAddress = address;
        return true;
    }

    /**
     * Gets the socket port.
     *
     * @return The socket port.
     */
    public int getSocketPort() {
        return socketPort;
    }

    /**
     * Sets the socket port if it is valid.
     *
     * @param port The new socket port.
     * @return True if the port is valid and set, otherwise false.
     */
    public boolean setSocketPort(int port){
        if(!isPortValid(port)){
            return false;
        }

        socketPort = port;
        return true;
    }

    /**
     * Gets the RMI port.
     *
     * @return The RMI port.
     */
    public int getRmiPort() {
        return rmiPort;
    }

    /**
     * Sets the RMI port if it is valid.
     *
     * @param port The new RMI port.
     * @return True if the port is valid and set, otherwise false.
     */
    public boolean setRmiPort(int port){
        if(!isPortValid(port)){
            return false;
        }

        rmiPort = port;
        return true;
    }

    /**
     * Gets the current game state.
     *
     * @return The current game state.
     */
    public GameState getGameState(){ return gameState; }

    /**
     * Sets the game state for a specified user.
     *
     * @param username The username for the game state.
     */
    public void setGameState(String username){
        gameState = new GameState(username);
    }

    /**
     * Sets up a socket connection with the server.
     *
     * @throws IOException If an I/O error occurs when creating the socket.
     */
    public void setUpSocketConnection() throws IOException {
        System.out.print("Setting up socket connection...\r\n");

        Socket socket = new Socket(serverAddress, socketPort);
        SocketConnection conn = new SocketConnection(socket, this);
        new Thread(conn::start).start();
        System.out.print("Connection Established\r\n");
        connection = conn;
    }

    /**
     * Sets up an RMI connection with the server.
     *
     * @throws RemoteException If a remote communication error occurs.
     * @throws NotBoundException If the registry does not contain the requested binding.
     */
    public void setUpRMIConnection() throws RemoteException, NotBoundException {
        System.out.print("Setting up RMI connection...\r\n");

        // Locate the registry where the server is running
        System.out.println(serverAddress);
        Registry registry = LocateRegistry.getRegistry(serverAddress, rmiPort);

        // Look up the remote object from the registry
        ServerRemote server = (ServerRemote) registry.lookup("RMIServer");

        // Create a callback object
        Skeleton skeleton = new Skeleton(server, this);
        connection = skeleton;

        //Start HeartBeat
        new Thread(skeleton::heartBeat).start();
        System.out.print("Connection Established\r\n");
    }

    /**
     * Checks if the provided IP address is valid.
     * @param ip The IP address to check.
     * @return True if the IP address is valid, otherwise false.
     */
    private boolean isIPValid(String ip){
        String[] parts = ip.split("\\.");

        if(parts.length != 4)
            return false;

        for(String part : parts){
            try {
                int num = Integer.parseInt(part);
                if(num < 0 || num > 255)
                    return false;
            } catch(NumberFormatException e){
                return false;
            }
        }

        return true; // IP address is valid
    }

    /**
     * Checks if the provided port number is valid.
     *
     * @param port The port number to check.
     * @return True if the port number is valid, otherwise false.
     */
    private boolean isPortValid(int port){
        return port >= 0 && port <= 65536;
    }

    /**
     * Starts the view, to be implemented by subclasses.
     */
    public abstract void run();

    /**
     * Updates the view with a received message and dispatches it to the appropriate handler.
     *
     * @param m The received message.
     */
    public void update(Message m){
        onMessage(m);
        switch (m.getType()){
            case SUCCESS:
                onSuccessMessage((SuccessMessage) m);
                break;
            case ERROR:
                onErrorMessage((ErrorMessage) m);
                break;
            case LOBBIES:
                onGetLobbiesMessage((GetLobbiesResponseMessage) m);
                break;
            case LOBBY_INFO:
                onLobbyInfoMessage((LobbyInfoMessage) m);
                break;
            case PLAYER_JOINED_LOBBY:
                onPlayerJoinedMessage((PlayerJoinedLobbyMessage) m);
                break;
            case PLAYER_LEFT_LOBBY:
                onPlayerLeftMessage((PlayerLeftLobbyMessage) m);
                break;
            case CHAT:
                ChatMessage chatMsg = (ChatMessage) m;
                gameState.updateChat(chatMsg);

                onChatMessage(chatMsg);
                break;
            case GAMESTATE:
                onGameStateMessage((GameStateMessage) m);
                break;
            case RECONNECTION:
                onReconnectionMessage((GameReconnectionMessage) m);
                break;
            case RECONNECTION_FAILED:
                onReconnectionFailedMessage((FailedGameReconnectionMessage) m);
                break;
            case FAILED_TO_START_MATCH:
                onFailedToStartMatchMessage((FailedToStartMatchMessage) m);
                break;
            case INFO:
                NotifyMessage notifyMsg = (NotifyMessage) m;
                gameState.updateState(notifyMsg);

                switch (notifyMsg.getNotifyType()) {
                    case MATCH_STARTED:
                        onMatchStartedMessage((NotifyMatchStarted) notifyMsg);
                        break;
                    case CHOOSE_STARTER:
                        onChooseStarterMessage((NotifyToChooseStarter) notifyMsg);
                        break;
                    case CHOOSE_OBJECTIVE:
                        onChooseObjectiveMessage((NotifyToChooseObjective) notifyMsg);
                        break;
                    case CARD_STATE:
                        onCardStateMessage((NotifyCardState) notifyMsg);
                        break;
                    case DRAW:
                        onDrawMessage((NotifyDraw) notifyMsg);
                        break;
                    case YOUR_TURN:
                        onNotifyTurnMessage((NotifyTurn) notifyMsg);
                        break;
                    case SET_UP_END:
                        onSetUpEndedMessage((NotifySetUpFinished) notifyMsg);
                        break;
                    case PLAY_CARD:
                        onCardPlayedMessage((NotifyCardPlayed) notifyMsg);
                        break;
                    case PLAYER_HAND:
                        onPlayerHandMessage((NotifyPlayerHand) notifyMsg);
                        break;
                    case COLOR_ASSIGNMENT:
                        onColorAssignmentMessage((NotifyColorsAssignment) notifyMsg);
                        break;
                    case STARTER_CARDS:
                        onStarterCardsMessage((NotifyStarterCards) notifyMsg);
                        break;
                    case END_MATCH:
                        onEndMatchMessage((NotifyEndMatch) notifyMsg);
                        break;
                    case LAST_ROUND:
                        onLastRoundMessage((NotifyLastRound) notifyMsg);
                        break;
                    case OBJECTIVE_CHOSEN:
                        onObjectiveConfirmationMessage((NotifyChosenObjective) notifyMsg);
                        break;
                    case STARTER_CHOSEN:
                        onStarterConfirmationMessage((NotifyChosenStarter) notifyMsg);
                        break;
                    case GLOBAL_OBJECTIVES:
                        onGlobalObjectiveMessage((NotifyGlobalObjectives) notifyMsg);
                        break;
                    default:
                        onUnknownNotifyMessage(notifyMsg);
                }
                break;
            default:
                onUnknownMessage(m);
        }
    }

    // callback methods, override to modify the behaviour
    protected void onMessage(Message m){}
    protected void onSuccessMessage(SuccessMessage m){}
    protected void onErrorMessage(ErrorMessage m){}
    protected void onGetLobbiesMessage(GetLobbiesResponseMessage m){}
    protected void onLobbyInfoMessage(LobbyInfoMessage m){}
    protected void onPlayerJoinedMessage(PlayerJoinedLobbyMessage m){}
    protected void onPlayerLeftMessage(PlayerLeftLobbyMessage m){}
    protected void onChatMessage(ChatMessage m){}
    protected void onGameStateMessage(GameStateMessage m) {}
    protected void onReconnectionMessage(GameReconnectionMessage m) {}
    protected void onReconnectionFailedMessage(FailedGameReconnectionMessage m) {}
    protected void onFailedToStartMatchMessage(FailedToStartMatchMessage m){}
    protected void onChooseStarterMessage(NotifyToChooseStarter notifyMsg){}
    protected void onChooseObjectiveMessage(NotifyToChooseObjective notifyMsg){}
    protected void onMatchStartedMessage(NotifyMatchStarted m){}
    protected void onCardStateMessage(NotifyCardState notifyMsg){}
    protected void onDrawMessage(NotifyDraw notifyMsg){}
    protected void onNotifyTurnMessage(NotifyTurn notifyMsg){}
    protected void onSetUpEndedMessage(NotifySetUpFinished notifyMsg){}
    protected void onCardPlayedMessage(NotifyCardPlayed notifyMsg){}
    protected void onPlayerHandMessage(NotifyPlayerHand notifyMsg){}
    protected void onColorAssignmentMessage(NotifyColorsAssignment notifyMsg){}
    protected void onStarterCardsMessage(NotifyStarterCards notifyMsg){}
    protected void onEndMatchMessage(NotifyEndMatch notifyMsg){}
    protected void onLastRoundMessage(NotifyLastRound notifyMsg){}
    protected void onObjectiveConfirmationMessage(NotifyChosenObjective notifyMsg){}
    protected void onStarterConfirmationMessage(NotifyChosenStarter notifyMsg){}
    protected void onGlobalObjectiveMessage(NotifyGlobalObjectives notifyMsg){}
    protected void onUnknownNotifyMessage(NotifyMessage m){}
    protected void onUnknownMessage(Message m){}

    /**
     * Handles the disconnection from the server.
     * This method should be implemented to manage any necessary cleanup or state adjustments
     * when the connection to the server is lost or intentionally terminated.
     */
    public abstract void disconnection();
}
