package it.polimi.ingsw.message.notify;

import it.polimi.ingsw.model.objective.Objective;

/**
 * This class represents a notification message that is sent to update the global objectives.
 */
public class NotifyGlobalObjectives extends NotifyMessage {
    private final Objective[] objectives;

    /**
     * Constructs a new NotifyGlobalObjectives message.
     *
     * @param objectives an array of global objectives
     */
    public NotifyGlobalObjectives(Objective[] objectives) {
        this.objectives = objectives;
    }

    /**
     * Returns the array of global objectives.
     *
     * @return the array of global objectives
     */
    public Objective[] getObjectives() {
        return objectives;
    }

    /**
     * Retrieves the type of this message, which is {@link NotifyType#GLOBAL_OBJECTIVES}.
     *
     * @return The message type, which is {@link NotifyType#GLOBAL_OBJECTIVES}.
     */
    @Override
    public NotifyType getNotifyType() {
        return NotifyType.GLOBAL_OBJECTIVES;
    }
}
