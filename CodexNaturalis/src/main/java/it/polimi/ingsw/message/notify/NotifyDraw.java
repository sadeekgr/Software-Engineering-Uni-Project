package it.polimi.ingsw.message.notify;

import it.polimi.ingsw.model.card.PlayableCard;
import it.polimi.ingsw.model.card.Symbol;
import it.polimi.ingsw.model.player.PlayerColor;

/**
 * This class represents a notification message that is sent when a player draws a card.
 */
public class NotifyDraw extends NotifyMessage {
    private final PlayerColor who;
    private final DrawType where;
    private final int index;
    private final PlayableCard card;
    private final Symbol kingdom;

    /**
     * Constructs a new NotifyDraw message when a card is drawn from the resource or gold deck (used for the others players that didn't draw).
     *
     * @param who the player who drew the card
     * @param where the draw type indicating where the card was drawn from
     * @param kingdom the symbol representing the kingdom of the card
     */
    public NotifyDraw(PlayerColor who, DrawType where, Symbol kingdom) {
        this.who = who;
        this.where = where;
        this.kingdom = kingdom;
        index = -1;
        card = null;
    }

    /**
     * Constructs a new NotifyDraw message when a card is drawn (from the market).
     *
     * @param who the player who drew the card
     * @param where the draw type indicating where the card was drawn from
     * @param kingdom the symbol representing the kingdom of the card
     * @param index the index of the card in the market
     * @param card the card that was drawn
     */
    public NotifyDraw(PlayerColor who, DrawType where, Symbol kingdom, int index, PlayableCard card) {
        this.who = who;
        this.where = where;
        this.index = index;
        this.card = card;
        this.kingdom = kingdom;
    }

    /**
     * Constructs a new NotifyDraw message when a card is drawn from the resource or gold deck (used to show the card drawn to the player who draw).
     *
     * @param who the player who drew the card
     * @param where the draw type indicating where the card was drawn from
     * @param kingdom the symbol representing the kingdom
     * @param card the card that was drawn
     */
    public NotifyDraw(PlayerColor who, DrawType where, Symbol kingdom, PlayableCard card) {
        this.who = who;
        this.where = where;
        this.kingdom = kingdom;
        this.index = -1;
        this.card = card;
    }

    /**
     * Returns the player who drew the card.
     *
     * @return the player who drew the card
     */
    public PlayerColor getWho() {
        return who;
    }

    /**
     * Returns the draw type indicating where the card was drawn from.
     *
     * @return the draw type indicating where the card was drawn from
     */
    public DrawType getWhere() {
        return where;
    }

    /**
     * Returns the index of the card in the market.
     *
     * @return the index of the card in the market
     */
    public int getIndex() {
        return index;
    }

    /**
     * Returns the card that was drawn.
     *
     * @return the card that was drawn
     */
    public PlayableCard getCard() {
        return card;
    }

    /**
     * Returns the symbol representing the kingdom of the card.
     *
     * @return the symbol representing the kingdom of the card
     */
    public Symbol getKingdom(){return kingdom;}

    /**
     * Retrieves the type of this message, which is {@link NotifyType#DRAW}.
     *
     * @return The message type, which is {@link NotifyType#DRAW}.
     */
    @Override
    public NotifyType getNotifyType() {
        return NotifyType.DRAW;
    }

    /**
     * Enum representing the types of draws.
     */
    public enum DrawType {GOLD, RESOURCE, MARKET}











}
