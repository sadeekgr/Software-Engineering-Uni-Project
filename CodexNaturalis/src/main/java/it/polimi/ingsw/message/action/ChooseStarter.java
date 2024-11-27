package it.polimi.ingsw.message.action;

/**
 * A class representing a message for the "choose starter" game action.
 * This class extends {@link GameMessage} and specifies the action type as {@link GameAction#CHOOSE_STARTER}.
 */
public class ChooseStarter extends GameMessage {
    private final boolean side;

    /**
     * Constructs a ChooseStarter message with the specified side.
     *
     * @param side True if the front side is chosen, false otherwise.
     */
    public ChooseStarter(boolean side){
        this.side = side;
    }

    /**
     * Checks if the front side is chosen.
     *
     * @return True if the front side is chosen, false otherwise.
     */
    public boolean isFront(){
        return side;
    }

    /**
     * Gets the action type for this message.
     *
     * @return The action type {@link GameAction#CHOOSE_STARTER}.
     */
    @Override
    public GameAction getAction(){
        return GameAction.CHOOSE_STARTER;
    }
}
