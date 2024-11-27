package it.polimi.ingsw.message.notify;

import it.polimi.ingsw.model.card.StarterCard;
import it.polimi.ingsw.model.player.PlayerColor;

import java.util.List;

/**
 * This class represents a notification message that is sent to inform players about the starter cards chosen by each player.
 */
public class NotifyStarterCards extends NotifyMessage {
    private final List<PlayerColor> colors;
    private final List<StarterCard> starterCards;
    private final List<Boolean> sides;

    /**
     * Constructs a new NotifyStarterCards message.
     *
     * @param colors the list of player colors indicating the players involved
     * @param starterCards the list of starter cards chosen by each player
     * @param sides the list indicating the sides chosen for each starter card
     */
    public NotifyStarterCards(List<PlayerColor> colors, List<StarterCard> starterCards, List<Boolean> sides) {
        this.colors = colors;
        this.starterCards = starterCards;
        this.sides = sides;
    }

    /**
     * Returns the list of player colors involved in the starter card selection.
     *
     * @return the list of player colors involved in the starter card selection
     */
    public List<PlayerColor> getColors() {
        return colors;
    }

    /**
     * Returns the list of starter cards chosen by each player.
     *
     * @return the list of starter cards chosen by each player
     */
    public List<StarterCard> getStarterCards() {
        return starterCards;
    }

    /**
     * Returns the list indicating the sides chosen for each starter card.
     *
     * @return the list indicating the sides chosen for each starter card
     */
    public List<Boolean> getSides() {
        return sides;
    }

    /**
     * Retrieves the type of this message, which is {@link NotifyType#STARTER_CARDS}.
     *
     * @return The message type, which is {@link NotifyType#STARTER_CARDS}.
     */
    @Override
    public NotifyType getNotifyType() {
        return NotifyType.STARTER_CARDS;
    }
}
