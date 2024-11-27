package it.polimi.ingsw.message.notify;

import it.polimi.ingsw.model.player.PlayerColor;

import java.util.List;

/**
 * This class represents a notification message that is sent when the game setup has finished.
 */
public class NotifySetUpFinished extends NotifyMessage {

    private final List<PlayerColor> colors;

    /**
     * Constructs a new NotifySetUpFinished message.
     *
     * @param colors the list of player colors indicating the players involved in the game setup
     */
    public NotifySetUpFinished(List<PlayerColor> colors) {
        this.colors = colors;
    }

    /**
     * Returns the list of player colors involved in the game setup.
     *
     * @return the list of player colors involved in the game setup
     */
    public List<PlayerColor> getColors() {
        return colors;
    }

    /**
     * Retrieves the type of this message, which is {@link NotifyType#SET_UP_END}.
     *
     * @return The message type, which is {@link NotifyType#SET_UP_END}.
     */
    @Override
    public NotifyType getNotifyType(){ return NotifyType.SET_UP_END; }
}
