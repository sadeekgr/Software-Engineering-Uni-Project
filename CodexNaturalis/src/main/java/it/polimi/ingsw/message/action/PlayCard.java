package it.polimi.ingsw.message.action;

import it.polimi.ingsw.model.field.Position;

/**
 * Represents a message to play a card in the game.
 * Extends GameMessage to include specific details like card index, position, and side.
 */
public class PlayCard extends GameMessage {
    private final int cardIndex;
    private final Position position;
    private final boolean side;

    /**
     * Constructs a PlayCard message with the specified card index, position, and side.
     *
     * @param cardIndex The index of the card to play.
     * @param position  The position on the field where the card will be played.
     * @param side      The side (front or back) of the card to play.
     */
    public PlayCard(int cardIndex, Position position, boolean side){
        this.cardIndex = cardIndex;
        this.position = position;
        this.side = side;
    }

    /**
     * Gets the index of the card to play.
     *
     * @return The card index.
     */
    public int getCardIndex(){
        return cardIndex;
    }

    /**
     * Gets the position on the field where the card will be played.
     *
     * @return The position on the field.
     */
    public Position getPosition(){
        return position;
    }

    /**
     * Gets the side (front or back) of the card to play.
     *
     * @return The side of the card.
     */
    public boolean getSide(){
        return side;
    }

    /**
     * Gets the action associated with this message, which is GameAction.PLAY_CARD.
     *
     * @return The GameAction.PLAY_CARD action.
     */
    @Override
    public GameAction getAction(){
        return GameAction.PLAY_CARD;
    }

}
