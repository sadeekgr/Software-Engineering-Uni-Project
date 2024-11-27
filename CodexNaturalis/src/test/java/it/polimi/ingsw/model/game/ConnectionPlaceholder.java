package it.polimi.ingsw.model.game;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.message.Message;
import it.polimi.ingsw.network.server.Connection;

/**
 * A placeholder implementation of the Connection interface used for testing.
 * This class initializes a Controller instance and provides stubs for connection-related methods.
 * It is intended for testing purposes to simulate basic connection behavior.
 */
public class ConnectionPlaceholder implements Connection {
    private final Controller c;

    /**
     * Constructs a new ConnectionPlaceholder instance for testing.
     * Initializes a Controller with a reference to this connection.
     */
    public ConnectionPlaceholder(){
        c = new Controller(this);
    }

    @Override
    public void send(Message m) {}

    @Override
    public void setUsername(String username){}

    @Override
    public String getUsername(){ return ""; }

    @Override
    public void close(){}

    @Override
    public Controller getController(){
        return c;
    }
}
