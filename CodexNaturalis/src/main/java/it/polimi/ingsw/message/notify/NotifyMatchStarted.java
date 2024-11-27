package it.polimi.ingsw.message.notify;

/**
 * This class represents a notification message that is sent when a match has started.
 */
public class NotifyMatchStarted extends NotifyMessage {

    /**
     * Retrieves the type of this message, which is {@link NotifyType#MATCH_STARTED}.
     *
     * @return The message type, which is {@link NotifyType#MATCH_STARTED}.
     */
    @Override
    public NotifyType getNotifyType(){ return NotifyType.MATCH_STARTED; }
}