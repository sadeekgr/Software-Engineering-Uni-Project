package it.polimi.ingsw.message.error;

import it.polimi.ingsw.message.Message;
import it.polimi.ingsw.message.MessageType;

/**
 * The {@code ErrorMessage} class serves as an abstract base class for all error messages
 * within the system. It implements the {@code Message} interface and provides a common
 * structure for handling error messages.
 */
public abstract class ErrorMessage implements Message {

    /**
     * Returns the type of the message, which is always {@code MessageType.ERROR}
     * for instances of {@code ErrorMessage}.
     *
     * @return the message type, {@code MessageType.ERROR}.
     */
    @Override
    public MessageType getType() {
        return MessageType.ERROR;
    }

    /**
     * Returns the specific error code associated with this error message.
     * Subclasses must provide the implementation for this method.
     *
     * @return the error code as an integer.
     */
    public abstract int getErrorCode();

    /**
     * Returns the specific error message associated with this error message.
     * Subclasses must provide the implementation for this method.
     *
     * @return a string describing the error.
     */
    public abstract String getErrorMessage();
}
