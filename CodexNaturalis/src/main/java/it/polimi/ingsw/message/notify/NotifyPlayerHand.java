package it.polimi.ingsw.message.notify;

import it.polimi.ingsw.model.card.PlayableCard;
import it.polimi.ingsw.model.card.Symbol;
import it.polimi.ingsw.model.player.PlayerColor;

import java.util.Map;

/**
 * This class represents a notification message that is sent to update a player's hand and back hands.
 */
public class NotifyPlayerHand extends NotifyMessage {
    private final PlayableCard[] hand;
    private final Map<PlayerColor, Symbol[]> backHands;

    /**
     * Constructs a new NotifyPlayerHand message.
     *
     * @param hand the array of playable cards in the player's hand
     * @param backHands a map containing each player's color mapped to their array of symbols representing back hands
     */
    public NotifyPlayerHand(PlayableCard[] hand, Map<PlayerColor, Symbol[]> backHands) {
        this.hand = hand;
        this.backHands = backHands;
    }

    /**
     * Returns the array of playable cards in the player's hand.
     *
     * @return the array of playable cards in the player's hand
     */
    public PlayableCard[] getHand(){
        return hand;
    }

    /**
     * Returns the map containing each player's color mapped to their array of symbols representing back hands.
     *
     * @return the map containing each player's color mapped to their array of symbols representing back hands
     */
    public Map<PlayerColor, Symbol[]> getBackHands() {
        return backHands;
    }

    /**
     * Retrieves the type of this message, which is {@link NotifyType#PLAYER_HAND}.
     *
     * @return The message type, which is {@link NotifyType#PLAYER_HAND}.
     */
    @Override
    public NotifyType getNotifyType(){ return NotifyType.PLAYER_HAND; }
}