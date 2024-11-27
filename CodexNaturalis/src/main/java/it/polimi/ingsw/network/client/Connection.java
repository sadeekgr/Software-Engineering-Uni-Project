package it.polimi.ingsw.network.client;

import it.polimi.ingsw.message.Message;

/**
 * The Connection interface represents a connection to a server in a networked environment.
 * It provides methods to send messages and to close the connection.
 */
public interface Connection {

    /**
     * Sends a message over the connection to the server.
     *
     * @param m the Message object to be sent
     */
    void send(Message m);

    /**
     * Closes the connection.
     */
    void close();
}
