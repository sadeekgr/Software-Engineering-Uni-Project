package it.polimi.ingsw.message.error;

/**
 * Error message indicating an error occurred while leaving a match.
 */
public class LeaveMatchError extends ErrorMessage {

    /**
     * Retrieves the error code associated with this error message.
     *
     * @return The error code (in this case, 9).
     */
    @Override
    public int getErrorCode(){ return 9; }

    /**
     * Retrieves the error message describing the error in leaving the match.
     *
     * @return The error message string.
     */
    @Override
    public String getErrorMessage(){ return "Error in leaving match."; }
}
