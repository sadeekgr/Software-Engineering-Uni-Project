package it.polimi.ingsw.network.server;

import it.polimi.ingsw.message.Message;
import it.polimi.ingsw.network.client.ClientRemote;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * The ServerRemote interface defines remote methods that a server can expose to clients
 * for communication in a distributed system.
 */
public interface ServerRemote extends Remote {

    /**
     * Receives a message from a client identified by UUID.
     *
     * @param uuid the unique identifier of the client
     * @param m the Message object received from the client
     * @throws RemoteException if there is a communication-related exception during the remote method call
     */
    void receive(String uuid, Message m) throws RemoteException;

    /**
     * Sets the callback interface for a client identified by UUID.
     *
     * @param callback the ClientRemote object representing the callback interface for the client
     * @return the UUID assigned to the client
     * @throws RemoteException if there is a communication-related exception during the remote method call
     */
    String setCallback(ClientRemote callback) throws RemoteException;

    /**
     * Sends a heartbeat signal to keep a client's connection alive.
     *
     * @param uuid the unique identifier of the client
     * @throws RemoteException if there is a communication-related exception during the remote method call
     */
    void heartbeat(String uuid) throws RemoteException;
}
