package it.polimi.ingsw.message.error;

/**
 * Error message indicating an error in joining a match.
 */
public class JoinMatchError extends ErrorMessage {

    /**
     * Returns the specific error code for the join match error.
     *
     * @return the error code, which is 11 for this specific error.
     */
    @Override
    public int getErrorCode(){ return 11; }

    /**
     * Returns the error message indicating a failure to join a match.
     *
     * @return a string describing the error, which is "Error in joining match."
     */
    @Override
    public String getErrorMessage(){ return "Error in joining match."; }
}