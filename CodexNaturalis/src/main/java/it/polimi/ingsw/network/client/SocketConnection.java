package it.polimi.ingsw.network.client;

import it.polimi.ingsw.message.Message;
import it.polimi.ingsw.view.View;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The SocketConnection class represents a client-side socket-based connection handler
 * that communicates with a server. It implements the Connection interface.
 */
public class SocketConnection implements Connection {
    private static final Logger logger = Logger.getLogger(SocketConnection.class.getName());
    private final Socket socket;
    private final ObjectInputStream inputStream;
    private final ObjectOutputStream outputStream;
    private volatile boolean running;
    private final View view;

    /**
     * Constructs a SocketConnection instance with the given Socket and View.
     *
     * @param s     the Socket instance representing the connection to the server
     * @param view  the View instance associated with this connection
     * @throws IOException if an I/O error occurs when creating the input or output stream, or if the socket is closed
     */
    public SocketConnection(Socket s, View view) throws IOException {
        socket = s;
        inputStream = new ObjectInputStream(socket.getInputStream());
        outputStream = new ObjectOutputStream(socket.getOutputStream());
        this.view = view;
    }

    /**
     * Sends a message over the socket connection to the server.
     *
     * @param m the Message object to be sent
     */
    @Override
    public void send(Message m) {
        try {
            outputStream.writeObject(m);
            outputStream.flush();
        } catch (IOException e){
            logger.log(Level.SEVERE, "Error sending message" + e.getMessage() + "\r\n");
        }
    }

    /**
     * Starts listening for incoming messages from the server.
     * This method runs in a loop until the connection is closed or an error occurs.
     */
    public void start(){
        running = true;
        try {
            Message message;
            while (running && (message = (Message) inputStream.readObject()) != null) {
                view.update(message);
            }
        } catch (EOFException e) {
            logger.log(Level.INFO, "Disconnected");
            //view.disconnection();
        } catch (IOException | ClassNotFoundException e) {
            // server disconnected
            logger.log(Level.SEVERE, "Error in connection handling");
            view.disconnection();
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                logger.log(Level.WARNING, "Error closing socket");
            }
        }
    }

    /**
     * Closes the socket connection and stops the SocketConnection instance.
     */
    @Override
    public void close() {
        running = false;
        try {
            send(null);
        } catch (Exception e){
            logger.warning(e.getMessage());
        }
    }
}
