package it.polimi.ingsw.network.client;

import it.polimi.ingsw.message.Message;
import it.polimi.ingsw.network.server.ServerRemote;
import it.polimi.ingsw.view.View;

import java.rmi.NoSuchObjectException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.logging.Logger;

/**
 * The Skeleton class represents a client-side implementation of both ClientRemote and Connection interfaces
 * for interacting with a remote server.
 */
public class Skeleton extends UnicastRemoteObject implements ClientRemote, Connection {
    private static final Logger logger = Logger.getLogger(Skeleton.class.getName());
    private final ServerRemote server;
    private final String uuid;
    private final View view;
    private boolean running;

    /**
     * Constructs a Skeleton instance with the given ServerRemote instance and View.
     *
     * @param s     the ServerRemote instance to communicate with
     * @param view  the View instance associated with this Skeleton
     * @throws RemoteException if an RMI-related exception occurs
     */
    public Skeleton(ServerRemote s, View view) throws RemoteException {
        super();
        if (s == null){
            throw new RemoteException("Server not set");
        }
        server = s;
        uuid = s.setCallback(this);
        this.view = view;
        running = true;
    }

    /**
     * Receives a message from the server and updates the associated view.
     *
     * @param m the Message received from the server
     * @throws RemoteException if there is a communication-related exception during the remote method call
     */
    @Override
    public void receive(Message m) throws RemoteException {
        view.update(m);
    }

    /**
     * Sends a message to the server.
     *
     * @param m the Message to be sent
     */
    @Override
    public void send(Message m) {
        try {
            server.receive(uuid, m);
        } catch (RemoteException e){
            logger.warning("Failed to send message: "+ e.getMessage());
        }
    }

    /**
     * Closes the connection and stops the Skeleton instance.
     */
    @Override
    public void close(){
        running = false;
        try {
            UnicastRemoteObject.unexportObject(this, true);
        } catch (NoSuchObjectException e) {
            throw new RuntimeException("Failed to close the connection: " + e.getMessage());
        }
    }

    /**
     * Initiates a heartbeat mechanism to maintain the connection with the server.
     * If a RemoteException or InterruptedException occurs, the running status is set to false,
     * and the view's disconnection method is called.
     */
    public void heartBeat(){
        while(running) {
            try {
                server.heartbeat(uuid);
                Thread.sleep(5000);
            } catch (RemoteException | InterruptedException e) {
                // server disconnected
                logger.warning("Lost connection with server: " + e.getMessage());
                running = false;
                view.disconnection();
            }
        }
    }
}
