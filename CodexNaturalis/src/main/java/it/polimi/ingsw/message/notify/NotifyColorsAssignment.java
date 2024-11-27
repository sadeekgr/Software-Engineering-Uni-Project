package it.polimi.ingsw.message.notify;

import it.polimi.ingsw.model.player.PlayerColor;

import java.util.Map;

/**
 * This class represents a notification message that is sent to assign colors to players.
 */
public class NotifyColorsAssignment extends NotifyMessage{
    private final Map<String, PlayerColor> colors;

    /**
     * Constructs a new NotifyColorsAssignment message.
     *
     * @param colors a map containing player names as keys and their assigned colors as values
     */
    public NotifyColorsAssignment(Map<String, PlayerColor> colors) {
        this.colors = colors;
    }

    /**
     * Returns the map of player colors assignments.
     *
     * @return the map of player colors assignments
     */
    public Map<String, PlayerColor> getColors() {
        return colors;
    }

    /**
     * Retrieves the type of this message, which is {@link NotifyType#COLOR_ASSIGNMENT}.
     *
     * @return The message type, which is {@link NotifyType#COLOR_ASSIGNMENT}.
     */
    @Override
    public NotifyType getNotifyType() {
        return NotifyType.COLOR_ASSIGNMENT;
    }
}
