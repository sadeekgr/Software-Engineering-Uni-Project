package it.polimi.ingsw.network.server;

import it.polimi.ingsw.message.Message;
import it.polimi.ingsw.network.client.ClientRemote;
import it.polimi.ingsw.network.Server;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The Stub class implements the ServerRemote interface for handling RMI communication
 * with clients. It manages client callbacks, message reception, and heartbeat monitoring.
 */
public class Stub extends UnicastRemoteObject implements ServerRemote {
    private static final Logger logger = Logger.getLogger(Stub.class.getName());
    private final long timeout = 20000;
    private final Map<String, Callback> clients;
    private final Map<String, Long> lastHeartbeatTimes;
    private boolean running;

    /**
     * Constructs a Stub instance.
     *
     * @throws RemoteException if there is an RMI-related exception
     */
    public Stub() throws RemoteException {
        super();
        clients = new HashMap<>();
        running = true;
        lastHeartbeatTimes = new HashMap<>();
    }

    /**
     * Generates a unique identifier combining a random UUID and current timestamp.
     *
     * @return a unique identifier string
     */
    private static String generateUniqueId() {
        // Generate a random UUID
        String uuid = UUID.randomUUID().toString();

        // Add timestamp to ensure uniqueness
        long timestamp = System.currentTimeMillis();

        // Combine timestamp and UUID

        return timestamp + "-" + uuid;
    }

    /**
     * Receives a message from a client identified by UUID and processes it using ServerMessageHandler.
     *
     * @param uuid the unique identifier of the client
     * @param m the Message object received from the client
     * @throws RemoteException if there is an RMI-related exception or if the client is not found
     */
    @Override
    public void receive(String uuid, Message m) throws RemoteException {
        if(!clients.containsKey(uuid)){
            throw new RemoteException("Client not found.");
        }

        ServerMessageHandler.handle(clients.get(uuid), m);
    }

    /**
     * Sets the callback interface for a new client connection and returns the assigned UUID.
     *
     * @param c the ClientRemote object representing the callback interface for the client
     * @return the UUID assigned to the client
     */
    @Override
    public String setCallback(ClientRemote c){
        logger.info("New RMI connection.");
        String uuid = generateUniqueId();
        clients.put(uuid, new Callback(c));
        return uuid;
    }

    /**
     * Sends a heartbeat signal to indicate that the client identified by UUID is still connected.
     *
     * @param uuid the unique identifier of the client
     * @throws RemoteException if there is an RMI-related exception or if the client is not found
     */
    @Override
    public void heartbeat(String uuid) throws RemoteException {
        logger.info(uuid + " heartbeat");
        if(!clients.containsKey(uuid)){
            throw new RemoteException("Client not found.");
        }

        lastHeartbeatTimes.put(uuid, System.currentTimeMillis());
    }

    /**
     * Monitors and handles disconnections of clients based on heartbeat timeouts.
     * Runs in a loop until stopped.
     */
    public void findDisconnectedClients() {
        while(running) {
            Set<String> disconnectedClients = new HashSet<>();
            long currentTime = System.currentTimeMillis();

            for (Map.Entry<String, Long> entry : lastHeartbeatTimes.entrySet()) {
                String clientUUID = entry.getKey();
                long lastHeartbeatTime = entry.getValue();
                long timeSinceLastHeartbeat = currentTime - lastHeartbeatTime;

                if (timeSinceLastHeartbeat > timeout) {
                    logger.log(Level.INFO, "Client " + clientUUID + " Disconnected");
                    disconnectedClients.add(clientUUID);
                    clients.get(clientUUID).getController().disconnected();
                }
            }

            for(String UUID : disconnectedClients) {
                Connection c = clients.get(UUID);
                Server.handleDisconnection(c);
                clients.remove(UUID);
                lastHeartbeatTimes.remove(UUID);
            }

            try {
                Thread.sleep(20000);
            } catch (InterruptedException e){
                logger.warning("Error while waiting to check disconnection");
            }
        }
    }

    /**
     * Stops the disconnection monitoring loop.
     */
    public void stop(){
        running = false;
    }
}
