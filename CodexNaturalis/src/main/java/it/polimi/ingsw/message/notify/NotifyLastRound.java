package it.polimi.ingsw.message.notify;

/**
 * This class represents a notification message that is sent to indicate the last round of the game.
 */
public class NotifyLastRound extends NotifyMessage{

    /**
     * Retrieves the type of this message, which is {@link NotifyType#LAST_ROUND}.
     *
     * @return The message type, which is {@link NotifyType#LAST_ROUND}.
     */
    @Override
    public NotifyType getNotifyType() {
        return NotifyType.LAST_ROUND;
    }
}
