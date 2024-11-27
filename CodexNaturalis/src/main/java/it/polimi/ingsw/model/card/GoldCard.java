package it.polimi.ingsw.model.card;

import it.polimi.ingsw.exception.InvalidSymbolException;
import it.polimi.ingsw.model.field.PlayerField;

import java.util.Map;
import java.util.HashMap;
import java.util.Set;

/**
 * Represents a gold card that can be played under certain conditions.
 */
public abstract class GoldCard extends PlayableCard{

    /**
     * Map object where keys are symbols and values are their required occurrences in a player's field for the card to be played
     */
    protected final Map<Symbol, Integer> requirements;

    /**
     * Constructs a gold card with the specified kingdom, front corners, requirements, and id.
     *
     * @param kingdom the kingdom symbol of the card
     * @param frontCorners the corners of the front side of the card
     * @param requirements the symbol requirements for the front side of the card
     * @param id the unique identifier of the card
     * @throws InvalidSymbolException if any symbol in the requirements is not of type kingdom
     */
    protected GoldCard(Symbol kingdom, Map<CornerPosition, Corner> frontCorners, Map<Symbol, Integer> requirements, String id) throws InvalidSymbolException {
        super(kingdom, frontCorners, id);

        Set<Symbol> keyset = requirements.keySet();
        for(Symbol s: keyset) {
            if (!s.isKingdom()) {
                throw new InvalidSymbolException("Requirements must be kingdom symbol!");
            }
        }

        this.requirements = requirements;
    }

    /**
     * Returns the symbol requirements of the card depending on the side it's played.
     *
     * @param isFront {@code true} for the front side of the card, {@code false} for the back side
     * @return a map where keys are symbols and values are their required occurrences in a player's field for the card to be played
     */
    public Map<Symbol, Integer> getRequirements(boolean isFront) {
        if(isFront) {
            return requirements;
        }

        return new HashMap<>();
    }

    /**
     * Checks if the requirements for the card are met by a given player's field.
     *
     * @param field the field of a given player
     * @param isFront {@code true} for the front side of the card, {@code false} for the back side
     * @return {@code true} if the requirements for the card are met by the given player's field, otherwise {@code false}
     */
    @Override
    public boolean checkRequirements(PlayerField field, boolean isFront){
        if(isFront) {
            for (Symbol s : requirements.keySet()) {
                if (requirements.get(s) > field.getSymbolNum(s)) {
                    return false;
                }
            }
        }

        return true;
    }
}
