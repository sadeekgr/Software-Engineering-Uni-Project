package it.polimi.ingsw.model.objective;

import it.polimi.ingsw.model.field.PlayerField;

import java.io.Serializable;

/**
 * Abstract class representing an objective in the game. Objectives define scoring criteria for players.
 * Subclasses of Objective implement specific types of objectives with additional functionality.
 */
public abstract class Objective implements Serializable {
    /**
     * The base score associated with achieving this objective.
     */
    private final int score;
    private final String id;

    /**
     * Constructs an Objective object with the given score and identifier.
     *
     * @param score The base score associated with this objective.
     * @param id    The unique identifier for this objective.
     */
    protected Objective(int score, String id){
        this.score = score;
        this.id = id;
    }

    /**
    /**
     * Retrieves the score associated with this objective.
     *
     * @return The score value.
     */
    public int getScore(){ return score; }

    /**
     * Gets the ID of the object.
     *
     * @return The ID of the object as a String.
     */
    public String getId(){return id;}

    /**
     * Calculates the total score achievable for this objective based on the provided player field.
     *
     * @param field the player field on which to calculate the score
     * @return the total score achievable for this objective based on the provided player field
     */
    public int calculateObjectiveScore(PlayerField field){
        return score * calculateObjectiveCompletionTimes(field);
    }

    /**
     * Abstract method to be implemented by subclasses for calculating the number of times the objective condition is fulfilled on the provided player field.
     *
     * @param field the player field on which to calculate the occurrences
     * @return the number of times the objective condition is fulfilled on the provided player field
     */
    public abstract int calculateObjectiveCompletionTimes(PlayerField field);

}
