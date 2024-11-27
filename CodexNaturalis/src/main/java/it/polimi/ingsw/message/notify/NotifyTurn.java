package it.polimi.ingsw.message.notify;

import it.polimi.ingsw.model.player.PlayerColor;

/**
 * This class represents a notification message that is sent to inform a player about their turn.
 */
public class NotifyTurn extends NotifyMessage {
    private final PlayerColor color;

    /**
     * Constructs a new NotifyTurn message indicating whose turn it is.
     *
     * @param color the color of the player whose turn it is
     */
    public NotifyTurn(PlayerColor color) {
        this.color = color;
    }

    /**
     * Returns the color of the player whose turn it is.
     *
     * @return the color of the player whose turn it is
     */
    public PlayerColor getColor() {
        return color;
    }

    /**
     * Retrieves the type of this message, which is {@link NotifyType#YOUR_TURN}.
     *
     * @return The message type, which is {@link NotifyType#YOUR_TURN}.
     */
    @Override
    public NotifyType getNotifyType(){ return NotifyType.YOUR_TURN; }
}
