package it.polimi.ingsw.model.card;

import it.polimi.ingsw.exception.InvalidSymbolException;

import java.util.ArrayList;
import java.util.Map;

/**
 * Represents a starter card that can be played by a player in their field.
 * Starter cards have front and back sides with corners and central symbols on the back side.
 */
public class StarterCard extends Card{

    /**
     * Map representing the corner positions and their associated corner objects for the back side of the card
     */
    private final Map<CornerPosition, Corner> backCorners;
    /**
     * list of the central symbols in the back side of the card
     */
    private final ArrayList<Symbol> centerSymbols;

    /**
     * Constructs a starter card with specified front and back corners and central symbols.
     *
     * @param frontCorners  Corners of the front side of the card.
     * @param backCorners   Corners of the back side of the card.
     * @param centerSymbols List of central symbols on the back side of the card.
     * @param id            Identifier for the card.
     * @throws InvalidSymbolException If a central symbol isn't of type kingdom or if the symbol of the card isn't of type kingdom.
     */
    public StarterCard(Map<CornerPosition, Corner> frontCorners, Map<CornerPosition, Corner> backCorners, ArrayList<Symbol> centerSymbols, String id) throws InvalidSymbolException {
        super(id, frontCorners);
        this.backCorners = backCorners;
        for (Symbol s: centerSymbols) {
            if (!s.isKingdom()) {
                throw new InvalidSymbolException("Center symbols must be of kingdom type");
            }
        }
        this.centerSymbols = centerSymbols;
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
        return backCorners.get(cornerPosition);
    }

    /**
     * Retrieves the list of symbols present on the specified side of the card (front or back).
     *
     * @param isFront True for front side of the card, false for back side.
     * @return List of symbols present on the specified side.
     */
    @Override
    public ArrayList<Symbol> getSymbolsOnSide(boolean isFront){
        ArrayList<Symbol> symbols = getCenterSymbols(isFront);
        for (CornerPosition cornerPosition: CornerPosition.values()) {
            Corner corner = getCorner(cornerPosition, isFront);
            if (corner.IsPresent() && corner.getSymbol() != Symbol.EMPTY) {
                symbols.add(corner.getSymbol());
            }
        }
        return symbols;
    }

    /**
     * Retrieves the list of central symbols present on the back side of the card.
     * Returns an empty list if isFront is true.
     *
     * @param isFront True for front side of the card, false for back side.
     * @return List of central symbols on the back side if isFront is false, otherwise an empty list.
     */
    public ArrayList<Symbol> getCenterSymbols(boolean isFront){
        if(isFront)
            return new ArrayList<>(centerSymbols);
        return new ArrayList<>();
    }

    /**
     * Returns the symbol representing the kingdom associated with this object.
     * In this implementation, it always returns {@link Symbol#EMPTY}.
     *
     * @return The symbol representing the kingdom, which is {@link Symbol#EMPTY}.
     */
    @Override
    public Symbol getKingdom(){
        return Symbol.EMPTY;
    }
}
