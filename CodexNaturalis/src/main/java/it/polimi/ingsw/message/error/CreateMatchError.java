package it.polimi.ingsw.message.error;

/**
 * Error message indicating an error in creating a match.
 */
public class CreateMatchError extends ErrorMessage {

    /**
     * Returns the specific error code for the create match error.
     *
     * @return the error code, which is 1 for this specific error.
     */
    @Override
    public int getErrorCode(){ return 1; }

    /**
     * Returns the error message indicating a failure in creating a match.
     *
     * @return a string describing the error, which is "Error in creating match."
     */
    @Override
    public String getErrorMessage(){ return "Error in creating match."; }
}