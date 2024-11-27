package it.polimi.ingsw.network;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.exception.*;
import it.polimi.ingsw.lobby.Lobby;
import it.polimi.ingsw.lobby.LobbyInfo;
import it.polimi.ingsw.message.FailedGameReconnectionMessage;
import it.polimi.ingsw.message.GameReconnectionMessage;
import it.polimi.ingsw.message.lobby.PlayerJoinedLobbyMessage;
import it.polimi.ingsw.network.server.Connection;
import it.polimi.ingsw.network.server.Stub;
import it.polimi.ingsw.network.server.SocketConnection;

import java.io.*;
import java.lang.reflect.Type;
import java.net.ServerSocket;
import java.net.Socket;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * The server class manages game connections and lobbies, handling client interactions via both socket and RMI protocols.
 * It supports functionalities such as client login, lobby creation and joining, game management, and reconnections.
 */
public class Server {
    private static final Logger logger = Logger.getLogger(Server.class.getName());

    private static String serverIp = null;
    private static final int SOCKET_PORT = 13337;
    private static final int RMI_PORT = 33445;
    private static final int THREAD_POOL_SIZE = 10;
    private static final Map<String, Connection> clients = new HashMap<>();
    private static final Map<String, Integer> gameId = new HashMap<>();
    private static final Map<Integer, Lobby> lobbies = new HashMap<>();
    private static final Object lockId = new Object();
    private static int nextId = 0;

    private static Map<Integer, List<String>> interruptedGames = null;
    private static final Map<Integer, List<Controller>> interruptedClients = new HashMap<>();

    /**
     * Main method to start the server.
     * Initializes the server with optional command-line arguments to specify the server IP address.
     * Loads interrupted games, starts socket communication, configures RMI settings, and registers
     * the server in the RMI registry.
     *
     * @param args Command line arguments. Use "-ip:ip_address" to specify the server IP address.
     */
    public static void main(String[] args) {
        // Check command line arguments
        for (String arg : args) {
            if(arg.startsWith("-ip:")){
                serverIp = arg.substring(4);
            } else {
                logger.warning("Unknown argument: " + arg);
            }
        }

        if(serverIp == null)
            serverIp =  "127.0.0.1";

        loadInterruptedGames();
        if(interruptedGames != null) {
            if(interruptedGames.keySet().stream().max(Integer::compare).isPresent()){
                nextId = interruptedGames.keySet().stream().max(Integer::compare).get() + 1;
            }
        }

        new Thread(Server::serverSocket).start();

        System.setProperty("java.rmi.server.hostname", serverIp);
        startRMIRegistry();
        registerRMIServer();
    }

    /**
     * Starts a server socket and listens for incoming connections.
     * When a client connects, it creates a new instance of SocketConnection
     * and handles the connection in a separate thread.
     */
    private static void serverSocket() {
        try (ExecutorService executorService = Executors.newFixedThreadPool(THREAD_POOL_SIZE);
             ServerSocket serverSocket = new ServerSocket(SOCKET_PORT)) {

            logger.info("Server listening on port " + SOCKET_PORT);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                logger.info("New connection from " + clientSocket.getInetAddress().getHostAddress());

                // Create a new instance of ConnectionHandler for each connection
                SocketConnection conn = new SocketConnection(clientSocket);

                // Execute the connection handler in a separate thread
                executorService.execute(new Thread(conn::start));
            }
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Exception occurred in serverSocket", e);
        }
    }

    /**
     * Starts the RMI registry on the specified port.
     */
    private static void startRMIRegistry() {
        try {
            // Create or obtain a reference to the RMI registry on the specified port
            LocateRegistry.createRegistry(RMI_PORT);
            logger.info("RMI registry started on port " + RMI_PORT);
        } catch (RemoteException e) {
            logger.log(Level.SEVERE, "Exception occurred in startRMIRegistry", e);
        }
    }

    /**
     * Registers the RMI server with the RMI registry.
     * Also starts a thread to handle reconnections for disconnected clients.
     */
    private static void registerRMIServer() {
        try {
            Stub server = new Stub();
            new Thread(server::findDisconnectedClients).start();
            Registry registry = LocateRegistry.getRegistry(RMI_PORT);
            registry.rebind("RMIServer", server);

        } catch (RemoteException e) {
            logger.log(Level.SEVERE, "RemoteException occurred in registerRMIServer", e);
        }
    }

    /**
     * Generates a unique lobby ID in a thread-safe manner.
     *
     * @return The generated lobby ID.
     */
    private static int generateLobbyId() {
        synchronized (lockId) {
            return nextId++;
        }
    }

    /**
     * Allows a client to log in to the server.
     *
     * @param username The username of the client.
     * @param conn     The connection object associated with the client.
     * @throws NetworkExceptions If the client is already logged in or if the username is not available.
     */
    public static boolean login(String username, Connection conn) throws NetworkExceptions {
        synchronized (clients) {
            if (clients.containsKey(username)) {
                if (clients.get(username).equals(conn)) {
                    throw new NetworkExceptions(NetworkExceptions.ErrorCode.CLIENT_ALREADY_LOGGED, "The client is already logged!");
                } else {
                    throw new NetworkExceptions(NetworkExceptions.ErrorCode.USERNAME_NOT_AVAILABLE, "Username not available!");
                }
            }
            conn.setUsername(username);
            clients.put(username, conn);

            // Check if user is part of an interrupted game
            if(interruptedGames != null) {
                for (int id : interruptedGames.keySet()) {
                    if(interruptedGames.get(id).contains(username)){
                        conn.send(new GameReconnectionMessage());

                        interruptedGames.get(id).remove(username);
                        if(!interruptedClients.containsKey(id)){
                            interruptedClients.put(id, new ArrayList<>());
                        }
                        interruptedClients.get(id).add(conn.getController());
                        if(interruptedGames.get(id).isEmpty()){
                            interruptedGames.remove(id);
                            Lobby lobby = null;
                            try {
                                lobby = new Lobby(id, interruptedClients.get(id));
                            } catch (IOException e) {
                                logger.log(Level.SEVERE, "Reconnection failed", e);
                                conn.send(new FailedGameReconnectionMessage());
                            }
                            interruptedClients.remove(id);

                            if(lobby != null) {
                                for (String name : lobby.getPlayers()) {
                                    gameId.put(name, id);
                                }
                            }

                            lobbies.put(id, lobby);
                        }
                        return false;
                    }
                }
            }
        }

        return true;
    }

    /**
     * Allows a client to join a lobby.
     *
     * @param username The username of the client.
     * @param id       The ID of the lobby.
     * @param c        The controller associated with the client.
     * @throws NetworkExceptions If there is a network-related error.
     * @throws MatchExceptions   If there is an error related to the match.
     */
    public static Lobby joinLobby(String username, int id, Controller c) throws NetworkExceptions, MatchExceptions {
        synchronized (gameId) {
            if (gameId.containsKey(username)) {
                throw new NetworkExceptions(NetworkExceptions.ErrorCode.PLAYER_ALREADY_IN_GAME, "Player " + username + " is playing in another game.");
            }

            if (!lobbies.containsKey(id)) {
                throw new NetworkExceptions(NetworkExceptions.ErrorCode.GAME_NOT_FOUND, "Game id " + id + " doesn't exist.");
            }

            Lobby lobby = lobbies.get(id);
            List<String> usernames = lobby.getPlayers();
            for(String user : usernames){
                clients.get(user).send(new PlayerJoinedLobbyMessage(username));
            }

            lobby.join(username, c);
            gameId.put(username, id);

            saveGameId();

            return lobby;
        }
    }

    /**
     * Creates a new lobby and allows a client to join it.
     *
     * @param username   The username of the client.
     * @param numPlayers The number of players for the match.
     * @param c          The controller associated with the client.
     * @throws NetworkExceptions If there is a network-related error.
     * @throws MatchExceptions   If there is an error related to the match.
     */
    public static Lobby createLobby(String username, int numPlayers, Controller c) throws NetworkExceptions, MatchExceptions {
        if(numPlayers < 2 || numPlayers > 4){
            throw new NetworkExceptions(NetworkExceptions.ErrorCode.INVALID_GAME_PARAMETER, "Invalid game parameter!");
        }

        synchronized (gameId) {
            if (gameId.containsKey(username)) {
                throw new NetworkExceptions(NetworkExceptions.ErrorCode.PLAYER_ALREADY_IN_GAME, "Player " + username + " is playing in another game.");
            }

            int id = generateLobbyId();
            Lobby lobby = new Lobby(id, numPlayers);

            lobby.join(username, c);

            gameId.put(username, id);
            lobbies.put(id, lobby);

            saveGameId();

            return lobby;
        }
    }


    /**
     * Allows a client to leave a lobby.
     *
     * @param username The username of the client.
     * @param c        The controller associated with the client.
     * @throws NetworkExceptions If there is a network-related error.
     * @throws MatchExceptions   If there is an error related to the match.
     */
    public static void leaveLobby(String username, Controller c) throws NetworkExceptions, MatchExceptions {
        synchronized (gameId) {
            if (!gameId.containsKey(username)) {
                throw new NetworkExceptions(NetworkExceptions.ErrorCode.PLAYER_NOT_IN_GAME, "Player " + username + " is playing in any games.");
            }

            int id = gameId.remove(username);
            Lobby lobby = lobbies.get(id);
            lobby.leave(username, c);
            if (lobby.isEmpty()) {
                lobbies.remove(id);
            }

            saveGameId();
        }
    }

    /**
     * Retrieves non-started lobbies.
     *
     * @return List of non-started lobbies.
     */
    public static List<LobbyInfo> getNonStartedLobbies() {
        return lobbies.entrySet().stream()
                .parallel()
                .filter(entry -> !entry.getValue().isStarted())
                .map(map -> new LobbyInfo(map.getKey(), map.getValue().getCurrentPlayersNum(), map.getValue().getMaxPlayersNum()))
                .collect(Collectors.toList());
    }

    /**
     * Gets the lobby associated with a client.
     *
     * @param username The username of the client.
     * @return The lobby object associated with the client.
     */
    public static Lobby getLobby(String username) {
        if (!gameId.containsKey(username)) {
            return null;
        }
        return lobbies.get(gameId.get(username));
    }

    /**
     * Handles disconnection of a client.
     * Terminates ongoing matches if applicable and removes the client from active connections.
     *
     * @param c The Connection object representing the client.
     */
    public static void handleDisconnection(Connection c){
        String username = c.getUsername();
        if(username != null && clients.containsKey(username) && clients.get(username) == c){
            clients.remove(username);
            Lobby lobby = getLobby(username);
            if(lobby != null){
                if(lobby.isStarted()){
                    lobby.terminateMatch();
                } else {
                    try {
                        leaveLobby(username, c.getController());
                    } catch (MatchExceptions | NetworkExceptions e){
                        logger.warning("Failed to leave lobby : " + e.getMessage());
                    }
                }
            }
        }
    }

    /**
     * Handles the end of a match in a lobby.
     * Cleans up resources associated with the match, including removing entries from active game lists and deleting associated files.
     *
     * @param lobby The Lobby object representing the lobby where the match ended.
     */
    public static void matchEnded(Lobby lobby){
        List<String> players = lobby.getPlayers();
        lobbies.remove(gameId.get(players.getFirst()));
        for(String username : players){
            gameId.remove(username);
        }
        saveGameId();

        // Deleting the file
        File directory = new File("interruptedGames");
        if (!directory.exists() || !directory.isDirectory()) {
            logger.warning("Directory does not exist: " + directory.getAbsolutePath());
            return;
        }

        File file = new File(directory, lobby.getId() + ".json");
        if (file.exists() && file.isFile()) {
            if (!file.delete()) {
                logger.warning("Failed to delete file: " + file.getAbsolutePath());
            }
        } else {
            logger.warning("File does not exist or is not a file: " + file.getAbsolutePath());
        }
    }

    /**
     * Saves the current game IDs and associated players to a JSON file.
     * This method is used to persist interrupted games for potential reconnections.
     */
    private static void saveGameId(){
        HashMap<Integer, List<String>> games = new HashMap<>();
        synchronized (gameId) {
            for(String key : gameId.keySet()){
                if(lobbies.get(gameId.get(key)).getMaxPlayersNum() == lobbies.get(gameId.get(key)).getCurrentPlayersNum()) {
                    if (!games.containsKey(gameId.get(key))) {
                        games.put(gameId.get(key), new ArrayList<>());
                    }
                    games.get(gameId.get(key)).add(key);
                }
            }
        }

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        File directory = new File("interruptedGames");

        // Create directory if it doesn't exist
        if (!directory.exists()) {
            boolean created = directory.mkdirs();
            if (!created) {
                logger.warning("Failed to create directory: " + directory.getPath());
                return;
            }
        }

        File file = new File(directory, "info.json");

        // Create file if it doesn't exist
        try {
            if (!file.exists()) {
                boolean created = file.createNewFile();
                if (!created) {
                    logger.warning("Failed to create file: " + file.getPath());
                    return;
                }
            }
        } catch (IOException e) {
            logger.warning("Failed to create file: " + e.getMessage());
            return;
        }

        // Write data to the file
        try (FileWriter writer = new FileWriter(file)) {
            gson.toJson(games, writer); // Assuming 'games' is your data structure to be serialized
            logger.info("Successfully saved active games to file: " + file.getPath());
        } catch (IOException e) {
            logger.warning("Failed to save active games: " + e.getMessage());
        }
    }

    /**
     * Loads interrupted games and associated players from a JSON file into memory.
     * This method is called during server startup to resume interrupted game states.
     */
    private static void loadInterruptedGames(){
        Gson gson = new Gson();
        try (FileReader reader = new FileReader("interruptedGames/info.json")) {
            Type mapType = new TypeToken<HashMap<Integer, List<String>>>(){}.getType();
            interruptedGames = gson.fromJson(reader, mapType);
        } catch (FileNotFoundException e) {
            logger.warning("File not found: " + e.getMessage());
        } catch (IOException e) {
            logger.warning("Failed to load interrupted games: " + e.getMessage());
        }
    }
}