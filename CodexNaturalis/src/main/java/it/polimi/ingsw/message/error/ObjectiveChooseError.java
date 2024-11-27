package it.polimi.ingsw.message.error;

/**
 * Error message indicating an error occurred during objective selection.
 */
public class ObjectiveChooseError extends ErrorMessage {

    /**
     * Retrieves the error code associated with this objective choose error message.
     *
     * @return The error code (in this case, 4).
     */
    @Override
    public int getErrorCode(){ return 4; }

    /**
     * Retrieves the error message describing the error in objective selection.
     *
     * @return The error message string.
     */
    @Override
    public String getErrorMessage(){ return "Error in objective choose."; }
}
