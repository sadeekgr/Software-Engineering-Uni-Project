package it.polimi.ingsw.message.error;

/**
 * Error message indicating that a match does not exist for the requested action.
 */
public class MatchDoesNotExistError extends ErrorMessage {

    /**
     * Retrieves the error code associated with this match does not exist error message.
     *
     * @return The error code (in this case, 2).
     */
    @Override
    public int getErrorCode(){ return 2; }

    /**
     * Retrieves the error message describing that the match does not exist for the requested action.
     *
     * @return The error message string.
     */
    @Override
    public String getErrorMessage(){ return "Invalid Action: match does not exist."; }
}
