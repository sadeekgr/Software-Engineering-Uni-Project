package it.polimi.ingsw.message.notify;

import it.polimi.ingsw.model.card.StarterCard;

/**
 * This class represents a notification message that is sent to prompt a player to choose a starter card.
 */
public class NotifyToChooseStarter extends NotifyMessage {
    StarterCard card;

    /**
     * Constructs a new NotifyToChooseStarter message to prompt the player to choose a starter card.
     *
     * @param c the starter card to choose from
     */
    public NotifyToChooseStarter(StarterCard c){
        card = c;
    }

    /**
     * Returns the starter card to choose from.
     *
     * @return the starter card to choose from
     */
    public StarterCard getStarter(){
        return card;
    }

    /**
     * Retrieves the type of this message, which is {@link NotifyType#CHOOSE_STARTER}.
     *
     * @return The message type, which is {@link NotifyType#CHOOSE_STARTER}.
     */
    @Override
    public NotifyType getNotifyType(){ return NotifyType.CHOOSE_STARTER; }
}
