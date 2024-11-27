package it.polimi.ingsw.message.notify;

import it.polimi.ingsw.model.objective.Objective;

/**
 * This class represents a notification message that is sent when an objective is chosen.
 */
public class NotifyChosenObjective extends NotifyMessage {
    private final Objective objective;

    /**
     * Constructs a new NotifyChosenObjective message.
     *
     * @param objective the chosen objective
     */
    public NotifyChosenObjective(Objective objective){
        this.objective = objective;
    }

    /**
     * Returns the chosen objective.
     *
     * @return the chosen objective
     */
    public Objective getObjective(){
        return objective;
    }

    /**
     * Retrieves the type of this message, which is {@link NotifyType#OBJECTIVE_CHOSEN}.
     *
     * @return The message type, which is {@link NotifyType#OBJECTIVE_CHOSEN}.
     */
    @Override
    public NotifyType getNotifyType(){ return NotifyType.OBJECTIVE_CHOSEN; }
}