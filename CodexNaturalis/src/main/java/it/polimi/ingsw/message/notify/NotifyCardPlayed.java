package it.polimi.ingsw.message.notify;

import it.polimi.ingsw.model.card.PlayableCard;
import it.polimi.ingsw.model.field.Position;
import it.polimi.ingsw.model.player.PlayerColor;

/**
 * This class represents a notification message that is sent when a card is played.
 */
public class NotifyCardPlayed extends NotifyMessage {
    private final PlayerColor who;
    private final int index;
    private final PlayableCard card;
    private final Position position;
    private final boolean side;
    private final int score;


    /**
     * Constructs a new NotifyCardPlayed message.
     *
     * @param who the color of the player who played the card
     * @param index the index of the card in the player's hand
     * @param card the card that was played
     * @param position the position where the card was played
     * @param side the side of the card that was played
     * @param score the score after the card was played
     */
    public NotifyCardPlayed(PlayerColor who, int index, PlayableCard card, Position position, boolean side, int score) {
        this.who = who;
        this.index = index;
        this.card = card;
        this.position = position;
        this.side = side;
        this.score = score;
    }

    /**
     * Returns the color of the player who played the card.
     *
     * @return the color of the player who played the card
     */
    public PlayerColor getWho() {
        return who;
    }

    /**
     * Returns the index of the card in the player's hand.
     *
     * @return the index of the card in the player's hand
     */
    public int getIndex() {
        return index;
    }

    /**
     * Returns the card that was played.
     *
     * @return the card that was played
     */
    public PlayableCard getCard() {
        return card;
    }

    /**
     * Returns the position where the card was played.
     *
     * @return the position where the card was played
     */
    public Position getPosition() {
        return position;
    }

    /**
     * Returns the side of the card that was played.
     *
     * @return the side of the card that was played
     */
    public boolean getSide() {
        return side;
    }

    /**
     * Returns the score after the card was played.
     *
     * @return the score after the card was played
     */
    public int getScore() {
        return score;
    }

    /**
     * Retrieves the type of this message, which is {@link NotifyType#PLAY_CARD}.
     *
     * @return The message type, which is {@link NotifyType#PLAY_CARD}.
     */
    @Override
    public NotifyType getNotifyType() {
        return NotifyType.PLAY_CARD;
    }
}
