package it.polimi.ingsw.model.card;

import it.polimi.ingsw.exception.InvalidSymbolException;
import it.polimi.ingsw.model.field.PlayerField;
import it.polimi.ingsw.model.field.Position;

import java.util.HashMap;
import java.util.Map;

/**
 * Represents a resource card that can be played by a player in their field.
 * Resource cards have a score value and do not have any specific requirements.
 * They can be played on the front side to score points directly.
 */
public class ResourceCard extends PlayableCard{
    /**
     * number of points the card is worth
     */
    private final int score;

    /**
     * Constructs a resource card with a specified kingdom, front corners, and score value.
     *
     * @param kingdom      Kingdom of the card.
     * @param frontCorners Corners of the front side of the card.
     * @param score        Number of points the card is worth.
     * @param id           Identifier for the card.
     * @throws InvalidSymbolException if the symbol of the card isn't of type kingdom.
     */
    public ResourceCard(Symbol kingdom, Map<CornerPosition, Corner> frontCorners, int score, String id) throws InvalidSymbolException {
        super(kingdom, frontCorners, id);
        this.score = score;
    }

    /**
     * Retrieves the score value of the resource card.
     *
     * @return The score value of the card.
     */
    public int getScore() {
        return this.score;
    }

    /**
     * Returns an empty map as resource type cards have no specific requirements.
     *
     * @param isFront True for front side of the card, false for back side.
     * @return An empty map since resource cards have no requirements.
     */
    @Override
    public Map<Symbol, Integer> getRequirements(boolean isFront){ return new HashMap<>(); }

    /**
     * Checks if the requirements for the resource card are met (always returns true as resource cards have no requirements).
     *
     * @param field  The player's field.
     * @param isFront True for front side of the card, false for back side.
     * @return Always true since resource type cards have no requirements.
     */
    @Override
    public boolean checkRequirements(PlayerField field, boolean isFront){ return true; }

    /**
     * Calculates the score obtained by playing the resource card.
     *
     * @param field The player's field.
     * @param isFront True for front side of the card, false for back side.
     * @param pos    Position of the card in the player's field.
     * @return Score obtained by playing the card in the way specified by field, isFront and pos.
     */
    @Override
    public int calcScore(PlayerField field, boolean isFront, Position pos) {
        if (isFront){
            return score;
        }
        return 0;
    }

}
