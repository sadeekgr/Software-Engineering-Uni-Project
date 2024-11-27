package it.polimi.ingsw.message.action;

import it.polimi.ingsw.message.Message;
import it.polimi.ingsw.message.MessageType;

/**
 * An abstract class representing a game-related message.
 * This class implements the {@link Message} interface and provides common functionality
 * for messages related to game actions.
 */
public abstract class GameMessage implements Message {

    /**
     * Returns the specific game action associated with this message.
     *
     * @return The game action associated with this message.
     */
    abstract public GameAction getAction();

    /**
     * Returns the type of this message, which is {@link MessageType#GAME}.
     *
     * @return The type of this message.
     */
    @Override
    public MessageType getType() {
        return MessageType.GAME;
    }
}
