package it.polimi.ingsw.message.notify;

import it.polimi.ingsw.model.objective.Objective;

/**
 * This class represents a notification message that is sent to prompt a player to choose between two objectives.
 */
public class NotifyToChooseObjective extends NotifyMessage {
    private final Objective[] objs;

    /**
     * Constructs a new NotifyToChooseObjective message with two objectives to choose from.
     *
     * @param obj1 the first objective to choose from
     * @param obj2 the second objective to choose from
     */
    public NotifyToChooseObjective(Objective obj1, Objective obj2){
        objs = new Objective[]{obj1, obj2};
    }

    /**
     * Returns the array of objectives to choose from.
     *
     * @return the array of objectives to choose from
     */
    public Objective[] getObjective(){
        return objs;
    }

    /**
     * Retrieves the type of this message, which is {@link NotifyType#CHOOSE_OBJECTIVE}.
     *
     * @return The message type, which is {@link NotifyType#CHOOSE_OBJECTIVE}.
     */
    @Override
    public NotifyType getNotifyType(){ return NotifyType.CHOOSE_OBJECTIVE; }
}
