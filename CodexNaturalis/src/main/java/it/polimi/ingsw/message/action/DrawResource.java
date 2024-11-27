package it.polimi.ingsw.message.action;

/**
 * A class representing a message for the "draw resource" game action.
 * This class extends {@link GameMessage} and specifies the action type as {@link GameAction#DRAW_RESOURCE}.
 */
public class DrawResource extends GameMessage {

    /**
     * Gets the action type for this message.
     *
     * @return The action type {@link GameAction#DRAW_RESOURCE}.
     */
    @Override
    public GameAction getAction(){
        return GameAction.DRAW_RESOURCE;
    }
}
