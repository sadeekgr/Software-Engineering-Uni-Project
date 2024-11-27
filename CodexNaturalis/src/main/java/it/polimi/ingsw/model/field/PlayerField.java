package it.polimi.ingsw.model.field;

import it.polimi.ingsw.model.card.Card;
import it.polimi.ingsw.model.card.StarterCard;
import it.polimi.ingsw.model.card.PlayableCard;
import it.polimi.ingsw.model.card.Symbol;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Represents the field of a player where cards can be placed.
 */
public class PlayerField implements Serializable {
    // List to represent the player's field.
    private final ArrayList<CardPlacement> field;
    // The pool of symbol associated to the player's field.
    private final SymbolPool symbolPool;

    /**
     * Constructs a new PlayerField object with an empty field and initializes the symbol pool.
     */
    public PlayerField(){
        field = new ArrayList<>();
        symbolPool = new SymbolPool();
    }

    /**
     * Adds symbols from a card to the symbol pool based on the specified side.
     *
     * @param c The card from which symbols will be added.
     * @param isFront A boolean indicating whether the symbols are from the front side of the card.
     */
    private void addSymbols(Card c, boolean isFront){
        for (Symbol s : c.getSymbolsOnSide(isFront)){
            symbolPool.addSymbol(s);
        }
    }

    /**
     * Removes symbols covered by a placed card at the specified position from the symbol pool.
     *
     * @param pos The position of the card placement.
     */
    private void removeCoveredSymbols(Position pos){
        for (CardPlacement c : field){
            if (c.isCornerCovered(pos) && c.getCorner(c.getCoveredCornerPosition(pos)).IsPresent()){
                Symbol s = c.getCorner(c.getCoveredCornerPosition(pos)).getSymbol();
                if (s != Symbol.EMPTY) {
                    symbolPool.removeSymbol(s);
                }
            }
        }
    }

    /**
     * Places the starter card on the player's field.
     *
     * @param c The starter card to be placed.
     * @param isFront A boolean indicating whether the card is placed on the front side.
     */
    public void placeStarterCard(StarterCard c, boolean isFront){
        Position pos = new Position(0, 0);
        CardPlacement cardPlacement = new CardPlacement(isFront, pos, c);
        field.add(cardPlacement);

        addSymbols(c, isFront);
    }

    /**
     * Places a playable card on the player's field.
     *
     * @param c The playable card to be placed.
     * @param isFront A boolean indicating whether the card is placed on the front side.
     * @param pos The position where the card will be placed.
     */
    public void placeCard(PlayableCard c, boolean isFront, Position pos){
        CardPlacement cardPlacement = new CardPlacement(isFront, pos, c);

        addSymbols(c, isFront);

        removeCoveredSymbols(pos);

        field.add(cardPlacement);
    }


    /**
     * Checks if a card is placeable at the specified position on the field.
     *
     * @param pos The position to be checked.
     * @return True if a card is placeable at the position, false otherwise.
     */
    public boolean isCardPlaceableAt(Position pos){
        // Odd coordinates are invalid.
        if(pos.sum() % 2 != 0){ return false; }

        boolean ret = false;

        for (CardPlacement c : field){
            if (c.getPosition().equals(pos)){
                return false;
            } else if(c.isCornerCovered(pos)) {
                // If the corner is present it means I can place the card.
                if(c.getCorner(c.getCoveredCornerPosition(pos)).IsPresent()){
                    ret = true;
                }
                else{
                    return false;
                }
            }
        }

        return ret;
    }

    /**
     * Gets the number of a specific symbol present on the field.
     *
     * @param symbol The symbol to be counted.
     * @return The number of occurrences of the symbol.
     */
    public int getSymbolNum(Symbol symbol){
        return symbolPool.getNumOfSymbol(symbol);
    }

    /**
     * Gets a copy of the list of card placements on the field.
     *
     * @return An ArrayList containing all card placements on the field.
     */
    public ArrayList<CardPlacement> getCards(){
        return new ArrayList<>(field);
    }
}
