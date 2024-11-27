package it.polimi.ingsw.network.server;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.message.Message;
import it.polimi.ingsw.network.Server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The SocketConnection class represents a server-side socket-based connection handler
 * that manages communication with a client.
 * It implements the Connection interface.
 */
public class SocketConnection implements Connection {
    private static final Logger logger = Logger.getLogger(SocketConnection.class.getName());
    private final Socket socket;
    private final ObjectInputStream inputStream;
    private final ObjectOutputStream outputStream;
    private String username;
    private final Controller controller;
    private volatile boolean running;

    /**
     * Constructs a SocketConnection instance with the given Socket.
     *
     * @param s the Socket instance representing the connection to the client
     * @throws IOException if an I/O error occurs when creating the input or output stream, or if the socket is closed
     */
    public SocketConnection(Socket s) throws IOException {
        socket = s;
        outputStream = new ObjectOutputStream(socket.getOutputStream());
        inputStream = new ObjectInputStream(socket.getInputStream());
        controller = new Controller(this);
    }

    /**
     * Sends a message over the socket connection to the client.
     *
     * @param m the Message object to be sent
     */
    @Override
    public void send(Message m) {
        try {
            logger.info("Send message to " + username + ": " + m.getType());
            outputStream.writeObject(m);
            outputStream.flush();
        } catch (IOException e){
            logger.warning("Error sending message : " + e.getMessage());
        }
    }

    /**
     * Sets the username associated with this connection.
     *
     * @param username the username to set
     */
    @Override
    public void setUsername(String username){
        if(username != null && this.username == null){
            this.username = username;
        }
    }

    /**
     * Returns the username associated with this connection.
     *
     * @return the username
     */
    @Override
    public String getUsername(){
        return username;
    }

    /**
     * Starts listening for incoming messages from the client.
     * This method runs in a loop until the connection is closed or an error occurs.
     */
    public void start(){
        running = true;
        try {
            Message message;
            while (running && (message = (Message) inputStream.readObject()) != null) {
                ServerMessageHandler.handle(this, message);
            }
        } catch (ClassNotFoundException | IOException e) {
            logger.log(Level.INFO, "Disconnected");
        } finally {
            try {
                controller.disconnected();
                Server.handleDisconnection(this);
                socket.close();
            } catch (IOException e) {
                logger.log(Level.WARNING, "Error closing socket", e);
            }
        }
    }

    /**
     * Returns the Controller instance associated with this connection.
     *
     * @return the Controller instance
     */
    @Override
    public Controller getController(){
        return controller;
    }

    /**
     * Closes the socket connection and stops the SocketConnection instance.
     */
    @Override
    public void close() {
        running = false;
        try {
            send(null); //
        } catch (Exception e){
            logger.warning("Error while closing connection : " + e.getMessage());
        }
    }
}
