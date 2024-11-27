package it.polimi.ingsw.message.notify;

import it.polimi.ingsw.model.card.StarterCard;

/**
 * This class represents a notification message that is sent when a starter card is chosen.
 */
public class NotifyChosenStarter extends NotifyMessage {
    private final StarterCard card;
    private final boolean side;

    /**
     * Constructs a new NotifyChosenStarter message.
     *
     * @param card the chosen starter card
     * @param side the side of the card that is chosen
     */
    public NotifyChosenStarter(StarterCard card, boolean side){
        this.card = card;
        this.side = side;
    }

    /**
     * Returns the chosen starter card.
     *
     * @return the chosen starter card
     */
    public StarterCard getCard(){
        return card;
    }

    /**
     * Returns the side of the card that is chosen.
     *
     * @return the side of the card that is chosen
     */
    public boolean getSide() {
        return side;
    }

    /**
     * Retrieves the type of this message, which is {@link NotifyType#STARTER_CHOSEN}.
     *
     * @return The message type, which is {@link NotifyType#STARTER_CHOSEN}.
     */
    @Override
    public NotifyType getNotifyType(){ return NotifyType.STARTER_CHOSEN; }
}