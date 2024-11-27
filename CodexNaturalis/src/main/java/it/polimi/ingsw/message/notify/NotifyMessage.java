package it.polimi.ingsw.message.notify;

import it.polimi.ingsw.message.Message;
import it.polimi.ingsw.message.MessageType;

/**
 * Abstract class representing a notification message.
 */
public abstract class NotifyMessage implements Message {

    /**
     * Returns the type of the message, which is always {@link MessageType#INFO}.
     *
     * @return The message type, which is {@link MessageType#INFO}.
     */
    @Override
    public MessageType getType(){ return MessageType.INFO; }

    /**
     * Abstract method to be implemented by subclasses to retrieve the specific type of notification.
     *
     * @return The specific type of notification.
     */
    abstract public NotifyType getNotifyType();
}
