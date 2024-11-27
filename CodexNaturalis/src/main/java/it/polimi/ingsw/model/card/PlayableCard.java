package it.polimi.ingsw.model.card;

import it.polimi.ingsw.exception.InvalidSymbolException;
import it.polimi.ingsw.model.field.PlayerField;
import it.polimi.ingsw.model.field.Position;

import java.util.ArrayList;
import java.util.Map;

/**
 * Represents any card that can be played by a player in his field
 */
public abstract class PlayableCard extends Card{
    private final Symbol kingdom;

    /**
     * Constructs a playable card with a specified kingdom and front corners.
     *
     * @param kingdom      Kingdom of the card.
     * @param frontCorners Corners of the front side of the card.
     * @param id           Identifier for the card.
     * @throws InvalidSymbolException if the specified symbol isn't of type kingdom.
     */
    protected PlayableCard(Symbol kingdom, Map<CornerPosition, Corner> frontCorners, String id) throws InvalidSymbolException {
        super(id, frontCorners);
        if (kingdom.isKingdom()) {
            this.kingdom = kingdom;
        }
        else {
            throw new InvalidSymbolException("Card type must be a kingdom!");
        }
    }

    /**
     * Retrieves the kingdom of the card.
     *
     * @return The kingdom symbol of the card.
     */
    @Override
    public Symbol getKingdom(){
        return kingdom;
    }

    /**
     * Retrieves the corner object at the specified position and side (front or back).
     *
     * @param cornerPosition Position of the corner.
     * @param isFront        True for front side of the card, false for back side.
     * @return Corner object at the specified position and side.
     */
    @Override
    public Corner getCorner(CornerPosition cornerPosition, boolean isFront){
        if (isFront)
            return frontCorners.get(cornerPosition);
        return new Corner(true, Symbol.EMPTY);
    }

    /**
     * Retrieves the list of symbols present on the specified side of the card.
     *
     * @param isFront True for front side of the card, false for back side.
     * @return List of symbols present on the specified side.
     */
    @Override
    public ArrayList<Symbol> getSymbolsOnSide(boolean isFront){
        ArrayList<Symbol> symbols = new ArrayList<>();

        if(isFront){
            for (CornerPosition cornerPosition: CornerPosition.values()) {
                Corner corner = getCorner(cornerPosition, true);
                if(corner.IsPresent() && corner.getSymbol() != Symbol.EMPTY) {
                    symbols.add(corner.getSymbol());
                }
            }
        }
        else{
            symbols.add(getKingdom());
        }

        return symbols;
    }

    /**
     * Gets symbol requirements of the card depending on the side it's played
     *
     * @param isFront true for front side of the card, false for back side
     * @return Map object where keys are symbols and values are their required occurrences in a player's field
     */
    public abstract Map<Symbol, Integer> getRequirements(boolean isFront);

    /**
     * Checks if the requirements for the card are met by a given player's field on the specified side.
     *
     * @param field  The player's field.
     * @param isFront True for front side of the card, false for back side.
     * @return True if the requirements for the card are met, otherwise false.
     */
    public abstract boolean checkRequirements(PlayerField field, boolean isFront);

    /**
     * Calculates the score obtained by playing the card in the specified manner.
     *
     * @param field The player's field.
     * @param isFront True for front side of the card, false for back side.
     * @param pos    Position of the card in the player's field.
     * @return Score obtained by playing the card in the specified manner.
     */
    public abstract int calcScore(PlayerField field, boolean isFront, Position pos);

    /**
     * Returns a string representation of the object, including details about
     * the kingdom, requirements, and symbols at the four corners if present.
     *
     * @return A string representing the object's kingdom, requirements,
     * and corner symbols.
     */
    @Override
    public String toString() {

        String s = "";
        s+= "Kingdom: " + (this).getKingdom() + "\n";

        if(!(this.getRequirements(true)).isEmpty()) {
            s+= "Requirements: " + (this).getRequirements(true) + "\n";
        }

        if(this.getCorner(CornerPosition.TOP_LEFT, true).IsPresent()) {
            s+= "Top left Corner: " + this.getCorner(CornerPosition.TOP_LEFT, true).getSymbol() + "\n";
        }
        if(this.getCorner(CornerPosition.TOP_RIGHT, true).IsPresent()) {
            s+= "Top Right Corner: " + this.getCorner(CornerPosition.TOP_RIGHT, true).getSymbol() + "\n";
        }
        if(this.getCorner(CornerPosition.BOTTOM_LEFT, true).IsPresent()) {
            s+= "Bottom Left Corner: " + this.getCorner(CornerPosition.BOTTOM_LEFT, true).getSymbol() + "\n";
        }
        if(this.getCorner(CornerPosition.BOTTOM_RIGHT, true).IsPresent()) {
            s+= "Bottom Right Corner: " + this.getCorner(CornerPosition.BOTTOM_RIGHT, true).getSymbol() + "\n";
        }

        return s;
    }

}
