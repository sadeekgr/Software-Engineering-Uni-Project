package it.polimi.ingsw.message.action;

/**
 * A class representing a message for the "choose objective" game action.
 * This class extends {@link GameMessage} and specifies the action type as {@link GameAction#CHOOSE_OBJECTIVE}.
 */
public class ChooseObjective extends GameMessage {
    private final int numObj;

    /**
     * Constructs a ChooseObjective message with the specified objective number.
     *
     * @param numObj The number of the chosen objective.
     */
    public ChooseObjective(int numObj){
        this.numObj = numObj;
    }

    /**
     * Gets the number of the chosen objective.
     *
     * @return The number of the chosen objective.
     */
    public int getNumObj(){
        return numObj;
    }

    /**
     * Gets the action type for this message.
     *
     * @return The action type {@link GameAction#CHOOSE_OBJECTIVE}.
     */
    @Override
    public GameAction getAction(){
        return GameAction.CHOOSE_OBJECTIVE;
    }
}
