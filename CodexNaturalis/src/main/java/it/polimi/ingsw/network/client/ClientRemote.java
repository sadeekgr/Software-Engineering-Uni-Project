package it.polimi.ingsw.network.client;

import it.polimi.ingsw.message.Message;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * The ClientRemote interface represents the remote interface for a client in a distributed system.
 * Clients that implement this interface can receive messages sent remotely.
 *
 * <p>Implementations of this interface should handle the reception of {@link Message} objects.
 * Messages can be sent remotely to clients using this interface.
 */
public interface ClientRemote extends Remote {

    /**
     * Receives a message sent remotely.
     *
     * @param m the Message object received from the server
     * @throws RemoteException if there is a communication-related exception during the remote method call
     */
    void receive(Message m) throws RemoteException;
}
