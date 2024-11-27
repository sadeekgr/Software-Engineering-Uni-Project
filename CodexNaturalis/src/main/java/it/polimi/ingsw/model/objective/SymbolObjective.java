package it.polimi.ingsw.model.objective;

import it.polimi.ingsw.model.field.PlayerField;
import it.polimi.ingsw.model.card.Symbol;

import java.util.HashMap;
import java.util.Map;

/**
 * Represents an objective based on symbol occurrences.
 * This subclass of Objective calculates the number of times a specific set of symbols appears on a player's field,
 * relative to predefined target occurrences.
 */
public class SymbolObjective extends Objective{
    // Map to store symbols and their required occurrences
    private final Map<Symbol, Integer> symbols;

    public Map<Symbol, Integer> getSymbols() {
        return new HashMap<>(symbols);
    }

    /**
     * Constructs a SymbolObjective object with the given symbols and score.
     *
     * @param s The map of symbols and their required occurrences.
     * @param score   The base score associated with this objective.
     * @param id      The unique identifier for this objective.
     */
    public SymbolObjective(Map<Symbol, Integer> s, int score, String id){
        super(score, id);
        symbols = s;
    }

    /**
     * Calculates the number of times the objective condition is fulfilled on the provided player field.
     * The calculation considers the occurrence of each symbol relative to its predefined target occurrences.
     *
     * @param field the player field on which to calculate the occurrences
     * @return the number of times the objective condition is fulfilled on the provided player field
     */
    @Override
    public int calculateObjectiveCompletionTimes(PlayerField field){
        int occurrences = Integer.MAX_VALUE;

        for(Symbol s: symbols.keySet()){
            if(field.getSymbolNum(s) / symbols.get(s) < occurrences){
                occurrences = field.getSymbolNum(s) / symbols.get(s);
            }
        }

        return occurrences;
    }
}

