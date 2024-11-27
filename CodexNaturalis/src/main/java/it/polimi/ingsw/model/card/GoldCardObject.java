package it.polimi.ingsw.model.card;

import it.polimi.ingsw.exception.InvalidSymbolException;
import it.polimi.ingsw.model.field.CardPlacement;
import it.polimi.ingsw.model.field.PlayerField;
import it.polimi.ingsw.model.field.Position;

import java.util.Map;

/**
 * The GoldCardObject class represents a specific type of gold card in the game.
 * This card awards points based on the occurrence of a specified symbol in a player's field.
 */
public class GoldCardObject extends GoldCard{
    /**
     * number of points gained per occurrence of the symbol specified by this card in a player's field
     */
    private final int scorePerSymbol;

    /**
     * symbol specified by this card for scoring
     */
    private final Symbol symbol;

    /**
     * Constructs a gold card worth a specified score per specified symbol present in a player's field.
     *
     * @param kingdom Kingdom of the card.
     * @param frontCorners Corners of the front side of the card.
     * @param requirements Symbol requirements present on the front side of the card.
     * @param scorePerSymbol Number of points gained per specified symbol present in a player's field.
     * @param symbol Symbol specified by this card for scoring.
     * @param id Identifier for the card.
     * @throws InvalidSymbolException If the symbol of the card isn't of type object.
     */
    public GoldCardObject(Symbol kingdom, Map<CornerPosition, Corner> frontCorners, Map<Symbol, Integer> requirements, int scorePerSymbol, Symbol symbol, String id) throws InvalidSymbolException {
        super(kingdom, frontCorners, requirements, id);
        this.scorePerSymbol = scorePerSymbol;
        if (symbol.isObject()) {
            this.symbol = symbol;
        } else {
            throw new InvalidSymbolException("Score symbol must be an Object");
        }
    }

    /**
     * Retrieves the score per symbol awarded by this card.
     *
     * @return The score per symbol.
     */
    public int getScorePerSymbol() {
        return this.scorePerSymbol;
    }

    /**
     * Retrieves the symbol specified by this card for scoring.
     *
     * @return The symbol specified for scoring.
     */
    public Symbol getSymbol() {
        return symbol;
    }

    /**
     * @return number of occurrences in the card's front side of the symbol specified for scoring
     */
    private int numFrontScoreSymbol(){
        int count = 0;
        for(CornerPosition p : CornerPosition.values()){
            Corner corner = getCorner(p, true);
            if(corner.IsPresent() && corner.getSymbol() == symbol) {
                count += 1;
            }
        }

        return count;
    }

    /**
     * Calculates the score obtained by playing the card in the specified manner.
     *
     * @param field The player's field.
     * @param isFront True for the front side of the card, false for the back side.
     * @param pos Position of the card in the player's field.
     * @return Score obtained by playing the card in the specified manner.
     */
    public int calcScore(PlayerField field, boolean isFront, Position pos) {
        if(isFront) {
            int coveredScoreSymbols = 0;
            for (CardPlacement c : field.getCards()) {
                if (c.isCornerCovered(pos) && (
                        c.getCorner(c.getCoveredCornerPosition(pos)).getSymbol() == symbol)) {
                    coveredScoreSymbols += 1;
                }
            }

            return scorePerSymbol * (field.getSymbolNum(symbol) + numFrontScoreSymbol() - coveredScoreSymbols);
        }

        return 0;
    }

    /**
     * Generates a string representation of the GoldCardObject object, including its score per symbol, the symbol itself, and other details.
     *
     * @return A string representing the GoldCardObject object, including its score per symbol, the symbol, and other inherited details.
     */
    @Override
    public String toString() {

        String s = "Score per Symbol : ";
        s+= this.scorePerSymbol + "\n";

        s+= "Symbol : ";
        s+= this.symbol + "\n";

        s+= super.toString();

        return s;
    }


}
