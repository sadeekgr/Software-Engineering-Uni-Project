package it.polimi.ingsw.model.card;

import it.polimi.ingsw.exception.InvalidSymbolException;
import it.polimi.ingsw.model.field.CardPlacement;
import it.polimi.ingsw.model.field.PlayerField;
import it.polimi.ingsw.model.field.Position;

import java.util.Map;

/**
 * Represents a gold card that awards points based on the number of corners it occupies on the field.
 */
public class GoldCardCorner extends GoldCard{
    /**
     * number of points gained for each corner on the field that gets occupied by playing this card
     */
    private final int scorePerCorner;

    /**
     * Constructs a gold card worth a specified score per corner that gets occupied.
     *
     * @param kingdom the kingdom symbol of the card
     * @param frontCorners the corners of the front side of the card
     * @param requirements the symbol requirements present on the front side of the card
     * @param scorePerCorner the number of points gained per corner that gets occupied
     * @param id the unique identifier of the card
     * @throws InvalidSymbolException if the symbol of the card isn't of type kingdom
     */
    public GoldCardCorner(Symbol kingdom, Map<CornerPosition, Corner> frontCorners, Map<Symbol, Integer> requirements, int scorePerCorner, String id) throws InvalidSymbolException {
        super(kingdom, frontCorners, requirements, id);
        this.scorePerCorner = scorePerCorner;
    }

    /**
     * Retrieves the score per corner awarded by this card.
     *
     * @return The score per corner.
     */
    public int getScorePerCorner() {
        return scorePerCorner;
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
        if (isFront) {
            int coveredCorners = 0;
            for (CardPlacement c : field.getCards()) {
                if (c.isCornerCovered(pos)) {
                    coveredCorners += 1;
                }
            }
            return coveredCorners * scorePerCorner;
        }

        return 0;
    }

    /**
     * Generates a string representation of the GoldCardCorner object, including its score per corner and other details.
     *
     * @return A string representing the GoldCardCorner object, including its score per corner and other inherited details.
     */
    @Override
    public String toString() {

        String s = "Score per Corner : ";
        s+= this.scorePerCorner + "\n";

        s+= super.toString();

        return s;
    }

}
