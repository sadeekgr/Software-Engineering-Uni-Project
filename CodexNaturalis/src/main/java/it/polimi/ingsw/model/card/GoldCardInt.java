package it.polimi.ingsw.model.card;

import it.polimi.ingsw.exception.InvalidSymbolException;
import it.polimi.ingsw.model.field.PlayerField;
import it.polimi.ingsw.model.field.Position;

import java.util.Map;

/**
 * Represents a gold card that is worth a specified number of points.
 */
public class GoldCardInt extends GoldCard{
    /**
     * number of points the card is worth
     */
    private final int score;

    /**
     * Constructs a gold card worth a specified score.
     *
     * @param kingdom the kingdom symbol of the card
     * @param frontCorners the corners of the front side of the card
     * @param requirements the symbol requirements present on the front side of the card
     * @param score the number of points the card is worth
     * @param id the unique identifier of the card
     * @throws InvalidSymbolException if the symbol of the card isn't of type kingdom
     */
    public GoldCardInt(Symbol kingdom, Map<CornerPosition, Corner> frontCorners, Map<Symbol, Integer> requirements, int score, String id) throws InvalidSymbolException {
        super(kingdom, frontCorners, requirements, id);
        this.score = score;
    }

    /**
     * Retrieves the score that this card is worth.
     *
     * @return The score of the card.
     */
    public int getScore() {
        return this.score;
    }

    /**
     * Calculates the score obtained by playing the card in the specified position on the player's field.
     *
     * @param field the player's field
     * @param isFront {@code true} for the front side of the card, {@code false} for the back side
     * @param pos the position of the card in the player's field
     * @return the score obtained by playing the card in the specified way
     */
    @Override
    public int calcScore(PlayerField field, boolean isFront, Position pos) {
        if(isFront){
            return score;
        }
        return 0;
    }

    /**
     * Generates a string representation of the GoldCardInt object, including its score and other details.
     *
     * @return A string representing the GoldCardInt object, including its score and other inherited details.
     */
    @Override
    public String toString() {

        String s = "Score : ";
        s+= this.score + "\n";
        s+= super.toString();

        return s;
    }


}
