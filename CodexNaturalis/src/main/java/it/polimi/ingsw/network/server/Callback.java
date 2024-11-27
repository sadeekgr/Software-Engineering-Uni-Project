package it.polimi.ingsw.network.server;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.message.Message;
import it.polimi.ingsw.network.client.ClientRemote;

import java.rmi.RemoteException;
import java.util.logging.Logger;

/**
 * The Callback class represents a server-side callback handler that interacts with a remote client.
 * It implements the Connection interface.
 */
public class Callback implements Connection {
    private static final Logger logger = Logger.getLogger(Callback.class.getName());
    private final ClientRemote client;
    private String username;
    private final Controller controller;

    /**
     * Constructs a Callback instance with the given ClientRemote instance.
     *
     * @param client the ClientRemote instance representing the remote client
     */
    public Callback(ClientRemote client) {
        this.client = client;
        this.controller = new Controller(this);
    }

    /**
     * Sends a message to the associated remote client.
     *
     * @param m the Message object to be sent
     */
    @Override
    public void send(Message m) {
        try {
            client.receive(m);
        } catch (RemoteException e){
            logger.warning("Error while sending message : " + e.getMessage());
        }
    }

    /**
     * Sets the username associated with this callback handler.
     *
     * @param username the username to set
     */
    @Override
    public void setUsername(String username) {
        if(username != null && this.username == null){
            this.username = username;
        }
    }

    /**
     * Returns the username associated with this callback handler.
     *
     * @return the username
     */
    @Override
    public String getUsername() {
        return username;
    }

    /**
     * Returns the Controller instance associated with this callback handler.
     *
     * @return the Controller instance
     */
    @Override
    public Controller getController() {
        return controller;
    }

    /**
     * Placeholder method for closing the callback handler.
     * Currently does nothing in this implementation.
     */
    @Override
    public void close() {}
}
