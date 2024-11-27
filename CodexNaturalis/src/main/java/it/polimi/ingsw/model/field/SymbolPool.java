package it.polimi.ingsw.model.field;

import it.polimi.ingsw.model.card.Symbol;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Represents a pool of symbols available on the player's field.
 */
public class SymbolPool implements Serializable {
    // Map used to store, for each symbol, its occurrences.
    private final Map<Symbol, Integer> symbolMap;

    /**
     * Constructs a new SymbolPool object with an empty symbol map.
     */
    public SymbolPool(){ symbolMap = new HashMap<>(); }

    /**
     * Adds a symbol to the symbol pool.
     *
     * @param s The symbol to be added.
     */
    public void addSymbol(Symbol s){
        symbolMap.put(s, getNumOfSymbol(s) + 1);
    }

    /**
     * Removes a symbol from the symbol pool.
     *
     * @param s The symbol to be removed.
     */
    public void removeSymbol(Symbol s){
        symbolMap.put(s, getNumOfSymbol(s) - 1);
    }

    /**
     * Gets the number of occurrences of a symbol in the symbol pool.
     *
     * @param symbol The symbol to be counted.
     * @return The number of occurrences of the symbol.
     */
    public int getNumOfSymbol(Symbol symbol){
        if(symbolMap.containsKey(symbol)) {
            return symbolMap.get(symbol);
        }
        return 0;
    }
}
