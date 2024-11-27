package it.polimi.ingsw.message.notify;

import it.polimi.ingsw.model.card.PlayableCard;
import it.polimi.ingsw.model.card.Symbol;

/**
 * This class represents a notification message that is sent to update the state of the cards in the game.
 */
public class NotifyCardState extends NotifyMessage {
    private final PlayableCard[] market;
    private final Symbol resourceKingdom;
    private final Symbol goldKingdom;

    /**
     * Constructs a new NotifyCardState message.
     *
     * @param market the array of cards in the market
     * @param resourceKingdom the symbol representing the resource kingdom
     * @param goldKingdom the symbol representing the gold kingdom
     */
    public NotifyCardState(PlayableCard[] market, Symbol resourceKingdom, Symbol goldKingdom){
        this.market = market;
        this.goldKingdom = goldKingdom;
        this.resourceKingdom = resourceKingdom;
    }

    /**
     * Returns the array of cards in the market.
     *
     * @return the array of cards in the market
     */
    public PlayableCard[] getMarket() {
        return market;
    }

    /**
     * Returns the symbol representing the resource kingdom.
     *
     * @return the symbol representing the resource kingdom
     */
    public Symbol getResourceKingdom() {
        return resourceKingdom;
    }

    /**
     * Returns the symbol representing the gold kingdom.
     *
     * @return the symbol representing the gold kingdom
     */
    public Symbol getGoldKingdom() {
        return goldKingdom;
    }

    /**
     * Retrieves the type of this message, which is {@link NotifyType#CARD_STATE}.
     *
     * @return The message type, which is {@link NotifyType#CARD_STATE}.
     */
    @Override
    public NotifyType getNotifyType(){ return NotifyType.CARD_STATE; }
}
