package it.polimi.ingsw.model.card;

import it.polimi.ingsw.exception.CornerNotPresentException;

import java.io.Serializable;

/**
 * Represents the corner of a card
 */
public class Corner implements Serializable {
    /**
     * true if the corner is present, otherwise false
     */
    private final boolean isPresent;
    /**
     * symbol located in the corner if isPresent is true, otherwise null
     */
    private final Symbol symbol;

    /**
     * Constructs a Corner by specifying whether it's present and the symbol located in it.
     *
     * @param isPresent true if the corner is present, otherwise false
     * @param symbol the symbol located in the corner if {@code isPresent} is true, otherwise null
     */
    public Corner(boolean isPresent, Symbol symbol){
        this.isPresent = isPresent;
        this.symbol = symbol;
    }

    /**
     * Returns the symbol located in the corner.
     *
     * @return the symbol located in the corner
     * @throws CornerNotPresentException if the corner is not present
     */
    public Symbol getSymbol() throws CornerNotPresentException {
        if(!isPresent){
            throw new CornerNotPresentException();
        }
        return symbol;
    }

    /**
     * Returns true if the corner is present, otherwise false.
     *
     * @return true if the corner is present, otherwise false
     */
    public boolean IsPresent(){
        return isPresent;
    }
}
