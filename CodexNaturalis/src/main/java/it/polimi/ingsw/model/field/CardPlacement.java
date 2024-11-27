package it.polimi.ingsw.model.field;

import it.polimi.ingsw.exception.NoCoveredCornerException;

import it.polimi.ingsw.model.card.Card;
import it.polimi.ingsw.model.card.Symbol;
import it.polimi.ingsw.model.card.Corner;
import it.polimi.ingsw.model.card.CornerPosition;

import java.io.Serializable;

import static java.lang.Math.abs;

/**
 * Represents the placement of a card on the player's field.
 */
public class CardPlacement implements Serializable {
    // Boolean variable that indicates whether the card is on the front side.
    private final boolean isFront;
    // Integer variable used to store the card's position.
    private final Position pos;
    // The card itself.
    private final Card card;

    /**
     * Constructs a new CardPlacement object.
     *
     * @param isFront A boolean indicating whether the card is placed on the front side.
     * @param pos The position where the card is placed.
     * @param card The card being placed.
     */
    public CardPlacement(boolean isFront, Position pos, Card card){
        this.isFront = isFront;
        this.pos = pos;
        this.card = card;
    }

    /**
     * Gets the card placed in this CardPlacement.
     *
     * @return The card placed.
     */
    public Card getCard(){ return this.card; }

    /**
     * Gets the corner of the card at the specified position.
     *
     * @param cornerPosition The position of the corner.
     * @return The corner of the card.
     */
    public Corner getCorner(CornerPosition cornerPosition){
        return card.getCorner(cornerPosition, isFront);
    }

    /**
     * Gets the side of the card.
     *
     * @return True if the card is placed on the front side, false otherwise.
     */
    public boolean isFront(){
        return isFront;
    }

    /**
     * Gets the position where the card is placed.
     *
     * @return The position of the card placement.
     */
    public Position getPosition(){
        return pos;
    }

    /**
     * Gets the kingdom symbol of the card.
     *
     * @return The kingdom symbol of the card.
     */
    public Symbol getKingdom(){
        return card.getKingdom();
    }

    /**
     * Checks if a specified position is covered by the card.
     *
     * @param p The position to be checked.
     * @return True if the position is covered by a corner of the card, false otherwise.
     */
    public boolean isCornerCovered(Position p){
        Position diff = pos.distance(p);
        return abs(diff.x()) == 1 && abs(diff.y()) == 1;
    }

    /**
     * Gets the position of the covered corner relative to a specified position.
     *
     * @param p The position to which the covered corner's position is relative.
     * @return The position of the covered corner relative to the specified position.
     * @throws NoCoveredCornerException if there is no covered corner at the specified position.
     */
    public CornerPosition getCoveredCornerPosition(Position p) throws NoCoveredCornerException {
        Position diff = pos.distance(p);

        if(diff.x() == 1 && diff.y() == 1){ return CornerPosition.TOP_RIGHT; }
        else if(diff.x() == 1 && diff.y() == -1){ return CornerPosition.BOTTOM_RIGHT; }
        else if(diff.x() == -1 && diff.y() == 1){ return CornerPosition.TOP_LEFT; }
        else if(diff.x() == -1 && diff.y() == -1){ return CornerPosition.BOTTOM_LEFT; }
        else{ throw new NoCoveredCornerException("There aren't covered corners"); }
    }
}
