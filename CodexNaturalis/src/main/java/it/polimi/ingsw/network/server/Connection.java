package it.polimi.ingsw.network.server;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.message.Message;

/**
 * The Connection interface represents a connection handler on the server side.
 * It defines methods for sending messages, managing user information, accessing controllers,
 * and closing the connection.
 */
public interface Connection {

    /**
     * Sends a message over the connection.
     *
     * @param m the Message object to be sent
     */
    void send(Message m);

    /**
     * Sets the username associated with this connection.
     *
     * @param username the username to set
     */
    void setUsername(String username);

    /**
     * Returns the username associated with this connection.
     *
     * @return the username
     */
    String getUsername();

    /**
     * Returns the Controller instance associated with this connection.
     *
     * @return the Controller instance
     */
    Controller getController();

    /**
     * Closes the connection.
     * Implementations should release any resources associated with the connection.
     */
    void close();
}
